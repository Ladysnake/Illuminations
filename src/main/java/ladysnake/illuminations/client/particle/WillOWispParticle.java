package ladysnake.illuminations.client.particle;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.entity.model.WillOWispModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.stream.Stream;

public class WillOWispParticle extends Particle {
    private final Model model;
    private final RenderLayer LAYER;
    public final Identifier texture;

    public float yaw;
    public float pitch;
    public float prevYaw;
    public float prevPitch;

    public float speedModifier;

    protected WillOWispParticle(ClientWorld world, double x, double y, double z, Identifier texture) {
        super(world, x, y, z);
        this.texture = texture;
        this.model = new WillOWispModel();
        this.LAYER = RenderLayer.getEntityTranslucent(texture);
        this.gravityStrength = 0.0F;
        this.maxAge = 9999;
        speedModifier = 0.1f + Math.max(0, random.nextFloat() - 0.1f);
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        public DefaultFactory(SpriteProvider spriteProvider) {
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new WillOWispParticle(clientWorld, d, e, f,  new Identifier(IlluminationsClient.MODID, "textures/entity/will_o_wisp.png"));
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(f, g, h);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, this.prevYaw, this.yaw) - 180));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.lerp(g, this.prevPitch, this.pitch)));
        matrixStack.scale(0.5F, -0.5F, 0.5F);
        matrixStack.translate(0, -1, 0);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer2 = immediate.getBuffer(this.LAYER);
        if (colorAlpha > 0) {
            this.model.render(matrixStack, vertexConsumer2, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);
        }
        immediate.draw();
    }

    protected double xTarget;
    protected double yTarget;
    protected double zTarget;
    protected int targetChangeCooldown = 0;

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

        if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
            selectBlockTarget();
        }

        Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
        double length = targetVector.length();
        targetVector = targetVector.multiply(speedModifier / length);

        velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
        velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
        velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;

        this.prevYaw = this.yaw;
        this.prevPitch = this.pitch;
        Vec3d vec3d = new Vec3d(velocityX, velocityY, velocityZ);
        float f = MathHelper.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
        this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
        this.pitch = (float) (MathHelper.atan2(vec3d.y, f) * 57.2957763671875D);

        for (int i = 0; i < 10 * this.speedModifier; i++) {
            this.world.addParticle(IlluminationsClient.WISP_TRAIL, this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0.2d, 0);
        }

        if (!new BlockPos(x, y, z).equals(this.getTargetPosition())) {
            this.move(velocityX, velocityY, velocityZ);
        }

        if (random.nextInt(20) == 0) {
            this.world.playSound(new BlockPos(this.x, this.y, this.z), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, 1.0f, 1.5f, true);
        }
    }

    @Override
    public void move(double dx, double dy, double dz) {
        double d = dx;
        double e = dy;
        if (this.collidesWithWorld && (dx != 0.0D || dy != 0.0D || dz != 0.0D)) {
            Vec3d vec3d = Entity.adjustMovementForCollisions(null, new Vec3d(dx, dy, dz), this.getBoundingBox(), this.world, ShapeContext.absent(), new ReusableStream(Stream.empty()));
            dx = vec3d.x;
            dy = vec3d.y;
            dz = vec3d.z;
        }

        if (dx != 0.0D || dy != 0.0D || dz != 0.0D) {
            this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
            this.repositionFromBoundingBox();
        }

        this.onGround = dy != dy && e < 0.0D && !this.world.getBlockState(new BlockPos(this.x, this.y, this.z)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);
        if (d != dx) {
            this.velocityX = 0.0D;
        }

        if (dz != dz) {
            this.velocityZ = 0.0D;
        }
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    private void selectBlockTarget() {
        // Behaviour
        this.xTarget = this.x + random.nextGaussian() * 10;
        this.yTarget = this.y + random.nextGaussian() * 5;
        this.zTarget = this.z + random.nextGaussian() * 10;

        BlockPos targetPos = new BlockPos(this.xTarget, this.yTarget, this.zTarget);
        if (this.world.getBlockState(targetPos).isFullCube(world, targetPos) && !this.world.getBlockState(new BlockPos(this.x, this.y, this.z)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
            this.selectBlockTarget();
        }

        speedModifier = 0.1f + Math.max(0, random.nextFloat() - 0.1f);
        targetChangeCooldown = random.nextInt() % (int) (100 / this.speedModifier);
    }
}
