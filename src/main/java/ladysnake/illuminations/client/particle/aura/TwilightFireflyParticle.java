package ladysnake.illuminations.client.particle.aura;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.particle.FireflyParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Random;

public class TwilightFireflyParticle extends FireflyParticle {
    private final PlayerEntity owner;

    public TwilightFireflyParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

        this.maxAge = 20;
        this.owner = world.getClosestPlayer((new TargetPredicate()).setBaseMaxDistance(1D), this.x, this.y, this.z);
        this.maxHeight = 2;

        if (owner != null && owner.getUuid() != null && IlluminationsClient.PLAYER_COSMETICS.get(owner.getUuid()) != null) {
            String playerColor = IlluminationsClient.PLAYER_COSMETICS.get(owner.getUuid()).getColor();
            Color color = Color.decode(playerColor);
            this.colorRed = color.getRed()/255f;
            this.colorGreen = color.getGreen()/255f;
            this.colorBlue = color.getBlue()/255f;
            this.nextAlphaGoal = 1f;
        } else {
            this.markDead();
        }

        this.setPos(this.x + TwilightFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 2d, this.z + TwilightFireflyParticle.getWanderingDistance(this.random));
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new TwilightFireflyParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }

    @Override
    public void tick() {
        if (owner != null) {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            // fade and die on daytime or if old enough
            if (this.age++ >= this.maxAge) {
                nextAlphaGoal = -BLINK_STEP;
                if (colorAlpha < 0f) {
                    this.markDead();
                }
            }

            // blinking
            if (colorAlpha > nextAlphaGoal - BLINK_STEP && colorAlpha < nextAlphaGoal + BLINK_STEP) {
                nextAlphaGoal = new Random().nextFloat();
            } else {
                if (nextAlphaGoal > colorAlpha) {
                    colorAlpha = Math.min(colorAlpha + BLINK_STEP, 1f);
                } else if (nextAlphaGoal < colorAlpha) {
                    colorAlpha = Math.max(colorAlpha - BLINK_STEP, 0f);
                }
            }

            this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

            if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.025 / length);


            if (!this.world.getBlockState(new BlockPos(this.x, this.y - 0.1, this.z)).getBlock().canMobSpawnInside()) {
                velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
                velocityY = 0.05;
                velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
            } else {
                velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
                velocityY = (0.2) * velocityY + (0.1) * targetVector.y;
                velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
            }

            if (!new BlockPos(x, y, z).equals(this.getTargetPosition())) {
                this.move(velocityX, velocityY, velocityZ);
            }
        } else {
            this.markDead();
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

        this.xTarget = owner.getX() + random.nextGaussian();
        this.yTarget = Math.min(Math.max(owner.getY() + random.nextGaussian(), groundLevel), groundLevel + maxHeight);
        this.zTarget = owner.getZ() + random.nextGaussian();

        BlockPos targetPos = new BlockPos(this.xTarget, this.yTarget, this.zTarget);
        if (this.world.getBlockState(targetPos).isFullCube(world, targetPos)
                && this.world.getBlockState(targetPos).isSolidBlock(world, targetPos)) {
            this.yTarget += 1;
        }

        targetChangeCooldown = random.nextInt() % 100;
    }

    public static double getWanderingDistance(Random random) {
        return random.nextGaussian()/5d;
    }

}
