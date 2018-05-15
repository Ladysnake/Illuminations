package ladysnake.lightorbs.common.items;

import ladysnake.lightorbs.common.entities.*;
import ladysnake.lightorbs.common.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSolarOrb extends Item {
    private String pet;
    private boolean petInside;

    public ItemSolarOrb(String pet, boolean petInside) {
        this.pet = pet;
        this.petInside = petInside;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // if light orb inside, releasing it
        if (this.petInside) {
            if (!playerIn.isCreative()) {
                playerIn.getHeldItem(handIn).shrink(1);
                if (!playerIn.addItemStackToInventory(new ItemStack(ModItems.GLASS_JAR))) playerIn.dropItem(new ItemStack(ModItems.GLASS_JAR), true);
            }

            if (!worldIn.isRemote) {
                AbstractCompanionOrb spawnedPet = null;
                switch (this.pet) {
                    case "solar_orb":
                        spawnedPet = new EntitySolarOrb(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn);
                        break;
                }
                if (spawnedPet != null) worldIn.spawnEntity(spawnedPet);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
