package ladysnake.lumen.common.items;

import ladysnake.lumen.common.entities.EntityCompanionOrb;
import ladysnake.lumen.common.entities.EntityCompanionOrbAnimated;
import ladysnake.lumen.common.init.ModEntities;
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
    private ModEntities.Companion companion;

    public ItemCompanionSummoner(ModEntities.Companion companion) {
        this.companion = companion;
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
            EntityCompanionOrb spawnedPet;
            if (this.companion.isAnimated()) spawnedPet = new EntityCompanionOrbAnimated(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.getUniqueID(), ModEntities.Companion.valueOf(this.companion.toString()));
            else spawnedPet = new EntityCompanionOrb(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.getUniqueID(), ModEntities.Companion.valueOf(this.companion.toString()));
            if (playerIn.getHeldItem(handIn).hasDisplayName()) spawnedPet.setCustomNameTag(playerIn.getHeldItem(handIn).getDisplayName());
            else spawnedPet.setCustomNameTag(I18n.format(this.companion.toString()));
            if (!worldIn.isRemote) worldIn.spawnEntity(spawnedPet);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.lumen."+this.companion.toString().toLowerCase()+"_summoner.tooltip"));
        tooltip.add(I18n.format("item.lumen.companion_summoner.tooltip"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
