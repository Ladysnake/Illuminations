package ladysnake.illuminations.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "randomDisplayTick", at = @At("RETURN"))
    protected void illuminations$randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
    }
}