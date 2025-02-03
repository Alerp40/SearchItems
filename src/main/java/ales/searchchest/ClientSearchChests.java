package ales.searchchest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class ClientSearchChests implements ClientModInitializer {
    private static KeyBinding OpenRenderMenuKB;
    @Override
    public void onInitializeClient() {
        OpenRenderMenuKB = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.searchchests.openrendermenu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.searchchests.rendermenu"
        ));
        KeyInputHandler.register();
}
    public static KeyBinding getOpenRenderMenuKB() {
        return OpenRenderMenuKB;
    }
}