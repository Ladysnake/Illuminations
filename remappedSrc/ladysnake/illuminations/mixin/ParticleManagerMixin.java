package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Shadow
    protected ClientWorld world;
    @Shadow
    @Final
    private Random random;

    @Shadow
    public abstract Particle addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Inject(method = "addBlockBreakParticles", at = @At(value = "RETURN"))
    public void addBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.getBlock() == Blocks.CHORUS_FLOWER) {
            for (int i = 0; i < (6 - state.get(ChorusFlowerBlock.AGE)) * 10 * Config.getChorusPetalsSpawnMultiplier(); i++) {
                this.addParticle(Illuminations.CHORUS_PETAL, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, random.nextGaussian() / 10f, random.nextGaussian() / 10f, random.nextGaussian() / 10f);
            }
        }
    }
}
