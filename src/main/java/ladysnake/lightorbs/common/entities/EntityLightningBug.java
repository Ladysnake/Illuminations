package ladysnake.lightorbs.common.entities;

import net.minecraft.entity.effect.EntityLightningBolt;
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
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        // if night time, superior than sea level and thundering
        if (!this.world.isDaytime() && this.getPosition().getY() >= this.world.getSeaLevel() && this.world.isThundering()) {
            int rand = new Random().nextInt(16)+10;
            for (int i = 0; i < rand; i++)
                this.world.spawnEntity(new EntityLightningBug(this.world, this.posX, this.posY, this.posZ));
            return true;
        } else return false;
    }

}
