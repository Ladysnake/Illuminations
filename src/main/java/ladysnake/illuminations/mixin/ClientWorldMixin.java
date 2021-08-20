package ladysnake.illuminations.mixin;

import com.google.common.collect.ImmutableSet;
import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.data.IlluminationData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
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

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, @Coerce Object blockParticle, BlockPos.Mutable pos, CallbackInfo ci) {
        Biome.Category biomeCategory = this.getBiome(pos).getCategory();
        Identifier biome = this.getRegistryManager().get(Registry.BIOME_KEY).getId(this.getBiome(pos));

        if (Illuminations.ILLUMINATIONS_BIOME_CATEGORIES.containsKey(biomeCategory)) {
            ImmutableSet<IlluminationData> illuminationDataSet = Illuminations.ILLUMINATIONS_BIOME_CATEGORIES.get(biomeCategory);
            spawnParticles(pos, illuminationDataSet);
        }

        if (Illuminations.ILLUMINATIONS_BIOMES.containsKey(biome)) {
            ImmutableSet<IlluminationData> illuminationDataSet = Illuminations.ILLUMINATIONS_BIOMES.get(biome);
            spawnParticles(pos, illuminationDataSet);
        }

        // spooky eyes
        if (Illuminations.EYES_LOCATION_PREDICATE.test(this, pos)
                && random.nextFloat() <= Illuminations.EYES_SPAWN_CHANCE) {
            this.addParticle(Illuminations.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
        }
    }

    private void spawnParticles(BlockPos.Mutable pos, ImmutableSet<IlluminationData> illuminationDataSet) {
        illuminationDataSet.forEach(illuminationData -> {
            if (illuminationData.locationSpawnPredicate().test(this, pos)
                    && illuminationData.shouldAddParticle(this.random)) {
                this.addParticle(illuminationData.illuminationType(), (double) pos.getX() + this.random.nextDouble(), (double) pos.getY() + this.random.nextDouble(), (double) pos.getZ() + this.random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
        });
    }

    @Inject(method = "addPlayer", at = @At(value = "RETURN"))
    public void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        Illuminations.loadPlayerCosmetics();
    }

}
