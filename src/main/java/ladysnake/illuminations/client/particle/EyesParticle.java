package ladysnake.illuminations.client.particle;

import ladysnake.illuminations.client.Config;
import ladysnake.illuminations.client.IlluminationsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EyesParticle extends SpriteBillboardParticle {
    protected float alpha = 1f;

    private static final Random RANDOM = new Random();
    private final SpriteProvider spriteProvider;

    public EyesParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider.getSprite(0, 3));

        this.scale *= 1f + new Random().nextFloat();
        this.maxAge = ThreadLocalRandom.current().nextInt(400, 1201); // live between   seconds and one minute
        this.collidesWithWorld = true;

        this.colorRed = 1f;
        this.colorGreen = 1f;
        this.colorBlue = 1f;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        super.buildGeometry(vertexConsumer, camera, tickDelta);

        // disable if night vision or config is set to disabled
        if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity) camera.getFocusedEntity()).hasStatusEffect(StatusEffects.NIGHT_VISION) || Config.getEyesInTheDark() == Config.EyesInTheDark.DISABLE) {
            this.markDead();
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EyesParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

    public void tick() {
        if (this.age++ < this.maxAge) {
            if (this.age < 1) {
                this.setSprite(spriteProvider.getSprite(0, 3));
            } else if (this.age < 2) {
                this.setSprite(spriteProvider.getSprite(1, 3));
            } else if (this.age < 3) {
                this.setSprite(spriteProvider.getSprite(2, 3));
            } else {
                this.setSprite(spriteProvider.getSprite(3, 3));
            }
        } else {
            if (this.age < this.maxAge+1) {
                this.setSprite(spriteProvider.getSprite(2, 3));
            } else if (this.age < this.maxAge+2) {
                this.setSprite(spriteProvider.getSprite(1, 3));
            } else if (this.age < this.maxAge+3) {
                this.setSprite(spriteProvider.getSprite(0, 3));
            } else {
                this.markDead();
            }
        }

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        // disappear if light or if player gets too close
        if (this.maxAge > this.age && (world.getLightLevel(new BlockPos(x, y, z)) > 0 || world.getClosestPlayer(x, y, z, IlluminationsClient.EYES_VANISHING_DISTANCE, false) != null)) {
            this.maxAge = this.age;
        }
    }

}
