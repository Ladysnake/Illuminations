package ladysnake.lightorbs.common.items;

import ladysnake.lightorbs.common.entities.EntityFirefly;
import ladysnake.lightorbs.common.entities.EntityLightningBug;
import ladysnake.lightorbs.common.entities.EntityPsiFirefly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGlassJar extends Item {
    private String content;

    public ItemGlassJar() {
        this.content = "";
    }

    public ItemGlassJar(String content) {
        this.content = content;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        // if empty jar and entity is a firefly, catching it
        if (content.equals("") && target instanceof EntityFirefly) {
            stack.setCount(stack.getCount() - 1);
            playerIn.addItemStackToInventory(new ItemStack(new ItemGlassJar(target.getName())));
            target.setDead();
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // if light orb inside, releasing it
        if (!this.content.equals("")) {
            playerIn.getHeldItem(handIn).setCount(playerIn.getHeldItem(handIn).getCount() - 1);
            playerIn.addItemStackToInventory(new ItemStack(new ItemGlassJar()));
            switch (this.content) {
                case "firefly":
                    worldIn.spawnEntity(new EntityFirefly(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ));
                    break;
                case "psi_firefly":
                    worldIn.spawnEntity(new EntityPsiFirefly(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ));
                    break;
                case "lightning_bug":
                    worldIn.spawnEntity(new EntityLightningBug(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ));
                    break;
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
