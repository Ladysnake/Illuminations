package ladysnake.lightorbs.common.entities;

import net.minecraft.world.World;

import java.util.Random;

public class EntityLightningBug extends EntityFirefly {

    public EntityLightningBug(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityLightningBug(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.world.isThundering()) {
            int rand = new Random().nextInt(16) + 10;
            for (int i = 0; i < rand; i++)
                this.world.spawnEntity(new EntityLightningBug(this.world, this.posX, this.posY, this.posZ));
            return true;
        } else return false;
    }

}
