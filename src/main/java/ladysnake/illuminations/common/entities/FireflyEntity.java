package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import ladysnake.illuminations.common.init.IlluminationsItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.HashMap;
import java.util.Random;

public class FireflyEntity extends LightOrbEntity {
    // Attributes
    protected float scaleModifier;
    protected float colorModifier;
    protected int alpha;
    protected boolean canDespawn;
    protected boolean isAttractedByLight;
    protected int nextAlphaGoal;
    protected int offset;
    
    private AttributeContainer attributes;

    // Constructors
    public FireflyEntity(EntityType<? extends FireflyEntity> entityType, World world) {
        super(entityType, world);

        this.scaleModifier = 0.1F + new Random().nextFloat() * 0.15F;
        this.colorModifier = 0.25F + new Random().nextFloat() * 0.75F;
        this.offset = this.getRandom().nextInt(20);
        this.isAttractedByLight = true;
        this.canDespawn = true;
        this.alpha = 255;
    }

    public FireflyEntity(World world, double x, double y, double z) {
        this(IlluminationsEntities.FIREFLY, world);
        this.updatePosition(x, y, z);
    }
    
    private DefaultAttributeContainer initAttributes() {
    	return MobEntity.createMobAttributes()
						.add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
						.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
						.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
						.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
						.build();
    }
    
    @Override
    public AttributeContainer getAttributes() {
    	if (attributes == null) {
    		this.attributes = new AttributeContainer(this.initAttributes());
    	}
    	return this.attributes;
    }
    
    // Getters & setters
    public float getScaleModifier() {
        return this.scaleModifier;
    }

    public float getColorModifier() {
        return this.colorModifier;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getNextAlphaGoal() {
        return this.nextAlphaGoal;
    }

    public void setNextAlphaGoal(int nextAlphaGoal) {
        this.nextAlphaGoal = nextAlphaGoal;
    }

    public boolean isAttractedByLight() {
        return isAttractedByLight;
    }

    // NBT
    @Override
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);

        this.scaleModifier = compound.getFloat("scale");
        this.colorModifier = compound.getFloat("colorModifier");
        this.alpha = compound.getInt("alpha");
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
        compound.putFloat("scale", this.scaleModifier);
        compound.putFloat("colorModifier", this.colorModifier);
        compound.putInt("alpha", this.alpha);
        compound.putBoolean("canDespawn", this.canDespawn);

        return super.toTag(compound);
    }

    // Properties
    @Override
    public boolean hasNoGravity() {
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

        if (!this.world.isClient && !this.dead) {
            // despawn if players are too far away (check every second + offset)
            if (((this.world.getTime() + this.offset) % 20 == 0) && !world.isPlayerInRange(this.getX(), this.getY(), this.getZ(), 48)) {
                this.remove();
            }

            // despawn on daytime
            float tod = this.world.getLevelProperties().getTimeOfDay() % 24000;
            if (tod >= 1010 && tod < 12990) {
                this.remove();
            }

            // die in fire
            if (this.isOnFire()) {
                this.remove();
            }

            this.targetChangeCooldown -= (this.getPos().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

            if (((this.world.getTime() + this.offset) % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - this.getX(), this.yTarget - this.getY(), this.zTarget - this.getZ());
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.1 / length);


            if (!this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 0.1, this.getZ())).getBlock().canMobSpawnInside()) {
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
                BlockState checkedBlock = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - i, this.getZ()));
                if (!checkedBlock.getBlock().canMobSpawnInside()) {
                    this.groundLevel = this.getY() - i;
                }
                if (this.groundLevel != 0) break;
            }

            this.xTarget = this.getX() + random.nextGaussian() * 10;
            this.yTarget = Math.min(Math.max(this.getY() + random.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
            this.zTarget = this.getZ() + random.nextGaussian() * 10;

            if (this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canMobSpawnInside())
                this.yTarget += 1;

            if (this.world.getLightLevel(LightType.SKY, this.getBlockPos()) > 8 && !this.world.isDay())
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

            if (this.world.getLightLevel(LightType.BLOCK, this.getBlockPos()) <= 8 || this.world.isDay())
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
            BlockPos randBP = new BlockPos(this.getX() + random.nextGaussian() * 10, this.getY() + random.nextGaussian() * 10, this.getZ() + random.nextGaussian() * 10);
            randBlocks.put(randBP, this.world.getLightLevel(LightType.BLOCK, randBP));
        }
        return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return !this.world.isDay() && !this.world.isThundering();
    }

    @Override
    public void kill() {
        super.remove();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        player.inventory.insertStack(new ItemStack(IlluminationsItems.FIREFLY));
        this.remove();
        return super.interactMob(player, hand);
    }
}
