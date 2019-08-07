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
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Random;


public class FireflyRender<E extends FireflyEntity> extends EntityRenderer<E> {
    public FireflyRender(EntityRenderDispatcher renderManager) {
        super(renderManager);
        this.field_4672 = 0;
    }

    @Override
    public void render(@Nonnull E entity, double x, double y, double z, float entityYaw, float tickDelta) {
        if (!this.renderOutlines) {
            GlStateManager.pushMatrix();

            GlStateManager.translatef((float) x, (float) y + 0.1f, (float) z);

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();

            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240f, 240f);

            GlStateManager.rotatef(180.0F - this.renderManager.cameraYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((float) (this.renderManager.gameOptions.perspective == 2 ? -1 : 1) * -this.renderManager.cameraPitch, 1.0F, 0.0F, 0.0F);

            this.bindEntityTexture(entity);
            float alpha = entity.getAlpha();
            float scale = entity.getScaleModifier();
            float color = entity.getColorModifier();
            Float nextAlphaGoal = entity.getNextAlphaGoal();

            // if just spawned
            if (entity.age < 50) {
                alpha = 0;
            }

            // if day
            float tod = entity.world.getLevelProperties().getTimeOfDay();
            if (tod >= 1000 && tod < 13000) {
                nextAlphaGoal = 0.0F;
            }

            if (nextAlphaGoal == null || nextAlphaGoal.equals(round(alpha, 1))) {
                entity.setNextAlphaGoal(new Random().nextInt(11) / 10.0F);
            } else {
                if (nextAlphaGoal > alpha) {
                    alpha += 0.05F;
                } else if (nextAlphaGoal < alpha) {
                    alpha -= 0.05F;
                }
            }

            entity.setAlpha(Math.min(Math.max(alpha, 0), 1));
            GlStateManager.scalef(scale, scale, scale);
            GlStateManager.color4f(color, 1F, 0F, entity.getAlpha());

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

            this.bindTexture(new Identifier(Illuminations.MOD_ID, "textures/entity/firefly_overlay.png"));
            GlStateManager.color4f(1F, 1F, 1F, entity.getAlpha());
            bufferbuilder.begin(7, VertexFormats.POSITION_UV_NORMAL);
            bufferbuilder.vertex(-0.5D, -0.25D, 0.0D).texture((double) maxU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, -0.25D, 0.0D).texture((double) minU, (double) maxV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(0.5D, 0.75D, 0.0D).texture((double) minU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            bufferbuilder.vertex(-0.5D, 0.75D, 0.0D).texture((double) maxU, (double) minV).normal(0.0F, 1.0F, 0.0F).next();
            tessellator.draw();

            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            super.render(entity, x, y, z, entityYaw, tickDelta);
        }
    }

//    @Override
//    public void renderShadow(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
//    }

    @Override
    protected Identifier getTexture( E entity) {
        return new Identifier(Illuminations.MOD_ID, "textures/entity/firefly.png");
    }

    // Useful method
    private static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

}
