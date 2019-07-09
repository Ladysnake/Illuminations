package ladysnake.illuminations.common.blocks;

import ladysnake.illuminations.common.entities.FairyBellBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

public class FairyBellBlock extends FlowerBlock implements BlockEntityProvider {
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
    public boolean hasBlockEntity() {
        return super.hasBlockEntity();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new FairyBellBlockEntity();
    }

    public enum State implements StringIdentifiable {
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
