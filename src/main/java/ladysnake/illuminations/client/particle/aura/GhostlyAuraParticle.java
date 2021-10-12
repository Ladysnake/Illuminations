package ladysnake.illuminations.client.particle.aura;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class GhostlyAuraParticle extends SpriteBillboardParticle {
    private static final Random RANDOM = new Random();
    private final float MAXIMUM_ALPHA = 0.02f;
    private final PlayerEntity owner;
    private final int variant = RANDOM.nextInt(4);
    private final SpriteProvider spriteProvider;
    protected float alpha = 0f;
    protected float offsetX = RANDOM.nextFloat() * .7f - 0.35f;
    protected float offsetZ = RANDOM.nextFloat() * .7f - 0.35f;
    protected float offsetY = 0;

    public GhostlyAuraParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.owner = world.getClosestPlayer((TargetPredicate.createNonAttackable()).setBaseMaxDistance(1D), this.x, this.y, this.z);

        this.scale *= 1f + RANDOM.nextFloat();
        this.maxAge = RANDOM.nextInt(5) + 8;
        this.collidesWithWorld = true;
        this.setSprite(spriteProvider.getSprite(variant, 3));

        if (this.owner != null) {
            this.colorRed = 1f;
            this.colorGreen = 1f;
            this.colorBlue = 1f;
            this.setPos(owner.getX() + offsetX, owner.getY() + offsetY, owner.getZ() + offsetZ);
        } else {
            this.markDead();
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternion quaternion2;
        if (this.angle == 0.0F) {
            quaternion2 = camera.getRotation();
        } else {
            quaternion2 = new Quaternion(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion2.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
        }

        Vec3f Vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
        Vec3f.rotate(quaternion2);
        Vec3f[] Vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vec3f Vec3f2 = Vec3fs[k];
            float tmpY = Vec3f2.getY();
            Vec3f2.set(Vec3f2.getX(), 0, Vec3f2.getZ());
            // rotate so it always faces the player
            Vec3f2.rotate(quaternion2);
            Vec3f2.set(Vec3f2.getX() / (1 + offsetY * offsetY), tmpY * offsetY, Vec3f2.getZ() / (1 + offsetY * offsetY));
            Vec3f2.scale(j);
            Vec3f2.add(f, g, h);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = 15728880;
//        float a = MathHelper.clamp(this.alpha, 0.0F, MAXIMUM_ALPHA);

        vertexConsumer.vertex(Vec3fs[0].getX(), Vec3fs[0].getY(), Vec3fs[0].getZ()).texture(maxU, maxV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[1].getX(), Vec3fs[1].getY(), Vec3fs[1].getZ()).texture(maxU, minV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[2].getX(), Vec3fs[2].getY(), Vec3fs[2].getZ()).texture(minU, minV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[3].getX(), Vec3fs[3].getY(), Vec3fs[3].getZ()).texture(minU, maxV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        if (owner != null) {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            if (age++ < maxAge) {
                alpha += 0.01;
            } else {
                alpha -= 0.01;
                if (alpha <= 0) {
                    this.markDead();
                }
            }

            offsetY += 0.1;
            this.setPos(owner.getX() + offsetX, owner.getY() + offsetY, owner.getZ() + offsetZ);
        } else {
            this.markDead();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GhostlyAuraParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

}
