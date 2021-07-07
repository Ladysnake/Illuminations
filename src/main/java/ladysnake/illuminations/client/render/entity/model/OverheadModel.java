package ladysnake.illuminations.client.render.entity.model;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class OverheadModel extends Model {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(IlluminationsClient.MODID, "overhead"), "main");
    public final ModelPart head;

    public OverheadModel(EntityRendererFactory.Context ctx) {
        super(RenderLayer::getEntityTranslucent);
        this.head = ctx.getPart(MODEL_LAYER).getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 7).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 0, 0);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
