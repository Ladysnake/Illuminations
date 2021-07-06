package ladysnake.illuminations.client.render.entity.model;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class OverheadModel extends Model {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(IlluminationsClient.MODID, "overhead"), "main");
    public final ModelPart head;

    public OverheadModel(EntityRendererFactory.Context ctx) {
        super(RenderLayer::getEntityCutout);
        this.head = ctx.getPart(MODEL_LAYER).getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData modelPartData2 = modelPartData1.addChild("crown", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
        modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(7, 39).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
        modelPartData2.addChild("north_r1", ModelPartBuilder.create().uv(7, 15).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
        modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(7, 23).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
        modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(7, 31).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F), ModelTransform.pivot(0.0F, -7.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
    }

//    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
//        bone.pitch = x;
//        bone.yaw = y;
//        bone.roll = z;
//    }
}
