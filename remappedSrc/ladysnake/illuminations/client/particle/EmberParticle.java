package ladysnake.illuminations.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.*;

public class EmberParticle extends SpriteBillboardParticle {
    protected static final float BLINK_STEP = 0.2f;
    private final SpriteProvider spriteProvider;
    protected float nextAlphaGoal;
    protected double xTarget;
    protected double yTarget;
    protected double zTarget;
    protected int targetChangeCooldown = 0;

    public EmberParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;

        this.maxAge = 100;
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);
        this.alpha = 0f;
        this.nextAlphaGoal = 1f;

        this.scale = 0.01f;

        this.red = 1.0f;
        this.green = 106f / 255f;
        this.blue = 0f / 255f;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
            Vec3f2.rotate(quaternion2);
            Vec3f2.scale(j);
            Vec3f2.add(f, g, h);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = 15728880;
        float a = Math.min(1f, Math.max(0f, this.alpha));

        vertexConsumer.vertex(Vec3fs[0].getX(), Vec3fs[0].getY(), Vec3fs[0].getZ()).texture(maxU, maxV).color(red, green, blue, a).light(l).next();
        vertexConsumer.vertex(Vec3fs[1].getX(), Vec3fs[1].getY(), Vec3fs[1].getZ()).texture(maxU, minV).color(red, green, blue, a).light(l).next();
        vertexConsumer.vertex(Vec3fs[2].getX(), Vec3fs[2].getY(), Vec3fs[2].getZ()).texture(minU, minV).color(red, green, blue, a).light(l).next();
        vertexConsumer.vertex(Vec3fs[3].getX(), Vec3fs[3].getY(), Vec3fs[3].getZ()).texture(minU, maxV).color(red, green, blue, a).light(l).next();
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        for (int i = 0; i < 5; i++) {
            Block blockUnder = world.getBlockState(new BlockPos(this.x, this.y - i, this.z)).getBlock();
            if (blockUnder instanceof CampfireBlock || blockUnder instanceof FireBlock) {
                this.velocityY += 0.01f / (i + 1);
                break;
            } else {
                this.velocityY = Math.max(this.velocityY - 0.001f, 0.05f);
            }
        }

        if (this.age++ >= this.maxAge) {
            if (this.alpha <= 0f) {
                this.markDead();
            } else {
                this.alpha -= 0.1f;
            }
        } else {
            this.alpha += 0.1f;
        }

        if (nextAlphaGoal > alpha) {
            alpha = Math.min(alpha + BLINK_STEP, 1f);
        } else if (nextAlphaGoal < alpha) {
            alpha = Math.max(alpha - BLINK_STEP, 0f);
        }

        this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

        if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
            selectBlockTarget();
        }

        Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
        double length = targetVector.length();
        targetVector = targetVector.multiply(0.1 / length);


        velocityX = (0.3) * velocityX + (0.1) * targetVector.x * 3;
        velocityZ = (0.3) * velocityZ + (0.1) * targetVector.z * 3;

        if (!new BlockPos(x, y, z).equals(this.getTargetPosition())) {
            this.move(velocityX, velocityY, velocityZ);
        }
    }

    private void selectBlockTarget() {
        // Behaviour
        double groundLevel = 0;
        for (int i = 0; i < 20; i++) {
            BlockState checkedBlock = this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z));
            if (!checkedBlock.getBlock().canMobSpawnInside()) {
                groundLevel = this.y - i;
            }
            if (groundLevel != 0) break;
        }

        this.xTarget = this.x + random.nextGaussian();
        this.yTarget = this.y + random.nextGaussian() * 5;
        this.zTarget = this.z + random.nextGaussian();

        targetChangeCooldown = random.nextInt() % 10;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget, this.zTarget);
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EmberParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

}
