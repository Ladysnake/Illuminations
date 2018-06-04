package ladysnake.lightorbs.client.renders.entities;

import ladysnake.lightorbs.common.LightOrbs;
import ladysnake.lightorbs.common.entities.EntityFaerie;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderFaerie extends RenderWillOWisp<EntityFaerie>{

    public RenderFaerie(RenderManager renderManager) {
        super(renderManager);
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityFaerie entity) {
        return new ResourceLocation(LightOrbs.MOD_ID, "textures/entities/faerie.png");
    }

}
