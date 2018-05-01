package ladysnake.lightorbs.common.items;

import ladysnake.lightorbs.common.entities.EntityFirefly;
import ladysnake.lightorbs.common.entities.EntityLightningBug;
import ladysnake.lightorbs.common.entities.EntityPsiFirefly;
import ladysnake.lightorbs.common.init.ModItems;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGlassJar extends Item {
    private String content;

    public ItemGlassJar() {
        this("");
    }

    public ItemGlassJar(String content) {
        this.content = content;
        this.maxStackSize = 16;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        // if empty jar and entity is a firefly, catching it
        if (content.equals("") && target instanceof EntityFirefly) {
            if (!playerIn.isCreative()) {
                switch (EntityList.getKey(target.getClass()).toString()) {
                    case "lightorbs:firefly":
                        playerIn.addItemStackToInventory(new ItemStack(ModItems.FIREFLY_IN_A_JAR));
                        break;
                    case "lightorbs:psi_firefly":
                        playerIn.addItemStackToInventory(new ItemStack(ModItems.PSI_FIREFLY_IN_A_JAR));
                        break;
                    case "lightorbs:lightning_bug":
                        playerIn.addItemStackToInventory(new ItemStack(ModItems.LIGHTNING_BUG_IN_A_JAR));
                        break;
                }
                stack.shrink(1);
            }
            target.setDead();
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // if light orb inside, releasing it
        if (!this.content.equals("")) {
            if (!playerIn.isCreative()) {
                playerIn.getHeldItem(handIn).shrink(1);
                playerIn.addItemStackToInventory(new ItemStack(ModItems.GLASS_JAR));
            }
            if (!worldIn.isRemote) {
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
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
