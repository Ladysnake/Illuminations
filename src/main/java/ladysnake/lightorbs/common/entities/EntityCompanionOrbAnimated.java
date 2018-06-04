package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.init.ModEntities;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityCompanionOrbAnimated extends EntityCompanionOrb {

    public EntityCompanionOrbAnimated(World world) {
        super(world);
    }

    public EntityCompanionOrbAnimated(World world, double x, double y, double z, UUID ownerUUID, ModEntities.Companion type) {
        super(world, x, y, z, ownerUUID, type);
    }

}
