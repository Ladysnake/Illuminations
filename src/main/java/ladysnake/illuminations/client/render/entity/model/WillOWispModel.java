package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class WillOWispModel extends EntityModel<Entity> {
    private final ModelPart skull;

    public WillOWispModel() {
        textureWidth = 32;
        textureHeight = 32;
        skull = new ModelPart(this);
        skull.setPivot(0.0F, 16.0F, 0.0F);
        skull.setTextureOffset(0, 0).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        skull.setTextureOffset(0, 16).addCuboid(-3.0F, -3.0F, -3.0F, 6.0F, 7.0F, 6.0F, 0.25F, false);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        skull.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}