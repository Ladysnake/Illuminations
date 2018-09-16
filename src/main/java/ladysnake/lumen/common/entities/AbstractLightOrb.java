package ladysnake.lumen.common.entities;

import ladylib.client.lighting.MutableCheapLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractLightOrb extends EntityCreature {

    public AbstractLightOrb(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (Math.random() * 0.2 - 0.1) * 2.0F;
        this.motionY = (Math.random() * 0.2) * 2.0F;
        this.motionZ = (Math.random() * 0.2 - 0.1) * 2.0F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Nullable
    public MutableCheapLight createLight() {
        return null;
//        return new SimpleCheapLight(this.getPositionVector(), radius, new Color(r, g, b, a));
    }

}
