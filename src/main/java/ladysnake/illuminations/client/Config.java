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

    public enum EyesInTheDark {
        ENABLE, DISABLE, ALWAYS
    }

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
        }

        try {
            eyesInTheDark = EyesInTheDark.valueOf(config.getProperty("eyes-in-the-dark"));
            density = Integer.parseInt(config.getProperty("density"));
            fireflyWhiteAlpha = Integer.parseInt(config.getProperty("firefly-white-alpha"));
        } catch (Exception e) {
            setEyesInTheDark(EyesInTheDark.ENABLE);
            setDensity(100);
            setFireflyWhiteAlpha(100);
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
}
