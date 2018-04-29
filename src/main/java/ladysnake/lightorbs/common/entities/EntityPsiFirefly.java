package ladysnake.lightorbs.common.entities;

import net.minecraft.world.World;

import java.util.Random;

public class EntityPsiFirefly extends EntityFirefly {

    public EntityPsiFirefly(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityPsiFirefly(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        int rand = new Random().nextInt(16)+10;
        for (int i = 0; i < rand; i++)
            this.world.spawnEntity(new EntityPsiFirefly(this.world, this.posX, this.posY, this.posZ));
        return true;
    }

}
