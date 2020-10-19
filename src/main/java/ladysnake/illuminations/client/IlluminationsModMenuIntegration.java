package ladysnake.illuminations.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import ladysnake.illuminations.common.Illuminations;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class IlluminationsModMenuIntegration implements ModMenuApi {
    private static final Path PROPERTIES_PATH = FabricLoader.getInstance().getConfigDir().resolve("illuminations.properties");

    public enum EyesInTheDark {
        ENABLE, DISABLE, ALWAYS
    }

    @Override
    public String getModId() {
        return Illuminations.MODID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> {
            // load config
            final Properties config = new Properties();
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

            // create the config
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.illuminations.config"));

            // how to save the config
            builder.setSavingRunnable(() -> {
                try {
                    config.store(Files.newBufferedWriter(PROPERTIES_PATH), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // config categories and entries
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.illuminations.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder
                .startEnumSelector(new TranslatableText("option.illuminations.eyesInTheDark"), EyesInTheDark.class, EyesInTheDark.valueOf(config.getProperty("eyes-in-the-dark")))
                .setTooltip(
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.default"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.enable"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.disable"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.always"))
                .setSaveConsumer(val -> config.setProperty("eyes-in-the-dark", val.toString()))
                .setDefaultValue(EyesInTheDark.ENABLE)
                .build());

            // build and return the config screen
            return builder.build();
        };
    }
}

