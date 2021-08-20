package ladysnake.illuminations.client;

import static net.minecraft.world.biome.Biome.Category.*;

import com.google.common.base.CaseFormat;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.biome.Biome.Category;

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

            // config categories and entries
            ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.illuminations.general"));
            ConfigCategory biomeSettings = builder.getOrCreateCategory(new TranslatableText("category.illuminations.biomeSettings"));
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

            List<Category> biomes = List.of(JUNGLE, PLAINS, SAVANNA, TAIGA, FOREST, RIVER, SWAMP, OCEAN, BEACH, DESERT,
                    EXTREME_HILLS, ICY, MESA, MUSHROOM, NETHER, THEEND);

            for (Category biome : biomes)
            {
                Config.BiomeSettings defaultSettings = Config.getDefaultBiomeSettings(biome);
                Config.BiomeSettings settings = Config.getBiomeSettings(biome);

                AbstractConfigListEntry<Integer> fireflySpawnChance = entryBuilder
                        .startIntField(new TranslatableText("option.illuminations.fireflySpawnChance"), Math.round(settings.fireflySpawnChance() * 100000))
                        .setSaveConsumer(x -> Config.setFireflySettings(biome, x * 0.00001f))
                        .setDefaultValue(Math.round(defaultSettings.fireflySpawnChance() * 100000))
                        .build();

                AbstractConfigListEntry<Integer> glowwormSpawnChance = entryBuilder
                        .startIntField(new TranslatableText("option.illuminations.glowwormSpawnChance"), Math.round(settings.glowwormSpawnChance() * 100000))
                        .setSaveConsumer(x -> Config.setGlowwormSettings(biome, x * 0.00001f))
                        .setDefaultValue(Math.round(settings.glowwormSpawnChance() * 100000))
                        .build();

                AbstractConfigListEntry<Integer> planktonSpawnChance = entryBuilder
                        .startIntField(new TranslatableText("option.illuminations.planktonSpawnChance"), Math.round(settings.planktonSpawnChance() * 100000))
                        .setSaveConsumer(x -> Config.setPlanktonSettings(biome, x * 0.00001f))
                        .setDefaultValue(Math.round(defaultSettings.planktonSpawnChance() * 100000))
                        .build();

                biomeSettings.addEntry(entryBuilder
                        .startSubCategory(new TranslatableText("option.illuminations.biome." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, biome.name())),
                                List.of(fireflySpawnChance, glowwormSpawnChance, planktonSpawnChance))
                        .build());
            }

            builder.setSavingRunnable(Config::save);

            // build and return the config screen
            return builder.build();
        };
    }
}

