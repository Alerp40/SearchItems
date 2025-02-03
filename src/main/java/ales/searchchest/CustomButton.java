package ales.searchchest;

import org.jetbrains.annotations.Nullable;

import io.github.cottonmc.cotton.gui.widget.WButton;

//custom button class to override the setOnClick function to change the button state for render checks

public class CustomButton extends WButton{
    private boolean lastButtonPressed = false;
    public void setButtonPressed(boolean bool){
        this.lastButtonPressed = bool;
    }
    public boolean getButtonPressed(){
        return this.lastButtonPressed;
    }
    @Override
    public WButton setOnClick(@Nullable Runnable onClick) {
        setButtonPressed(true);
        return super.setOnClick(onClick);
    }
}
