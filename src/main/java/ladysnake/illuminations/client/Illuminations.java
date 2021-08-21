package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.data.OverheadData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.particle.*;
import ladysnake.illuminations.client.particle.aura.*;
import ladysnake.illuminations.client.particle.overhead.JackoParticle;
import ladysnake.illuminations.client.particle.overhead.PetParticle;
import ladysnake.illuminations.client.particle.overhead.PlayerWispParticle;
import ladysnake.illuminations.client.render.entity.feature.DripFeatureRenderer;
import ladysnake.illuminations.client.render.entity.feature.OverheadFeatureRenderer;
import ladysnake.illuminations.client.render.entity.model.*;
import ladysnake.illuminations.updater.IlluminationsUpdater;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

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
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class Illuminations implements ClientModInitializer {
    public static final String MODID = "illuminations";
    public static final Logger logger = LogManager.getLogger("Illuminations");

    // illuminations constants
    public static final int EYES_VANISHING_DISTANCE = 5;
    public static final Gson COSMETICS_GSON = new GsonBuilder().registerTypeAdapter(PlayerCosmeticData.class, new PlayerCosmeticDataParser()).create();
    // spawn predicates
    public static final BiPredicate<World, BlockPos> FIREFLY_LOCATION_PREDICATE = (world, blockPos) -> {
        // sky angle --> time of day
        // 0.25965086 --> 13000
        // 0.7403491 --> 23000
        return (Config.isDoFireflySpawnAlways() || ((world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491)))
                && world.getBlockState(blockPos).getBlock() == Blocks.AIR && (Config.isDoFireflySpawnUnderground() || world.isSkyVisible(blockPos));
    };
    public static final BiPredicate<World, BlockPos> GLOWWORM_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR;
    public static final BiPredicate<World, BlockPos> PLANKTON_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getFluidState().isIn(FluidTags.WATER) && world.getLightLevel(blockPos) < 2;
    public static final BiPredicate<World, BlockPos> EYES_LOCATION_PREDICATE = (world, blockPos) -> ((Config.getEyesInTheDark() == Config.EyesInTheDark.ENABLE && LocalDate.now().getMonth() == Month.OCTOBER) || Config.getEyesInTheDark() == Config.EyesInTheDark.ALWAYS) && (world.getBlockState(blockPos).getBlock() == Blocks.AIR || world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR) && world.getLightLevel(blockPos) <= 0 && world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), EYES_VANISHING_DISTANCE, false) == null && world.getRegistryKey().equals(World.OVERWORLD);
    public static final BiPredicate<World, BlockPos> WISP_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);
    // register overhead models
    public static final EntityModelLayer CROWN = new EntityModelLayer(new Identifier(MODID, "crown"), "main");
    static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>() {
    }.getType();
    // illuminations cosmetics
    private static final String COSMETICS_URL = "https://illuminations.uuid.gg/data";
    private static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();
    public static ImmutableMap<String, AuraData> AURAS_DATA;
    public static ImmutableMap<String, DefaultParticleType> PETS_DATA;
    public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;
    // particle types
    public static DefaultParticleType FIREFLY;
    public static DefaultParticleType GLOWWORM;
    public static DefaultParticleType PLANKTON;
    public static DefaultParticleType EYES;
    public static DefaultParticleType CHORUS_PETAL;
    public static DefaultParticleType WILL_O_WISP;
    public static ParticleType<WispTrailParticleEffect> WISP_TRAIL;
    // auras
    public static DefaultParticleType TWILIGHT_AURA;
    public static DefaultParticleType GHOSTLY_AURA;
    public static DefaultParticleType CHORUS_AURA;
    public static DefaultParticleType AUTUMN_LEAVES_AURA;
    public static DefaultParticleType SCULK_TENDRIL_AURA;
    public static DefaultParticleType SHADOWBRINGER_AURA;
    public static DefaultParticleType GOLDENROD_AURA;
    public static DefaultParticleType CONFETTI_AURA;
    public static DefaultParticleType PRISMATIC_CONFETTI_AURA;
    // pets
    public static DefaultParticleType PRIDE_PET;
    public static DefaultParticleType TRANS_PRIDE_PET;
    public static DefaultParticleType JACKO_PET;
    public static DefaultParticleType LESBIAN_PRIDE_PET;
    public static DefaultParticleType BI_PRIDE_PET;
    public static DefaultParticleType ACE_PRIDE_PET;
    public static DefaultParticleType NB_PRIDE_PET;
    public static DefaultParticleType INTERSEX_PRIDE_PET;
    public static DefaultParticleType WILL_O_WISP_PET;
    public static DefaultParticleType GOLDEN_WILL_PET;
    public static DefaultParticleType FOUNDING_SKULL_PET;
    public static DefaultParticleType DISSOLUTION_WISP_PET;
    // spawn biome categories and biomes
    public static ImmutableMap<Biome.Category, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOME_CATEGORIES;
    public static ImmutableMap<Identifier, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOMES;

    public static @Nullable PlayerCosmeticData getCosmeticData(PlayerEntity player) {
        return PLAYER_COSMETICS.get(player.getUuid());
    }

    public static void loadPlayerCosmetics() {
        // get illuminations player cosmetics
        CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
                logger.log(Level.INFO, "Retrieving Illuminations cosmetics from the dashboard...");
                return COSMETICS_GSON.<Map<UUID, PlayerCosmeticData>>fromJson(reader, COSMETIC_SELECT_TYPE);
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
                logger.log(Level.INFO, "Player cosmetics successfully registered");
            } else {
                PLAYER_COSMETICS = Collections.emptyMap();
                logger.log(Level.WARN, "Player cosmetics could not registered, cosmetics will be ignored");
            }
        }, MinecraftClient.getInstance());
    }

    @Override
    public void onInitializeClient() {
        // load config
        Config.load();

        // get illuminations player cosmetics
        loadPlayerCosmetics();

        // auto-updater
        if (!FabricLoader.getInstance().isDevelopmentEnvironment() && Config.isAutoUpdate()) {
            IlluminationsUpdater.init();
        }

        // load jeb shader
        if (FabricLoader.getInstance().isModLoaded("satin")) {
            Rainbowlluminations.init();
        }

        // register model layers
        EntityModelLayerRegistry.registerModelLayer(CrownModel.MODEL_LAYER, CrownModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HornsModel.MODEL_LAYER, HornsModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HaloModel.MODEL_LAYER, HaloModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TiaraModel.MODEL_LAYER, TiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(VoidheartTiaraModel.MODEL_LAYER, VoidheartTiaraModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WreathModel.MODEL_LAYER, WreathModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WillOWispModel.MODEL_LAYER, WillOWispModel::getTexturedModelData);

        // particles
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.FIREFLY, FireflyParticle.DefaultFactory::new);
        GLOWWORM = Registry.register(Registry.PARTICLE_TYPE, "illuminations:glowworm", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.GLOWWORM, GlowwormParticle.DefaultFactory::new);
        PLANKTON = Registry.register(Registry.PARTICLE_TYPE, "illuminations:plankton", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.PLANKTON, PlanktonParticle.DefaultFactory::new);
        EYES = Registry.register(Registry.PARTICLE_TYPE, "illuminations:eyes", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.EYES, EyesParticle.DefaultFactory::new);
        CHORUS_PETAL = Registry.register(Registry.PARTICLE_TYPE, "illuminations:chorus_petal", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.CHORUS_PETAL, ChorusPetalParticle.DefaultFactory::new);
        WILL_O_WISP = Registry.register(Registry.PARTICLE_TYPE, "illuminations:will_o_wisp", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.WILL_O_WISP, WillOWispParticle.DefaultFactory::new);
        WISP_TRAIL = Registry.register(Registry.PARTICLE_TYPE, "illuminations:wisp_trail", new ParticleType<WispTrailParticleEffect>(true, WispTrailParticleEffect.PARAMETERS_FACTORY) {
            @Override
            public Codec<WispTrailParticleEffect> getCodec() {
                return WispTrailParticleEffect.CODEC;
            }
        });
        ParticleFactoryRegistry.getInstance().register(Illuminations.WISP_TRAIL, WispTrailParticle.Factory::new);
        // aura particles
        TWILIGHT_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:twilight_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);
        GHOSTLY_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:ghostly_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.GHOSTLY_AURA, GhostlyParticle.DefaultFactory::new);
        CHORUS_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:chorus_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.CHORUS_AURA, ChorusAuraParticle.DefaultFactory::new);
        AUTUMN_LEAVES_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:autumn_leaves", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.AUTUMN_LEAVES_AURA, AutumnLeavesParticle.DefaultFactory::new);
        SCULK_TENDRIL_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:sculk_tendril", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.SCULK_TENDRIL_AURA, SculkTendrilParticle.DefaultFactory::new);
        SHADOWBRINGER_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:shadowbringer_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.SHADOWBRINGER_AURA, ShadowbringerParticle.DefaultFactory::new);
        GOLDENROD_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:goldenrod_aura", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.GOLDENROD_AURA, GoldenrodAuraParticle.DefaultFactory::new);
        CONFETTI_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:confetti", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.CONFETTI_AURA, ConfettiParticle.DefaultFactory::new);
        PRISMATIC_CONFETTI_AURA = Registry.register(Registry.PARTICLE_TYPE, "illuminations:prismatic_confetti", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.PRISMATIC_CONFETTI_AURA, PrismaticConfettiParticle.DefaultFactory::new);

        PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.PRIDE_PET, PetParticle.DefaultFactory::new);
        TRANS_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:trans_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.TRANS_PRIDE_PET, PetParticle.DefaultFactory::new);
        JACKO_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:jacko_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.JACKO_PET, JackoParticle.DefaultFactory::new);
        LESBIAN_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:lesbian_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.LESBIAN_PRIDE_PET, PetParticle.DefaultFactory::new);
        BI_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:bi_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.BI_PRIDE_PET, PetParticle.DefaultFactory::new);
        ACE_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:ace_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.ACE_PRIDE_PET, PetParticle.DefaultFactory::new);
        NB_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:nb_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.NB_PRIDE_PET, PetParticle.DefaultFactory::new);
        INTERSEX_PRIDE_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:intersex_pride_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.INTERSEX_PRIDE_PET, PetParticle.DefaultFactory::new);  ///haha sex
        WILL_O_WISP_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:will_o_wisp_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.WILL_O_WISP_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/will_o_wisp.png"), 1.0f, 1.0f, 1.0f, -0.1f, -0.01f, 0.0f));
        GOLDEN_WILL_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:golden_will_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.GOLDEN_WILL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/golden_will.png"), 1.0f, 0.3f, 1.0f, -0.05f, -0.01f, 0.0f));
        FOUNDING_SKULL_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:founding_skull_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.FOUNDING_SKULL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/founding_skull.png"), 1.0f, 0.0f, 0.25f, -0.03f, 0.0f, -0.01f));
        DISSOLUTION_WISP_PET = Registry.register(Registry.PARTICLE_TYPE, "illuminations:dissolution_wisp_pet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Illuminations.DISSOLUTION_WISP_PET, PetParticle.DefaultFactory::new);

        // crowns feature
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType == EntityType.PLAYER) {
                @SuppressWarnings("unchecked") var playerRenderer = (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) entityRenderer;
                registrationHelper.register(new OverheadFeatureRenderer(playerRenderer, context));
                registrationHelper.register(new DripFeatureRenderer(playerRenderer, context));
            }
        });

        // spawn biome categories for Illuminations
        ILLUMINATIONS_BIOME_CATEGORIES = ImmutableMap.copyOf(
                Config.getBiomeSettings()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                            Biome.Category biome = entry.getKey();
                            return ImmutableSet.<IlluminationData>builder()
                                    .add(new IlluminationData(FIREFLY, FIREFLY_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).fireflySpawnRate().spawnRate))
                                    .add(new IlluminationData(GLOWWORM, GLOWWORM_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).glowwormSpawnRate().spawnRate))
                                    .add(new IlluminationData(PLANKTON, PLANKTON_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).planktonSpawnRate().spawnRate))
                                    .build();
                        })));

        // specific spawn biomes for Illuminations
        ILLUMINATIONS_BIOMES = ImmutableMap.<Identifier, ImmutableSet<IlluminationData>>builder()
                .put(new Identifier("minecraft:soul_sand_valley"), ImmutableSet.of(
                        new IlluminationData(WILL_O_WISP, WISP_LOCATION_PREDICATE, () -> Config.getWillOWispsSpawnRate().spawnRate)))
                .build();


        // aura matching and spawn chances + overhead matching + crown matching
        // Currently set to default aura settings.
        // Uncomment settings related to auras in Config.java and change getDefaultAuraSettings to getAuraSettings to restore.
        AURAS_DATA = ImmutableMap.<String, AuraData>builder()
                .put("twilight", new AuraData(TWILIGHT_AURA, () -> Config.getDefaultAuraSettings("twilight")))
                .put("ghostly", new AuraData(GHOSTLY_AURA, () -> Config.getDefaultAuraSettings("ghostly")))
                .put("chorus", new AuraData(CHORUS_AURA, () -> Config.getDefaultAuraSettings("chorus")))
                .put("autumn_leaves", new AuraData(AUTUMN_LEAVES_AURA, () -> Config.getDefaultAuraSettings("autumn_leaves")))
                .put("sculk_tendrils", new AuraData(SCULK_TENDRIL_AURA, () -> Config.getDefaultAuraSettings("sculk_tendrils")))
                .put("shadowbringer_soul", new AuraData(SHADOWBRINGER_AURA, () -> Config.getDefaultAuraSettings("shadowbringer_soul")))
                .put("goldenrod", new AuraData(GOLDENROD_AURA, () -> Config.getDefaultAuraSettings("goldenrod")))
                .put("confetti", new AuraData(CONFETTI_AURA, () -> Config.getDefaultAuraSettings("confetti")))
                .put("prismatic_confetti", new AuraData(PRISMATIC_CONFETTI_AURA, () -> Config.getDefaultAuraSettings("prismatic_confetti")))
                .build();

        OVERHEADS_DATA = ImmutableMap.<String, OverheadData>builder()
                .put("solar_crown", new OverheadData(CrownModel::new, "solar_crown"))
                .put("frost_crown", new OverheadData(CrownModel::new, "frost_crown"))
                .put("pyro_crown", new OverheadData(CrownModel::new, "pyro_crown"))
                .put("chorus_crown", new OverheadData(CrownModel::new, "chorus_crown"))
                .put("bloodfiend_crown", new OverheadData(CrownModel::new, "bloodfiend_crown"))
                .put("dreadlich_crown", new OverheadData(CrownModel::new, "dreadlich_crown"))
                .put("mooncult_crown", new OverheadData(CrownModel::new, "mooncult_crown"))
                .put("deepsculk_horns", new OverheadData(HornsModel::new, "deepsculk_horns"))
                .put("springfae_horns", new OverheadData(HornsModel::new, "springfae_horns"))
                .put("voidheart_tiara", new OverheadData(VoidheartTiaraModel::new, "voidheart_tiara"))
                .put("worldweaver_halo", new OverheadData(HaloModel::new, "worldweaver_halo"))
                .put("summerbreeze_wreath", new OverheadData(WreathModel::new, "summerbreeze_wreath"))
                .put("glowsquid_cult_crown", new OverheadData(TiaraModel::new, "glowsquid_cult_crown"))
                .put("timeaspect_cult_crown", new OverheadData(TiaraModel::new, "timeaspect_cult_crown"))
                .build();
        PETS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
                .put("pride", PRIDE_PET)
                .put("trans_pride", TRANS_PRIDE_PET)
                .put("jacko", JACKO_PET)
                .put("lesbian_pride", LESBIAN_PRIDE_PET)
                .put("bi_pride", BI_PRIDE_PET)
                .put("ace_pride", ACE_PRIDE_PET)
                .put("nb_pride", NB_PRIDE_PET)
                .put("intersex_pride", INTERSEX_PRIDE_PET)
                .put("will_o_wisp", WILL_O_WISP_PET)
                .put("golden_will", GOLDEN_WILL_PET)
                .put("founding_skull", FOUNDING_SKULL_PET)
                .put("dissolution_wisp", DISSOLUTION_WISP_PET)
                .build();
    }

    private static class PlayerCosmeticDataParser implements JsonDeserializer<PlayerCosmeticData> {
        @Override
        public PlayerCosmeticData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new PlayerCosmeticData(jsonObject.get("aura")
                    , jsonObject.get("color")
                    , jsonObject.get("overhead")
                    , jsonObject.get("drip")
                    , jsonObject.get("pet"));
        }
    }
}
