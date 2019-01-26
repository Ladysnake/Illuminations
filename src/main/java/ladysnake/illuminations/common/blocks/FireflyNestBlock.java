package ladysnake.illuminations.common.blocks;

import ladysnake.illuminations.common.entities.FireflyEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FireflyNestBlock extends Block {

    public FireflyNestBlock(Settings settings) {
        super(settings);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState blockState) {
        return BlockSoundGroup.WOOD;
    }

    @Override
    public void onRandomTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        super.onRandomTick(blockState, world, blockPos, random);

        if (!world.isDaylight()) world.spawnEntity(new FireflyEntity(world, blockPos.getX(), blockPos.getY()+0.6, blockPos.getZ()));
    }

    @Override
    public boolean hasRandomTicks(BlockState blockState_1) {
        return true;
    }
    
}
