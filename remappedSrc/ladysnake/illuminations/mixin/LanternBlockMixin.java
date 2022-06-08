package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LanternBlock.class)
public abstract class LanternBlockMixin extends BlockMixin {
    @Override
    protected void illuminations$randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.getBlock() == Blocks.SOUL_LANTERN && random.nextInt(100) == 0) {
            world.addParticle(Illuminations.WILL_O_WISP, true, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0f, 0f, 0f);
        }
    }
}