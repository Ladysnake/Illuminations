package ladysnake.lumen.common.entities;

import ladysnake.lumen.common.config.LumenConfig;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
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
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0F);
        this.setHealth(1.0F);

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

    @Override
    protected boolean canDespawn() {
        return this.canDespawn;
    }

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
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.scaleModifier = compound.getFloat("scaleModifier");
        this.colorModifier = compound.getFloat("colorModifier");
        this.alpha = compound.getFloat("alpha");
        this.canDespawn = compound.getBoolean("canDespawn");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setFloat("scaleModifier", this.scaleModifier);
        compound.setFloat("colorModifier", this.colorModifier);
        compound.setFloat("alpha", this.alpha);
        compound.setBoolean("canDespawn", this.canDespawn);
    }

    // Properties
    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        // if night time, superior than sea level and not raining
        // spawn a number of fireflies between the config values
        if (this.getClass() == EntityFirefly.class) return LumenConfig.spawnFireflies && !this.world.isDaytime() && !this.world.isRaining() && super.getCanSpawnHere();
        else return super.getCanSpawnHere();
    }

    // Behaviour
    private double groundLevel;
    private BlockPos lightTarget;
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.despawnOnDaytime && this.canDespawn && this.alpha <= 0) this.setDead();

        if (this.posY > 300) this.outOfWorld();

        if (!this.world.isRemote && !this.isDead) {
            this.targetChangeCooldown -= (this.getPositionVector().squareDistanceTo(lastTickPosX, lastTickPosY, lastTickPosZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPosition().distanceSq(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - posX, this.yTarget - posY, this.zTarget - posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.1 / length);
            motionX = (0.9) * motionX + (0.1) * targetVector.x;
            motionY = (0.9) * motionY + (0.1) * targetVector.y;
            motionZ = (0.9) * motionZ + (0.1) * targetVector.z;
            if (this.getPosition() != this.getTargetPosition()) this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

            if (this.isInWater()) this.attackEntityFrom(DamageSource.DROWN, 1);
            if (this.isInLava()) this.attackEntityFrom(DamageSource.LAVA, 1);
        }
    }

    private void selectBlockTarget() {
        if (this.lightTarget == null || !this.isAttractedByLight()) {
            this.groundLevel = 0;
            for (int i = 0; i < 20; i++) {
                if (!this.world.getBlockState(new BlockPos(this.posX, this.posY - i, this.posZ)).getBlock().canSpawnInBlock())
                    this.groundLevel = this.posY - i;
                if (this.groundLevel != 0) break;
            }

            this.xTarget = this.posX + rand.nextGaussian() * 10;
            this.yTarget = Math.min(Math.max(this.posY + rand.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
            this.zTarget = this.posZ + rand.nextGaussian() * 10;

            while (!this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canSpawnInBlock())
                this.yTarget += 1;

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

        targetChangeCooldown = rand.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    private void detectGroundLevel() {
        for (int i = 0; i < 20; i++) {
            double groundTmp = 0;
            if (!this.world.getBlockState(new BlockPos(this.posX, this.posY - i, this.posZ)).getBlock().canSpawnInBlock())
                groundTmp = this.posY - i;
            if (groundTmp != 0) {
                this.groundLevel = groundTmp;
                break;
            }
        }
    }

    private BlockPos getRandomLitBlockAround() {
        HashMap<BlockPos, Integer> randBlocks = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            BlockPos randBP = new BlockPos(this.posX + rand.nextGaussian() * 10, this.posY + rand.nextGaussian() * 10, this.posZ + rand.nextGaussian() * 10);
            randBlocks.put(randBP, this.world.getLight(randBP, true));
        }
        return randBlocks.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(new BlockPos(this));
    }

}
