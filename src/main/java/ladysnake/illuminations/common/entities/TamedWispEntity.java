package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.CompoundTag;
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
        return (UUID)((Optional)this.dataTracker.get(OWNER_UUID)).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(WISP_TYPE, "tamed_wisp");
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
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWispType() == "") {
            this.setWispType("tamed_wisp");
        }
    }
}
