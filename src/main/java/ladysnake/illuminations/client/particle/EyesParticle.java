package ladysnake.illuminations.client.particle;

import ladysnake.illuminations.client.IlluminationsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Random;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.variant;

public class EyesParticle extends SpriteBillboardParticle {
    private final PlayerEntity owner;
    protected float alpha = 0f;

    private static final Random RANDOM = new Random();
    private final SpriteProvider spriteProvider;

    public EyesParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.owner = world.getClosestPlayer((new TargetPredicate()).setBaseMaxDistance(1D), this.x, this.y, this.z);

        this.scale *= 1f + new Random().nextFloat();
        this.maxAge = 60; // live between 20 seconds and one minute
        this.collidesWithWorld = true;
        this.setSpriteForAge(spriteProvider);

        this.colorRed = 1f;
        this.colorGreen = 1f;
        this.colorBlue = 1f;

        if (this.owner != null) {
            String playerColor = IlluminationsClient.PLAYER_COSMETICS.get(owner.getUuid()).getColor();
            Color color = Color.decode(playerColor);
            this.colorRed = color.getRed()/255f;
            this.colorGreen = color.getGreen()/255f;
            this.colorBlue = color.getBlue()/255f;

            this.setPos(owner.getX()+RANDOM.nextFloat()*2-1, owner.getY()+owner.getHeight()/2f+RANDOM.nextFloat()*1.5-0.5, owner.getZ()+RANDOM.nextFloat()*2-1);
        } else {
            this.markDead();
        }

    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternion quaternion2;
        if (this.angle == 0.0F) {
            quaternion2 = camera.getRotation();
        } else {
            quaternion2 = new Quaternion(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion2.hamiltonProduct(Vector3f.POSITIVE_Z.getRadialQuaternion(i));
        }

        Vector3f vector3f = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f.rotate(quaternion2);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for(int k = 0; k < 4; ++k) {
            Vector3f vector3f2 = vector3fs[k];
            vector3f2.rotate(quaternion2);
            vector3f2.scale(j);
            vector3f2.add(f, g, h);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = 15728880;

        vertexConsumer.vertex((double)vector3fs[0].getX(), (double)vector3fs[0].getY(), (double)vector3fs[0].getZ()).texture(maxU, maxV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex((double)vector3fs[1].getX(), (double)vector3fs[1].getY(), (double)vector3fs[1].getZ()).texture(maxU, minV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex((double)vector3fs[2].getX(), (double)vector3fs[2].getY(), (double)vector3fs[2].getZ()).texture(minU, minV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
        vertexConsumer.vertex((double)vector3fs[3].getX(), (double)vector3fs[3].getY(), (double)vector3fs[3].getZ()).texture(minU, maxV).color(colorRed, colorGreen, colorBlue, alpha).light(l).next();
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
        if (this.age < 5) {
            alpha = 0f;
        } else {
            alpha = 1f;
        }

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.setSpriteForAge(spriteProvider);

        // fade and die on daytime or if old enough
        if ((world.getTimeOfDay() >= 1000 && world.getTimeOfDay() < 13000) || this.age++ >= this.maxAge) {
            this.markDead();
        }
    }

}
