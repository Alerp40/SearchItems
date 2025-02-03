package ales.searchchest;


import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;


public class SearchScreen extends CottonClientScreen {
    public SearchScreen(GuiDescription guiDescription) {
        super(guiDescription);
    }
    @Override
    public boolean shouldPause(){
        return false;
    }
}