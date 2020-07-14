package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.features.FireflyPlantFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.biome.Biome.Category.*;

public class IlluminationsWorldFeatures {
    public static Feature<ProbabilityConfig> FIREFLY_PLANT;

    public static void init() {
        FIREFLY_PLANT = Registry.register(Registry.FEATURE, Illuminations.MOD_ID + ":firefly_plant", new FireflyPlantFeature(ProbabilityConfig.CODEC));
        List<Biome.Category> fireflyBiomes = Arrays.asList(PLAINS, SWAMP, FOREST, JUNGLE, SAVANNA, RIVER);
        for (Biome biome : Registry.BIOME) {
            if (fireflyBiomes.contains(biome.getCategory())) {
                biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, FIREFLY_PLANT.configure(new ProbabilityConfig(10F)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(90, 0, 0, 250))));
            }
        }
    }

}