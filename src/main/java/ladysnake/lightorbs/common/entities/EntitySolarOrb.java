package ladysnake.lightorbs.common.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntitySolarOrb extends AbstractCompanionOrb {

    public EntitySolarOrb(World world) {
        super(world);
    }

    public EntitySolarOrb(World world, double x, double y, double z, EntityPlayer player) {
        super(world, x, y, z, player);
    }

}
