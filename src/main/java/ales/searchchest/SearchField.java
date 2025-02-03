package ales.searchchest;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class SearchField extends WTextField{
    private Runnable onTypeAction;


    public void setOnTypeAction(Runnable action){
        this.onTypeAction = action;
    }
    
    @Override
    public InputResult onCharTyped(char ch) {
        if(onTypeAction != null){
            onTypeAction.run();
        }
        return super.onCharTyped(ch);
    }

}
