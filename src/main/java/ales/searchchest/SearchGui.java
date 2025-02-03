package ales.searchchest;

import java.util.ArrayList;
import java.util.HashMap;


import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;



public class SearchGui extends LightweightGuiDescription{
    private HashMap<BlockPos, ArrayList<ItemStack>> Chests = ChestContentLogger.getChestsContent();
    private int ammountOfItems = ChestContentLogger.getAmmountOfItems(Chests);
    private int ammountOfBoxes = (int) Math.ceil(ammountOfItems/14.0);
    private int counter = 0;
    private int chestGetter= 0;
    private WGridPanel root = new WGridPanel();
    private ArrayList<WBox> boxes = new ArrayList<>();

    private WTextField searchField = new WTextField(){
        @Override
        public InputResult onKeyPressed(int ch, int key, int modifiers) {
            InputResult inputResult = super.onKeyPressed(ch,key,modifiers);
            refreshPanel(root,searchField);
            return inputResult;
        };
    };
    public SearchGui(){
        HashMap<BlockPos, ArrayList<ItemStack>> Chests = ChestContentLogger.getChestsContent();
        counter = 0;
        root = new WGridPanel();
        setRootPanel(root);
        searchField.setSuggestion(Text.literal("Search..."));
        root.add(searchField,0,0,20,30);
        populateGrid(Chests);
    }
    public void populateGrid(HashMap<BlockPos, ArrayList<ItemStack>> chestlist){
        ammountOfItems = ChestContentLogger.getAmmountOfItems(chestlist);
        ammountOfBoxes = (int) Math.ceil(ammountOfItems/14.0);
        for(int i = 0; i < ammountOfBoxes; i++){
            boxes.add(buildBox(new WBox(Axis.HORIZONTAL),chestlist));
        }
        for(int i = 0; i<boxes.size(); i++){
            root.add(boxes.get(i),0,i+1);
        }


    }
    public WBox buildBox(WBox box, HashMap<BlockPos, ArrayList<ItemStack>> chests){
        ArrayList<BlockPos> chestList = new ArrayList<>(chests.keySet());
        int currentChestSize = chests.get(chestList.get(counter)).size();
        for (int j = 0; j < 14; j++){
            if(chestGetter < currentChestSize){
                box.add(buildButton(chests.get(chestList.get(counter)).get(chestGetter),chestList.get(counter),chestGetter),24,16);
                chestGetter++;
            }else if(chestList.size()-1 > counter){
                counter += 1;
                chestGetter = 0;
                currentChestSize = chests.get(chestList.get(counter)).size();
                box.add(buildButton(chests.get(chestList.get(counter)).get(chestGetter),chestList.get(counter),chestGetter),24,16);
                chestGetter++;
            }else{
                break;
            }
        }
        return box;
    }

    public CustomButton buildButton(ItemStack stack,BlockPos blockPos,int index){
        CustomButton button = new CustomButton();
        ItemIcon currIcon = new ItemIcon(stack);
        Runnable buttonFunc = ()->{
            RenderAroundBlock.initRender(blockPos,button);
            HighlightEvent.trigger(index);
            KeyInputHandler.setmenuOpen(false);
        };
        button.setIcon(currIcon).setIconSize(10);
        button.setLabel(Text.literal(stack.getCount()+"")).setAlignment(HorizontalAlignment.RIGHT);
        button.setSize(30, 30);
        button.setOnClick(buttonFunc);
        return button;
    }

    public void refreshPanel(WPanelWithInsets panel, WTextField searchfield){
        String lookup = searchfield.getText();
        HashMap<BlockPos, ArrayList<ItemStack>> resultHash = linearSearch(Chests, lookup);
        for(WBox box: boxes){
            panel.remove(box);
        }
        boxes.clear();
        counter = 0;
        chestGetter= 0;
        populateGrid(resultHash);
        panel.validate(this);
    }

    public HashMap<BlockPos, ArrayList<ItemStack>> linearSearch(HashMap<BlockPos, ArrayList<ItemStack>> inArr, String lookUp){
        boolean changed = false;
        inArr = new HashMap<>(ChestContentLogger.getChestsContent());
        ArrayList<BlockPos> toRemove = new ArrayList<>();
        ArrayList<ItemStack> newArrayList = new ArrayList<>();
        for(BlockPos pos : inArr.keySet()){
            for(int i = 0; i<inArr.get(pos).size(); i++){
                if(inArr.get(pos).get(i).getItemName().getString().toLowerCase().indexOf(lookUp.toLowerCase()) >= 0){
                    newArrayList.add(inArr.get(pos).get(i));
                    changed = true;
                }
            }
            if(!changed){
                toRemove.add(pos);
            }else{
                inArr.put(pos,new ArrayList<>(newArrayList));
            }
            newArrayList.clear();
            changed = false;
        }
        for(BlockPos pos:toRemove){
           inArr.remove(pos);
        }
        return inArr;   
    }
}