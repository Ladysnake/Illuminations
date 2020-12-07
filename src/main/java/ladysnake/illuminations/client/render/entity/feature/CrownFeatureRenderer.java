package ladysnake.illuminations.client.render.entity.feature;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.CrownRenderLayer;
import ladysnake.illuminations.client.render.entity.model.CrownEntityModel;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import static net.minecraft.client.render.RenderLayer.of;

public class CrownFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier SKIN = new Identifier("illuminations", "textures/entity/solar_crown.png");
    private final CrownEntityModel<T> crown = new CrownEntityModel<>();

    public CrownFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        Identifier identifier4 = SKIN;

        matrixStack.push();
        matrixStack.translate(0.0D, -2.05D - Math.sin(livingEntity.age/20f)/20f, 0.0D);
        this.getContextModel().copyStateTo(this.crown);
        this.crown.setAngles(livingEntity, f, g, j, k, l);
        this.crown.
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, CrownRenderLayer.getCrown(identifier4), false, true);
        this.crown.render(matrixStack, vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

}
