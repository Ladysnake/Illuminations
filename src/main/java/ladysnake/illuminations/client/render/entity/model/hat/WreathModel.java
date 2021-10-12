package ladysnake.illuminations.client.render.entity.model.hat;

import ladysnake.illuminations.client.Illuminations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WreathModel extends OverheadModel {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(Illuminations.MODID, "wreath"), "main");

    public WreathModel(EntityRendererFactory.Context ctx) {
        super(ctx, MODEL_LAYER);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData1.addChild("wreath", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -34.5F, -5.0F, 10.0F, 5.0F, 8.0F, new Dilation(0.5f)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 48, 16);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}