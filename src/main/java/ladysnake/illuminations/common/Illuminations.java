package ladysnake.illuminations.common;

import ladysnake.illuminations.common.features.FireflyPlantFeature;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import ladysnake.illuminations.common.init.IlluminationsEntities;
import ladysnake.illuminations.common.init.IlluminationsItems;
import ladysnake.illuminations.common.init.IlluminationsWorldFeatures;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.BambooFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import static net.minecraft.util.registry.Registry.register;

public class Illuminations implements ModInitializer {
    public static final String MOD_ID = "illuminations";

    @Override
    public void onInitialize() {
        IlluminationsEntities.init();
        IlluminationsItems.init();
        IlluminationsBlocks.init();
        IlluminationsWorldFeatures.init();

    }
}

