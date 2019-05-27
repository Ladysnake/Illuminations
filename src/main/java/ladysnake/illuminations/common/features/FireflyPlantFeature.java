package ladysnake.illuminations.common.features;

import com.mojang.datafixers.Dynamic;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import net.minecraft.block.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class FireflyPlantFeature extends Feature<ProbabilityConfig> {
    private static final int MIN_PLANT_HEIGHT = 0;
    private static final int MAX_PLANT_HEIGHT = 3;

    public FireflyPlantFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> function_1) {
        super(function_1);
    }

    @Override
    public boolean generate(IWorld iWorld, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos blockPos, ProbabilityConfig probabilityConfig) {
        int int_1 = 0;
        BlockPos.Mutable blockPos$Mutable_1 = new BlockPos.Mutable(blockPos);
        BlockPos.Mutable blockPos$Mutable_2 = new BlockPos.Mutable(blockPos);
        if (iWorld.isAir(blockPos$Mutable_1)) {
            if (Blocks.BAMBOO.getDefaultState().canPlaceAt(iWorld, blockPos$Mutable_1)) {
                int int_2 = random.nextInt(12) + 5;
                int int_3;
                if (random.nextFloat() < probabilityConfig.probability) {
                    int_3 = random.nextInt(4) + 1;

                    for(int int_4 = blockPos.getX() - int_3; int_4 <= blockPos.getX() + int_3; ++int_4) {
                        for(int int_5 = blockPos.getZ() - int_3; int_5 <= blockPos.getZ() + int_3; ++int_5) {
                            int int_6 = int_4 - blockPos.getX();
                            int int_7 = int_5 - blockPos.getZ();
                            if (int_6 * int_6 + int_7 * int_7 <= int_3 * int_3) {
                                blockPos$Mutable_2.set(int_4, iWorld.getTop(Heightmap.Type.WORLD_SURFACE, int_4, int_5) - 1, int_5);
                                BlockPos bp = new BlockPos(blockPos$Mutable_2.getX(), blockPos$Mutable_2.getY()+1, blockPos$Mutable_2.getZ());
                                if (iWorld.getBlockState(blockPos$Mutable_2).getBlock().matches(BlockTags.DIRT_LIKE)
                                        && blockPos$Mutable_2.getY() < 254) {
                                    if (new Random().nextBoolean()) {
                                        ((TallPlantBlock) IlluminationsBlocks.FIREFLY_TALL_GRASS).placeAt(iWorld, bp, 2);
                                    } else {
                                        iWorld.setBlockState(bp, IlluminationsBlocks.FIREFLY_GRASS.getDefaultState(), 2);
                                    }
                                }
                            }
                        }
                    }
                }

                for(int_3 = 0; int_3 < int_2 && iWorld.isAir(blockPos$Mutable_1); ++int_3) {
                    iWorld.setBlockState(blockPos$Mutable_1, Blocks.CAKE.getDefaultState(), 2);
                    blockPos$Mutable_1.setOffset(Direction.UP, 1);
                }

                if (blockPos$Mutable_1.getY() - blockPos.getY() >= 3) {
                    iWorld.setBlockState(blockPos$Mutable_1, Blocks.CAKE.getDefaultState(), 2);
                    iWorld.setBlockState(blockPos$Mutable_1.setOffset(Direction.DOWN, 1), Blocks.CAKE.getDefaultState(), 2);
                    iWorld.setBlockState(blockPos$Mutable_1.setOffset(Direction.DOWN, 1), Blocks.CAKE.getDefaultState(), 2);
                }
            }

            ++int_1;
        }

        return int_1 > 0;
    }

}
