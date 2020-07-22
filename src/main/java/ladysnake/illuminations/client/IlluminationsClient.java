package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.particle.GlowwormParticle;
import ladysnake.illuminations.client.particle.PlanktonParticle;
import ladysnake.illuminations.client.particle.aura.TwilightFireflyParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Blocks;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger("Illuminations");

    // illuminations auras
    private static final String URL = "https://illuminations.glitch.me/auras.json";
    private static final Gson GSON = new GsonBuilder().create();
    private static final Type AURA_SELECT_TYPE = new TypeToken<Map<UUID, String>>(){}.getType();
    public static ImmutableMap<UUID, String> PLAYER_AURAS;
    public static ImmutableMap<String, AuraData> AURAS_DATA;

    // particle types
    public static DefaultParticleType FIREFLY;
    public static DefaultParticleType GLOWWORM;
    public static DefaultParticleType PLANKTON;

    // aura particle types
    public static DefaultParticleType TWILIGHT_AURA;

    // spawn biomes
    public static ImmutableMap<Biome.Category, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOME_CATEGORIES;

    // spawn predicates
    public static final Predicate<Long> FIREFLY_TIME_PREDICATE = aLong -> !(aLong >= 1000 && aLong < 13000);
    public static final BiPredicate<World, BlockPos> FIREFLY_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.AIR;
    public static final Predicate<Long> GLOWWORM_TIME_PREDICATE = aLong -> true;
    public static final BiPredicate<World, BlockPos> GLOWWORM_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR;
    public static final Predicate<Long> PLANKTON_TIME_PREDICATE = aLong -> true;
    public static final BiPredicate<World, BlockPos> PLANKTON_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getFluidState().isIn(FluidTags.WATER) && world.getLightLevel(blockPos) < 2;

    @Override
    public void onInitializeClient() {
        // get illuminations users
        getUsers();

        // particles
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);
        GLOWWORM = Registry.register(Registry.PARTICLE_TYPE, "illuminations:glowworm", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GLOWWORM, GlowwormParticle.DefaultFactory::new);
        PLANKTON = Registry.register(Registry.PARTICLE_TYPE, "illuminations:plankton", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.PLANKTON, PlanktonParticle.DefaultFactory::new);

        // aura particles
        TWILIGHT_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:twilight_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);

        // spawn biomes for Illuminations
        ILLUMINATIONS_BIOME_CATEGORIES = ImmutableMap.<Biome.Category, ImmutableSet<IlluminationData>>builder()
                .put(Biome.Category.JUNGLE, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00002F), // few
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00004F))) // few
                .put(Biome.Category.PLAINS, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00002F), // few
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00004F))) // few
                .put(Biome.Category.SAVANNA, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00002F), // few
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00004F))) // few
                .put(Biome.Category.TAIGA, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00002F), // few
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00004F))) // few
                .put(Biome.Category.FOREST, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00010F), // some
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00020F))) // some
                .put(Biome.Category.RIVER, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00010F), // some
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00020F))) // some
                .put(Biome.Category.SWAMP, ImmutableSet.of(
                        new IlluminationData(FIREFLY, FIREFLY_TIME_PREDICATE, FIREFLY_LOCATION_PREDICATE, 0.00025F), // many
                        new IlluminationData(GLOWWORM, GLOWWORM_TIME_PREDICATE, GLOWWORM_LOCATION_PREDICATE, 0.00050F))) // many
                .put(Biome.Category.OCEAN, ImmutableSet.of(
                        new IlluminationData(PLANKTON, PLANKTON_TIME_PREDICATE, PLANKTON_LOCATION_PREDICATE, 0.00250F) // many
                ))
                .build();

        // aura spawn chances
        AURAS_DATA = ImmutableMap.<String, AuraData>builder()
                .put("twilight", new AuraData(TWILIGHT_AURA, 0.1f))
                .build();
    }

    public static void getUsers() {
        try(Reader reader = new InputStreamReader(new URL(URL).openStream())) {
            PLAYER_AURAS = ImmutableMap.copyOf((Map<? extends UUID, ? extends String>) GSON.fromJson(reader, AURA_SELECT_TYPE));
            logger.log(Level.INFO, "Illuminations player auras retrieved");
        } catch (MalformedURLException e) {
            logger.log(Level.ERROR, "Could not get Illuminations player auras because of malformed URL: " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not get Illuminations player auras because of I/O Error: " + e.getMessage());
        }
    }

}
