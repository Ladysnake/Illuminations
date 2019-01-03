package ladysnake.illuminations.common.entities;

import net.minecraft.entity.SpawnType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class EntityLightningBug extends EntityFirefly {

    public EntityLightningBug(World world) {
        super(world);
        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return !this.world.isDaylight() && this.world.isThundering();
    }

}
