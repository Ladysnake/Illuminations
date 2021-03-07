package ladysnake.illuminations.client.particle.aura;

import ladysnake.illuminations.client.particle.ChorusPetalParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

public class ShadowbringerParticle extends ChorusPetalParticle {

    boolean negateX = random.nextBoolean(), negateZ = random.nextBoolean();

    public ShadowbringerParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

        this.maxAge = 60 + random.nextInt(60);
        this.velocityY = random.nextFloat()/10;
        this.velocityX = negateX ? -random.nextGaussian()/70 : random.nextGaussian()/70;
        this.velocityZ = negateZ ? -random.nextGaussian()/70 : random.nextGaussian()/70;

        colorAlpha = 0;


        this.setPos(this.x + TwilightFireflyParticle.getWanderingDistance(this.random), this.y, this.z + TwilightFireflyParticle.getWanderingDistance(this.random));
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new ShadowbringerParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

    public void tick() {
        if (this.age++ < this.maxAge) {
            this.colorAlpha = Math.min(1f, this.colorAlpha + 0.02f);
        }

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        velocityX = velocityX * 0.7 + (negateX ? -(Math.sin(age / 3.0) / 20.0) : Math.sin(age / 3.0) / 20.0);
        velocityZ = velocityZ * 0.7 + (negateZ ? -(Math.sin(age / 3.0) / 20.0) : Math.sin(age / 3.0) / 20.0);

        this.move(this.velocityX, this.velocityY, this.velocityZ);

        if (this.age >= this.maxAge) {
            this.colorAlpha = Math.max(0f, this.colorAlpha-0.01f);

            if (this.colorAlpha <= 0f) {
                this.markDead();
            }
        }

        this.prevAngle = this.angle;
        if (this.onGround || this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).getFluid() != Fluids.EMPTY) {
            this.velocityY *= 0.95;
        }

        if (this.velocityY != 0) {
            this.angle += Math.PI * Math.sin(rotationFactor * this.age) / 2;
        }
    }
}
