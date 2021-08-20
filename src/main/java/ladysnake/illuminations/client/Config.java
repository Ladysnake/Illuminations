package ladysnake.illuminations.client;

import static net.minecraft.world.biome.Biome.Category.BEACH;
import static net.minecraft.world.biome.Biome.Category.DESERT;
import static net.minecraft.world.biome.Biome.Category.EXTREME_HILLS;
import static net.minecraft.world.biome.Biome.Category.FOREST;
import static net.minecraft.world.biome.Biome.Category.ICY;
import static net.minecraft.world.biome.Biome.Category.JUNGLE;
import static net.minecraft.world.biome.Biome.Category.MESA;
import static net.minecraft.world.biome.Biome.Category.MUSHROOM;
import static net.minecraft.world.biome.Biome.Category.NETHER;
import static net.minecraft.world.biome.Biome.Category.OCEAN;
import static net.minecraft.world.biome.Biome.Category.PLAINS;
import static net.minecraft.world.biome.Biome.Category.RIVER;
import static net.minecraft.world.biome.Biome.Category.SAVANNA;
import static net.minecraft.world.biome.Biome.Category.SWAMP;
import static net.minecraft.world.biome.Biome.Category.TAIGA;
import static net.minecraft.world.biome.Biome.Category.THEEND;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.biome.Biome.Category;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Supplier;

public class Config {
    public static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir().resolve("illuminations.properties");
    private static final Properties config = new Properties() {
        @Override
        public @NotNull Set<Map.Entry<Object, Object>> entrySet() {
            Iterator<Map.Entry<Object, Object>> iterator = super.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().toString())).iterator();

            Set<Map.Entry<Object, Object>> temp = new LinkedHashSet<>(super.entrySet().size());
            while (iterator.hasNext())
                temp.add(iterator.next());

