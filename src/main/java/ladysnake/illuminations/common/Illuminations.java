package ladysnake.illuminations.common;

import ladysnake.illuminations.common.items.GlowMealItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class Illuminations implements ModInitializer {
    public static final String MOD_ID = "illuminations";

    public static Block FIREFLY_IN_A_BOTTLE;
    public static Item GLOW_MEAL;

    @Override
    public void onInitialize() {
        GLOW_MEAL = registerItem(new GlowMealItem((new Item.Settings()).group(ItemGroup.MISC)), "glow_meal");
        FIREFLY_IN_A_BOTTLE = registerBlock(new LanternBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3F, 0.3F).sounds(BlockSoundGroup.GLASS).lightLevel(15)), "firefly_in_a_bottle");
    }

    public static Item registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, Illuminations.MOD_ID + ":" + name, item);
        return item;
    }

    private static Block registerBlock(Block block, String name) {
        return registerBlock(block, name, true);
    }

    private static Block registerBlock(Block    block, String name, boolean doItem) {
        Registry.register(Registry.BLOCK, Illuminations.MOD_ID + ":" + name, block);

        if (doItem) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(ItemGroup.DECORATIONS));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            registerItem(item, name);
        }

        return block;
    }

}

