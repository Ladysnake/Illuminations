package ladysnake.illuminations.client.config;

import static ladysnake.illuminations.client.enums.BiomeCategory.*;

import com.google.common.collect.ImmutableMap;
import ladysnake.illuminations.client.data.AuraSettings;
import ladysnake.illuminations.client.data.BiomeSettings;
import ladysnake.illuminations.client.enums.BiomeCategory;
import ladysnake.illuminations.client.enums.EyesInTheDark;
import ladysnake.illuminations.client.enums.EyesInTheDarkSpawnRate;
import ladysnake.illuminations.client.enums.FireflySpawnRate;
import ladysnake.illuminations.client.enums.GlowwormSpawnRate;
import ladysnake.illuminations.client.enums.PlanktonSpawnRate;
import ladysnake.illuminations.client.enums.WillOWispsSpawnRate;

public class DefaultConfig {

    public static final EyesInTheDark EYES_IN_THE_DARK = EyesInTheDark.ENABLE;
    public static final EyesInTheDarkSpawnRate EYES_IN_THE_DARK_SPAWN_RATE = EyesInTheDarkSpawnRate.MEDIUM;
    public static final WillOWispsSpawnRate WILL_O_WISPS_SPAWN_RATE = WillOWispsSpawnRate.MEDIUM;
    public static final int CHORUS_PETALS_SPAWN_MULTIPLIER = 1;
    public static final int DENSITY = 100;
    public static final boolean FIREFLY_SPAWN_ALWAYS = false;
    public static final boolean FIREFLY_SPAWN_UNDERGROUND = false;
    public static final int FIREFLY_WHITE_ALPHA = 100;
    public static final boolean AUTO_UPDATE = false;
    public static final boolean VIEW_AURAS_FP = false;
    public static final boolean DISPLAY_GREETING_SCREEN = true;

    public static final ImmutableMap<BiomeCategory, BiomeSettings> BIOME_SETTINGS = ImmutableMap.<BiomeCategory, BiomeSettings>builder()
            .put(FOREST, new BiomeSettings(FireflySpawnRate.MEDIUM, 0xBFFF00, GlowwormSpawnRate.MEDIUM, PlanktonSpawnRate.DISABLE))
            .put(TAIGA, new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.LOW, PlanktonSpawnRate.DISABLE))
            .put(SNOWY, new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(PLAINS, new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.LOW, PlanktonSpawnRate.DISABLE))
            .put(DESERT, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(SAVANNA, new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.LOW, PlanktonSpawnRate.DISABLE))
            .put(JUNGLE, new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.LOW, PlanktonSpawnRate.DISABLE))
            .put(BEACH, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(SWAMP, new BiomeSettings(FireflySpawnRate.HIGH, 0xBFFF00, GlowwormSpawnRate.HIGH, PlanktonSpawnRate.DISABLE))
            .put(RIVER, new BiomeSettings(FireflySpawnRate.MEDIUM, 0xBFFF00, GlowwormSpawnRate.MEDIUM, PlanktonSpawnRate.DISABLE))
            .put(OCEAN, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.HIGH))
            .put(WARM_OCEAN, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.HIGH))
            .put(THE_NETHER, new BiomeSettings(FireflySpawnRate.DISABLE, 0xFF8000, null, null))
            .put(THE_END, new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, null, PlanktonSpawnRate.DISABLE))
            .put(BADLANDS, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(MOUNTAINS, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(DRIPSTONE_CAVES, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(LUSH_CAVES, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(MUSHROOM, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .put(OTHER, new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
            .build();

    public static final ImmutableMap<String, AuraSettings> AURA_SETTINGS = ImmutableMap.<String, AuraSettings>builder()
            .put("twilight", new AuraSettings(0.1f, 1))
            .put("ghostly", new AuraSettings(0.1f, 1))
            .put("chorus", new AuraSettings(0.1f, 1))
            .put("autumn_leaves", new AuraSettings(0.3f, 1))
            .put("sculk_tendrils", new AuraSettings(0.1f, 1))
            .put("shadowbringer_soul", new AuraSettings(0.1f, 1))
            .put("goldenrod", new AuraSettings(0.4f, 1))
            .put("confetti", new AuraSettings(0.1f, 1))
            .put("prismatic_confetti", new AuraSettings(0.1f, 1))
            .build();

    public static BiomeSettings getBiomeSettings(BiomeCategory biome) {
        return BIOME_SETTINGS.get(biome);
    }

    public static AuraSettings getAuraSettings(String aura) {
        return AURA_SETTINGS.get(aura);
    }
}
