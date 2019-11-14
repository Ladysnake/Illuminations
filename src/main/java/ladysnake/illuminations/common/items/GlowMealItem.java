package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.init.IlluminationsBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class GlowMealItem extends Item {
    public GlowMealItem(Item.Settings item$Settings_1) {
        super(item$Settings_1);
    }

    public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
        World world_1 = itemUsageContext_1.getWorld();
        BlockPos blockPos_1 = itemUsageContext_1.getBlockPos();
        BlockPos blockPos_2 = blockPos_1.offset(itemUsageContext_1.getPlayerFacing());
        if (useOnFertilizable(itemUsageContext_1.getStack(), world_1, blockPos_1)) {
            if (!world_1.isClient) {
                world_1.playLevelEvent(2005, blockPos_1, 0);
            }

            return ActionResult.SUCCESS;
        } else {
            BlockState blockState_1 = world_1.getBlockState(blockPos_1);
            boolean boolean_1 = Block.isSolidFullSquare(blockState_1, world_1, blockPos_1, itemUsageContext_1.getPlayerFacing());
            if (boolean_1 && useOnGround(itemUsageContext_1.getStack(), world_1, blockPos_2, itemUsageContext_1.getPlayerFacing())) {
                if (!world_1.isClient) {
                    world_1.playLevelEvent(2005, blockPos_2, 0);
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    public static boolean useOnFertilizable(ItemStack itemStack, World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() == Blocks.GRASS_BLOCK) {
            blockPos = blockPos.up();
            Random random = world.random;
            BlockState blockState_2 = IlluminationsBlocks.FIREFLY_GRASS.getDefaultState();

            label48:
            for(int int_1 = 0; int_1 < 32; ++int_1) {
                for(int int_2 = 0; int_2 < int_1 / 16; ++int_2) {
                    blockPos = blockPos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos.down()).getBlock() != blockState.getBlock() || Block.isShapeFullCube(world.getBlockState(blockPos).getCollisionShape(world, blockPos))) {
                        continue label48;
                    }
                }

                BlockState blockState_3 = world.getBlockState(blockPos);
                if (blockState_3.getBlock() == blockState_2.getBlock() && random.nextInt(10) == 0) {
                    ((Fertilizable)blockState_2.getBlock()).grow(world, random, blockPos, blockState_3);
                }

                if (blockState_3.isAir()) {
                    if (blockState_2.canPlaceAt(world, blockPos)) {
                        world.setBlockState(blockPos, blockState_2, 3);
                    }
                }
            }
            return true;
        } else return false;
    }

    public static boolean useOnGround(ItemStack itemStack_1, World world_1, BlockPos blockPos_1, @Nullable Direction direction_1) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public static void playEffects(IWorld iWorld_1, BlockPos blockPos_1, int int_1) {
        if (int_1 == 0) {
            int_1 = 15;
        }

        BlockState blockState_1 = iWorld_1.getBlockState(blockPos_1);
        if (!blockState_1.isAir()) {
            for(int int_2 = 0; int_2 < int_1; ++int_2) {
                double double_1 = iWorld_1.getRandom().nextGaussian() * 0.02D;
                double double_2 = iWorld_1.getRandom().nextGaussian() * 0.02D;
                double double_3 = iWorld_1.getRandom().nextGaussian() * 0.02D;
                iWorld_1.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)((float)blockPos_1.getX() + iWorld_1.getRandom().nextFloat()), (double)blockPos_1.getY() + (double)iWorld_1.getRandom().nextFloat() * blockState_1.getOutlineShape(iWorld_1, blockPos_1).getMaximum(Direction.Axis.Y), (double)((float)blockPos_1.getZ() + iWorld_1.getRandom().nextFloat()), double_1, double_2, double_3);
            }

        }
    }
}
