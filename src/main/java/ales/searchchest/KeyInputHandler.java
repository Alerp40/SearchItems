package ales.searchchest;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyInputHandler {
    private static boolean menuOpen = false;
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(ClientSearchChests.getOpenRenderMenuKB().wasPressed()) {
                menuOpen = true;
            if(client.currentScreen == null) client.setScreen(new SearchScreen(new SearchGui()));
            }
        });
    }
    public static boolean getmenuOpen(){
        return menuOpen;
    }
    public static void setmenuOpen(Boolean booltoSet){
        menuOpen = booltoSet;
    }
}
