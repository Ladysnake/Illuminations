package ladysnake.illuminations.client.renders.entities;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import ladysnake.illuminations.common.Illuminations;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;


public class WillOWispRender<T extends Entity> extends EntityRenderer<T> {
    public WillOWispRender(EntityRenderDispatcher renderManager) {
        super(renderManager);
        this.field_4672 = 0;
    }

    @Override
    public void render(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            GlStateManager.pushMatrix();

            GlStateManager.translatef((float) x, (float) y + 0.1f, (float) z);

            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();

            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240f, 240f);

            GlStateManager.rotatef(180.0F - this.renderManager.field_4679, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((float) (this.renderManager.settings.field_1850 == 2 ? -1 : 1) * -this.renderManager.field_4677, 1.0F, 0.0F, 0.0F);

            this.bindEntityTexture((T) entity);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
            int i = entity.age % 34 / 2;
            float minU = 0;
            float minV = (i % 17 * 16) / 272f;
            float maxU = 1;
            float maxV = (i % 17 * 16 + 16) / 272f;
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
            super.render((T) entity, x, y, z, entityYaw, partialTicks);
        }
    }

//    @Override
//    public void renderShadow(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
//    }

    @Override
    protected Identifier getTexture( T entity) {
        return new Identifier(Illuminations.MOD_ID, "textures/entity/will_o_wisp.png");
    }

}
