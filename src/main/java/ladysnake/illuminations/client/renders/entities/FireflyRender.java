package ladysnake.illuminations.client.renders.entities;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.entities.FireflyEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;


public class FireflyRender<T extends Entity> extends EntityRenderer<T> {
    public static final Identifier FIREFLY_TEXTURE = new Identifier(Illuminations.MOD_ID, "textures/entity/firefly.png");
    public static final Identifier FIREFLY_OVERLAY_TEXTURE = new Identifier(Illuminations.MOD_ID, "textures/entity/firefly_overlay.png");

    public FireflyRender(EntityRenderDispatcher renderManager) {
        super(renderManager);
        this.field_4672 = 0;
    }

    @Override
    public void render(T entity, double x, double y, double z, float entityYaw, float tickDelta) {
        if (!this.renderOutlines) {
            GlStateManager.pushMatrix();

            GlStateManager.translatef((float) x, (float) y + 0.1f, (float) z);

            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();

            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240f, 240f);

            GlStateManager.rotatef(180.0F - this.renderManager.cameraYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((float) (this.renderManager.gameOptions.perspective == 2 ? -1 : 1) * -this.renderManager.cameraPitch, 1.0F, 0.0F, 0.0F);

            this.bindEntityTexture(entity);
            if (entity instanceof FireflyEntity) {
                FireflyEntity firefly = (FireflyEntity) entity;

                int alpha = firefly.getAlpha();
                float scale = firefly.getScaleModifier();
                float color = firefly.getColorModifier();
                int nextAlphaGoal = firefly.getNextAlphaGoal();

                // if day
                float tod = entity.world.getLevelProperties().getTimeOfDay();
                if (tod >= 1000 && tod < 13000) {
                    nextAlphaGoal = 0;
                }

                // select next alpha goal
                if (nextAlphaGoal == alpha) {
                    firefly.setNextAlphaGoal(firefly.getRand().nextInt(FireflyEntity.ALPHA_MAX + 1));
                } else {
                    if (nextAlphaGoal > alpha) {
                        alpha += 1;
                    } else {
                        alpha -= 1;
                    }
                }

                firefly.setAlpha(Math.min(Math.max(alpha, 0), FireflyEntity.ALPHA_MAX));
                GlStateManager.scalef(scale, scale, scale);
                GlStateManager.color4f(color, 1F, 0F, firefly.getAlpha() / (float) FireflyEntity.ALPHA_MAX);
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
            float minU = 0;
            float minV = 0;
            float maxU = 1;
            float maxV = 1;
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_NORMAL);
            bufferbuilder.vertex(-0.5D, -0.25D, 0.0D).texture((double) maxU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, -0.25D, 0.0D).texture((double) minU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, 0.75D, 0.0D).texture((double) minU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(-0.5D, 0.75D, 0.0D).texture((double) maxU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            tessellator.draw();

            this.bindTexture(FIREFLY_OVERLAY_TEXTURE);
            GlStateManager.color4f(1F, 1F, 1F, ((FireflyEntity) entity).getAlpha() / (float) FireflyEntity.ALPHA_MAX);
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_NORMAL);
            bufferbuilder.vertex(-0.5D, -0.25D, 0.0D).texture((double) maxU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, -0.25D, 0.0D).texture((double) minU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, 0.75D, 0.0D).texture((double) minU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(-0.5D, 0.75D, 0.0D).texture((double) maxU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            tessellator.draw();

            GlStateManager.disableAlphaTest();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            super.render(entity, x, y, z, entityYaw, tickDelta);
        }
    }

    @Override
    protected Identifier getTexture(T entity) {
        return FIREFLY_TEXTURE;
    }

}
