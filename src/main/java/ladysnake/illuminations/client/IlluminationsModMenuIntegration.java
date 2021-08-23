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
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IlluminationsModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // load config
            Config.load();

            // create the config
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.illuminations.config"));
            builder.setSavingRunnable(Config::save);

            // config categories and entries
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.illuminations.general"));
            ConfigCategory biomeSettings = builder.getOrCreateCategory(new TranslatableText("category.illuminations.biomeSettings"));
            // ConfigCategory auraSettings = builder.getOrCreateCategory(new TranslatableText("category.illuminations.auraSettings"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder
                    .startEnumSelector(new TranslatableText("option.illuminations.eyesInTheDark"), EyesInTheDark.class, Config.getEyesInTheDark())
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.eyesInTheDark"),
                            new TranslatableText("option.tooltip.illuminations.eyesInTheDark.default"),
                            new TranslatableText("option.tooltip.illuminations.eyesInTheDark.enable"),
                            new TranslatableText("option.tooltip.illuminations.eyesInTheDark.disable"),
                            new TranslatableText("option.tooltip.illuminations.eyesInTheDark.always"))
                    .setSaveConsumer(Config::setEyesInTheDark)
                    .setDefaultValue(EyesInTheDark.ENABLE)
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
                    .setDefaultValue(EyesInTheDarkSpawnRate.MEDIUM)
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
                    .setDefaultValue(WillOWispsSpawnRate.MEDIUM)
                    .build());

            general.addEntry(entryBuilder
                    .startIntSlider(new TranslatableText("option.illuminations.chorusPetalsSpawnMultiplier"), Config.getChorusPetalsSpawnMultiplier(), 0, 10)
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier"),
                            new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.lowest"),
                            new TranslatableText("option.tooltip.illuminations.chorusPetalsSpawnMultiplier.highest"))
                    .setSaveConsumer(Config::setChorusPetalsSpawnMultiplier)
                    .setDefaultValue(1)
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
                    .startBooleanToggle(new TranslatableText("option.illuminations.fireflySpawnAlways"), Config.doesFireflySpawnAlways())
                    .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflySpawnAlways"))
                    .setSaveConsumer(Config::setFireflySpawnAlways)
                    .setDefaultValue(false)
                    .build());

            general.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableText("option.illuminations.fireflySpawnUnderground"), Config.doesFireflySpawnUnderground())
                    .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflySpawnUnderground"))
                    .setSaveConsumer(Config::setFireflySpawnUnderground)
                    .setDefaultValue(false)
                    .build());

            general.addEntry(entryBuilder
                    .startIntSlider(new TranslatableText("option.illuminations.fireflyWhiteAlpha"), Config.getFireflyWhiteAlpha(), 0, 100)
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.fireflyWhiteAlpha"))
                    .setSaveConsumer(Config::setFireflyWhiteAlpha)
                    .setDefaultValue(100)
                    .build());

            general.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableText("option.illuminations.autoUpdate"), Config.isAutoUpdate())
                    .setTooltip(
                            new TranslatableText("option.tooltip.illuminations.autoUpdate"))
                    .setSaveConsumer(Config::setAutoUpdate)
                    .setDefaultValue(false)
                    .build());

            general.addEntry(entryBuilder
                    .startBooleanToggle(new TranslatableText("option.illuminations.viewAurasFP"), Config.getViewAurasFP())
                    .setTooltip(new TranslatableText("option.tooltip.illuminations.viewAurasFP"))
                    .setSaveConsumer(Config::setViewAurasFP)
                    .setDefaultValue(false)
                    .build());

            for (BiomeCategory biome : values()) {
                String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, biome.name());
                BiomeSettings defaultSettings = DefaultConfig.getBiomeSettings(biome);
                BiomeSettings settings = Config.getBiomeSettings(biome);

                // Firefly spawn rate
                AbstractConfigListEntry<FireflySpawnRate> fireflySpawnRate = entryBuilder
                        .startEnumSelector(new TranslatableText("option.illuminations.fireflySpawnRate"), FireflySpawnRate.class, settings.fireflySpawnRate())
                        .setTooltip(
                                new TranslatableText("option.tooltip.illuminations.fireflySpawnRate"),
                                new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.disable"),
                                new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.low"),
                                new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.medium"),
                                new TranslatableText("option.tooltip.illuminations.fireflySpawnRate.high"))
                        .setSaveConsumer(x -> Config.setFireflySettings(biome, x))
                        .setDefaultValue(defaultSettings.fireflySpawnRate())
                        .build();

                // Firefly color
                int color = settings.fireflyColor();
                int dColor = defaultSettings.fireflyColor();
                AbstractConfigListEntry<Integer> fireflyColor = entryBuilder
                        .startColorField(new TranslatableText("option.illuminations.fireflyColor"), color)
                        .setTooltip(new TranslatableText("option.tooltip.illuminations.fireflyColor"))
                        .setSaveConsumer(x -> Config.setFireflyColorSettings(biome, x))
                        .setDefaultValue(dColor)
                        .build();

                // Glowworm spawn rate
                AbstractConfigListEntry<GlowwormSpawnRate> glowwormSpawnRate = null;
                if (settings.glowwormSpawnRate() != null)
                    glowwormSpawnRate = entryBuilder
                            .startEnumSelector(new TranslatableText("option.illuminations.glowwormSpawnRate"), GlowwormSpawnRate.class, settings.glowwormSpawnRate())
                            .setTooltip(
                                    new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate"),
                                    new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.disable"),
                                    new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.low"),
                                    new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.medium"),
                                    new TranslatableText("option.tooltip.illuminations.glowwormSpawnRate.high"))
                            .setSaveConsumer(x -> Config.setGlowwormSettings(biome, x))
                            .setDefaultValue(defaultSettings.glowwormSpawnRate())
                            .build();

                AbstractConfigListEntry<PlanktonSpawnRate> planktonSpawnRate = null;
                if (settings.planktonSpawnRate() != null)
                    planktonSpawnRate = entryBuilder
                            .startEnumSelector(new TranslatableText("option.illuminations.planktonSpawnRate"), PlanktonSpawnRate.class, settings.planktonSpawnRate())
                            .setTooltip(
                                    new TranslatableText("option.tooltip.illuminations.planktonSpawnRate"),
                                    new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.disable"),
                                    new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.low"),
                                    new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.medium"),
                                    new TranslatableText("option.tooltip.illuminations.planktonSpawnRate.high"))
                            .setSaveConsumer(x -> Config.setPlanktonSettings(biome, x))
                            .setDefaultValue(defaultSettings.planktonSpawnRate())
                            .build();

                // Biome tooltip
                Text[] tooltip = new Text[biome.getValues().length + 1];
                tooltip[0] = new TranslatableText(biome != OTHER ? "option.tooltip.illuminations.biome" : "option.tooltip.illuminations.biome.other");
                for (int i = 0; i < biome.getValues().length; i++) {
                    tooltip[i + 1] = new TranslatableText("biome.minecraft." + biome.getValues()[i].getPath());
                }

                List<AbstractConfigListEntry> biomeEntries = new ArrayList<>(4);
                biomeEntries.add(fireflySpawnRate);
                biomeEntries.add(fireflyColor);
                if (glowwormSpawnRate != null) biomeEntries.add(glowwormSpawnRate);
                if (settings.planktonSpawnRate() != null) biomeEntries.add(planktonSpawnRate);

                biomeSettings.addEntry(entryBuilder
                        .startSubCategory(new TranslatableText("option.illuminations.biome." + name), biomeEntries)
                        .setTooltip(tooltip)
                        .build());
            }

            /*List<String> auras = List.of("twilight", "ghostly", "chorus", "autumn_leaves", "sculk_tendrils",
                    "shadowbringer_soul", "goldenrod", "confetti", "prismatic_confetti");

            for (String aura : auras) {
                AuraSettings defaultSettings = Config.getDefaultAuraSettings(aura);
                AuraSettings settings = Config.getAuraSettings(aura);

                AbstractConfigListEntry<Integer> auraSpawnRate = entryBuilder
                        .startIntSlider(new TranslatableText("option.illuminations.auraSpawnRate"), Math.round(settings.spawnRate() * 100), 0, 100)
                        .setTooltip(
                                new TranslatableText("option.tooltip.illuminations.auraSpawnRate"),
                                new TranslatableText("option.tooltip.illuminations.auraSpawnRate.lowest"),
                                new TranslatableText("option.tooltip.illuminations.auraSpawnRate.highest"))
                        .setSaveConsumer(x -> Config.setAuraSpawnRate(aura, x * 0.01f))
                        .setDefaultValue(Math.round(defaultSettings.spawnRate() * 100))
                        .build();

                AbstractConfigListEntry<Integer> auraDelay = entryBuilder
                        .startIntSlider(new TranslatableText("option.illuminations.auraDelay"), settings.delay(), 1, 100)
                        .setTooltip(
                                new TranslatableText("option.tooltip.illuminations.auraDelay"),
                                new TranslatableText("option.tooltip.illuminations.auraDelay.lowest"),
                                new TranslatableText("option.tooltip.illuminations.auraDelay.highest"))
                        .setSaveConsumer(x -> Config.setAuraDelay(aura, x))
                        .setDefaultValue(defaultSettings.delay())
                        .build();

                auraSettings.addEntry(entryBuilder
                        .startSubCategory(new TranslatableText("option.illuminations.aura." + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, aura)),
                                List.of(auraSpawnRate, auraDelay))
                        .build());
            }*/

            // build and return the config screen
            return builder.build();
        };
    }
}

