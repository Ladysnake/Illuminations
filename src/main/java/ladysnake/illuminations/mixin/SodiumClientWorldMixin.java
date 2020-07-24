package ladysnake.illuminations.mixin;

import com.google.common.collect.ImmutableSet;
import ladysnake.illuminations.client.IlluminationData;
import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class SodiumClientWorldMixin extends World {
    @Shadow @Final private WorldRenderer worldRenderer;

    protected SodiumClientWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    @Inject(method = "performBiomeParticleDisplayTick", at = @At("RETURN"))
    public void randomBlockDisplayTick(BlockPos pos, Random random, CallbackInfo info) {
        Biome.Category biomeCategory = this.getBiome(pos).getCategory();

        // if night, in correct biome and not in a cave
        if (IlluminationsClient.ILLUMINATIONS_BIOME_CATEGORIES.containsKey(biomeCategory)) {
            ImmutableSet<IlluminationData> illuminationDataSet = IlluminationsClient.ILLUMINATIONS_BIOME_CATEGORIES.get(biomeCategory);

            illuminationDataSet.forEach(illuminationData -> {
                if (illuminationData.getTimeSpawnPredicate().test(this.getTimeOfDay())
                        && illuminationData.getLocationSpawnPredicate().test(this.getWorld(), pos)
                        && illuminationData.shouldAddParticle(this.random)) {
                    this.addParticle(illuminationData.getIlluminationType(), (double)pos.getX() + this.random.nextDouble(), (double)pos.getY() + this.random.nextDouble(), (double)pos.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            });
        }
    }

}
