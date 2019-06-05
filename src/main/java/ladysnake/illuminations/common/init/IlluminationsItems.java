package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.items.BugNetItem;
import ladysnake.illuminations.common.items.FireflyItem;
import ladysnake.illuminations.common.items.FireflyPouchItem;
import ladysnake.illuminations.common.items.WillOWispItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsItems {

    public static Item WILL_O_WISP;
    public static Item BUG_NET;
    public static Item FIREFLY;
    public static Item FIREFLY_POUCH;

    public static void init() {
        WILL_O_WISP = registerItem(new WillOWispItem((new Item.Settings())), "will_o_wisp");
        BUG_NET = registerItem(new BugNetItem((new Item.Settings()).durability(238).itemGroup(ItemGroup.TOOLS)), "bug_net");
        FIREFLY = registerItem(new FireflyItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "firefly");
        FIREFLY_POUCH = registerItem(new FireflyPouchItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "firefly_pouch");
    }

    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Illuminations.MOD_ID + ":" + name, item);
        return item;
    }

}