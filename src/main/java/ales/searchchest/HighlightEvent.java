package ales.searchchest;

import java.util.function.Consumer;

public class HighlightEvent {
    private static Consumer<Integer> highlightListener;
    
    public static void register(Consumer<Integer> listener) {
        highlightListener = listener;
    }
    
    public static void trigger(int slotIndex) {
        if (highlightListener != null) {
            highlightListener.accept(slotIndex);
        }
    }
}
