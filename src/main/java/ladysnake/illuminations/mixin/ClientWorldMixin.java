package ladysnake.illuminations.mixin;

import com.google.common.collect.ImmutableSet;
import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.enums.BiomeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    @Shadow
    @Final
    private MinecraftClient client;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryKey, RegistryEntry<DimensionType> dimensionType, Supplier<Profiler> supplier, boolean bl, boolean bl2, long l) {
        super(properties, registryKey, dimensionType, supplier, bl, bl2, l);
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, Random random, @Coerce Object blockParticle, BlockPos.Mutable blockPos, CallbackInfo ci) {
        BlockPos.Mutable pos = blockPos.add(this.random.nextGaussian() * 50, this.random.nextGaussian() * 25, this.random.nextGaussian() * 50).mutableCopy();

        Biome b = this.getBiome(pos).value();
        Identifier biome = this.getRegistryManager().get(Registry.BIOME_KEY).getId(b);

        // Main biome settings
        BiomeCategory biomeCategory = BiomeCategory.find(biome, b.getCategory()); // Returns OTHER if no association for this biome was found.
        spawnParticles(pos, Illuminations.ILLUMINATIONS_BIOME_CATEGORIES.get(biomeCategory));

        // Other miscellaneous biome settings
        if (Illuminations.ILLUMINATIONS_BIOMES.containsKey(biome)) {
            ImmutableSet<IlluminationData> illuminationDataSet = Illuminations.ILLUMINATIONS_BIOMES.get(biome);
            spawnParticles(pos, illuminationDataSet);
        }

        // spooky eyes
        if (Illuminations.EYES_LOCATION_PREDICATE.test(this, pos)
                && random.nextFloat() <= Config.getEyesInTheDarkSpawnRate().spawnRate) {
            this.addParticle(Illuminations.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
        }
    }

    private void spawnParticles(BlockPos.Mutable pos, ImmutableSet<IlluminationData> illuminationDataSet) {
        for (IlluminationData illuminationData : illuminationDataSet) {
            if (illuminationData.locationSpawnPredicate().test(this, pos)
                    && illuminationData.shouldAddParticle(this.random)) {
                this.addParticle(illuminationData.illuminationType(), (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Inject(method = "addPlayer", at = @At(value = "RETURN"))
    public void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        Illuminations.loadPlayerCosmetics();
    }

}
