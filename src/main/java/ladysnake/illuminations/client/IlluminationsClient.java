package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.data.OverheadData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.particle.ChorusPetalParticle;
import ladysnake.illuminations.client.particle.EyesParticle;
import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.particle.GlowwormParticle;
import ladysnake.illuminations.client.particle.PlanktonParticle;
import ladysnake.illuminations.client.particle.aura.*;
import ladysnake.illuminations.client.particle.overhead.JackoParticle;
import ladysnake.illuminations.client.particle.overhead.OverheadParticle;
import ladysnake.illuminations.client.render.entity.feature.OverheadFeatureRenderer;
import ladysnake.illuminations.client.render.entity.model.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
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
    public static final String MODID = "illuminations";
    public static final Logger logger = LogManager.getLogger("Illuminations");

    // illuminations constants
    public static final float EYES_SPAWN_CHANCE = 0.001f;
    public static final int EYES_VANISHING_DISTANCE = 5;

    // illuminations cosmetics
    private static final String COSMETICS_URL = "https://illuminations.uuid.gg/data";
    private static final Gson COSMETICS_GSON = new GsonBuilder().create();
    static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>(){}.getType();
    public static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();
    public static ImmutableMap<String, AuraData> AURAS_DATA;
    public static ImmutableMap<String, DefaultParticleType> OLD_OVERHEADS_DATA;
    public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;

    // update information
    private static final String UPDATES_URL = "https://illuminations.glitch.me/latest?version=";

    // particle types
    public static DefaultParticleType FIREFLY;
    public static DefaultParticleType GLOWWORM;
    public static DefaultParticleType PLANKTON;
    public static DefaultParticleType EYES;
    public static DefaultParticleType CHORUS_PETAL;

    // aura particle types
    public static DefaultParticleType TWILIGHT_AURA;
    public static DefaultParticleType GHOSTLY_AURA;
    public static DefaultParticleType CHORUS_AURA;
    public static DefaultParticleType AUTUMN_LEAVES_AURA;
    public static DefaultParticleType SCULK_TENDRIL_AURA;
    public static DefaultParticleType SHADOWBRINGER_AURA;
    public static DefaultParticleType GOLDENROD_AURA;
    public static DefaultParticleType PRIDE_OVERHEAD;
    public static DefaultParticleType TRANS_PRIDE_OVERHEAD;
    public static DefaultParticleType JACKO_OVERHEAD;
    public static DefaultParticleType LESBIAN_PRIDE_OVERHEAD;
    public static DefaultParticleType BI_PRIDE_OVERHEAD;
    public static DefaultParticleType ACE_PRIDE_OVERHEAD;
    public static DefaultParticleType NB_PRIDE_OVERHEAD;
    public static DefaultParticleType INTERSEX_PRIDE_OVERHEAD;

    // spawn biomes
    public static ImmutableMap<Biome.Category, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOME_CATEGORIES;

    // spawn predicates
    public static final Predicate<Long> FIREFLY_TIME_PREDICATE = aLong -> true;
    public static final BiPredicate<World, BlockPos> FIREFLY_LOCATION_PREDICATE = (world, blockPos) -> {
        // sky angle --> time of day
        // 0.25965086 --> 13000
        // 0.7403491 --> 23000
        return (world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491)
            && world.getBlockState(blockPos).getBlock() == Blocks.AIR && world.isSkyVisible(blockPos);
    };
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
        loadPlayerCosmetics();

        // particles
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);
        GLOWWORM = Registry.register(Registry.PARTICLE_TYPE, "illuminations:glowworm", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GLOWWORM, GlowwormParticle.DefaultFactory::new);
        PLANKTON = Registry.register(Registry.PARTICLE_TYPE, "illuminations:plankton", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.PLANKTON, PlanktonParticle.DefaultFactory::new);
        EYES = Registry.register(Registry.PARTICLE_TYPE, "illuminations:eyes", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.EYES, EyesParticle.DefaultFactory::new);
        CHORUS_PETAL = Registry.register(Registry.PARTICLE_TYPE, "illuminations:chorus_petal", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.CHORUS_PETAL, ChorusPetalParticle.DefaultFactory::new);
        // aura particles
        TWILIGHT_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:twilight_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);
        GHOSTLY_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:ghostly_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GHOSTLY_AURA, GhostlyParticle.DefaultFactory::new);
        CHORUS_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:chorus_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.CHORUS_AURA, ChorusAuraParticle.DefaultFactory::new);
        AUTUMN_LEAVES_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:autumn_leaves", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.AUTUMN_LEAVES_AURA, AutumnLeavesParticle.DefaultFactory::new);
        SCULK_TENDRIL_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:sculk_tendril", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.SCULK_TENDRIL_AURA, SculkTendrilParticle.DefaultFactory::new);
        SHADOWBRINGER_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:shadowbringer_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.SHADOWBRINGER_AURA, ShadowbringerParticle.DefaultFactory::new);
        GOLDENROD_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:goldenrod_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.GOLDENROD_AURA, GoldenrodAuraParticle.DefaultFactory::new);
        PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        TRANS_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:trans_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.TRANS_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        JACKO_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:jacko_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.JACKO_OVERHEAD, JackoParticle.DefaultFactory::new);
        LESBIAN_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:lesbian_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.LESBIAN_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        BI_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:bi_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.BI_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        ACE_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:ace_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.ACE_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        NB_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:nb_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.NB_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);
        INTERSEX_PRIDE_OVERHEAD = Registry.register(Registry.PARTICLE_TYPE, "illuminations:intersex_pride_overhead", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.INTERSEX_PRIDE_OVERHEAD, OverheadParticle.DefaultFactory::new);  ///haha sex

        // crowns feature
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, livingEntityRenderer, registrationHelper) -> {
            if (entityType == EntityType.PLAYER) {
  		        registrationHelper.register(new OverheadFeatureRenderer((FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) livingEntityRenderer));
  	        }
        });

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
                        new IlluminationData(PLANKTON, PLANKTON_TIME_PREDICATE, PLANKTON_LOCATION_PREDICATE, 0.00250F))) // many
                .build();

        // aura matching and spawn chances + overhead matching + crown matching
        AURAS_DATA = ImmutableMap.<String, AuraData>builder()
                .put("twilight", new AuraData(TWILIGHT_AURA, 0.1f, 1))
                .put("ghostly", new AuraData(GHOSTLY_AURA, 0.1f, 1))
                .put("chorus", new AuraData(CHORUS_AURA, 0.1f, 1))
                .put("autumn_leaves", new AuraData(AUTUMN_LEAVES_AURA, 0.3f, 1))
                .put("sculk_tendril", new AuraData(SCULK_TENDRIL_AURA, 0.3f, 1))
                .put("shadowbringer", new AuraData(SHADOWBRINGER_AURA, 0.1f, 1))
                .put("goldenrod", new AuraData(GOLDENROD_AURA, 0.4f, 1))
                .build();
        OLD_OVERHEADS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
                .put("pride", PRIDE_OVERHEAD)
                .put("trans_pride", TRANS_PRIDE_OVERHEAD)
                .put("jacko", JACKO_OVERHEAD)
                .put("lesbian_pride_overhead", LESBIAN_PRIDE_OVERHEAD)
                .put("bi_pride_overhead", BI_PRIDE_OVERHEAD)
                .put("ace_pride_overhead", ACE_PRIDE_OVERHEAD)
                .put("nb_pride_overhead", NB_PRIDE_OVERHEAD)
                .put("intersex_pride_overhead", INTERSEX_PRIDE_OVERHEAD)
                .build();
        OVERHEADS_DATA = ImmutableMap.<String, OverheadData>builder()
                .put("solar_crown", new OverheadData(new CrownEntityModel(), "solar_crown"))
                .put("frost_crown", new OverheadData(new CrownEntityModel(), "frost_crown"))
                .put("chorus_crown", new OverheadData(new CrownEntityModel(), "chorus_crown"))
                .put("dragon_horns", new OverheadData(new CrownEntityModel(), "dragon_horns"))
                .put("deepsculk_horns", new OverheadData(new HornEntityModel(), "deepsculk_horns"))
                .put("springfae_horns", new OverheadData(new HornEntityModel(), "springfae_horns"))
                .put("bloodfiend_crown", new OverheadData(new CrownEntityModel(), "bloodfiend_crown"))
                .put("dreadlich_crown", new OverheadData(new CrownEntityModel(), "dreadlich_crown"))
                .put("mooncult_crown", new OverheadData(new CrownEntityModel(), "mooncult_crown"))
                .put("voidheart_tiara", new OverheadData(new VoidheartTiaraEntityModel(), "voidheart_tiara"))
                .put("worldweaver_halo", new OverheadData(new WorldweaverHaloEntityModel(), "worldweaver_halo"))
                .put("summerbreeze_wreath", new OverheadData(new WreathEntityModel(), "summerbreeze_wreath"))
                .put("glowsquid_cult_crown", new OverheadData(new TiaraCrownEntityModel(), "glowsquid_cult_crown"))
                .put("timeaspect_cult_crown", new OverheadData(new TiaraCrownEntityModel(), "timeaspect_cult_crown"))
                .build();
    }

    public static void loadPlayerCosmetics() {
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
        }).exceptionally(throwable -> {
            logger.log(Level.ERROR, "Could not get player cosmetics because wtf is happening", throwable);
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
    }
}
