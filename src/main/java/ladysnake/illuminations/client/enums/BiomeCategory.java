package ladysnake.illuminations.client.enums;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum BiomeCategory {
    FOREST("minecraft:forest", "minecraft:wooded_hills", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:birch_forest_hills", "minecraft:tall_birch_forest", "minecraft:tall_birch_hills", "minecraft:dark_forest", "minecraft:dark_forest_hills"),
    TAIGA("minecraft:taiga", "minecraft:taiga_hills", "minecraft:taiga_mountains", "minecraft:giant_tree_taiga", "minecraft:giant_tree_taiga_hills", "minecraft:giant_spruce_taiga", "minecraft:giant_spruce_taiga_hills"),
    SNOWY("minecraft:snowy_tundra", "minecraft:snowy_mountains", "minecraft:ice_spikes", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills", "minecraft:snowy_taiga_mountains", "minecraft:frozen_river", "minecraft:frozen_ocean", "minecraft:deep_frozen_ocean"),
    PLAINS("minecraft:plains", "minecraft:sunflower_plains"),
    DESERT("minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"),
    SAVANNA("minecraft:savanna", "minecraft:savanna_plateau", "minecraft:shattered_savanna", "minecraft:shattered_savanna_plateau"),
    JUNGLE("minecraft:jungle", "minecraft:jungle_hills", "minecraft:modified_jungle", "minecraft:jungle_edge", "minecraft:modified_jungle_edge", "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills"),
    BEACH("minecraft:beach", "minecraft:stone_shore"),
    SWAMP("minecraft:swamp", "minecraft:swamp_hills"),
    RIVER("minecraft:river"),
    OCEAN("minecraft:ocean", "minecraft:deep_ocean", "minecraft:cold_ocean", "minecraft:deep_cold_ocean"),
    WARM_OCEAN("minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:warm_ocean", "minecraft:deep_warm_ocean"),
    THE_NETHER("minecraft:nether_wastes", "minecraft:crimson_forest", "minecraft:warped_forest", "minecraft:soul_sand_valley", "minecraft:basalt_deltas"),
    THE_END("minecraft:the_end", "minecraft:small_end_islands", "minecraft:end_midlands", "minecraft:end_highlands", "minecraft:end_barrens", "minecraft:the_void"),
    BADLANDS("minecraft:badlands", "minecraft:badlands_plateau", "minecraft:modified_badlands_plateau", "minecraft:wooded_badlands_plateau", "minecraft:modified_wooded_badlands_plateau", "minecraft:eroded_badlands"),
    MOUNTAINS("minecraft:mountains", "minecraft:wooded_mountains", "minecraft:gravelly_mountains", "minecraft:modified_gravelly_mountains", "minecraft:mountain_edge"),
    DRIPSTONE_CAVES("minecraft:dripstone_caves"),
    LUSH_CAVES("minecraft:lush_caves"),
    MUSHROOM("minecraft:mushroom_fields", "minecraft:mushroom_field_shore"),
    OTHER();

    private final Identifier[] values;
    private static final Map<Identifier, BiomeCategory> lookUp = Collections.unmodifiableMap(initializeLookUp());

    BiomeCategory(String ...values) {
        this.values = new Identifier[values.length];
        for (int i = 0; i < values.length; i++)
            this.values[i] = new Identifier(values[i]);
    }

    public Identifier[] getValues() {
        return values;
    }

    private static Map<Identifier, BiomeCategory> initializeLookUp() {
        Map<Identifier, BiomeCategory> lookUp = new HashMap<>();
        for (BiomeCategory category : BiomeCategory.values()) {
            for (Identifier biome : category.values) {
                lookUp.put(biome, category);
            }
        }
        return lookUp;
    }

    public static BiomeCategory find(Identifier biome) {
        return lookUp.getOrDefault(biome, OTHER);
    }
}
