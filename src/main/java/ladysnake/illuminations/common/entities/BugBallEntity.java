package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.IlluminationsItems;
import ladysnake.illuminations.common.network.Packets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BugBallEntity extends ThrownItemEntity {
    public BugBallEntity(EntityType<BugBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public BugBallEntity(World world, LivingEntity owner) {
        super(Illuminations.BUGBALL, owner, world);
    }

    public BugBallEntity(World world, double x, double y, double z) {
        super(Illuminations.BUGBALL, x, y, z, world);
    }

    protected Item getDefaultItem() {
        return IlluminationsItems.BUGBALL;
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 0f);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (this.world.isClient()) {
            if (world.getBlockState(this.getBlockPos()).isAir()) {
                this.world.setBlockState(this.getBlockPos(), Blocks.DIRT.getDefaultState());
                world.breakBlock(this.getBlockPos(), false);
            }
        }

        for (int i = 0; i < 100; i++) {
            world.addParticle(IlluminationsClient.FIREFLY, (double)this.getBlockPos().getX() + this.random.nextDouble(), (double)this.getBlockPos().getY() + this.random.nextDouble(), (double)this.getBlockPos().getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);;
        }

        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.remove();
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return Packets.newSpawnPacket(this);
    }

}
