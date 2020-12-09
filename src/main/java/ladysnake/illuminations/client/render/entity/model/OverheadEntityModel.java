// Made with Blockbench 3.7.4

package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class OverheadEntityModel extends EntityModel<Entity> {
    public final ModelPart head;

    public OverheadEntityModel() {
        textureWidth = 32;
        textureHeight = 48;
        head = new ModelPart(this);
        head.setPivot(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}