package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.blocks.FireflyGrassBlock;
import ladysnake.illuminations.common.blocks.FireflyTallGrassBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class IlluminationsBlocks {

    public static Block FIREFLY_IN_A_BOTTLE;
    public static Block FIREFLY_GRASS;
    public static Block FIREFLY_TALL_GRASS;
    public static Block FAIRY_BELL;

    public static void init() {
        FIREFLY_IN_A_BOTTLE = registerBlock(new LanternBlock(FabricBlockSettings.of(Material.GLASS).strength(0.3F, 0.3F).sounds(BlockSoundGroup.GLASS).lightLevel(10).build()), "firefly_in_a_bottle");
        FIREFLY_GRASS = registerBlock(new FireflyGrassBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).lightLevel(1).build()), "firefly_grass");
        FIREFLY_TALL_GRASS = registerBlock(new FireflyTallGrassBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).lightLevel(1).build()), "firefly_tall_grass");
        // registering firefly grass flammability
        ((FireBlock)Blocks.FIRE).registerFlammableBlock(FIREFLY_GRASS, 60, 100);
        ((FireBlock)Blocks.FIRE).registerFlammableBlock(FIREFLY_TALL_GRASS, 60, 100);
        FAIRY_BELL = registerBlock(new FlowerBlock(StatusEffects.INSTANT_HEALTH, 1, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).build()), "fairy_bell");

//      GLOWWORM = registerBlock(new GlowwormBlock(FabricBlockSettings.of(Material.REPLACEABLE_PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.SLIME).lightLevel(5).build()), "glowworm");
    }

    private static Block registerBlock(Block block, String name) {
        return registerBlock(block, name, true);
    }

    private static Block registerBlock(Block    block, String name, boolean doItem) {
        Registry.register(Registry.BLOCK, Illuminations.MOD_ID + ":" + name, block);

        if (doItem) {
            BlockItem item = new BlockItem(block, new Item.Settings().group(ItemGroup.DECORATIONS));
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            IlluminationsItems.registerItem(item, name);
        }

        return block;
    }

}