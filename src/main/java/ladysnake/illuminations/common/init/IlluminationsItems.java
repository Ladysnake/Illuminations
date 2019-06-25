package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsItems {

    public static Item WILL_O_WISP;
    public static Item BUG_NET;
    public static Item FIREFLY;
    public static Item GLOW_MEAL;
    public static Item TAMED_WISP_SUMMONER;
    public static Item GOLDEN_WILL_SUMMONER;

    public static void init() {
        WILL_O_WISP = registerItem(new WillOWispItem((new Item.Settings())), "will_o_wisp");
        BUG_NET = registerItem(new BugNetItem((new Item.Settings()).durability(238).itemGroup(ItemGroup.TOOLS)), "bug_net");
        FIREFLY = registerItem(new FireflyItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "firefly");
        GLOW_MEAL = registerItem(new GlowMealItem((new Item.Settings()).itemGroup(ItemGroup.MISC)), "glow_meal");
//        TAMED_WISP_SUMMONER = registerItem(new TamedWispSummonerItem((new Item.Settings()).itemGroup(ItemGroup.MISC), "tamed_wisp"), "tamed_wisp_summoner");
//        GOLDEN_WILL_SUMMONER = registerItem(new TamedWispSummonerItem((new Item.Settings()).itemGroup(ItemGroup.MISC), "golden_will"), "golden_will_summoner");
    }

    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Illuminations.MOD_ID + ":" + name, item);
        return item;
    }

}