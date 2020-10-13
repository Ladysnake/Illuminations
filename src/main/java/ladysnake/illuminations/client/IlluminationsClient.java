package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.network.EntityDispatcher;
import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.particle.GlowwormParticle;
import ladysnake.illuminations.client.particle.PlanktonParticle;
import ladysnake.illuminations.client.particle.overhead.JackoParticle;
import ladysnake.illuminations.client.particle.overhead.OverheadParticle;
import ladysnake.illuminations.client.particle.aura.TwilightFireflyParticle;
import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.network.Packets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger("Illuminations");

    // illuminations auras
    private static final String URL = "https://illuminations.glitch.me/data";
    private static final Gson GSON = new GsonBuilder().create();
    static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>(){}.getType();

    public static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS;
    public static ImmutableMap<String, AuraData> AURAS_DATA;
    public static ImmutableMap<String, DefaultParticleType> OVERHEADS_DATA;

    // particle types
    public static DefaultParticleType FIREFLY;
    public static DefaultParticleType GLOWWORM;
    public static DefaultParticleType PLANKTON;

    // aura particle types
    public static DefaultParticleType TWILIGHT_AURA;
    public static DefaultParticleType PRIDE_OVERHEAD;
    public static DefaultParticleType TRANS_PRIDE_OVERHEAD;
    public static DefaultParticleType JACKO_OVERHEAD;

    // spawn biomes
    public static ImmutableMap<Biome.Category, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOME_CATEGORIES;

    // spawn predicates
    public static final Predicate<Long> FIREFLY_TIME_PREDICATE = aLong -> (aLong >= 13000 && aLong < 23000);
    public static final BiPredicate<World, BlockPos> FIREFLY_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.AIR;
    public static final Predicate<Long> GLOWWORM_TIME_PREDICATE = aLong -> true;
    public static final BiPredicate<World, BlockPos> GLOWWORM_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR;
    public static final Predicate<Long> PLANKTON_TIME_PREDICATE = aLong -> true;
    public static final BiPredicate<World, BlockPos> PLANKTON_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getFluidState().isIn(FluidTags.WATER) && world.getLightLevel(blockPos) < 2;

    @Override
    public void onInitializeClient() {
        // get illuminations player cosmetics
        CompletableFuture.supplyAsync(() -> {
            try(Reader reader = new InputStreamReader(new URL(URL).openStream())) {
                Map<UUID, PlayerCosmeticData> playerData = GSON.fromJson(reader, COSMETIC_SELECT_TYPE);
                return playerData;
            } catch (MalformedURLException e) {
                logger.log(Level.ERROR, "Could not get player cosmetics because of malformed URL: " + e.getMessage());
            } catch (IOException e) {
                logger.log(Level.ERROR, "Could not get player cosmetics because of I/O Error: " + e.getMessage());
            }

            return null;
        }).thenAcceptAsync(playerData -> {
            if (playerData != null) {
                PLAYER_COSMETICS = playerData;
                logger.log(Level.INFO, "Player cosmetics retrieved");
            } else {
                PLAYER_COSMETICS = Collections.emptyMap();
                logger.log(Level.WARN, "Player cosmetics could not be retrieved, cosmetics will be ignored");
            }
        }, MinecraftClient.getInstance());

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
        PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        TRANS_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:trans_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.TRANS_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        JACKO_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:jacko_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.JACKO_OVERHEAD, JackoParticle.DefaultFactory::new);

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

        // aura matching and spawn chances + overhead matching
        AURAS_DATA = ImmutableMap.<String, AuraData>builder()
                .put("twilight", new AuraData(TWILIGHT_AURA, 0.1f, 1))
                .build();
        OVERHEADS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
                .put("pride", PRIDE_OVERHEAD)
                .put("trans_pride", TRANS_PRIDE_OVERHEAD)
                .put("jacko", JACKO_OVERHEAD)
                .build();

        // register renders
        registerRenders();

        // packet registry
        ClientSidePacketRegistry.INSTANCE.register(Packets.SPAWN, EntityDispatcher::spawnFrom);
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(Illuminations.BUGBALL, (manager, context) -> new FlyingItemEntityRenderer(manager, context.getItemRenderer()));
    }
}
