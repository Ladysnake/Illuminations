package ladysnake.illuminations.client.render.entity.feature;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.CrownRenderLayer;
import ladysnake.illuminations.client.render.entity.model.CrownEntityModel;
import ladysnake.illuminations.client.render.entity.model.OverheadEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class OverheadFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public OverheadFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        OverheadEntityModel crownModel = new CrownEntityModel();
        crownModel.head.pivotX = this.getContextModel().head.pivotX;
        crownModel.head.pivotY = this.getContextModel().head.pivotY;
        crownModel.head.pitch = this.getContextModel().head.pitch;
        crownModel.head.yaw = this.getContextModel().head.yaw;
        crownModel.render(matrices, vertexConsumers.getBuffer(CrownRenderLayer.getCrown(new Identifier(IlluminationsClient.MODID, "textures/entity/solar_crown.png"))), 15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }
}
