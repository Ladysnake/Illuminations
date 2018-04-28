package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum HorseArmorType
{
    NONE(0),
    IRON(5, "iron", "meo"),
    GOLD(7, "gold", "goo"),
    DIAMOND(11, "diamond", "dio");

    private final String textureName;
    private final String hash;
    private final int protection;

    private HorseArmorType(int armorStrengthIn)
    {
        this.protection = armorStrengthIn;
        this.textureName = null;
        this.hash = "";
    }

    private HorseArmorType(int armorStrengthIn, String p_i46800_4_, String p_i46800_5_)
    {
        this.protection = armorStrengthIn;
        this.textureName = "textures/entity/horse/armor/horse_armor_" + p_i46800_4_ + ".png";
        this.hash = p_i46800_5_;
    }

    public int getOrdinal()
    {
        return this.ordinal();
    }

    @SideOnly(Side.CLIENT)
    public String getHash()
    {
        return this.hash;
    }

    public int getProtection()
    {
        return this.protection;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getTextureName()
    {
        return this.textureName;
    }

    @Deprecated /**Forge: Use getByName. Ordinals of mod-added enum constants are dependent on load order, unlike names.**/
    public static HorseArmorType getByOrdinal(int ordinal)
    {
        return values()[ordinal];
    }

    public static HorseArmorType getByItemStack(ItemStack stack)
    {
        return stack.getItem().getHorseArmorType(stack);
    }

    @Deprecated //Forge: Use getByItemStack
    public static HorseArmorType getByItem(Item itemIn)
    {
        if (itemIn == Items.IRON_HORSE_ARMOR)
        {
            return IRON;
        }
        else if (itemIn == Items.GOLDEN_HORSE_ARMOR)
        {
            return GOLD;
        }
        else
        {
            return itemIn == Items.DIAMOND_HORSE_ARMOR ? DIAMOND : NONE;
        }
    }

    @Deprecated //Forge: Use ItemStack sensitive overload
    public static boolean isHorseArmor(Item itemIn)
    {
        return getByItem(itemIn) != NONE;
    }
    
    /* ======================================== FORGE START ======================================== */
    //Allows for textures located outside the vanilla horse armor folder
    private HorseArmorType(String defaultTextureLocation, int armorStrengthIn)
    {
        this.protection = armorStrengthIn;
        this.textureName = defaultTextureLocation;
        this.hash = "forge";
    }
    
    public static HorseArmorType getByName(String name)
    {
        HorseArmorType type = HorseArmorType.valueOf(name);
        return type != null ? type : NONE;
    }
    
    public static boolean isHorseArmor(ItemStack stack)
    {
        return getByItemStack(stack) != NONE;
    }
    /* ======================================== FORGE END ======================================== */
}