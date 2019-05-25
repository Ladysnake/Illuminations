package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsBlocks {

//    public static Block FIREFLY_NEST;
//    public static Block LIGHTNING_BUG_NEST;
    public static Block FIREFLY_IN_A_BOTTLE;
    public static Block GLOWWORM;


    public static void init() {
//        FIREFLY_NEST = registerBlock(new FireflyNestBlock(Block.Settings.of(Material.WOOD).strength(0.5F, 1.5F)), "firefly_nest", false);
//        LIGHTNING_BUG_NEST = registerBlock(new LightningBugNestBlock(Block.Settings.of(Material.WOOD).strength(0.5F, 1.5F)), "lightning_bug_nest", false);
        FIREFLY_IN_A_BOTTLE = registerBlock(new LanternBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3F, 0.3F).sounds(BlockSoundGroup.GLASS).lightLevel(10).build()), "firefly_in_a_bottle");
//        GLOWWORM = registerBlock(new GlowwormBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.SLIME).lightLevel(5).build()), "glowworm");
    }

    private static Block registerBlock(Block block, String name) {
        return registerBlock(block, name, true);
    }

    private static Block registerBlock(Block block, String name, boolean doItem) {
        Registry.register(Registry.BLOCK, Illuminations.MOD_ID + ":" + name, block);

        if (doItem) {
            BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(ItemGroup.DECORATIONS));
            item.registerBlockItemMap(Item.BLOCK_ITEM_MAP, item);
            IlluminationsItems.registerItem(item, name);
        }

        return block;
    }

}