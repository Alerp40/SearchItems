package ales.searchchest;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class RenderMenu extends Screen {
    private TextFieldWidget searchField;
    public RenderMenu() {
    super(Text.translatable("searchchests.menu"));
    }

    @Override
    protected void init(){
        int centerX = (int) (width / 2);
        int centerY = (int) (height / 2);

        searchField = new TextFieldWidget(
            this.textRenderer,
            centerX - 100,
            centerY - 50,
            200,
            20,
            Text.literal("search chests")
        );

        searchField.setMaxLength(60);
        searchField.setDrawsBackground(true);
        searchField.setVisible(true);
        searchField.setEditable(true);
    
        addDrawableChild(searchField);
    }
    @Override
    public void render(DrawContext context,int mouseX, int mouseY, float delta){
        renderBackground(context,mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, this.title, this.width / 2, 20 , 0xFFFFFF);

        super.render(context,mouseX, mouseY, delta);
    }

}
