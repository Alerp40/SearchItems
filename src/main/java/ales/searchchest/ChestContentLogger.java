package ales.searchchest;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;



public class ChestContentLogger {
    private static HashMap<BlockPos, ArrayList<ItemStack>> ChestsCont = new HashMap<>();
    Logger LOGGER = LoggerFactory.getLogger(ChestContentLogger.class);
    private long lastUpdate = 0;
    int currentSize = 0;
    BlockPos lastChestPos = null;
    private void onScreenHandlerOpened(ScreenHandler handler, BlockPos pos) {
    try{
        if (handler.getType() == ScreenHandlerType.GENERIC_9X3) {
            currentSize = 27;
            logChestContent(handler, pos);
        } else if (handler.getType() == ScreenHandlerType.GENERIC_9X6) {
            currentSize = 54;
            logChestContent(handler, pos);
        } else if (handler.getType() == ScreenHandlerType.SHULKER_BOX) {
            currentSize = 27;
            logChestContent(handler, pos);
        }else{
            LOGGER.info("inventory type not supported");
        }
    }catch(Exception e){
        LOGGER.warn(e.toString());
    }
    }
    private void logChestContent(ScreenHandler handler, BlockPos pos) {
        ArrayList<ItemStack> tempstack = new ArrayList<>();
        for(int i = 0; i < currentSize; i++) {
            Slot slot = handler.slots.get(i);
            if(slot.hasStack()){
                ItemStack stack = slot.getStack();
                tempstack.add(stack);
                ChestsCont.put(pos, tempstack);
            } 
        }
        LOGGER.info(ChestsCont.toString());
    }
    public ChestContentLogger() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            long now = System.currentTimeMillis();
            // Check if the opened screen is a container (HandledScreen)
            if(now - lastUpdate > 500){
                if (client.currentScreen instanceof HandledScreen<?> handledScreen) {
                    ScreenHandler handler = handledScreen.getScreenHandler();
                    HitResult chestPostar = client.crosshairTarget;
                    if(chestPostar instanceof BlockHitResult blockHitResult){
                        lastChestPos = blockHitResult.getBlockPos();
                    } 
                    onScreenHandlerOpened(handler, lastChestPos);
                }
            lastUpdate = now;
            }
        });
    }
    public static HashMap<BlockPos, ArrayList<ItemStack>> getChestsContent(){
        return ChestsCont;
    }
    public static int getAmmountOfItems(HashMap<BlockPos, ArrayList<ItemStack>> chestsCont){
        int size = 0;
        for(BlockPos pos : chestsCont.keySet()){
            size +=chestsCont.get(pos).size();
        }
        return size;
    }
}
