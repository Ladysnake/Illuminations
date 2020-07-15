package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import ladysnake.illuminations.client.particle.FireflyParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static DefaultParticleType FIREFLY;

    public static ImmutableMap<Biome.Category, IlluminationData> ILLUMINATIONS_BIOME_CATEGORIES;
    @Override
    public void onInitializeClient() {
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);

        ILLUMINATIONS_BIOME_CATEGORIES = ImmutableMap.<Biome.Category, IlluminationData>builder()
                .put(Biome.Category.JUNGLE, new IlluminationData(FIREFLY, 0.00002F)) // few
                .put(Biome.Category.PLAINS, new IlluminationData(FIREFLY, 0.00002F)) // few
                .put(Biome.Category.SAVANNA, new IlluminationData(FIREFLY, 0.00002F)) // few
                .put(Biome.Category.TAIGA, new IlluminationData(FIREFLY, 0.00002F)) // few
                .put(Biome.Category.FOREST, new IlluminationData(FIREFLY, 0.00010F)) // some
                .put(Biome.Category.RIVER, new IlluminationData(FIREFLY, 0.00010F)) // some
                .put(Biome.Category.SWAMP, new IlluminationData(FIREFLY, 0.00025F)) // many
                .build();
    }

}
