package ladysnake.illuminations.common.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.sortme.Living;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractLightOrb extends MobEntityWithAi implements Living {

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

    @Override
    public boolean isUnaffectedByGravity() {
        return true;
    }

    @Override
    protected void method_5623(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
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
    public boolean shouldRenderAtDistance(double double_1) {
        return true;
    }

    @Override
    public boolean method_5658() {
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
