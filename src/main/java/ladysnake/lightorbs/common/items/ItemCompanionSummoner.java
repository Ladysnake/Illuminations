package ladysnake.lightorbs.common.items;

import ladysnake.lightorbs.common.entities.EntityCompanionOrb;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCompanionSummoner extends Item {
    private String pet;

    public ItemCompanionSummoner(String pet) {
        this.pet = pet;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // getting rid of all previous companion orbs
        List<Entity> companions = worldIn.getEntities(EntityCompanionOrb.class, entity -> {
            return ((EntityCompanionOrb) entity).getOwner() == playerIn;
        });
        companions.forEach(entity -> entity.setDead());

        // if sneaking, removing pets
        if (!playerIn.isSneaking()) {
            EntityCompanionOrb spawnedPet = new EntityCompanionOrb(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.getUniqueID());
            if (playerIn.getHeldItem(handIn).hasDisplayName()) spawnedPet.setCustomNameTag(playerIn.getHeldItem(handIn).getDisplayName());
            else spawnedPet.setCustomNameTag(I18n.format(this.pet));
            spawnedPet.setProperties(this.pet, 243, 126, 74, 8);
            if (!worldIn.isRemote) worldIn.spawnEntity(spawnedPet);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.lightorbs."+this.pet+"_summoner.tooltip"));
        tooltip.add(I18n.format("item.lightorbs.companion_summoner.tooltip"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
