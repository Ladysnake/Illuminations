package ladysnake.illuminations.common.blocks;

import ladysnake.illuminations.common.entities.LightningBugEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LightningBugNestBlock extends FireflyNestBlock {

    public LightningBugNestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onRandomTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        super.onRandomTick(blockState, world, blockPos, random);

        if (!world.isDaylight()) world.spawnEntity(new LightningBugEntity(world, blockPos.getX(), blockPos.getY()+0.6, blockPos.getZ()));
    }


}
