package ladysnake.illuminations.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class FairyBellBlock extends FlowerBlock {
    public static final EnumProperty<State> STATE = EnumProperty.of("state", State.class);

    public FairyBellBlock(StatusEffect statusEffect, int i, Settings settings) {
        super(statusEffect, i, settings);
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
        return this.getDefaultState().with(STATE, State.EMPTY);
    }

    @Override
    public boolean hasRandomTicks(BlockState blockState_1) {
        return true;
    }

    @Override
    public void onRandomTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {

    }

    enum State implements StringIdentifiable {
        OPEN("open"),
        CLOSED("closed"),
        EMPTY("empty");

        private final String name;

        State(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.asString();
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

}
