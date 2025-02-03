package ales.searchchest;


import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;


//custom ClientScreen class so we can check if its instanceOf in the future and so we can make the game not pause when opening the screen

public class SearchScreen extends CottonClientScreen {
    public SearchScreen(GuiDescription guiDescription) {
        super(guiDescription);
    }
    @Override
    public boolean shouldPause(){
        return false;
    }
}