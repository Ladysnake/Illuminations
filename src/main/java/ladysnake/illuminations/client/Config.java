package ladysnake.illuminations.client;

import static net.minecraft.world.biome.Biome.Category.*;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.biome.Biome.Category;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    public static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir().resolve("illuminations.properties");
    private static final Properties config = new Properties();
    private static EyesInTheDark eyesInTheDark;
    private static int density;
    private static int fireflyWhiteAlpha;
    private static boolean viewAurasFP;
    private static boolean autoUpdate;
    private static boolean displayGreetingScreen;
    private static ImmutableMap<Category, BiomeSettings> defaultBiomeSettings;
    private static HashMap<Category, BiomeSettings> biomeSettings;

    public static void load() {
        // Initialize default biome settings
        defaultBiomeSettings = ImmutableMap.<Category, BiomeSettings>builder()
                .put(JUNGLE, new BiomeSettings(.00002f, .00004f, 0f))
                .put(PLAINS, new BiomeSettings(.00002f, .00004f, 0f))
                .put(SAVANNA, new BiomeSettings(.00002f, .00004f, 0f))
                .put(TAIGA, new BiomeSettings(.00002f, .00004f, 0f))
                .put(FOREST, new BiomeSettings(.00010f, .00020f, 0f))
                .put(RIVER, new BiomeSettings(.00010f, .00020f, 0f))
                .put(SWAMP, new BiomeSettings(.00025f, .00050f, 0f))
                .put(OCEAN, new BiomeSettings(0f, 0f, .0250f))
                .put(BEACH, new BiomeSettings(0f, 0f, 0f))
                .put(DESERT, new BiomeSettings(0f, 0f, 0f))
                .put(EXTREME_HILLS, new BiomeSettings(0f, 0f, 0f))
                .put(ICY, new BiomeSettings(0f, 0f, 0f))
                .put(MESA, new BiomeSettings(0f, 0f, 0f))
                .put(MUSHROOM, new BiomeSettings(0f, 0f, 0f))
                .put(NETHER, new BiomeSettings(0f, 0f, 0f))
                .put(THEEND, new BiomeSettings(0f, 0f, 0f))
                .build();

        // if illuminations.properties exist, load it
        if (Files.isRegularFile(PROPERTIES_PATH)) {
            // load illuminations.properties
            try {
                config.load(Files.newBufferedReader(PROPERTIES_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // if no illuminations.properties, load default values
            // define default properties
            setEyesInTheDark(EyesInTheDark.ENABLE);
            setDensity(100);
            setFireflyWhiteAlpha(100);
            setAutoUpdate(false);
            setDisplayGreetingScreen(true);
            setViewAurasFP(false);

            biomeSettings = new HashMap<>();
            defaultBiomeSettings.forEach(Config::setBiomeSettings);

            Config.save();
            return;
        }

        try {
            eyesInTheDark = EyesInTheDark.valueOf(config.getProperty("eyes-in-the-dark"));
            density = Integer.parseInt(config.getProperty("density"));
            fireflyWhiteAlpha = Integer.parseInt(config.getProperty("firefly-white-alpha"));
            autoUpdate = Boolean.parseBoolean(config.getProperty("auto-update"));
            displayGreetingScreen = Boolean.parseBoolean(config.getProperty("display-greeting-screen"));
        } catch (Exception e) {
            setEyesInTheDark(EyesInTheDark.ENABLE);
            setDensity(100);
            setFireflyWhiteAlpha(100);
            setAutoUpdate(false);
            setDisplayGreetingScreen(true);
        }
        try {
            viewAurasFP = Boolean.parseBoolean(config.getProperty("view-auras-fp"));
        } catch (Exception e) {
            setViewAurasFP(false);
        }

        biomeSettings = new HashMap<>();
        defaultBiomeSettings.forEach((biome, v) -> {
            try {
                String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
                float f = Float.parseFloat(config.getProperty(name + "-firefly-spawn-chance"));
                float g = Float.parseFloat(config.getProperty(name + "-glowworm-spawn-chance"));
                float p = Float.parseFloat(config.getProperty(name + "-plankton-spawn-chance"));
                biomeSettings.put(biome, new BiomeSettings(f, g, p));
            } catch (Exception e) {
                setBiomeSettings(biome, v);
            }
        });

        Config.save();
    }

    public static void save() {
        try {
            config.store(Files.newBufferedWriter(Config.PROPERTIES_PATH), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EyesInTheDark getEyesInTheDark() {
        return eyesInTheDark;
    }

    public static void setEyesInTheDark(EyesInTheDark value) {
        eyesInTheDark = value;
        config.setProperty("eyes-in-the-dark", value.toString());
    }

    public static int getDensity() {
        return density;
    }

    public static void setDensity(int value) {
        density = value;
        config.setProperty("density", Integer.toString(value));
    }

    public static int getFireflyWhiteAlpha() {
        return fireflyWhiteAlpha;
    }

    public static void setFireflyWhiteAlpha(int value) {
        fireflyWhiteAlpha = value;
        config.setProperty("firefly-white-alpha", Integer.toString(value));
    }

    public static boolean getViewAurasFP() {
        return viewAurasFP;
    }

    public static void setViewAurasFP(boolean value) {
        viewAurasFP = value;
        config.setProperty("view-auras-fp", Boolean.toString(value));
    }

    public static boolean isAutoUpdate() {
        return autoUpdate;
    }

    public static void setAutoUpdate(boolean value) {
        autoUpdate = value;
        config.setProperty("auto-update", Boolean.toString(value));
    }

    public static boolean isDisplayGreetingScreen() {
        return displayGreetingScreen;
    }

    public static void setDisplayGreetingScreen(boolean value) {
        displayGreetingScreen = value;
        config.setProperty("display-greeting-screen", Boolean.toString(value));
    }

    public static ImmutableMap<Category, BiomeSettings> getDefaultBiomeSettings()
    {
        return defaultBiomeSettings;
    }

    public static BiomeSettings getDefaultBiomeSettings(Category biome)
    {
        return defaultBiomeSettings.get(biome);
    }

    public static Map<Category, BiomeSettings> getBiomeSettings()
    {
        return biomeSettings;
    }

    public static BiomeSettings getBiomeSettings(Category biome)
    {
        return biomeSettings.get(biome);
    }

    public static void setBiomeSettings(Category biome, BiomeSettings settings)
    {
        biomeSettings.put(biome, settings);
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-firefly-spawn-chance", Float.toString(settings.fireflySpawnChance));
        config.setProperty(name + "-glowworm-spawn-chance", Float.toString(settings.glowwormSpawnChance));
        config.setProperty(name + "-plankton-spawn-chance", Float.toString(settings.planktonSpawnChance));
    }

    public static void setFireflySettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withFireflySpawnChance(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-firefly-spawn-chance", Float.toString(value));
    }

    public static void setGlowwormSettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withGlowwormSpawnChance(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-glowworm-spawn-chance", Float.toString(value));
    }

    public static void setPlanktonSettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withPlanktonSpawnChance(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-plankton-spawn-chance", Float.toString(value));
    }

    public enum EyesInTheDark {
        ENABLE, DISABLE, ALWAYS
    }

    public record BiomeSettings(float fireflySpawnChance, float glowwormSpawnChance, float planktonSpawnChance) {

        public BiomeSettings withFireflySpawnChance(float value)
        {
            return new BiomeSettings(value, glowwormSpawnChance, planktonSpawnChance);
        }

        public BiomeSettings withGlowwormSpawnChance(float value)
        {
            return new BiomeSettings(fireflySpawnChance, value, planktonSpawnChance);
        }

        public BiomeSettings withPlanktonSpawnChance(float value)
        {
            return new BiomeSettings(fireflySpawnChance, glowwormSpawnChance, value);
        }
    }
}