            return temp;
        }
    };
    private static EyesInTheDark eyesInTheDark;
    private static float eyesInTheDarkSpawnRate;
    private static float willOWispsSpawnRate;
    private static int chorusPetalsSpawnMultiplier;
    private static int density;
    private static int fireflyWhiteAlpha;
    private static boolean viewAurasFP;
    private static boolean autoUpdate;
    private static boolean displayGreetingScreen;
    private static HashMap<Category, BiomeSettings> biomeSettings;
    private static HashMap<String, AuraSettings> auraSettings;

    private static final ImmutableMap<Category, BiomeSettings> defaultBiomeSettings = ImmutableMap.<Category, BiomeSettings>builder()
            .put(JUNGLE, new BiomeSettings(.00002f, .00004f, 0f))
            .put(PLAINS, new BiomeSettings(.00002f, .00004f, 0f))
            .put(SAVANNA, new BiomeSettings(.00002f, .00004f, 0f))
            .put(TAIGA, new BiomeSettings(.00002f, .00004f, 0f))
            .put(FOREST, new BiomeSettings(.00010f, .00020f, 0f))
            .put(RIVER, new BiomeSettings(.00010f, .00020f, 0f))
            .put(SWAMP, new BiomeSettings(.00025f, .00050f, 0f))
            .put(OCEAN, new BiomeSettings(0f, 0f, .00250f))
            .put(BEACH, new BiomeSettings(0f, 0f, 0f))
            .put(DESERT, new BiomeSettings(0f, 0f, 0f))
            .put(EXTREME_HILLS, new BiomeSettings(0f, 0f, 0f))
            .put(ICY, new BiomeSettings(0f, 0f, 0f))
            .put(MESA, new BiomeSettings(0f, 0f, 0f))
            .put(MUSHROOM, new BiomeSettings(0f, 0f, 0f))
            .put(NETHER, new BiomeSettings(0f, 0f, 0f))
            .put(THEEND, new BiomeSettings(0f, 0f, 0f))
            .build();

    private static final ImmutableMap<String, AuraSettings> defaultAuraSettings = ImmutableMap.<String, AuraSettings>builder()
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

    public static void load() {
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
            setEyesInTheDarkSpawnRate(0.0001f);
            setWillOWispsSpawnRate(0.0001f);
            setChorusPetalsSpawnMultiplier(1);
            setDensity(100);
            setFireflyWhiteAlpha(100);
            setAutoUpdate(false);
            setDisplayGreetingScreen(true);
            setViewAurasFP(false);

            biomeSettings = new HashMap<>();
            defaultBiomeSettings.forEach(Config::setBiomeSettings);

            auraSettings = new HashMap<>();
            defaultAuraSettings.forEach(Config::setAuraSettings);

            Config.save();
            return;
        }

        setEyesInTheDark(tryOrDefault(() -> EyesInTheDark.valueOf(config.getProperty("eyes-in-the-dark")), EyesInTheDark.ENABLE));
        setEyesInTheDarkSpawnRate(tryOrDefault(() -> Float.parseFloat(config.getProperty("eyes-in-the-dark-spawn-rate")), 0.0001f));
        setWillOWispsSpawnRate(tryOrDefault(() -> Float.parseFloat(config.getProperty("will-o-wisps-spawn-rate")), 0.0001f));
        setChorusPetalsSpawnMultiplier(tryOrDefault(() -> Integer.parseInt(config.getProperty("chorus-petal-spawn-multiplier")), 1));
        setDensity(tryOrDefault(() -> Integer.parseInt(config.getProperty("density")), 100));
        setFireflyWhiteAlpha(tryOrDefault(() -> Integer.parseInt(config.getProperty("firefly-white-alpha")), 100));
        setAutoUpdate(tryOrDefault(() -> Boolean.parseBoolean(config.getProperty("auto-update")), false));
        setDisplayGreetingScreen(tryOrDefault(() -> Boolean.parseBoolean(config.getProperty("display-greeting-screen")), true));
        setViewAurasFP(tryOrDefault(() -> Boolean.parseBoolean(config.getProperty("view-auras-fp")), false));

        biomeSettings = new HashMap<>();
        defaultBiomeSettings.forEach((biome, v) ->
            setBiomeSettings(biome,
                    tryOrDefault(() -> {
                        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
                        float f = Float.parseFloat(config.getProperty(name + "-firefly-spawn-rate"));
                        float g = Float.parseFloat(config.getProperty(name + "-glowworm-spawn-rate"));
                        float p = Float.parseFloat(config.getProperty(name + "-plankton-spawn-rate"));
                        return new BiomeSettings(f, g, p);
                    }, v))
        );

        auraSettings = new HashMap<>();
        defaultAuraSettings.forEach((aura, v) ->
                setAuraSettings(aura,
                        tryOrDefault(() -> {
                            String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
                            float s = Float.parseFloat(config.getProperty(name + "-aura-spawn-rate"));
                            int d = Integer.parseInt(config.getProperty(name + "-aura-delay"));
                            return new AuraSettings(s, d);
                        }, v)));

        Config.save();
    }

    private static <T> T tryOrDefault(Supplier<? extends T> supplier, T defaultValue)
    {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
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

    public static float getEyesInTheDarkSpawnRate() {
        return eyesInTheDarkSpawnRate;
    }

    public static void setEyesInTheDarkSpawnRate(float value)
    {
        eyesInTheDarkSpawnRate = value;
        config.setProperty("eyes-in-the-dark-spawn-rate", Float.toString(value));
    }

    public static float getWillOWispsSpawnRate() {
        return willOWispsSpawnRate;
    }

    public static void setWillOWispsSpawnRate(float value)
    {
        willOWispsSpawnRate = value;
        config.setProperty("will-o-wisps-spawn-rate", Float.toString(value));
    }

    public static int getChorusPetalsSpawnMultiplier() {
        return chorusPetalsSpawnMultiplier;
    }

    public static void setChorusPetalsSpawnMultiplier(int value) {
        chorusPetalsSpawnMultiplier = value;
        config.setProperty("chorus-petal-spawn-multiplier", Integer.toString(value));
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
        config.setProperty(name + "-firefly-spawn-rate", Float.toString(settings.fireflySpawnRate));
        config.setProperty(name + "-glowworm-spawn-rate", Float.toString(settings.glowwormSpawnRate));
        config.setProperty(name + "-plankton-spawn-rate", Float.toString(settings.planktonSpawnRate));
    }

    public static void setFireflySettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withFireflySpawnRate(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-firefly-spawn-rate", Float.toString(value));
    }

    public static void setGlowwormSettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withGlowwormSpawnRate(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-glowworm-spawn-rate", Float.toString(value));
    }

    public static void setPlanktonSettings(Category biome, float value)
    {
        BiomeSettings settings = biomeSettings.get(biome);
        biomeSettings.put(biome, settings.withPlanktonSpawnRate(value));
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, biome.name());
        config.setProperty(name + "-plankton-spawn-rate", Float.toString(value));
    }

    public static ImmutableMap<String, AuraSettings> getDefaultAuraSettings()
    {
        return defaultAuraSettings;
    }

    public static AuraSettings getDefaultAuraSettings(String aura)
    {
        return defaultAuraSettings.get(aura);
    }

    public static Map<String, AuraSettings> getAuraSettings()
    {
        return auraSettings;
    }

    public static AuraSettings getAuraSettings(String aura)
    {
        return auraSettings.get(aura);
    }

    public static void setAuraSettings(String aura, AuraSettings settings)
    {
        auraSettings.put(aura, settings);
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-spawn-rate", Float.toString(settings.spawnRate()));
        config.setProperty(name + "-aura-delay", Integer.toString(settings.delay()));
    }

    public static void setAuraSpawnRate(String aura, float value)
    {
        AuraSettings settings = auraSettings.get(aura);
        auraSettings.put(aura, settings.withSpawnRate(value));
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-spawn-rate", Float.toString(value));
    }

    public static void setAuraDelay(String aura, int value)
    {
        AuraSettings settings = auraSettings.get(aura);
        auraSettings.put(aura, settings.withDelay(value));
        String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, aura);
        config.setProperty(name + "-aura-delay", Integer.toString(value));
    }

    public enum EyesInTheDark {
        ENABLE, DISABLE, ALWAYS
    }

    public record BiomeSettings(float fireflySpawnRate, float glowwormSpawnRate, float planktonSpawnRate) {

        public BiomeSettings withFireflySpawnRate(float value)
        {
            return new BiomeSettings(value, glowwormSpawnRate, planktonSpawnRate);
        }

        public BiomeSettings withGlowwormSpawnRate(float value)
        {
            return new BiomeSettings(fireflySpawnRate, value, planktonSpawnRate);
        }

        public BiomeSettings withPlanktonSpawnRate(float value)
        {
            return new BiomeSettings(fireflySpawnRate, glowwormSpawnRate, value);
        }
    }

    public record AuraSettings(float spawnRate, int delay)
    {
        public AuraSettings withSpawnRate(float value)
        {
            return new AuraSettings(value, delay);
        }

        public AuraSettings withDelay(int value)
        {
            return new AuraSettings(spawnRate, delay);
        }
    }
}
