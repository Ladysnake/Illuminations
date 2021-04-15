package ladysnake.illuminations.client.particle.overhead;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.particle.WillOWispParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerWispParticle extends WillOWispParticle {
    protected PlayerEntity owner;

    protected PlayerWispParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.maxAge = 35;
        this.owner = world.getClosestPlayer((new TargetPredicate()).setBaseMaxDistance(1D), this.x, this.y, this.z);

        if (this.owner == null) {
            this.markDead();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        public DefaultFactory(SpriteProvider spriteProvider) {
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new PlayerWispParticle(clientWorld, d, e, f);
        }
    }

    @Override
    public void tick() {
        if (this.age > 10) {
            this.colorAlpha = 1f;

            for (int i = 0; i < 1; i++) {
                this.world.addParticle(IlluminationsClient.WISP_TRAIL, this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0.2d, 0);
            }
        } else {
            this.colorAlpha = 0;
        }

        if (owner != null) {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            // die if old enough
            if (this.age++ >= this.maxAge) {
                this.markDead();
            }

            this.setPos(owner.getX() + Math.cos(owner.bodyYaw/50) * 0.5, owner.getY() + owner.getHeight() + 0.5f  + Math.sin(owner.age / 12f) / 12f, owner.getZ() - Math.sin(owner.bodyYaw/50) * 0.5);

            this.pitch = -owner.pitch;
            this.prevPitch = -owner.prevPitch;
            this.yaw = -owner.yaw;
            this.prevYaw = -owner.prevYaw;
        } else {
            this.markDead();
        }
    }
}
