package ladysnake.lightorbs.common.entities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;

public class EntityEmber extends AbstractLightorb {
    protected float scaleModifier;
    protected float colorModifier;

    public float getScaleModifier() {
        return scaleModifier;
    }

    public float getColorModifier() {
        return colorModifier;
    }

    public EntityEmber(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityEmber(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0F);
        this.setHealth(1.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.4F;
        this.colorModifier = new Random().nextFloat() * 0.5F;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        entityIn.setFire(2);
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        entityIn.setFire(2);
    }

    protected BlockPos forcedTarget = BlockPos.ORIGIN;
    protected BlockPos lightTarget = null;
    protected double xTarget, yTarget, zTarget;
    protected int targetChangeCooldown = 0;

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    protected void selectBlockTarget() {
        if (this.forcedTarget == BlockPos.ORIGIN) {
            this.xTarget = this.posX + rand.nextGaussian() * 10;
            this.yTarget = this.posY + rand.nextGaussian() * 10;
            this.zTarget = this.posZ + rand.nextGaussian() * 10;

            while (!(this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getMaterial() == Material.AIR)) this.yTarget += 1;
        } else {
            this.xTarget = forcedTarget.getX() + rand.nextGaussian();
            this.yTarget = forcedTarget.getY() + rand.nextGaussian();
            this.zTarget = forcedTarget.getZ() + rand.nextGaussian();
        }

        targetChangeCooldown = rand.nextInt() % 200;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.posY > 300)
            this.outOfWorld();

        if (!this.world.isRemote && !this.isDead) {
            this.targetChangeCooldown -= (this.getPositionVector().squareDistanceTo(lastTickPosX, lastTickPosY, lastTickPosZ) < 0.0125) ? 10 : 1;

            // Change the target position regularly to simulate erratic movement
            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPosition().distanceSq(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            // targeted entities take precedence over target positions
            Vec3d targetVector = new Vec3d(this.xTarget - posX, this.yTarget - posY, this.zTarget - posZ);
            double length = targetVector.lengthVector();
            targetVector = targetVector.scale(0.1 / length);
            double weight = 0;
            // the range at which the wisp will start orbiting
            final double outerRange = 6;
            // the range at which the wisp will stop approaching its target
            final double innerRange = 1;
            if (length > innerRange && length < outerRange)
                weight = 0.25 * ((outerRange - length) / (outerRange - innerRange));
            else if (length <= innerRange)
                weight = 1;
            motionX = (1 - weight) * ((0.9) * motionX + (0.1) * targetVector.x) + weight * targetVector.z;
            motionY = (1 - weight) * ((0.9) * motionY + (0.1) * targetVector.y) + weight * targetVector.y;
            motionZ = (1 - weight) * ((0.9) * motionZ + (0.1) * targetVector.z) - weight * targetVector.x;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            if (this.isInWater()) this.attackEntityFrom(DamageSource.DROWN, 1);
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    public boolean isCreatureType(@Nonnull EnumCreatureType type, boolean forSpawnCount) {
        return type == EnumCreatureType.AMBIENT;
    }

    public void setForcedTarget(@Nonnull BlockPos target) {
        this.forcedTarget = target;
    }

    @Override
    public boolean getCanSpawnHere() {
        int rand = new Random().nextInt(16)+10;
        for (int i = 0; i < rand; i++)
            this.world.spawnEntity(new EntityEmber(this.world, this.posX, this.posY, this.posZ));
        return true;
    }

}
