package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class EntityFirefly extends AbstractLightOrb {
    // Attributes
    private float scaleModifier;
    float colorModifier;
    private float alpha;
    private boolean canDespawn;
    protected boolean isAttractedByLight;
    protected boolean despawnOnDaytime;

    // Constructors
    public EntityFirefly(World world) {
        super(IlluminationsEntities.FIREFLY, world);

        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.15F;
        this.colorModifier = 0.25F + new Random().nextFloat() * 0.75F;
        this.alpha = 1F;

        this.setSize(this.scaleModifier, this.scaleModifier);
        this.canDespawn = true;
        this.isAttractedByLight = true;
        this.despawnOnDaytime = true;
    }

    public EntityFirefly(World world, double x, double y, double z) {
        this(world);
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
//
//    @Override
//    protected boolean canDespawn() {
//        return this.canDespawn;
//    }

    public void setCanDespawn(boolean canDespawn) {
        this.canDespawn = canDespawn;
    }

    public boolean isAttractedByLight() {
        return isAttractedByLight;
    }

    public void setAttractedByLight(boolean attractedByLight) {
        isAttractedByLight = attractedByLight;
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

    // Behaviour
    private double groundLevel;
    private BlockPos lightTarget;
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void update() {
        super.update();

//        if (this.world.isDaylight() && this.world.getSkyLightLevel(this.getPos())) this.alpha -= 0.01; else this.alpha += 0.01;
        if (this.despawnOnDaytime && this.canDespawn && this.alpha <= 0) this.kill();

        if (this.y > 300) this.kill();

        if (!this.world.isClient && !this.method_5686()) {
            this.targetChangeCooldown -= (this.getPosVector().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceToCenter(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.1 / length);
            velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
            velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
            velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
            if (this.getPos() != this.getTargetPosition()) this.move(MovementType.SELF, this.velocityX, this.velocityY, this.velocityZ);

            if (this.isInsideWater()) this.damage(DamageSource.DROWN, 1);
            if (this.isTouchingLava()) this.damage(DamageSource.LAVA, 1);
        }
    }

    private void selectBlockTarget() {
        if (this.lightTarget == null || !this.isAttractedByLight()) {
            this.groundLevel = 0;
            for (int i = 0; i < 20; i++) {
                if (!this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z)).getBlock().canMobSpawnInside())
                    this.groundLevel = this.y - i;
                if (this.groundLevel != 0) break;
            }

            this.xTarget = this.x + random.nextGaussian() * 10;
            this.yTarget = Math.min(Math.max(this.y + random.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
            this.zTarget = this.z + random.nextGaussian() * 10;

            while (!this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canMobSpawnInside())
                this.yTarget += 1;

            if (this.world.getLightLevel(LightType.SKY, this.getPos()) > 8 && !this.world.isDaylight())
                this.lightTarget = getRandomLitBlockAround();
        } else {
            this.xTarget = this.lightTarget.getX() + random.nextGaussian();
            this.yTarget = this.lightTarget.getY() + random.nextGaussian();
            this.zTarget = this.lightTarget.getZ() + random.nextGaussian();

            if (this.world.getLightLevel(LightType.BLOCK, this.getPos()) > 8) {
                BlockPos possibleTarget = getRandomLitBlockAround();
                if (this.world.getLightLevel(LightType.BLOCK, possibleTarget) > this.world.getLightLevel(LightType.BLOCK, this.lightTarget))
                    this.lightTarget = possibleTarget;
            }

            if (this.world.getLightLevel(LightType.BLOCK, this.getPos()) <= 8 || this.world.isDaylight())
                this.lightTarget = null;
        }

        targetChangeCooldown = random.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    private void detectGroundLevel() {
        for (int i = 0; i < 20; i++) {
            double groundTmp = 0;
            if (!this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z)).getBlock().canMobSpawnInside())
                groundTmp = this.y - i;
            if (groundTmp != 0) {
                this.groundLevel = groundTmp;
                break;
            }
        }
    }

    private BlockPos getRandomLitBlockAround() {
        HashMap<BlockPos, Integer> randBlocks = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            BlockPos randBP = new BlockPos(this.x + random.nextGaussian() * 10, this.y + random.nextGaussian() * 10, this.z + random.nextGaussian() * 10);
            randBlocks.put(randBP, this.world.getLightLevel(LightType.BLOCK, randBP));
        }
        return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

}
