package ladysnake.lightorbs.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityLightOrb extends Entity {

    public EntityLightOrb(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1F);
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
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

}
