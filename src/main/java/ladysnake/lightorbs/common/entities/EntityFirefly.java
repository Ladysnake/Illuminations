package ladysnake.lightorbs.common.entities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;

public class EntityFirefly extends AbstractLightOrb {
    private float scaleModifier;
    float colorModifier;
    private float alpha;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getScaleModifier() {
        return scaleModifier;
    }

    public float getColorModifier() {
        return colorModifier;
    }

    public EntityFirefly(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityFirefly(World worldIn) {
        super(worldIn);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0F);
        this.setHealth(1.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25F);
        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.4F;
        this.colorModifier = 0.25F + new Random().nextFloat() * 0.75F;
        this.alpha = 1F;
    }

    private BlockPos forcedTarget = BlockPos.ORIGIN;
    private BlockPos lightTarget = null;
    private double xTarget, yTarget, zTarget;
    private int targetChangeCooldown = 0;

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    private void selectBlockTarget() {
        if (this.forcedTarget == BlockPos.ORIGIN) {
            if (this.lightTarget == null) {
                this.xTarget = this.posX + rand.nextGaussian() * 10;
                this.yTarget = this.posY + rand.nextGaussian() * 10;
                this.zTarget = this.posZ + rand.nextGaussian() * 10;

                while (!(this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getMaterial() == Material.AIR)) this.yTarget += 1;

                if (this.world.getLight(this.getPosition(), true) > 8 && !this.world.isDaytime())
                    this.lightTarget = getRandomLitBlockAround();
            } else {
                this.xTarget = this.lightTarget.getX() + rand.nextGaussian();
                this.yTarget = this.lightTarget.getY() + rand.nextGaussian();
                this.zTarget = this.lightTarget.getZ() + rand.nextGaussian();

                if (this.world.getLight(this.getPosition(), true) > 8) {
                    BlockPos possibleTarget = getRandomLitBlockAround();
                    if (this.world.getLight(possibleTarget, true) > this.world.getLight(this.lightTarget, true))
                        this.lightTarget = possibleTarget;
                }

                if (this.world.getLight(this.getPosition(), true) <= 8 || this.world.isDaytime())
                    this.lightTarget = null;
            }
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
            if (this.isInLava()) this.attackEntityFrom(DamageSource.LAVA, 1);
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

    private BlockPos getRandomLitBlockAround() {
        HashMap<BlockPos, Integer> randBlocks = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            BlockPos randBP = new BlockPos(this.posX + rand.nextGaussian() * 10, this.posY + rand.nextGaussian() * 10, this.posZ + rand.nextGaussian() * 10);
            randBlocks.put(randBP, this.world.getLight(randBP, true));
        }
        return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    @Override
    public boolean getCanSpawnHere() {
        // if night time, superior than sea level and not raining
        // spawn additional fireflies for swarm effect, and avoid Minecraft spawn entity limit
        if (!this.world.isDaytime() && this.getPosition().getY() >= this.world.getSeaLevel() && !this.world.isRaining()) {
            int rand = new Random().nextInt(16)+10;
            for (int i = 0; i < rand; i++)
                this.world.spawnEntity(new EntityFirefly(this.world, this.posX, this.posY, this.posZ));
                return true;
        } else return false;
    }

}
