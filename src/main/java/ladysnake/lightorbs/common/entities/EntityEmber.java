package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.config.LightOrbsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.Random;

public class EntityEmber extends EntityFirefly {

    public EntityEmber(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    public EntityEmber(World worldIn) {
        super(worldIn);
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    @Override
    public boolean getCanSpawnHere() {
        // if night time, superior than sea level and thundering
        if (LightOrbsConfig.spawnEmbers) {
            if (LightOrbsConfig.emberSwarmMinSize > 0 && LightOrbsConfig.emberSwarmMaxSize >= LightOrbsConfig.emberSwarmMinSize) {
                int swarmSize = new Random().nextInt(LightOrbsConfig.emberSwarmMaxSize - LightOrbsConfig.emberSwarmMinSize) + LightOrbsConfig.emberSwarmMinSize;
                for (int i = 0; i < swarmSize; i++)
                    this.world.spawnEntity(new EntityEmber(this.world, this.posX, this.posY, this.posZ));
                this.setDead();
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source == DamageSource.LAVA || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.HOT_FLOOR;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        entityIn.setFire(2);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.setFire(2);
    }


}
