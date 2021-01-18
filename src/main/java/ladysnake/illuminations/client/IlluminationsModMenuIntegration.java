package ladysnake.illuminations.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

public class IlluminationsModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() {
        return "illuminations";
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

            general.addEntry(entryBuilder
                    .startIntSlider(new TranslatableText("option.illuminations.density"), Config.getDensity(), 0, 1000)
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.density"),
                            new TranslatableText("option.tooltip.illuminations.density.lowest"),
                            new TranslatableText("option.tooltip.illuminations.density.highest"))
                    .setSaveConsumer(Config::setDensity)
                    .setDefaultValue(100)
                    .build());

            general.addEntry(entryBuilder
                    .startIntSlider(new TranslatableText("option.illuminations.fireflyWhiteAlpha"), Config.getFireflyWhiteAlpha(), 0, 100)
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.fireflyWhiteAlpha"))
                    .setSaveConsumer(Config::setFireflyWhiteAlpha)
                    .setDefaultValue(100)
                    .build());

            // build and return the config screen
            return builder.build();
        };
    }
}

