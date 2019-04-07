package ladysnake.illuminations.common.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TagHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.UUID;

// An exact copy of ThrownEntity extending MobEntityWithAi and implementing Living
public abstract class ThrownLightOrbEntity extends LightOrbEntity implements Projectile {
    private int blockX;
    private int blockY;
    private int blockZ;
    protected boolean inGround;
    public int shake;
    protected LivingEntity owner;
    private UUID field_7644;
    private Entity field_7637;
    private int field_7638;
    public boolean beingThrown;

    protected ThrownLightOrbEntity(EntityType<? extends ThrownLightOrbEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
        this.blockX = -1;
        this.blockY = -1;
        this.blockZ = -1;
    }

    protected ThrownLightOrbEntity(EntityType<? extends ThrownLightOrbEntity> entityType_1, double double_1, double double_2, double double_3, World world_1) {
        this(entityType_1, world_1);
        this.setPosition(double_1, double_2, double_3);
    }

    protected ThrownLightOrbEntity(EntityType<? extends ThrownLightOrbEntity> entityType_1, LivingEntity livingEntity_1, World world_1) {
        this(entityType_1, livingEntity_1.x, livingEntity_1.y + (double) livingEntity_1.getStandingEyeHeight() - 0.10000000149011612D, livingEntity_1.z, world_1);
        this.owner = livingEntity_1;
        this.field_7644 = livingEntity_1.getUuid();
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldRenderAtDistance(double double_1) {
        double double_2 = this.getBoundingBox().averageDimension() * 4.0D;
        if (Double.isNaN(double_2)) {
            double_2 = 4.0D;
        }

        double_2 *= 64.0D;
        return double_1 < double_2 * double_2;
    }

    public void method_7474(Entity entity_1, float float_1, float float_2, float float_3, float float_4, float float_5) {
        float float_6 = -MathHelper.sin(float_2 * 0.017453292F) * MathHelper.cos(float_1 * 0.017453292F);
        float float_7 = -MathHelper.sin((float_1 + float_3) * 0.017453292F);
        float float_8 = MathHelper.cos(float_2 * 0.017453292F) * MathHelper.cos(float_1 * 0.017453292F);
        this.setVelocity((double) float_6, (double) float_7, (double) float_8, float_4, float_5);
        Vec3d vec3d_1 = entity_1.getVelocity();
        this.setVelocity(this.getVelocity().add(vec3d_1.x, entity_1.onGround ? 0.0D : vec3d_1.y, vec3d_1.z));
    }

    public void setVelocity(double double_1, double double_2, double double_3, float float_1, float float_2) {
        Vec3d vec3d_1 = (new Vec3d(double_1, double_2, double_3)).normalize().add(this.random.nextGaussian() * 0.007499999832361937D * (double) float_2, this.random.nextGaussian() * 0.007499999832361937D * (double) float_2, this.random.nextGaussian() * 0.007499999832361937D * (double) float_2).multiply((double) float_1);
        this.setVelocity(vec3d_1);
        float float_3 = MathHelper.sqrt(squaredHorizontalLength(vec3d_1));
        this.yaw = (float) (MathHelper.atan2(vec3d_1.x, vec3d_1.z) * 57.2957763671875D);
        this.pitch = (float) (MathHelper.atan2(vec3d_1.y, (double) float_3) * 57.2957763671875D);
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
    }

    @Environment(EnvType.CLIENT)
    public void setVelocityClient(double double_1, double double_2, double double_3) {
        this.setVelocity(double_1, double_2, double_3);
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float float_1 = MathHelper.sqrt(double_1 * double_1 + double_3 * double_3);
            this.yaw = (float) (MathHelper.atan2(double_1, double_3) * 57.2957763671875D);
            this.pitch = (float) (MathHelper.atan2(double_2, (double) float_1) * 57.2957763671875D);
            this.prevYaw = this.yaw;
            this.prevPitch = this.pitch;
        }

    }

