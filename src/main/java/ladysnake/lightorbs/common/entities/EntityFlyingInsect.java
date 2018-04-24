package ladysnake.lightorbs.common.entities;

import net.minecraft.world.World;

public abstract class EntityFlyingInsect extends EntityLightOrb {
    public EntityFlyingInsect(World worldIn) {
        super(worldIn);
        this.setEntityInvulnerable(false);
        this.isImmuneToFire = false;
    }

    @Override
    public boolean canRenderOnFire() {
        return true;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return false;
    }

    @Override
    public void setFire(int seconds) {
        super.setFire(seconds);
    }

}
