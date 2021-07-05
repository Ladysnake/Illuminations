package ladysnake.illuminations.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            setDensity(100);
            setFireflyWhiteAlpha(100);
            setAutoUpdate(false);
            setDisplayGreetingScreen(true);
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
            setViewAurasFP(false);
        }
        try {
            viewAurasFP = Boolean.parseBoolean(config.getProperty("view-auras-fp"));
        } catch (Exception e) {
            setViewAurasFP(false);
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
        Config.save();
    }

    public static int getDensity() {
        return density;
    }

    public static void setDensity(int value) {
        density = value;
        config.setProperty("density", Integer.toString(value));
        Config.save();
    }

    public static int getFireflyWhiteAlpha() {
        return fireflyWhiteAlpha;
    }

    public static void setFireflyWhiteAlpha(int value) {
        fireflyWhiteAlpha = value;
        config.setProperty("firefly-white-alpha", Integer.toString(value));
        Config.save();
    }

    public static boolean getViewAurasFP() {
        return viewAurasFP;
    }

    public static void setViewAurasFP(boolean value) {
        viewAurasFP = value;
        config.setProperty("view-auras-fp", Boolean.toString(value));
        Config.save();
    }

    public static boolean isAutoUpdate() {
        return autoUpdate;
    }

    public static void setAutoUpdate(boolean value) {
        autoUpdate = value;
        config.setProperty("auto-update", Boolean.toString(value));
        Config.save();
    }

    public static boolean isDisplayGreetingScreen() {
        return displayGreetingScreen;
    }

    public static void setDisplayGreetingScreen(boolean value) {
        displayGreetingScreen = value;
        config.setProperty("display-greeting-screen", Boolean.toString(value));
        Config.save();
    }

    public enum EyesInTheDark {
        ENABLE, DISABLE, ALWAYS
    }

}
