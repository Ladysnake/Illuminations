package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.items.BugNetItem;
import ladysnake.illuminations.common.items.FireflyItem;
import ladysnake.illuminations.common.items.GlowMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsItems {

    public static Item BUG_NET;
    public static Item FIREFLY;
    public static Item GLOW_MEAL;

    public static void init() {
        BUG_NET = registerItem(new BugNetItem((new Item.Settings()).maxDamage(238).group(ItemGroup.TOOLS)), "bug_net");
        FIREFLY = registerItem(new FireflyItem((new Item.Settings()).group(ItemGroup.MISC)), "firefly");
        GLOW_MEAL = registerItem(new GlowMealItem((new Item.Settings()).group(ItemGroup.MISC)), "glow_meal");
    }

    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Illuminations.MOD_ID + ":" + name, item);
        return item;
    }

}