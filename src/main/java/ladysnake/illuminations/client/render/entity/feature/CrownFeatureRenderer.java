package ladysnake.illuminations.client.render.entity.feature;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.CrownRenderLayer;
import ladysnake.illuminations.client.render.entity.model.CrownEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CrownFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final CrownEntityModel<AbstractClientPlayerEntity> crown = new CrownEntityModel<>();

    public CrownFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (IlluminationsClient.PLAYER_COSMETICS.get(entity.getUuid()) != null) {
            String playerOverhead = IlluminationsClient.PLAYER_COSMETICS.get(entity.getUuid()).getOverhead();
            if (playerOverhead != null && IlluminationsClient.CROWNS_DATA.containsKey(playerOverhead)) {
                Identifier identifier4 = IlluminationsClient.CROWNS_DATA.get(playerOverhead);

                float o = MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw) - MathHelper.lerp(tickDelta, entity.prevBodyYaw, entity.bodyYaw);
                float p = MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch);
                matrices.push();
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(o));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(p));
                matrices.translate(0.0D, -Math.sin(entity.age / 30f) / 30f, 0.0D);
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-p));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-o));
                this.crown.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, CrownRenderLayer.getCrown(identifier4), true, true);

                this.crown.crown.copyPositionAndRotation(this.getContextModel().head);
                this.crown.crown.pivotX = 0;
                this.crown.crown.pivotY = 0;
                this.crown.animateModel(entity, limbAngle, limbDistance, tickDelta);
                this.crown.render(matrices, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }
        }
    }
}
