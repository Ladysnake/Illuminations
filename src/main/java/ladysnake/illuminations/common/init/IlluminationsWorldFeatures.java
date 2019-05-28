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

public class IlluminationsWorldFeatures {
    public static Feature<ProbabilityConfig> FIREFLY_PLANT;

    public static void init() {
        FIREFLY_PLANT = Registry.register(Registry.FEATURE, Illuminations.MOD_ID + ":firefly_plant", new FireflyPlantFeature(ProbabilityConfig::deserialize));
        for (Biome biome : Registry.BIOME) {
            biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FIREFLY_PLANT, new ProbabilityConfig(10F), Decorator.COUNT_RANGE, new RangeDecoratorConfig(90, 0, 0, 250)));
        }
    }

}