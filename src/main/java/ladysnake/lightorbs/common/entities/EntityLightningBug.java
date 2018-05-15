package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.config.LightOrbsConfig;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.Random;

public class EntityLightningBug extends EntityFirefly {

    public EntityLightningBug(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    public EntityLightningBug(World worldIn) {
        super(worldIn);
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    @Override
    public boolean getCanSpawnHere() {
        // if night time, superior than sea level and thundering
        if (LightOrbsConfig.spawnLightningBugs && !this.world.isDaytime() && this.getPosition().getY() >= this.world.getSeaLevel() && this.world.isThundering()) {
            if (LightOrbsConfig.lightningBugSwarmMinSize > 0 && LightOrbsConfig.lightningBugSwarmMaxSize >= LightOrbsConfig.lightningBugSwarmMinSize) {
                int swarmSize = new Random().nextInt(LightOrbsConfig.lightningBugSwarmMaxSize - LightOrbsConfig.lightningBugSwarmMinSize) + LightOrbsConfig.lightningBugSwarmMinSize;
                for (int i = 0; i < swarmSize; i++)
                    this.world.spawnEntity(new EntityLightningBug(this.world, this.posX, this.posY, this.posZ));
                this.setDead();
                return true;
            } else return false;
        } else return false;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source == DamageSource.LIGHTNING_BOLT;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

}
