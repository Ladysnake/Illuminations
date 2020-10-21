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
            config.setProperty("eyes-in-the-dark", EyesInTheDark.ENABLE.toString());
        }

        eyesInTheDark = EyesInTheDark.valueOf(config.getProperty("eyes-in-the-dark"));
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

}
