package ladysnake.illuminations.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import ladysnake.illuminations.common.Illuminations;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

public class IlluminationsModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() {
        return Illuminations.MODID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return parent -> {
            // load config
            Config.load();

            // create the config
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.illuminations.config"));

            // config categories and entries
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.illuminations.general"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder
                .startEnumSelector(new TranslatableText("option.illuminations.eyesInTheDark"), Config.EyesInTheDark.class, Config.getEyesInTheDark())
                .setTooltip(
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.default"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.enable"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.disable"),
                    new TranslatableText("option.tooltip.illuminations.eyesInTheDark.always"))
                .setSaveConsumer(Config::setEyesInTheDark)
                .setDefaultValue(Config.EyesInTheDark.ENABLE)
                .build());

            // build and return the config screen
            return builder.build();
        };
    }
}

