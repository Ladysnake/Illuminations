package ladysnake.illuminations.client;

import static ladysnake.illuminations.client.enums.BiomeCategory.*;

import com.google.common.base.CaseFormat;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.config.DefaultConfig;
import ladysnake.illuminations.client.data.BiomeSettings;
import ladysnake.illuminations.client.enums.BiomeCategory;
import ladysnake.illuminations.client.enums.EyesInTheDarkSpawnRate;
import ladysnake.illuminations.client.enums.FireflySpawnRate;
import ladysnake.illuminations.client.enums.GlowwormSpawnRate;
import ladysnake.illuminations.client.enums.PlanktonSpawnRate;
import ladysnake.illuminations.client.enums.WillOWispsSpawnRate;
import ladysnake.illuminations.client.enums.EyesInTheDark;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class IlluminationsModMenuIntegration implements ModMenuApi {

    private ConfigBuilder builder;
    private ConfigEntryBuilder entryBuilder;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // load config
            Config.load();

            // create the config
            builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.illuminations.config"));
            builder.setSavingRunnable(Config::save);

            entryBuilder = builder.entryBuilder();

            // config categories and entries
            GenerateGeneralSettings();
            GenerateBiomeSettings();

            // build and return the config screen
            return builder.build();
        };
    }

    private void GenerateGeneralSettings() {
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.illuminations.general"));

        general.addEntry(entryBuilder
                .startEnumSelector(new TranslatableText("option.illuminations.eyesInTheDark"), EyesInTheDark.class, Config.getEyesInTheDark())
                .setTooltip(
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDark"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDark.default"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDark.enable"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDark.disable"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDark.always"))
                .setSaveConsumer(Config::setEyesInTheDark)
                .setDefaultValue(DefaultConfig.EYES_IN_THE_DARK)
                .build());

        general.addEntry(entryBuilder
                .startEnumSelector(new TranslatableText("option.illuminations.eyesInTheDarkSpawnRate"), EyesInTheDarkSpawnRate.class, Config.getEyesInTheDarkSpawnRate())
                .setTooltip(
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDarkSpawnRate"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDarkSpawnRate.default"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDarkSpawnRate.low"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDarkSpawnRate.medium"),
                        new TranslatableText("option.tooltip.illuminations.eyesInTheDarkSpawnRate.high"))
                .setSaveConsumer(Config::setEyesInTheDarkSpawnRate)
                .setDefaultValue(DefaultConfig.EYES_IN_THE_DARK_SPAWN_RATE)
                .build());

        general.addEntry(entryBuilder
                .startEnumSelector(new TranslatableText("option.illuminations.willOWispsSpawnRate"), WillOWispsSpawnRate.class, Config.getWillOWispsSpawnRate())
                .setTooltip(
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate"),
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate.default"),
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate.disable"),
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate.low"),
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate.medium"),
                        new TranslatableText("option.tooltip.illuminations.willOWispsSpawnRate.high"))
                .setSaveConsumer(Config::setWillOWispsSpawnRate)
                .setDefaultValue(DefaultConfig.WILL_O_WISPS_SPAWN_RATE)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(new TranslatableText("option.illuminations.chorusPetalsSpawnMultiplier"), Config.getChorusPetalsSpawnMultiplier(), 0, 10)
                .setTooltip(
                        new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier"),
                        new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.lowest"),
                        new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.highest"))
                .setSaveConsumer(Config::setChorusPetalsSpawnMultiplier)
                .setDefaultValue(DefaultConfig.CHORUS_PETALS_SPAWN_MULTIPLIER)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(new TranslatableText("option.illuminations.density"), Config.getDensity(), 0, 1000)
                .setTooltip(
                        new TranslatableText("option.tooltip.illuminations.density"),
                        new TranslatableText("option.tooltip.illuminations.density.lowest"),
                        new TranslatableText("option.tooltip.illuminations.density.highest"))
                .setSaveConsumer(Config::setDensity)
                .setDefaultValue(DefaultConfig.DENSITY)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(new TranslatableText("option.illuminations.fireflySpawnAlways"), Config.doesFireflySpawnAlways())
                .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflySpawnAlways"))
                .setSaveConsumer(Config::setFireflySpawnAlways)
                .setDefaultValue(DefaultConfig.FIREFLY_SPAWN_ALWAYS)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(new TranslatableText("option.illuminations.fireflySpawnUnderground"), Config.doesFireflySpawnUnderground())
                .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflySpawnUnderground"))
                .setSaveConsumer(Config::setFireflySpawnUnderground)
                .setDefaultValue(DefaultConfig.FIREFLY_SPAWN_UNDERGROUND)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(new TranslatableText("option.illuminations.fireflyWhiteAlpha"), Config.getFireflyWhiteAlpha(), 0, 100)
                .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflyWhiteAlpha"))
                .setSaveConsumer(Config::setFireflyWhiteAlpha)
                .setDefaultValue(DefaultConfig.FIREFLY_WHITE_ALPHA)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(new TranslatableText("option.illuminations.fireflyRainbow"), Config.getFireflyRainbow())
                .setSaveConsumer(Config::setFireflyRainbow)
                .setDefaultValue(DefaultConfig.FIREFLY_RAINBOW)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(new TranslatableText("option.illuminations.autoUpdate"), Config.isAutoUpdate())
                .setTooltip(new TranslatableText("option.tooltip.illuminations.autoUpdate"))
                .setSaveConsumer(Config::setAutoUpdate)
                .setDefaultValue(DefaultConfig.AUTO_UPDATE)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(new TranslatableText("option.illuminations.viewAurasFP"), Config.getViewAurasFP())
                .setTooltip(new TranslatableText("option.tooltip.illuminations.viewAurasFP"))
                .setSaveConsumer(Config::setViewAurasFP)
                .setDefaultValue(DefaultConfig.VIEW_AURAS_FP)
                .build());
    }

    private void GenerateBiomeSettings() {
        HashMap<Identifier, ConfigCategory> biomeCategories = new HashMap<>();
        for (Identifier dimension : BiomeCategory.getDimensions()) {
            String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dimension.getPath());
            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("category.illuminations." + name));
            category.setDescription(new StringVisitable[] { new TranslatableText("category.illuminations." + name + ".description") });
            biomeCategories.put(dimension, category);
        }

        for (BiomeCategory biome : BiomeCategory.values()) {
            ConfigCategory category = biomeCategories.get(biome.getDimension());
            String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, biome.name());
            BiomeSettings defaultSettings = DefaultConfig.getBiomeSettings(biome);
            BiomeSettings settings = Config.getBiomeSettings(biome);

            // Biome tooltip
            Text[] tooltip = new Text[biome.getBiomes().length + 1];
            tooltip[0] = new TranslatableText("option.tooltip.illuminations.biome");
            for (int i = 0; i < biome.getBiomes().length; i++) {
                tooltip[i + 1] = new TranslatableText("biome.minecraft." + biome.getBiomes()[i].getPath());
            }

            // Biome sub category
            SubCategoryBuilder biomeCategoryBuilder = entryBuilder
                    .startSubCategory(new TranslatableText("option.illuminations.biome." + name))
                    .setTooltip(tooltip);

            // Firefly spawn rate
            biomeCategoryBuilder.add(entryBuilder
                    .startEnumSelector(new TranslatableText("option.illuminations.fireflySpawnRate"), FireflySpawnRate.class, settings.fireflySpawnRate())
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.fireflySpawnRate"),
                            new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.disable"),
                            new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.low"),
                            new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.medium"),
                            new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.high"))
                    .setSaveConsumer(x -> Config.setFireflySettings(biome, x))
                    .setDefaultValue(defaultSettings.fireflySpawnRate())
                    .build());

            // Firefly color
            biomeCategoryBuilder.add(entryBuilder
                    .startColorField(new TranslatableText("option.illuminations.fireflyColor"), settings.fireflyColor())
                    .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflyColor"))
                    .setSaveConsumer(x -> Config.setFireflyColorSettings(biome, x))
                    .setDefaultValue(defaultSettings.fireflyColor())
                    .build());

            // Glowworm spawn rate
            if (settings.glowwormSpawnRate() != null)
                biomeCategoryBuilder.add(entryBuilder
                        .startEnumSelector(new TranslatableText("option.illuminations.glowwormSpawnRate"), GlowwormSpawnRate.class, settings.glowwormSpawnRate())
                        .setTooltip(
                                new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate"),
                                new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.disable"),
                                new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.low"),
                                new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.medium"),
                                new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.high"))
                        .setSaveConsumer(x -> Config.setGlowwormSettings(biome, x))
                        .setDefaultValue(defaultSettings.glowwormSpawnRate())
                        .build());

            // Plankton spawn rate
            if (settings.planktonSpawnRate() != null)
                biomeCategoryBuilder.add(entryBuilder
                        .startEnumSelector(new TranslatableText("option.illuminations.planktonSpawnRate"), PlanktonSpawnRate.class, settings.planktonSpawnRate())
                        .setTooltip(
                                new TranslatableText("option.tooltip.illuminations.planktonSpawnRate"),
                                new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.disable"),
                                new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.low"),
                                new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.medium"),
                                new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.high"))
                        .setSaveConsumer(x -> Config.setPlanktonSettings(biome, x))
                        .setDefaultValue(defaultSettings.planktonSpawnRate())
                        .build());

            if (biome == OTHER) {
                biomeCategoryBuilder.forEach(category::addEntry);
            } else {
                category.addEntry(biomeCategoryBuilder.build());
            }
        }
    }
}

