package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.LightOrbs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EntityFirefly extends EntityFlyingInsect {
    public EntityFirefly(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityFirefly(World worldIn) {
        super(worldIn);
    }

    protected BlockPos forcedTarget = BlockPos.ORIGIN;
    protected double xTarget, yTarget, zTarget;
    protected int targetChangeCooldown = 0;

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    protected void selectBlockTarget() {
        if(this.forcedTarget == BlockPos.ORIGIN) {
            this.xTarget = this.posX + rand.nextGaussian() * 10;
            this.yTarget = this.posY + rand.nextGaussian() * 10;
            this.zTarget = this.posZ + rand.nextGaussian() * 10;
        } else {
            this.xTarget = forcedTarget.getX() + 0.5;
            this.yTarget = forcedTarget.getY() + rand.nextGaussian();
            this.zTarget = forcedTarget.getZ() + 0.5;
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
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticles() {
        for (double i = 0; i < 9; i++) {
            double coeff = i / 9.0;
            LightOrbs.proxy.spawnParticle(getEntityWorld(),
                    (float) (prevPosX + (posX - prevPosX) * coeff), (float) (prevPosY + (posY - prevPosY) * coeff), (float) (prevPosZ + (posZ - prevPosZ) * coeff),    //position
                    0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f), 0.0125f * (rand.nextFloat() - 0.5f),    //motion
                    255, 64, 16, 255,    //color
                    2.0f, 24);
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

}
