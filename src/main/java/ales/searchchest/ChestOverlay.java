package ales.searchchest;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class ChestOverlay {
    private static ChestOverlay INSTANCE;
    private boolean shouldHighlight = false;
    private BlockPos targetChestPos;
    private int targetSlot = -1;
    private HandledScreen<?> currentScreen;

    // Singleton pattern to ensure single instance
    public static ChestOverlay getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChestOverlay();
        }
        return INSTANCE;
    }

    private ChestOverlay() {
        // Register event listener
        HighlightEvent.register(this::handleHighlightRequest);
        
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof HandledScreen<?> handledScreen) {
                this.currentScreen = handledScreen;
                // Check if this is the chest we want to highlight
                BlockPos openPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
                if (openPos.equals(targetChestPos)) {
                    this.shouldHighlight = true;
                }
            }
        });

        HudRenderCallback.EVENT.register(this::renderHighlight);
    }

    private void handleHighlightRequest(int slotIndex) {
        if (currentScreen != null) {
            // If screen is already open, highlight immediately
            this.targetSlot = slotIndex;
            this.shouldHighlight = true;
        }
    }

    public void prepareHighlight(BlockPos chestPos, int slotIndex) {
        this.targetChestPos = chestPos;
        this.targetSlot = slotIndex;
        this.shouldHighlight = true;
    }

    private void renderHighlight(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (shouldHighlight && currentScreen != null && client.currentScreen == currentScreen) {
            // Verify we're looking at the correct chest
            BlockPos openPos = ((BlockHitResult) client.crosshairTarget).getBlockPos();
            
            if (openPos.equals(targetChestPos)) {
                var handler = currentScreen.getScreenHandler();
                
                if (targetSlot >= 0 && targetSlot < handler.slots.size()) {
                    // Calculate position using container slots
                    var slot = handler.getSlot(targetSlot);
                    int x =  slot.x;
                    int y =  slot.y;
                    
                    // Draw persistent highlight
                    drawContext.fill(x, y, x + 16, y + 16, 0x8000FF00);
                }
            }
        }
    }
}
