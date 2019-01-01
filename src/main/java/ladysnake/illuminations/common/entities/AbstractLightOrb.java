package ladysnake.illuminations.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class AbstractLightOrb extends Entity {

    public AbstractLightOrb(EntityType entityType, World worldIn) {
        super(entityType, worldIn);
        this.setSize(0.5F, 0.5F);
        this.setUnaffectedByGravity(true);
        this.yaw = (float) (Math.random() * 360.0D);
        this.velocityX = (Math.random() * 0.2 - 0.1) * 2.0F;
        this.velocityY = (Math.random() * 0.2) * 2.0F;
        this.velocityZ = (Math.random() * 0.2 - 0.1) * 2.0F;
    }

    @Override
    public void update() {
        super.update();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean isUnaffectedByGravity() {
        return true;
    }
//
//    @Override
//    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
//    }
//
//    @Override
//    protected void collideWithEntity(Entity entityIn) {
//    }
//
//    @Override
//    protected void collideWithNearbyEntities() {
//    }
//
//    @Override
//    public void onCollideWithPlayer(EntityPlayer entityIn) {
//    }
//
//    @Override
//    public boolean doesEntityNotTriggerPressurePlate() {
//        return true;
//    }

}
