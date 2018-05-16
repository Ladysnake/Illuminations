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
        return LightOrbsConfig.spawnLightningBugs && !this.world.isDaytime() && this.world.isThundering() && super.getCanSpawnHere();
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
