package ladysnake.illuminations.common.blocks;

import ladysnake.illuminations.common.entities.FairyBellBlockEntity;
import ladysnake.illuminations.common.entities.FairyEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FairyBellBlock extends FlowerBlock implements BlockEntityProvider {
    public static final EnumProperty<State> STATE = EnumProperty.of("state", State.class);
    protected static final VoxelShape HITBOX = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 13.0D, 11.0D);

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

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        Vec3d offset = blockState_1.getOffsetPos(blockView_1, blockPos_1);
        return this.HITBOX.offset(offset.x, offset.y, offset.z);
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        super.onBreak(world, blockPos, blockState, playerEntity);
        // if closed, release a fairy
        if (blockState.get(STATE) == State.CLOSED) {
            FairyBellBlockEntity blockEntity = (FairyBellBlockEntity) world.getBlockEntity(blockPos);
            Vec3d offset = blockState.getOffsetPos(world, blockPos);
            FairyEntity spawnedFairy = new FairyEntity(world, blockPos.getX()+offset.x+.5, blockPos.getY()+offset.y+.5, blockPos.getZ()+offset.z+.5, blockEntity.getFairyColor());
            world.spawnEntity(spawnedFairy);
        }
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
