package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class FireflyEntity extends LightOrbEntity {
    // Attributes
    protected float scaleModifier;
    protected float colorModifier;
    protected float alpha;
    protected boolean canDespawn;
    protected boolean isAttractedByLight;
    protected boolean despawnOnDaytime;
    private Float nextAlphaGoal;

    // Constructors
    public FireflyEntity(EntityType entityType, World world) {
        super(entityType, world);

        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.15F;
        this.colorModifier = 0.25F + new Random().nextFloat() * 0.75F;
        this.alpha = 1F;

        this.canDespawn = true;
        this.isAttractedByLight = true;
        this.despawnOnDaytime = true;
    }

    public FireflyEntity(World world, double x, double y, double z) {
        this(IlluminationsEntities.FIREFLY, world);
        this.setPosition(x, y, z);
    }

    // Getters & setters
    public float getScaleModifier() {
        return scaleModifier;
    }

    public float getColorModifier() {
        return colorModifier;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Float getNextAlphaGoal() {
        return nextAlphaGoal;
    }

    public void setNextAlphaGoal(Float nextAlphaGoal) {
        this.nextAlphaGoal = nextAlphaGoal;
    }

    public boolean isAttractedByLight() {
        return isAttractedByLight;
    }

    // NBT
    @Override
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);

        this.scaleModifier = compound.getFloat("scaleModifier");
        this.colorModifier = compound.getFloat("colorModifier");
        this.alpha = compound.getFloat("alpha");
        this.canDespawn = compound.getBoolean("canDespawn");
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compoundTag) {

    }

    @Override
    public void writeCustomDataToTag(CompoundTag compoundTag) {

    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putFloat("scaleModifier", this.scaleModifier);
        compound.putFloat("colorModifier", this.colorModifier);
        compound.putFloat("alpha", this.alpha);
        compound.putBoolean("canDespawn", this.canDespawn);

        return super.toTag(compound);
    }

    // Properties
    @Override
    public boolean isUnaffectedByGravity() {
        return true;
    }

    @Override
    public float getScaleFactor() {
        return this.scaleModifier;
    }

    // Behaviour
    private double groundLevel;
    private BlockPos lightTarget;
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.despawnOnDaytime && this.canDespawn && this.alpha <= 0) this.kill();

        if (this.y > 300) this.kill();

        if (!this.world.isClient && !this.dead) {
            this.targetChangeCooldown -= (this.getPosVector().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.1 / length);


            if (!this.world.getBlockState(new BlockPos(this.x, this.y - 0.1, this.z)).getBlock().canMobSpawnInside()) {
                this.setVelocity((0.9) * getVelocity().x + (0.1) * targetVector.x,
                            0.05,
                        (0.9) * getVelocity().z + (0.1) * targetVector.z);
            } else this.setVelocity(
                    (0.9) * getVelocity().x + (0.1) * targetVector.x,
                    (0.9) * getVelocity().y + (0.1) * targetVector.y,
                    (0.9) * getVelocity().z + (0.1) * targetVector.z
            );
            if (this.getBlockPos() != this.getTargetPosition()) this.move(MovementType.SELF, this.getVelocity());
        }
    }

    private void selectBlockTarget() {
        if (this.lightTarget == null || !this.isAttractedByLight()) {
            this.groundLevel = 0;
            for (int i = 0; i < 20; i++) {
                if (this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z)).getBlock() != Blocks.AIR)
                    this.groundLevel = this.y - i;
                if (this.groundLevel != 0) break;
            }

            this.xTarget = this.x + random.nextGaussian() * 10;
            this.yTarget = Math.min(Math.max(this.y + random.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
            this.zTarget = this.z + random.nextGaussian() * 10;

            if (this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canMobSpawnInside())
                this.yTarget += 1;

            if (this.world.getLightLevel(LightType.SKY, this.getBlockPos()) > 8 && !this.world.isDaylight())
                this.lightTarget = getRandomLitBlockAround();
        } else {
            this.xTarget = this.lightTarget.getX() + random.nextGaussian();
            this.yTarget = this.lightTarget.getY() + random.nextGaussian();
            this.zTarget = this.lightTarget.getZ() + random.nextGaussian();

            if (this.world.getLightLevel(LightType.BLOCK, this.getBlockPos()) > 8) {
                BlockPos possibleTarget = getRandomLitBlockAround();
                if (this.world.getLightLevel(LightType.BLOCK, possibleTarget) > this.world.getLightLevel(LightType.BLOCK, this.lightTarget))
                    this.lightTarget = possibleTarget;
            }

            if (this.world.getLightLevel(LightType.BLOCK, this.getBlockPos()) <= 8 || this.world.isDaylight())
                this.lightTarget = null;
        }

        targetChangeCooldown = random.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    private BlockPos getRandomLitBlockAround() {
        HashMap<BlockPos, Integer> randBlocks = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            BlockPos randBP = new BlockPos(this.x + random.nextGaussian() * 10, this.y + random.nextGaussian() * 10, this.z + random.nextGaussian() * 10);
            randBlocks.put(randBP, this.world.getLightLevel(LightType.BLOCK, randBP));
        }
        return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return !this.world.isDaylight() && !this.world.isThundering();
    }

}
