package ladysnake.illuminations.client;

import com.google.common.base.CaseFormat;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.config.DefaultConfig;
import ladysnake.illuminations.client.data.BiomeSettings;
import ladysnake.illuminations.client.enums.*;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.HashMap;

import static ladysnake.illuminations.client.enums.BiomeCategory.OTHER;

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
                    .setTitle(Text.translatable("title.illuminations.config"));
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
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.illuminations.general"));

        general.addEntry(entryBuilder
                .startEnumSelector(Text.translatable("option.illuminations.halloweenFeatures"), HalloweenFeatures.class, Config.getHalloweenFeatures())
                .setTooltip(
                        Text.translatable("option.tooltip.illuminations.halloweenFeatures"),
                        Text.translatable("option.tooltip.illuminations.halloweenFeatures.default"),
                        Text.translatable("option.tooltip.illuminations.halloweenFeatures.enable"),
                        Text.translatable("option.tooltip.illuminations.halloweenFeatures.disable"),
                        Text.translatable("option.tooltip.illuminations.halloweenFeatures.always"))
                .setSaveConsumer(Config::setHalloweenFeatures)
                .setDefaultValue(DefaultConfig.HALLOWEEN_FEATURES)
                .build());

        general.addEntry(entryBuilder
                .startEnumSelector(Text.translatable("option.illuminations.eyesInTheDarkSpawnRate"), EyesInTheDarkSpawnRate.class, Config.getEyesInTheDarkSpawnRate())
                .setTooltip(
                        Text.translatable("option.tooltip.illuminations.eyesInTheDarkSpawnRate"),
                        Text.translatable("option.tooltip.illuminations.eyesInTheDarkSpawnRate.default"),
                        Text.translatable("option.tooltip.illuminations.eyesInTheDarkSpawnRate.low"),
                        Text.translatable("option.tooltip.illuminations.eyesInTheDarkSpawnRate.medium"),
                        Text.translatable("option.tooltip.illuminations.eyesInTheDarkSpawnRate.high"))
                .setSaveConsumer(Config::setEyesInTheDarkSpawnRate)
                .setDefaultValue(DefaultConfig.EYES_IN_THE_DARK_SPAWN_RATE)
                .build());

        general.addEntry(entryBuilder
                .startEnumSelector(Text.translatable("option.illuminations.willOWispsSpawnRate"), WillOWispsSpawnRate.class, Config.getWillOWispsSpawnRate())
                .setTooltip(
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate"),
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate.default"),
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate.disable"),
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate.low"),
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate.medium"),
                        Text.translatable("option.tooltip.illuminations.willOWispsSpawnRate.high"))
                .setSaveConsumer(Config::setWillOWispsSpawnRate)
                .setDefaultValue(DefaultConfig.WILL_O_WISPS_SPAWN_RATE)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Text.translatable("option.illuminations.chorusPetalsSpawnMultiplier"), Config.getChorusPetalsSpawnMultiplier(), 0, 10)
                .setTooltip(
                        Text.translatable("option.tooltip.illuminations.chorusPetalsSpawnMultiplier"),
                        Text.translatable("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.lowest"),
                        Text.translatable("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.highest"))
                .setSaveConsumer(Config::setChorusPetalsSpawnMultiplier)
                .setDefaultValue(DefaultConfig.CHORUS_PETALS_SPAWN_MULTIPLIER)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Text.translatable("option.illuminations.density"), Config.getDensity(), 0, 1000)
                .setTooltip(
                        Text.translatable("option.tooltip.illuminations.density"),
                        Text.translatable("option.tooltip.illuminations.density.lowest"),
                        Text.translatable("option.tooltip.illuminations.density.highest"))
                .setSaveConsumer(Config::setDensity)
                .setDefaultValue(DefaultConfig.DENSITY)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.fireflySpawnAlways"), Config.doesFireflySpawnAlways())
                .setTooltip(Text.translatable("option.tooltip.illuminations.fireflySpawnAlways"))
                .setSaveConsumer(Config::setFireflySpawnAlways)
                .setDefaultValue(DefaultConfig.FIREFLY_SPAWN_ALWAYS)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.fireflySpawnUnderground"), Config.doesFireflySpawnUnderground())
                .setTooltip(Text.translatable("option.tooltip.illuminations.fireflySpawnUnderground"))
                .setSaveConsumer(Config::setFireflySpawnUnderground)
                .setDefaultValue(DefaultConfig.FIREFLY_SPAWN_UNDERGROUND)
                .build());

        general.addEntry(entryBuilder
                .startIntSlider(Text.translatable("option.illuminations.fireflyWhiteAlpha"), Config.getFireflyWhiteAlpha(), 0, 100)
                .setTooltip(Text.translatable("option.tooltip.illuminations.fireflyWhiteAlpha"))
                .setSaveConsumer(Config::setFireflyWhiteAlpha)
                .setDefaultValue(DefaultConfig.FIREFLY_WHITE_ALPHA)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.fireflyRainbow"), Config.getFireflyRainbow())
                .setSaveConsumer(Config::setFireflyRainbow)
                .setDefaultValue(DefaultConfig.FIREFLY_RAINBOW)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.displayCosmetics"), Config.shouldDisplayCosmetics())
                .setTooltip(Text.translatable("option.tooltip.illuminations.displayCosmetics"))
                .setSaveConsumer(Config::setDisplayCosmetics)
                .setDefaultValue(DefaultConfig.DISPLAY_COSMETICS)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.viewAurasFP"), Config.getViewAurasFP())
                .setTooltip(Text.translatable("option.tooltip.illuminations.viewAurasFP"))
                .setSaveConsumer(Config::setViewAurasFP)
                .setDefaultValue(DefaultConfig.VIEW_AURAS_FP)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.displayDonationToast"), Config.shouldDisplayDonationToast())
                .setTooltip(Text.translatable("option.tooltip.illuminations.displayDonationToast"))
                .setSaveConsumer(Config::setDisplayDonationToast)
                .setDefaultValue(DefaultConfig.DISPLAY_DONATION_TOAST)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("option.illuminations.debugMode"), Config.isDebugMode())
                .setTooltip(Text.translatable("option.tooltip.illuminations.debugMode"))
                .setSaveConsumer(Config::setDebugMode)
                .setDefaultValue(DefaultConfig.DEBUG_MODE)
                .build());
    }

    private void GenerateBiomeSettings() {
        HashMap<Identifier, ConfigCategory> biomeCategories = new HashMap<>();
        for (Identifier dimension : BiomeCategory.getDimensions()) {
            String name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dimension.getPath());
            ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.illuminations." + name));
            category.setDescription(new StringVisitable[]{Text.translatable("category.illuminations." + name + ".description")});
            biomeCategories.put(dimension, category);
        }

        for (BiomeCategory biome : BiomeCategory.values()) {
            ConfigCategory category = biomeCategories.get(biome.getDimension());
            String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, biome.name());
            BiomeSettings defaultSettings = DefaultConfig.getBiomeSettings(biome);
            BiomeSettings settings = Config.getBiomeSettings(biome);

            // Biome tooltip
            Text[] tooltip = new Text[biome.getBiomes().length + 1];
            tooltip[0] = Text.translatable("option.tooltip.illuminations.biome");
            for (int i = 0; i < biome.getBiomes().length; i++) {
                tooltip[i + 1] = Text.translatable("biome.minecraft." + biome.getBiomes()[i].getPath());
            }

            // Biome sub category
            SubCategoryBuilder biomeCategoryBuilder = entryBuilder
                    .startSubCategory(Text.translatable("option.illuminations.biome." + name))
                    .setTooltip(tooltip);

            // Firefly spawn rate
            biomeCategoryBuilder.add(entryBuilder
                    .startEnumSelector(Text.translatable("option.illuminations.fireflySpawnRate"), FireflySpawnRate.class, settings.fireflySpawnRate())
                    .setTooltip(
                            Text.translatable("option.tooltip.illuminations.fireflySpawnRate"),
                            Text.translatable("option.tooltip.illuminations.fireflySpawnRate.disable"),
                            Text.translatable("option.tooltip.illuminations.fireflySpawnRate.low"),
                            Text.translatable("option.tooltip.illuminations.fireflySpawnRate.medium"),
                            Text.translatable("option.tooltip.illuminations.fireflySpawnRate.high"))
                    .setSaveConsumer(x -> Config.setFireflySettings(biome, x))
                    .setDefaultValue(defaultSettings.fireflySpawnRate())
                    .build());

            // Firefly color
            biomeCategoryBuilder.add(entryBuilder
                    .startColorField(Text.translatable("option.illuminations.fireflyColor"), settings.fireflyColor())
                    .setTooltip(Text.translatable("option.tooltip.illuminations.fireflyColor"))
                    .setSaveConsumer(x -> Config.setFireflyColorSettings(biome, x))
                    .setDefaultValue(defaultSettings.fireflyColor())
                    .build());

            // Glowworm spawn rate
            if (settings.glowwormSpawnRate() != null)
                biomeCategoryBuilder.add(entryBuilder
                        .startEnumSelector(Text.translatable("option.illuminations.glowwormSpawnRate"), GlowwormSpawnRate.class, settings.glowwormSpawnRate())
                        .setTooltip(
                                Text.translatable("option.tooltip.illuminations.glowwormSpawnRate"),
                                Text.translatable("option.tooltip.illuminations.glowwormSpawnRate.disable"),
                                Text.translatable("option.tooltip.illuminations.glowwormSpawnRate.low"),
                                Text.translatable("option.tooltip.illuminations.glowwormSpawnRate.medium"),
                                Text.translatable("option.tooltip.illuminations.glowwormSpawnRate.high"))
                        .setSaveConsumer(x -> Config.setGlowwormSettings(biome, x))
                        .setDefaultValue(defaultSettings.glowwormSpawnRate())
                        .build());

            // Plankton spawn rate
            if (settings.planktonSpawnRate() != null)
                biomeCategoryBuilder.add(entryBuilder
                        .startEnumSelector(Text.translatable("option.illuminations.planktonSpawnRate"), PlanktonSpawnRate.class, settings.planktonSpawnRate())
                        .setTooltip(
                                Text.translatable("option.tooltip.illuminations.planktonSpawnRate"),
                                Text.translatable("option.tooltip.illuminations.planktonSpawnRate.disable"),
                                Text.translatable("option.tooltip.illuminations.planktonSpawnRate.low"),
                                Text.translatable("option.tooltip.illuminations.planktonSpawnRate.medium"),
                                Text.translatable("option.tooltip.illuminations.planktonSpawnRate.high"))
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

