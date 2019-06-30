package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class FairyEntity extends LightOrbEntity {
    // Attributes
    protected float colorModifierR;
    protected float colorModifierG;
    protected float colorModifierB;

    // Constructors
    public FairyEntity(EntityType entityType, World world) {
        super(entityType, world);

        // Fairy color
        this.colorModifierR = 0;
        this.colorModifierG = 0;
        this.colorModifierB = 0;
        int colorToIgnore = new Random().nextInt(3);
        if (colorToIgnore != 0) {
            this.colorModifierR = new Random().nextFloat();
        }
        if (colorToIgnore != 1) {
            this.colorModifierG = new Random().nextFloat();
        }
        if (colorToIgnore != 2) {
            this.colorModifierB = new Random().nextFloat();
        }

        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    public FairyEntity(World world, double x, double y, double z) {
        this(IlluminationsEntities.FAIRY, world);
        this.setPosition(x, y, z);
    }

    // Getters & setters
    public float getColorModifierR() {
        return colorModifierR;
    }

    public float getColorModifierG() {
        return colorModifierG;
    }

    public float getColorModifierB() {
        return colorModifierB;
    }

    // NBT
    @Override
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);

        this.colorModifierR = compound.getFloat("colorModifierR");
        this.colorModifierG = compound.getFloat("colorModifierG");
        this.colorModifierB = compound.getFloat("colorModifierB");
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compoundTag) {

    }

    @Override
    public void writeCustomDataToTag(CompoundTag compoundTag) {

    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putFloat("colorModifierR", this.colorModifierR);
        compound.putFloat("colorModifierG", this.colorModifierG);
        compound.putFloat("colorModifierB", this.colorModifierB);

        return super.toTag(compound);
    }

    // Properties
    @Override
    public boolean hasNoGravity() {
        return true;
    }

    // Behaviour
    private double groundLevel;
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient && !this.dead) {
            // despawn if players are too far away
//            boolean arePlayersNear = world.isPlayerInRange(this.x, this.y, this.z, 48);
//            if (!arePlayersNear) this.remove();

            // die in fire
            if (this.isOnFire()) {
                this.remove();
            }

            this.targetChangeCooldown -= (this.getPosVector().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.1 / length);

            float f = new Random().nextFloat() / 2;
            if (!this.world.getBlockState(new BlockPos(this.x, this.y - 0.1, this.z)).getBlock().canMobSpawnInside()) {
                this.setVelocity((0.9) * getVelocity().x + (f) * targetVector.x,
                    0.05,
                    (0.9) * getVelocity().z + (f) * targetVector.z);
            } else this.setVelocity(
                    (0.9) * getVelocity().x + (f) * targetVector.x,
                    (0.9) * getVelocity().y + (f) * targetVector.y,
                    (0.9) * getVelocity().z + (f) * targetVector.z
            );
            if (this.getBlockPos() != this.getTargetPosition()) this.move(MovementType.SELF, this.getVelocity());
        }
    }

    private void selectBlockTarget() {
        this.groundLevel = 0;
        for (int i = 0; i < 20; i++) {
            BlockState checkedBlock = this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z));
            if (!checkedBlock.getBlock().canMobSpawnInside()) {
                this.groundLevel = this.y - i;
            }
            if (this.groundLevel != 0) break;
        }

        this.xTarget = this.x + random.nextGaussian() * 10;
        this.yTarget = Math.min(Math.max(this.y + random.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
        this.zTarget = this.z + random.nextGaussian() * 10;

        if (this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canMobSpawnInside())
            this.yTarget += 1;

        targetChangeCooldown = random.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return this.world.isDaylight() && !this.world.isThundering();
    }

    @Override
    public void kill() {
        super.remove();
    }
}
