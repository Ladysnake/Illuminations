package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class TamedWispEntity extends LightOrbEntity {
    private static TrackedData<String> WISP_TYPE;
    protected static TrackedData<Optional<UUID>> OWNER_UUID;

    public TamedWispEntity(EntityType entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public TamedWispEntity(World world, double x, double y, double z) {
        this(IlluminationsEntities.TAMED_WISP, world);
        this.setPosition(x, y, z);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource_1) {
        return this.isInvulnerable() && damageSource_1 != DamageSource.OUT_OF_WORLD;
    }

    public String getWispType() {
        return this.dataTracker.get(WISP_TYPE);
    }

    public void setWispType(String string) {
        this.dataTracker.set(WISP_TYPE, string);
    }

    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(WISP_TYPE, "tamed_wisp");
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    public void writeCustomDataToTag(CompoundTag compoundTag) {
        super.writeCustomDataToTag(compoundTag);
        compoundTag.putString("WispType", this.getWispType());
    }

    public void readCustomDataFromTag(CompoundTag compoundTag) {
        super.readCustomDataFromTag(compoundTag);
        this.setWispType(compoundTag.getString("WispType"));
    }

    static {
        WISP_TYPE = DataTracker.registerData(TamedWispEntity.class, TrackedDataHandlerRegistry.STRING);
        OWNER_UUID = DataTracker.registerData(TamedWispEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    // Behaviour
    private double xTarget;
    private double yTarget;
    private double zTarget;

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient) {
            // remove if no type set
            if (this.getWispType() == "") {
                this.remove();
            }

            if (this.getOwnerUuid() != null) {
                PlayerEntity owner = this.world.getPlayerByUuid(this.getOwnerUuid());
                if (owner != null) {
                    this.xTarget = owner.x;
                    this.yTarget = owner.y + owner.getHeight()/1.25;
                    this.zTarget = owner.z;

                    // if too far away, teleport to the player
                    if (this.x > owner.x + 20 || this.x < owner.x - 20
                            || this.y > owner.y + 20 || this.y < owner.y - 20
                            || this.z > owner.z + 20 || this.z < owner.z - 20)
                        this.setPosition(owner.x, owner.y, owner.z);

                    Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
                    double length = targetVector.length();
                    targetVector = targetVector.multiply(0.1 / length);

                    double velX = 0;
                    if (this.xTarget - x > 0.5 || this.xTarget - x < -0.5) {
                        velX = (0.9) * getVelocity().x + (0.6) * targetVector.x;
                    }
                    double velY = (0.9) * getVelocity().y + (0.4) * targetVector.y;
                    double velZ = 0;
                    if (this.zTarget - z > 0.5 || this.zTarget - z < -0.5) {
                        velZ = (0.9) * getVelocity().z + (0.6) * targetVector.z;
                    }

                    this.setVelocity(velX, velY, velZ);

                    if (this.getBlockPos() != this.getTargetPosition()) this.move(MovementType.SELF, this.getVelocity());
                } else {
                    this.setInvisible(true);
                }
            } else {
                this.remove();
            }
        }
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

}
