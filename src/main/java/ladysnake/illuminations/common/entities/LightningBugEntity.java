package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.SpawnType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class LightningBugEntity extends FireflyEntity {

    public LightningBugEntity(World world) {
        super(IlluminationsEntities.LIGHTNING_BUG, world);

        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.15F;
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
        this.alpha = 1F;

        this.setSize(this.scaleModifier, this.scaleModifier);
        this.canDespawn = true;
        this.isAttractedByLight = true;
        this.despawnOnDaytime = true;
    }

    public LightningBugEntity(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return !this.world.isDaylight() && this.world.isThundering();
    }

}
