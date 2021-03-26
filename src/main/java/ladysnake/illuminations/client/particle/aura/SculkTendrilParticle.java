package ladysnake.illuminations.client.particle.aura;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Box;

import java.util.Random;

public class SculkTendrilParticle extends SpriteBillboardParticle {
    private boolean wasOnGround = false;
    private final SpriteProvider provider;
    private static final Random RANDOM = new Random();

    public SculkTendrilParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x + (RANDOM.nextFloat() - 0.5f), y, z + (RANDOM.nextFloat() - 0.5f), velocityX, velocityY, velocityZ);
        this.setSprite(spriteProvider.getSprite(0, 1));
        provider = spriteProvider;
        this.maxAge = 100;
        this.angle = 0f;

        this.scale = 0f;
        this.collidesWithWorld = true;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new SculkTendrilParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

    @Override
    public void tick() {
        if (!this.onGround) {
            this.move(0, -1D, 0);
            wasOnGround = false;
            return;
        }
        if (!wasOnGround) {
            this.repositionFromBoundingBox();
            wasOnGround = true;
        }
        if (this.age++ < this.maxAge) {
            this.scale = Math.min(0.4f, this.scale + 0.05f);
            if (this.age == 75) {
                this.setSprite(provider.getSprite(1,1));
            }
        }
        this.setBoundingBox(this.getBoundingBox().expand(0, 1, 0));


        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.prevAngle = this.angle;

        if (this.age >= this.maxAge) {
            this.scale = Math.max(0f, this.scale-0.05f);

            if (this.scale <= 0f) {
                this.markDead();
            }
        }
    }

    @Override
    protected void repositionFromBoundingBox() {
        Box box = this.getBoundingBox();
        this.x = (box.minX + box.maxX) / 2.0D;
        this.y = box.minY+0.3D;
        this.z = (box.minZ + box.maxZ) / 2.0D;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
}
