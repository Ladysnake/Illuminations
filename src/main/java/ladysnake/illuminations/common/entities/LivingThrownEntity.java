package ladysnake.illuminations.common.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.sortme.Living;
import net.minecraft.entity.sortme.Projectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.HitResult;
import net.minecraft.util.HitResult.Type;
import net.minecraft.util.TagHelper;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

// An exact copy of ThrownEntity extending MobEntityWithAi and implementing Living
public abstract class LivingThrownEntity extends MobEntityWithAi implements Projectile, Living {
    private int blockX;
    private int blockY;
    private int blockZ;
    protected boolean inGround;
    public int shake;
    protected LivingEntity owner;
    private UUID field_7644;
    public Entity field_7637;
    private int field_7638;

    protected LivingThrownEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
        this.blockX = -1;
        this.blockY = -1;
        this.blockZ = -1;
        this.setSize(0.25F, 0.25F);
    }

    protected LivingThrownEntity(EntityType<?> entityType_1, double double_1, double double_2, double double_3, World world_1) {
        this(entityType_1, world_1);
        this.setPosition(double_1, double_2, double_3);
    }

    protected LivingThrownEntity(EntityType<?> entityType_1, LivingEntity livingEntity_1, World world_1) {
        this(entityType_1, livingEntity_1.x, livingEntity_1.y + (double)livingEntity_1.getEyeHeight() - 0.10000000149011612D, livingEntity_1.z, world_1);
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

    public void calculateVelocity(Entity entity_1, float float_1, float float_2, float float_3, float float_4, float float_5) {
        float float_6 = -MathHelper.sin(float_2 * 0.017453292F) * MathHelper.cos(float_1 * 0.017453292F);
        float float_7 = -MathHelper.sin((float_1 + float_3) * 0.017453292F);
        float float_8 = MathHelper.cos(float_2 * 0.017453292F) * MathHelper.cos(float_1 * 0.017453292F);
        this.setVelocity((double)float_6, (double)float_7, (double)float_8, float_4, float_5);
        this.velocityX += entity_1.velocityX;
        this.velocityZ += entity_1.velocityZ;
        if (!entity_1.onGround) {
            this.velocityY += entity_1.velocityY;
        }

    }

    public void setVelocity(double double_1, double double_2, double double_3, float float_1, float float_2) {
        float float_3 = MathHelper.sqrt(double_1 * double_1 + double_2 * double_2 + double_3 * double_3);
        double_1 /= (double)float_3;
        double_2 /= (double)float_3;
        double_3 /= (double)float_3;
        double_1 += this.random.nextGaussian() * 0.007499999832361937D * (double)float_2;
        double_2 += this.random.nextGaussian() * 0.007499999832361937D * (double)float_2;
        double_3 += this.random.nextGaussian() * 0.007499999832361937D * (double)float_2;
        double_1 *= (double)float_1;
        double_2 *= (double)float_1;
        double_3 *= (double)float_1;
        this.velocityX = double_1;
        this.velocityY = double_2;
        this.velocityZ = double_3;
        float float_4 = MathHelper.sqrt(double_1 * double_1 + double_3 * double_3);
        this.yaw = (float)(MathHelper.atan2(double_1, double_3) * 57.2957763671875D);
        this.pitch = (float)(MathHelper.atan2(double_2, (double)float_4) * 57.2957763671875D);
        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
    }

    @Environment(EnvType.CLIENT)
    public void setVelocityClient(double double_1, double double_2, double double_3) {
        this.velocityX = double_1;
        this.velocityY = double_2;
        this.velocityZ = double_3;
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            float float_1 = MathHelper.sqrt(double_1 * double_1 + double_3 * double_3);
            this.yaw = (float)(MathHelper.atan2(double_1, double_3) * 57.2957763671875D);
            this.pitch = (float)(MathHelper.atan2(double_2, (double)float_1) * 57.2957763671875D);
            this.prevYaw = this.yaw;
            this.prevPitch = this.pitch;
        }

    }

    public void update() {
        this.prevRenderX = this.x;
        this.prevRenderY = this.y;
        this.prevRenderZ = this.z;
        super.update();
        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            this.inGround = false;
            this.velocityX *= (double)(this.random.nextFloat() * 0.2F);
            this.velocityY *= (double)(this.random.nextFloat() * 0.2F);
            this.velocityZ *= (double)(this.random.nextFloat() * 0.2F);
        }

        Vec3d vec3d_1 = new Vec3d(this.x, this.y, this.z);
        Vec3d vec3d_2 = new Vec3d(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
        HitResult hitResult_1 = this.world.rayTrace(vec3d_1, vec3d_2);
        vec3d_1 = new Vec3d(this.x, this.y, this.z);
        vec3d_2 = new Vec3d(this.x + this.velocityX, this.y + this.velocityY, this.z + this.velocityZ);
        if (hitResult_1 != null) {
            vec3d_2 = new Vec3d(hitResult_1.pos.x, hitResult_1.pos.y, hitResult_1.pos.z);
        }

        Entity entity_1 = null;
        List<Entity> list_1 = this.world.getVisibleEntities(this, this.getBoundingBox().stretch(this.velocityX, this.velocityY, this.velocityZ).expand(1.0D));
        double double_1 = 0.0D;
        boolean boolean_1 = false;

        for(int int_1 = 0; int_1 < list_1.size(); ++int_1) {
            Entity entity_2 = (Entity)list_1.get(int_1);
            if (entity_2.doesCollide()) {
                if (entity_2 == this.field_7637) {
                    boolean_1 = true;
                } else if (this.owner != null && this.age < 2 && this.field_7637 == null) {
                    this.field_7637 = entity_2;
                    boolean_1 = true;
                } else {
                    boolean_1 = false;
                    BoundingBox boundingBox_1 = entity_2.getBoundingBox().expand(0.30000001192092896D);
                    HitResult hitResult_2 = boundingBox_1.rayTrace(vec3d_1, vec3d_2);
                    if (hitResult_2 != null) {
                        double double_2 = vec3d_1.squaredDistanceTo(hitResult_2.pos);
                        if (double_2 < double_1 || double_1 == 0.0D) {
                            entity_1 = entity_2;
                            double_1 = double_2;
                        }
                    }
                }
            }
        }

        if (this.field_7637 != null) {
            if (boolean_1) {
                this.field_7638 = 2;
            } else if (this.field_7638-- <= 0) {
                this.field_7637 = null;
            }
        }

        if (entity_1 != null) {
            hitResult_1 = new HitResult(entity_1);
        }

        if (hitResult_1 != null) {
            if (hitResult_1.type == Type.BLOCK && this.world.getBlockState(hitResult_1.getBlockPos()).getBlock() == Blocks.NETHER_PORTAL) {
                this.setInPortal(hitResult_1.getBlockPos());
            } else {
                this.onCollision(hitResult_1);
            }
        }

        this.x += this.velocityX;
        this.y += this.velocityY;
        this.z += this.velocityZ;
        float float_1 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ);
        this.yaw = (float)(MathHelper.atan2(this.velocityX, this.velocityZ) * 57.2957763671875D);

        for(this.pitch = (float)(MathHelper.atan2(this.velocityY, (double)float_1) * 57.2957763671875D); this.pitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
            ;
        }

        while(this.pitch - this.prevPitch >= 180.0F) {
            this.prevPitch += 360.0F;
        }

        while(this.yaw - this.prevYaw < -180.0F) {
            this.prevYaw -= 360.0F;
        }

        while(this.yaw - this.prevYaw >= 180.0F) {
            this.prevYaw += 360.0F;
        }

        this.pitch = MathHelper.lerp(0.2F, this.prevPitch, this.pitch);
        this.yaw = MathHelper.lerp(0.2F, this.prevYaw, this.yaw);
        float float_2 = 0.99F;
        float float_3 = this.getGravity();
        if (this.isInsideWater()) {
            for(int int_2 = 0; int_2 < 4; ++int_2) {
                float float_4 = 0.25F;
                this.world.addParticle(ParticleTypes.BUBBLE, this.x - this.velocityX * 0.25D, this.y - this.velocityY * 0.25D, this.z - this.velocityZ * 0.25D, this.velocityX, this.velocityY, this.velocityZ);
            }

            float_2 = 0.8F;
        }

        this.velocityX *= (double)float_2;
        this.velocityY *= (double)float_2;
        this.velocityZ *= (double)float_2;
        if (!this.isUnaffectedByGravity()) {
            this.velocityY -= (double)float_3;
        }

        this.setPosition(this.x, this.y, this.z);
    }

    protected float getGravity() {
        return 0.03F;
    }

    protected abstract void onCollision(HitResult var1);

    public void writeCustomDataToTag(CompoundTag compoundTag_1) {
        compoundTag_1.putInt("xTile", this.blockX);
        compoundTag_1.putInt("yTile", this.blockY);
        compoundTag_1.putInt("zTile", this.blockZ);
        compoundTag_1.putByte("shake", (byte)this.shake);
        compoundTag_1.putByte("inGround", (byte)(this.inGround ? 1 : 0));
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

    public LivingEntity getOwner() {
        if (this.owner == null && this.field_7644 != null && this.world instanceof ServerWorld) {
            Entity entity_1 = this.world.getEntityByUuid(this.field_7644);
            if (entity_1 instanceof LivingEntity) {
                this.owner = (LivingEntity)entity_1;
            } else {
                this.field_7644 = null;
            }
        }

        return this.owner;
    }

}
