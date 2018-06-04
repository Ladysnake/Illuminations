package ladysnake.lumen.common.items;

import ladysnake.lumen.common.entities.EntityEmber;
import ladysnake.lumen.common.entities.EntityFirefly;
import ladysnake.lumen.common.entities.EntityLightningBug;
import ladysnake.lumen.common.entities.EntityPsiFirefly;
import ladysnake.lumen.common.init.ModItems;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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

    public String getContent() {
        return this.content;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // if light orb inside, releasing it
        if (!this.content.equals("")) {
            if (!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
                if (!player.addItemStackToInventory(new ItemStack(ModItems.GLASS_JAR)))
                    player.dropItem(new ItemStack(ModItems.GLASS_JAR), true);
            }

            if (!worldIn.isRemote) {
                EntityFirefly spawnedFirefly = null;
                switch (this.content) {
                    case "firefly":
                        spawnedFirefly = new EntityFirefly(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
                        break;
                    case "psi_firefly":
                        spawnedFirefly = new EntityPsiFirefly(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
                        break;
                    case "lightning_bug":
                        spawnedFirefly = new EntityLightningBug(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
                        break;
                    case "ember":
                        spawnedFirefly = new EntityEmber(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
                        break;
                }
                if (spawnedFirefly != null) {
                    spawnedFirefly.setCanDespawn(false);
                    worldIn.spawnEntity(spawnedFirefly);
                }
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        // if empty jar and entity is a firefly, catching it
        if (this.content.equals("") && target instanceof EntityFirefly) {
            ItemStack obtainedStack = null;
            switch (EntityList.getKey(target.getClass()).toString()) {
                case "lumen:firefly":
                    obtainedStack = new ItemStack(ModItems.FIREFLY_IN_A_JAR);
                    break;
                case "lumen:psi_firefly":
                    obtainedStack = new ItemStack(ModItems.PSI_FIREFLY_IN_A_JAR);
                    break;
                case "lumen:lightning_bug":
                    obtainedStack = new ItemStack(ModItems.LIGHTNING_BUG_IN_A_JAR);
                    break;
                case "lumen:ember":
                    obtainedStack = new ItemStack(ModItems.EMBER_IN_A_JAR);
                    break;
            }

            if (!playerIn.isCreative() && obtainedStack != null) {
                stack.shrink(1);
                if (!playerIn.addItemStackToInventory(obtainedStack)) playerIn.dropItem(obtainedStack, true);
            }

            target.setDead();
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
/*
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        switch (this.content) {
            case "firefly":
                tooltip.add(TextFormatting.GREEN + I18n.format("item.lumen.firefly_in_a_jar.tooltip"));
                break;
            case "psi_firefly":
                tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.lumen.psi_firefly_in_a_jar.tooltip"));
                break;
            case "lightning_bug":
                tooltip.add(TextFormatting.AQUA + I18n.format("item.lumen.lightning_bug_in_a_jar.tooltip"));
                break;
            case "ember":
                tooltip.add(TextFormatting.YELLOW + I18n.format("item.lumen.ember_in_a_jar.tooltip"));
                break;
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
*/
}