package ladysnake.lightorbs.common.items;

import ladysnake.lightorbs.common.entities.AbstractCompanionOrb;
import ladysnake.lightorbs.common.entities.EntitySolarOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemCompanionSummoner extends Item {
    private String pet;

    public ItemCompanionSummoner(String pet, boolean petInside) {
        this.pet = pet;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            // getting rid of all previous companion orbs
            List<Entity> companions = worldIn.getEntities(AbstractCompanionOrb.class, entity -> {
                return ((AbstractCompanionOrb) entity).getOwner() == playerIn;
            });
            companions.forEach(entity -> entity.setDead());

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
