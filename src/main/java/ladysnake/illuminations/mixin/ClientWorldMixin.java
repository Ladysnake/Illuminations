package ladysnake.illuminations.mixin;

import com.google.common.collect.ImmutableSet;
import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.data.IlluminationData;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l) {
        super(properties, registryKey, dimensionType, supplier, bl, bl2, l);
    }

    @Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    public void randomBlockDisplayTick(int xCenter, int yCenter, int zCenter, int radius, Random random, boolean spawnBarrierParticles, BlockPos.Mutable pos, CallbackInfo info) {
        Biome.Category biomeCategory = this.getBiome(pos).getCategory();

        if (IlluminationsClient.ILLUMINATIONS_BIOME_CATEGORIES.containsKey(biomeCategory)) {
            ImmutableSet<IlluminationData> illuminationDataSet = IlluminationsClient.ILLUMINATIONS_BIOME_CATEGORIES.get(biomeCategory);
            illuminationDataSet.forEach(illuminationData -> {
                if (illuminationData.getTimeSpawnPredicate().test(this.getTimeOfDay())
                        && illuminationData.getLocationSpawnPredicate().test(this, pos)
                        && illuminationData.shouldAddParticle(this.random)) {
                    this.addParticle(illuminationData.getIlluminationType(), (double)pos.getX() + this.random.nextDouble(), (double)pos.getY() + this.random.nextDouble(), (double)pos.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            });
        }

        // spooky eyes
        if (IlluminationsClient.EYES_TIME_PREDICATE.test(this.getTimeOfDay())
                && IlluminationsClient.EYES_LOCATION_PREDICATE.test(this, pos)
                && random.nextFloat() <= IlluminationsClient.EYES_SPAWN_CHANCE) {
            this.addParticle(IlluminationsClient.EYES, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
        }
    }

}
