package ladysnake.lightorbs.common.items;

import ladylib.LadyLib;
import ladysnake.lightorbs.common.entities.*;
import ladysnake.lightorbs.common.init.ModItems;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCompanionSummoner extends Item {
    private String pet;

    public ItemCompanionSummoner(String pet, boolean petInside) {
        this.pet = pet;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            AbstractCompanionOrb spawnedPet = null;
            switch (this.pet) {
                case "solar_orb":
                    spawnedPet = new EntitySolarOrb(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn);
                    break;
            }
            if (spawnedPet != null) worldIn.spawnEntity(spawnedPet);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
