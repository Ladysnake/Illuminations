package ladysnake.illuminations.client.render.entity.model.hat;

import ladysnake.illuminations.client.Illuminations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class VoidheartTiaraModel extends OverheadModel {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(Illuminations.MODID, "voidheart_tiara"), "main");

    public VoidheartTiaraModel(EntityRendererFactory.Context ctx) {
        super(ctx, MODEL_LAYER);

        ModelPart crown = this.head.getChild("crown");
        ModelPart south_r1 = crown.getChild("south_r1");
        ModelPart east_r1 = crown.getChild("east_r1");
        ModelPart north_r1 = crown.getChild("north_r1");
        ModelPart west_r1 = crown.getChild("west_r1");
        setRotationAngle(east_r1, -0.2618F, 1.5708F, 0.0F);
        setRotationAngle(north_r1, -0.2618F, 3.1416F, 0.0F);
        setRotationAngle(west_r1, -0.2618F, -1.5708F, 0.0F);
        setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-4.0f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData modelPartData2 = modelPartData1.addChild("crown", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
        modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(0, 36).cuboid(-7.0F, -11.0F, 3.0F, 11.0F, 11.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
        modelPartData2.addChild("north_r1", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.5F, -7.0F, 0.0F));
        modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(0, 25).cuboid(-5.0F, -11.0F, 3.0F, 10.0F, 11.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, -6.0F, 1.0F));
        modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(12, 39).cuboid(-5.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.5F, -7.0F, 0.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}