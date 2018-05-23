package ladysnake.lightorbs.common.items;

import ladylib.LadyLib;
import ladysnake.lightorbs.common.entities.AbstractCompanionOrb;
import ladysnake.lightorbs.common.entities.EntitySolarOrb;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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

            // if sneaking, removing pets
            if (!playerIn.isSneaking()) {
                AbstractCompanionOrb spawnedPet = null;
                switch (this.pet) {
                    case "solar_orb":
                        spawnedPet = new EntitySolarOrb(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn);
                        break;
                }
                if (spawnedPet != null) {
                    if (playerIn.getHeldItem(handIn).hasDisplayName()) spawnedPet.setCustomNameTag(playerIn.getHeldItem(handIn).getDisplayName());
                    worldIn.spawnEntity(spawnedPet);
                }
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.lightorbs."+pet+"_summoner.tooltip"));
        tooltip.add(I18n.format("item.lightorbs.companion_summoner.tooltip"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
