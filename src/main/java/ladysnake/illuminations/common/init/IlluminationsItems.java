package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.items.FireflyItem;
import ladysnake.illuminations.common.items.GlowMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsItems {

    public static Item FIREFLY;
    public static Item GLOW_MEAL;

    public static void init() {
        FIREFLY = registerItem(new FireflyItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "firefly");
        GLOW_MEAL = registerItem(new GlowMealItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "glow_meal");
    }

    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Illuminations.MOD_ID + ":" + name, item);
        return item;
    }

}