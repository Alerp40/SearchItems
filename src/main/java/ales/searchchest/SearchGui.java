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


//Class that handles all the Gui drawign and rendering it implements libGui from CottonMC to make drawing a custom gui easyer

public class SearchGui extends LightweightGuiDescription{
    private HashMap<BlockPos, ArrayList<ItemStack>> Chests = ChestContentLogger.getChestsContent();
    private int ammountOfItems = ChestContentLogger.getAmmountOfItems(Chests);
    private int ammountOfBoxes = (int) Math.ceil(ammountOfItems/14.0);
    private int counter = 0;
    private int chestGetter= 0;
    private WGridPanel root = new WGridPanel();
    private ArrayList<WBox> boxes = new ArrayList<>();
    //creates a new textfield that refreshes the gui on every keypress making searching dynamic insead of having to rely on a button confirmation
    private WTextField searchField = new WTextField(){
        @Override
        public InputResult onKeyPressed(int ch, int key, int modifiers) {
            InputResult inputResult = super.onKeyPressed(ch,key,modifiers);
            refreshPanel(root,searchField);
            return inputResult;
        };
    };
    //method that handles the initial grid panel and calls other draw methods
    public SearchGui(){
        HashMap<BlockPos, ArrayList<ItemStack>> Chests = ChestContentLogger.getChestsContent();
        counter = 0;
        root = new WGridPanel();
        setRootPanel(root);
        searchField.setSuggestion(Text.literal("Search..."));
        root.add(searchField,0,0,20,30);
        populateGrid(Chests);
    }
    //adds all the boxes calculating beforehand howmany to add based on ammount of items
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
    //adds buttons to each individual box if theres enough items itll draw 14 from one chest if not itll draw as many as possible from one chest before moving to the next one withour starting a new box and still only adding 14
    public WBox buildBox(WBox box, HashMap<BlockPos, ArrayList<ItemStack>> chests){
        ArrayList<BlockPos> chestList = new ArrayList<>(chests.keySet());
        int currentChestSize = chests.get(chestList.get(counter)).size();
        for (int j = 0; j < 14; j++){
            if(chestGetter < currentChestSize){ //check to not go over the chestsize
                box.add(buildButton(chests.get(chestList.get(counter)).get(chestGetter),chestList.get(counter),chestGetter),24,16);
                chestGetter++;
            }else if(chestList.size()-1 > counter){ //check to not look for not existent chests
                counter += 1;
                chestGetter = 0;
                currentChestSize = chests.get(chestList.get(counter)).size();
                box.add(buildButton(chests.get(chestList.get(counter)).get(chestGetter),chestList.get(counter),chestGetter),24,16);
                chestGetter++;
            }else{
                break;
            }
        }
        return box; //returns the box to draw
    }

    //method to build each individual button and add functions on click

    public CustomButton buildButton(ItemStack stack,BlockPos blockPos,int index){
        CustomButton button = new CustomButton();
        ItemIcon currIcon = new ItemIcon(stack);
        Runnable buttonFunc = ()->{ //if we define lambda function inside the setonClick method it cannot access the paraneter vaiables or itself(this)
            RenderAroundBlock.initRender(blockPos,button);
            KeyInputHandler.setmenuOpen(false);
        };
        button.setIcon(currIcon).setIconSize(10);
        button.setLabel(Text.literal(stack.getCount()+"")).setAlignment(HorizontalAlignment.RIGHT);
        button.setSize(30, 30);
        button.setOnClick(buttonFunc);
        return button;
    }

    //handles clearing and redrawing every element for a dynamic feel, no better way to do this if we want dynamic drawing

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

    //linearly searches through all lists to find the items that have the caracters we write in the searchfield and returs a hashmap containing the chests positions of chests that contain the specified item and correspoding lists of the items that match the search, this search does not care for capitalization or at what point of the item name the provided string or character is

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