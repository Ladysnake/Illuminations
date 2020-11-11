package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.particle.EyesParticle;
import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.particle.GlowwormParticle;
import ladysnake.illuminations.client.particle.PlanktonParticle;
import ladysnake.illuminations.client.particle.aura.GhostlyParticle;
import ladysnake.illuminations.client.particle.aura.TwilightFireflyParticle;
import ladysnake.illuminations.client.particle.overhead.JackoParticle;
import ladysnake.illuminations.client.particle.overhead.OverheadParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static final Logger logger = LogManager.getLogger("Illuminations");

    // illuminations constants
    public static final float EYES_SPAWN_CHANCE = 0.001f;
    public static final int EYES_VANISHING_DISTANCE = 5;

    // illuminations cosmetics
    private static final String COSMETICS_URL = "https://illuminations.glitch.me/data";
    private static final Gson COSMETICS_GSON = new GsonBuilder().create();
    static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>(){}.getType();
    public static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();
    public static ImmutableMap<String, AuraData> AURAS_DATA;
    public static ImmutableMap<String, DefaultParticleType> OVERHEADS_DATA;

    // update information
    private static final String UPDATES_URL = "https://illuminations.glitch.me/latest?version=";
    private static String LATEST_VERSION;
    private static String LATEST_DOWNLOAD_URL;

    // particle types
    public static DefaultParticleType FIREFLY;
    public static DefaultParticleType GLOWWORM;
    public static DefaultParticleType PLANKTON;
    public static DefaultParticleType EYES;

    // aura particle types
    public static DefaultParticleType TWILIGHT_AURA;
    public static DefaultParticleType GHOSTLY_AURA;
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
    public static final Predicate<Long> EYES_TIME_PREDICATE = aLong -> ((Config.getEyesInTheDark() == Config.EyesInTheDark.ENABLE && LocalDate.now().getMonth() == Month.OCTOBER) || Config.getEyesInTheDark() == Config.EyesInTheDark.ALWAYS);
    public static final BiPredicate<World, BlockPos> EYES_LOCATION_PREDICATE = (world, blockPos) -> (world.getBlockState(blockPos).getBlock() == Blocks.AIR || world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR) && world.getLightLevel(blockPos) <= 0 && world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), EYES_VANISHING_DISTANCE, false) == null && world.getRegistryKey().equals(World.OVERWORLD);

    @Override
    public void onInitializeClient() {
        // load config
        Config.load();

        // get illuminations player cosmetics
        CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
                Map<UUID, PlayerCosmeticData> playerData = COSMETICS_GSON.fromJson(reader, COSMETIC_SELECT_TYPE);
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

        // get illuminations latest version for the current minecraft version
        String minecraftVersion =  MinecraftClient.getInstance().getGame().getVersion().getName();
        String illuminationsVersion = FabricLoader.getInstance().getModContainer("illuminations").get().getMetadata().getVersion().getFriendlyString();
        CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new InputStreamReader(new URL(UPDATES_URL + minecraftVersion).openStream())) {
                JsonParser jp = new JsonParser();
                JsonElement jsonElement = jp.parse(reader);
                return jsonElement.getAsJsonObject();
            } catch (MalformedURLException e) {
                logger.log(Level.ERROR, "Could not get update information because of malformed URL: " + e.getMessage());
            } catch (IOException e) {
                logger.log(Level.ERROR, "Could not get update information because of I/O Error: " + e.getMessage());
            }

            return null;
        }).thenAcceptAsync(latestVersionJson -> {
            if (latestVersionJson != null) {
                String latestVersion = latestVersionJson.get("version").getAsString();
                String latestFileName = latestVersionJson.get("filename").getAsString();
                // if not the latest version, update toast
                if (!latestVersion.equalsIgnoreCase(illuminationsVersion)) {
                    LATEST_VERSION = latestVersion;
                    LATEST_DOWNLOAD_URL = latestVersionJson.get("download").getAsString();
                    logger.log(Level.INFO, "Currently present Illuminations version is "+illuminationsVersion+" while the latest Illuminations version for Minecraft "+minecraftVersion+" is Illuminations "+latestVersion+"; how about updating?");

                    try {
                        // download new jar
                        URL website = new URL(latestVersionJson.get("download").getAsString());
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("mods/"+latestFileName);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    } catch (MalformedURLException e) {
                        logger.log(Level.ERROR, "Could not download update because of malformed URL: " + e.getMessage());
                    } catch (IOException e) {
                        logger.log(Level.ERROR, "Could not download update because of I/O Error: " + e.getMessage());
                    }
                } else {
                    logger.log(Level.INFO, "Illuminations is on the latest version, no update needed");
                }
            } else {
                logger.log(Level.WARN, "Update information could not be retrieved, auto-update will not be available");
            }
        }, MinecraftClient.getInstance());

        // particles
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);
        GLOWWORM = Registry.register(Registry.PARTICLE_TYPE, "illuminations:glowworm", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GLOWWORM, GlowwormParticle.DefaultFactory::new);
        PLANKTON = Registry.register(Registry.PARTICLE_TYPE, "illuminations:plankton", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.PLANKTON, PlanktonParticle.DefaultFactory::new);
        EYES = Registry.register(Registry.PARTICLE_TYPE, "illuminations:eyes", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.EYES, EyesParticle.DefaultFactory::new);

        // aura particles
        TWILIGHT_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:twilight_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);
        GHOSTLY_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:ghostly_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GHOSTLY_AURA, GhostlyParticle.DefaultFactory::new);
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
                .put("ghostly", new AuraData(GHOSTLY_AURA, 0.1f, 1))
                .build();
        OVERHEADS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
                .put("pride", PRIDE_OVERHEAD)
                .put("trans_pride", TRANS_PRIDE_OVERHEAD)
                .put("jacko", JACKO_OVERHEAD)
                .build();
    }

    public static String getLatestVersion() {
        return LATEST_VERSION;
    }
}
