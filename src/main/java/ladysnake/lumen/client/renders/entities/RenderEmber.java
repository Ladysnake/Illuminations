package ladysnake.lumen.client.renders.entities;

import ladysnake.lumen.common.Lumen;
import ladysnake.lumen.common.entities.EntityFirefly;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderEmber<T extends Entity> extends Render<T> {
    public RenderEmber(RenderManager renderManager) {
        super(renderManager);
        this.shadowOpaque = 0;
    }

    @Override
    public void doRender(@Nonnull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            GlStateManager.pushMatrix();

            GlStateManager.translate((float) x, (float) y + 0.1f, (float) z);

            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

            GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

            this.bindEntityTexture(entity);
            if (entity instanceof EntityFirefly) {
                float scale = ((EntityFirefly) entity).getScaleModifier();
                float color = ((EntityFirefly) entity).getColorModifier();
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.color(1F, color, 0F);
            }

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            float minU = 0;
            float minV = 0;
            float maxU = 1;
            float maxV = 1;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex((double) maxU, (double) maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex((double) minU, (double) maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex((double) minU, (double) minV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex((double) maxU, (double) minV).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();

            this.bindTexture(new ResourceLocation(Lumen.MOD_ID, "textures/entities/firefly_overlay.png"));
            GlStateManager.color(1F, 1F, 0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex((double) maxU, (double) maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex((double) minU, (double) maxV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex((double) minU, (double) minV).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex((double) maxU, (double) minV).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();

            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    public void doRenderShadowAndFire(@Nonnull Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull T entity) {
        return new ResourceLocation(Lumen.MOD_ID, "textures/entities/firefly.png");
    }
}
