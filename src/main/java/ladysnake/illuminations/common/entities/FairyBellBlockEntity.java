package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;

public class FairyBellBlockEntity extends BlockEntity {
    private int fairyColor;

    public FairyBellBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    public FairyBellBlockEntity() {
        super(IlluminationsEntities.FAIRY_BELL);
    }

    public void setFairyColor(int color) {
        this.fairyColor = color;
    }

    // NBT
    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        super.toTag(compoundTag);
        compoundTag.putInt("color", this.fairyColor);
        return compoundTag;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        super.fromTag(compoundTag);
        if (compoundTag.getInt("color") != 0) {
            this.fairyColor = compoundTag.getInt("color");
        }
    }

}
