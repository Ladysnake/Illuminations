package ladysnake.lightorbs.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public abstract class AbstractLightorb extends EntityCreature {

    public AbstractLightorb(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (Math.random() * 0.2 - 0.1) * 2.0F;
        this.motionY = (Math.random() * 0.2) * 2.0F;
        this.motionZ = (Math.random() * 0.2 - 0.1) * 2.0F;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

}
