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
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CrownFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    private final CrownEntityModel<T> crown = new CrownEntityModel<>();

    public CrownFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if (livingEntity instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) livingEntity;

            if (IlluminationsClient.PLAYER_COSMETICS.get(abstractClientPlayerEntity.getUuid()) != null) {
                String playerOverhead = IlluminationsClient.PLAYER_COSMETICS.get(abstractClientPlayerEntity.getUuid()).getOverhead();
                if (playerOverhead != null && IlluminationsClient.CROWNS_DATA.containsKey(playerOverhead)) {
                    Identifier identifier4 = IlluminationsClient.CROWNS_DATA.get(playerOverhead);

                    float o = MathHelper.lerp(h, abstractClientPlayerEntity.prevYaw, abstractClientPlayerEntity.yaw) - MathHelper.lerp(h, abstractClientPlayerEntity.prevBodyYaw, abstractClientPlayerEntity.bodyYaw);
                    float p = MathHelper.lerp(h, abstractClientPlayerEntity.prevPitch, abstractClientPlayerEntity.pitch);
                    matrixStack.push();
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(o));
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(p));
                    this.crown.crown.pivotX = 0;
                    this.crown.crown.pivotY = 0;
                    matrixStack.translate(0.0D, -0.55D - Math.sin(livingEntity.age / 30f) / 30f, 0.0D);
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-p));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-o));
                    this.crown.setAngles(livingEntity, f, g, j, k, l);
                    VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, CrownRenderLayer.getCrown(identifier4), true, true);

                    this.crown.crown.copyPositionAndRotation(this.getContextModel().head);
                    this.crown.render(matrixStack, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                    matrixStack.pop();
                }
            }
        }
    }

}
