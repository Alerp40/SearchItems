package ales.searchchest;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

//Class to handle input keys and open the menu on press

public class KeyInputHandler {
    private static boolean menuOpen = false;
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(ClientSearchChests.getOpenRenderMenuKB().wasPressed()) {
                menuOpen = true;
            if(client.currentScreen == null) client.setScreen(new SearchScreen(new SearchGui())); //prevents menu opening over others
            }
        });
    }
    public static boolean getmenuOpen(){ //menu open variable to see if the menu opened to clear rendering
        return menuOpen;
    }
    public static void setmenuOpen(Boolean booltoSet){//set menuOpen variable for block rendering
        menuOpen = booltoSet;
    }
}
