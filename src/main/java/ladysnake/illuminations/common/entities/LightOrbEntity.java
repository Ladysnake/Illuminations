package ladysnake.illuminations.common.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class LightOrbEntity extends FlyingEntity {

    public LightOrbEntity(EntityType<? extends LightOrbEntity> entityType, World worldIn) {
        super(entityType, worldIn);
//        this.setUnaffectedByGravity(true);
//        this.yaw = (float) (Math.random() * 360.0D);
//        this.velocityX = (Math.random() * 0.2 - 0.1) * 2.0F;
//        this.velocityY = (Math.random() * 0.2) * 2.0F;
//        this.velocityZ = (Math.random() * 0.2 - 0.1) * 2.0F;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void pushAway(Entity entityIn) {
    }

    @Override
    public void pushAwayFrom(Entity entityIn) {
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean canClimb() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource_1) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

}
