package ales.searchchest;


import org.joml.Matrix4f;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

//class to render lines around a specified block

public class RenderAroundBlock {
    
    //handles the actual creation of the lines based on the block position and camera postion 
    private void renderGreenBlockOutline(WorldRenderContext context,BlockPos blockPos){

        MatrixStack matrices = context.matrixStack();
        Camera camera = context.camera();

        Vec3d cameraPos = camera.getPos();

        matrices.push();

        matrices.translate(
        blockPos.getX() - cameraPos.x,
        blockPos.getY() - cameraPos.y,
        blockPos.getZ() - cameraPos.z
        );

        VertexConsumerProvider.Immediate renderBuffers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer = renderBuffers.getBuffer(RenderLayer.LINES);

        final float MIN = 0.0f;
        final float MAX = 1.0f;

        final float R = 0.0f, G = 1.0f, B = 0.0f, A = 0.8f;

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        addLine(matrix, vertexConsumer, MIN, MIN, MIN, MAX, MIN, MIN, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MIN, MIN, MAX, MIN, MAX, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MIN, MAX, MIN, MIN, MAX, R, G, B, A);
        addLine(matrix, vertexConsumer, MIN, MIN, MAX, MIN, MIN, MIN, R, G, B, A);

        addLine(matrix, vertexConsumer, MIN, MIN, MIN, MIN, MAX, MIN, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MIN, MIN, MAX, MAX, MIN, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MIN, MAX, MAX, MAX, MAX, R, G, B, A);
        addLine(matrix, vertexConsumer, MIN, MIN, MAX, MIN, MAX, MAX, R, G, B, A);

        addLine(matrix, vertexConsumer, MIN, MAX, MIN, MAX, MAX, MIN, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MAX, MIN, MAX, MAX, MAX, R, G, B, A);
        addLine(matrix, vertexConsumer, MAX, MAX, MAX, MIN, MAX, MAX, R, G, B, A);
        addLine(matrix, vertexConsumer, MIN, MAX, MAX, MIN, MAX, MIN, R, G, B, A);
        
    renderBuffers.draw();

    matrices.pop();
    
    }

    //add line function to streamline the repeated process of drawing lines for a whole block
    private static void addLine(Matrix4f matrix, VertexConsumer buffer,
    float x1, float y1, float z1,
    float x2, float y2, float z2,
    float r, float g, float b, float a){
        buffer.vertex(matrix,x1,y1,z1).color(r,g,b,a).normal(0, 0,0);
        buffer.vertex(matrix,x2,y2,z2).color(r,g,b,a).normal(0, 0,0);
    }

    private static BlockPos currentBlockPos;
    private static boolean isInitialized = false;
    private static final RenderAroundBlock blockRenderer = new RenderAroundBlock();
    public static void initRender(BlockPos blockPos,CustomButton button){ //actually starts renders with specification of only having one renderer (initialization checks) renders after everything else is rendered and checks the menu didnt open again and a button was pressed to stop/start rendering
        currentBlockPos = blockPos; //after first init the method is already registered we just need to change the block its rendering around
        if(!isInitialized){
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context ->{
            if((!(KeyInputHandler.getmenuOpen())) && button.getButtonPressed()){ 
                blockRenderer.renderGreenBlockOutline(context, currentBlockPos);
            }   
        });
    isInitialized = true;
    }
    

}
    

}