    public void tick() {
        if (beingThrown) {
            this.prevRenderX = this.x;
            this.prevRenderY = this.y;
            this.prevRenderZ = this.z;
            super.tick();
            if (this.shake > 0) {
                --this.shake;
            }

            if (this.inGround) {
                this.inGround = false;
                this.setVelocity(this.getVelocity().multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
            }

            BoundingBox boundingBox_1 = this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D);
            Iterator var2 = this.world.getEntities(this, boundingBox_1, (entity_1x) -> !entity_1x.isSpectator() && entity_1x.doesCollide()).iterator();

            while (var2.hasNext()) {
                Entity entity_1 = (Entity) var2.next();
                if (entity_1 == this.field_7637) {
                    ++this.field_7638;
                    break;
                }

                if (this.owner != null && this.age < 2 && this.field_7637 == null) {
                    this.field_7637 = entity_1;
                    this.field_7638 = 3;
                    break;
                }
            }

            HitResult hitResult_1 = ProjectileUtil.getCollision(this, boundingBox_1, (entity_1x) -> {
                return !entity_1x.isSpectator() && entity_1x.doesCollide() && entity_1x != this.field_7637;
            }, RayTraceContext.ShapeType.OUTLINE, true);
            if (this.field_7637 != null && this.field_7638-- <= 0) {
                this.field_7637 = null;
            }

            if (hitResult_1.getType() != HitResult.Type.NONE) {
                if (hitResult_1.getType() == HitResult.Type.BLOCK && this.world.getBlockState(((BlockHitResult) hitResult_1).getBlockPos()).getBlock() == Blocks.NETHER_PORTAL) {
                    this.setInPortal(((BlockHitResult) hitResult_1).getBlockPos());
                } else {
                    this.onCollision(hitResult_1);
                }
            }

            Vec3d vec3d_1 = this.getVelocity();
            this.x += vec3d_1.x;
            this.y += vec3d_1.y;
            this.z += vec3d_1.z;
            float float_1 = MathHelper.sqrt(squaredHorizontalLength(vec3d_1));
            this.yaw = (float) (MathHelper.atan2(vec3d_1.x, vec3d_1.z) * 57.2957763671875D);

            for (this.pitch = (float) (MathHelper.atan2(vec3d_1.y, (double) float_1) * 57.2957763671875D); this.pitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
            }

            while (this.pitch - this.prevPitch >= 180.0F) {
                this.prevPitch += 360.0F;
            }

            while (this.yaw - this.prevYaw < -180.0F) {
                this.prevYaw -= 360.0F;
            }

            while (this.yaw - this.prevYaw >= 180.0F) {
                this.prevYaw += 360.0F;
            }

            this.pitch = MathHelper.lerp(0.2F, this.prevPitch, this.pitch);
            this.yaw = MathHelper.lerp(0.2F, this.prevYaw, this.yaw);
            float float_4;
            if (this.isInsideWater()) {
                for (int int_1 = 0; int_1 < 4; ++int_1) {
                    float float_2 = 0.25F;
                    this.world.addParticle(ParticleTypes.BUBBLE, this.x - vec3d_1.x * 0.25D, this.y - vec3d_1.y * 0.25D, this.z - vec3d_1.z * 0.25D, vec3d_1.x, vec3d_1.y, vec3d_1.z);
                }

                float_4 = 0.8F;
            } else {
                float_4 = 0.99F;
            }

            this.setVelocity(vec3d_1.multiply((double) float_4));
            if (!this.isUnaffectedByGravity()) {
                Vec3d vec3d_2 = this.getVelocity();
                this.setVelocity(vec3d_2.x, vec3d_2.y - (double) this.getGravity(), vec3d_2.z);
            }

            this.setPosition(this.x, this.y, this.z);
        } else {
            super.tick();
        }
    }

    protected float getGravity() {
        return 0.03F;
    }

    protected abstract void onCollision(HitResult var1);

    public void writeCustomDataToTag(CompoundTag compoundTag_1) {
        compoundTag_1.putInt("xTile", this.blockX);
        compoundTag_1.putInt("yTile", this.blockY);
        compoundTag_1.putInt("zTile", this.blockZ);
        compoundTag_1.putByte("shake", (byte) this.shake);
        compoundTag_1.putByte("inGround", (byte) (this.inGround ? 1 : 0));
        if (this.field_7644 != null) {
            compoundTag_1.put("owner", TagHelper.serializeUuid(this.field_7644));
        }

    }

    public void readCustomDataFromTag(CompoundTag compoundTag_1) {
        this.blockX = compoundTag_1.getInt("xTile");
        this.blockY = compoundTag_1.getInt("yTile");
        this.blockZ = compoundTag_1.getInt("zTile");
        this.shake = compoundTag_1.getByte("shake") & 255;
        this.inGround = compoundTag_1.getByte("inGround") == 1;
        this.owner = null;
        if (compoundTag_1.containsKey("owner", 10)) {
            this.field_7644 = TagHelper.deserializeUuid(compoundTag_1.getCompound("owner"));
        }

    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.field_7644 != null && this.world instanceof ServerWorld) {
            Entity entity_1 = ((ServerWorld) this.world).getEntity(this.field_7644);
            if (entity_1 instanceof LivingEntity) {
                this.owner = (LivingEntity) entity_1;
            } else {
                this.field_7644 = null;
            }
        }

        return this.owner;
    }

    @Override
    public boolean isUnaffectedByGravity() {
        return !this.beingThrown;
    }
}