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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // if light orb inside, releasing it
        if (!this.content.equals("")) {
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
                if (!player.addItemStackToInventory(new ItemStack(ModItems.GLASS_JAR))) player.dropItem(new ItemStack(ModItems.GLASS_JAR), true);
            }

            if (!worldIn.isRemote) {
                switch (this.content) {
                    case "firefly":
                        worldIn.spawnEntity(new EntityFirefly(worldIn, pos.getX()+hitX, pos.getY()+hitY, pos.getZ()+hitZ));
                        break;
                    case "psi_firefly":
                        worldIn.spawnEntity(new EntityPsiFirefly(worldIn, pos.getX()+hitX, pos.getY()+hitY, pos.getZ()+hitZ));
                        break;
                    case "lightning_bug":
                        worldIn.spawnEntity(new EntityLightningBug(worldIn, pos.getX()+hitX, pos.getY()+hitY, pos.getZ()+hitZ));
                        break;
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        // if empty jar and entity is a firefly, catching it
        if (content.equals("") && target instanceof EntityFirefly) {
            ItemStack obtainedStack = new ItemStack(ModItems.GLASS_JAR);
            switch (EntityList.getKey(target.getClass()).toString()) {
                case "lightorbs:firefly":
                    obtainedStack = new ItemStack(ModItems.FIREFLY_IN_A_JAR);
                    break;
                case "lightorbs:psi_firefly":
                    obtainedStack = new ItemStack(ModItems.PSI_FIREFLY_IN_A_JAR);
                    break;
                case "lightorbs:lightning_bug":
                    obtainedStack = new ItemStack(ModItems.LIGHTNING_BUG_IN_A_JAR);
                    break;
            }

            if (!playerIn.isCreative() && !playerIn.world.isRemote) {
                stack.shrink(1);
                if (!playerIn.addItemStackToInventory(obtainedStack)) playerIn.dropItem(obtainedStack, true);
            }
            target.setDead();
        }
        return true;
    }

}
