package ladysnake.illuminations.client.renders.entities;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.entities.FireflyEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class FireflyRender<T extends FireflyEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(Illuminations.MOD_ID, "textures/entity/firefly.png");
    private static final Identifier OVERLAY = new Identifier(Illuminations.MOD_ID, "textures/entity/firefly_overlay.png");
    private static final RenderLayer TEXTURE_LAYER = RenderLayer.getEntityTranslucent(TEXTURE);
    private static final RenderLayer OVERLAY_LAYER = RenderLayer.getEntityTranslucent(OVERLAY);

    public FireflyRender(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        this.shadowRadius = 0F;
        this.shadowOpacity = 0F;
    }

    protected int getBlockLight(T firefly, float f) {
        return MathHelper.clamp(super.getLight(firefly, f) + 7, 0, 15);
    }

    public void render(T firefly, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float ux = 0;
        float uy = 1;
        float vx = 0;
        float vy = 1;
        int red = (int) (firefly.getColorModifier() * 255);
        float scale = firefly.getScaleFactor();
        matrixStack.translate(0, 0, 0);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        matrixStack.scale(scale, scale, scale);

        int alpha = firefly.getAlpha();
        int nextAlphaGoal = firefly.getNextAlphaGoal();

        // if just spawned
        if (firefly.age < 50) {
            alpha = 0;
        } else {
            // if day
            float tod = firefly.world.getLevelProperties().getTimeOfDay() % 24000;
            if (tod >= 1000 && tod < 13000) {
                nextAlphaGoal = 0;
            }

            if (alpha > nextAlphaGoal - 10 && alpha < nextAlphaGoal + 10) {
                firefly.setNextAlphaGoal(firefly.getRandom().nextInt(256));
            } else {
                if (nextAlphaGoal > alpha) {
                    alpha += 10;
                } else if (nextAlphaGoal < alpha) {
                    alpha -= 10;
                }
            }
        }

        if (MinecraftClient.getInstance().isPaused()) {
            alpha = firefly.getAlpha();
        }
        firefly.setAlpha(Math.min(Math.max(alpha, 0), 255));
        alpha = firefly.getAlpha();

        // firefly
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(TEXTURE_LAYER);
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getModel();
        Matrix3f matrix3f = entry.getNormal();
        method_23171(vertexConsumer, matrix4f, matrix3f, -0.5F, -0.25F, red, 0, alpha, ux, vy);
        method_23171(vertexConsumer, matrix4f, matrix3f, 0.5F, -0.25F, red, 0, alpha, uy, vy);
        method_23171(vertexConsumer, matrix4f, matrix3f, 0.5F, 0.75F, red, 0, alpha, uy, vx);
        method_23171(vertexConsumer, matrix4f, matrix3f, -0.5F, 0.75F, red, 0, alpha, ux, vx);

        // firefly overlay
        vertexConsumer = vertexConsumerProvider.getBuffer(OVERLAY_LAYER);
        method_23171(vertexConsumer, matrix4f, matrix3f, -0.5F, -0.25F, 255, 255, alpha, ux, vy);
        method_23171(vertexConsumer, matrix4f, matrix3f, 0.5F, -0.25F, 255, 255, alpha, uy, vy);
        method_23171(vertexConsumer, matrix4f, matrix3f, 0.5F, 0.75F, 255, 255, alpha, uy, vx);
        method_23171(vertexConsumer, matrix4f, matrix3f, -0.5F, 0.75F, 255, 255, alpha, ux, vx);

        matrixStack.pop();
        super.render(firefly, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void method_23171(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, int red, int blue, int alpha, float u, float v) {
        vertexConsumer.vertex(matrix4f, x, y, 0.0F).color(red, 255, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
    }

    public Identifier getTexture(T firefly) {
        return TEXTURE;
    }

}
