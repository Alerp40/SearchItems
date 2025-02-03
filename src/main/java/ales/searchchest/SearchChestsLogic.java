package ales.searchchest;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

public class SearchChestsLogic {
    public static ArrayList<BlockPos> findNearbyCheststoPlayer(ClientPlayerEntity player, int radius) {
    ArrayList<BlockPos> nearbyChests = new ArrayList<>();
    BlockPos playerPos = player.getBlockPos();
    
    // Iterate through a cubic area around the player
    for (int x = -radius; x <= radius; x++) {
        for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos checkPos = playerPos.add(x, y, z);
                BlockState blockState = player.getWorld().getBlockState(checkPos);
                
                // Check if the block is a chest
                if (blockState.getBlock() instanceof ChestBlock) {
                    nearbyChests.add(checkPos);
                }
            }
        }
    }
    
    return nearbyChests;
    }

    public static HashMap<BlockPos, HashMap<Item, Integer>> itemsInChests(ArrayList<BlockPos> chestPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        HashMap<BlockPos, HashMap<Item, Integer>> items = new HashMap<>();

        for (BlockPos chest : chestPos) {
            BlockEntity blockEntity = world.getBlockEntity(chest);

            if (blockEntity instanceof ChestBlockEntity) {
                HashMap<Item, Integer> itemStack = new HashMap<>();
                Inventory chestBlockEntity = (Inventory) blockEntity;
                for(int i = 0; i < chestBlockEntity.size(); i++) {
                    ItemStack stack = chestBlockEntity.getStack(i);

                    if(!stack.isEmpty()) {
                        Item item = stack.getItem();
                        int count = stack.getCount();
                        itemStack.merge(item, count, Integer::sum);
                    }
                }
                items.put(chest, itemStack);
            }
        }
        return items;
    }
}