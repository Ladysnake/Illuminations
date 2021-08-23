package ladysnake.illuminations.client.enums;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public enum BiomeCategory {
    FOREST("minecraft:overworld", "minecraft:forest", "minecraft:wooded_hills", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:birch_forest_hills", "minecraft:tall_birch_forest", "minecraft:tall_birch_hills", "minecraft:dark_forest", "minecraft:dark_forest_hills"),
    TAIGA("minecraft:overworld", "minecraft:taiga", "minecraft:taiga_hills", "minecraft:taiga_mountains", "minecraft:giant_tree_taiga", "minecraft:giant_tree_taiga_hills", "minecraft:giant_spruce_taiga", "minecraft:giant_spruce_taiga_hills"),
    SNOWY("minecraft:overworld", "minecraft:snowy_tundra", "minecraft:snowy_mountains", "minecraft:ice_spikes", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills", "minecraft:snowy_taiga_mountains", "minecraft:frozen_river", "minecraft:frozen_ocean", "minecraft:deep_frozen_ocean"),
    PLAINS("minecraft:overworld", "minecraft:plains", "minecraft:sunflower_plains"),
    DESERT("minecraft:overworld", "minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"),
    SAVANNA("minecraft:overworld", "minecraft:savanna", "minecraft:savanna_plateau", "minecraft:shattered_savanna", "minecraft:shattered_savanna_plateau"),
    JUNGLE("minecraft:overworld", "minecraft:jungle", "minecraft:jungle_hills", "minecraft:modified_jungle", "minecraft:jungle_edge", "minecraft:modified_jungle_edge", "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills"),
    BEACH("minecraft:overworld", "minecraft:beach", "minecraft:stone_shore"),
    SWAMP("minecraft:overworld", "minecraft:swamp", "minecraft:swamp_hills"),
    RIVER("minecraft:overworld", "minecraft:river"),
    OCEAN("minecraft:overworld", "minecraft:ocean", "minecraft:deep_ocean", "minecraft:cold_ocean", "minecraft:deep_cold_ocean"),
    WARM_OCEAN("minecraft:overworld", "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:warm_ocean", "minecraft:deep_warm_ocean"),
    BADLANDS("minecraft:overworld", "minecraft:badlands", "minecraft:badlands_plateau", "minecraft:modified_badlands_plateau", "minecraft:wooded_badlands_plateau", "minecraft:modified_wooded_badlands_plateau", "minecraft:eroded_badlands"),
    MOUNTAINS("minecraft:overworld", "minecraft:mountains", "minecraft:wooded_mountains", "minecraft:gravelly_mountains", "minecraft:modified_gravelly_mountains", "minecraft:mountain_edge"),
    DRIPSTONE_CAVES("minecraft:overworld", "minecraft:dripstone_caves"),
    LUSH_CAVES("minecraft:overworld", "minecraft:lush_caves"),
    MUSHROOM("minecraft:overworld", "minecraft:mushroom_fields", "minecraft:mushroom_field_shore"),
    // Nether Biomes
    NETHER_WASTES("minecraft:the_nether", "minecraft:nether_wastes"),
    CRIMSON_FOREST("minecraft:the_nether", "minecraft:crimson_forest"),
    WARPED_FOREST("minecraft:the_nether", "minecraft:warped_forest"),
    SOUL_SAND_VALLEY("minecraft:the_nether", "minecraft:soul_sand_valley"),
    BASALT_DELTAS("minecraft:the_nether", "minecraft:basalt_deltas"),
    // End Biomes
    THE_END("minecraft:the_end", "minecraft:the_end"),
    SMALL_END_ISLANDS("minecraft:the_end", "minecraft:small_end_islands"),
    END_MIDLANDS("minecraft:the_end", "minecraft:end_midlands"),
    END_HIGHLANDS("minecraft:the_end", "minecraft:end_highlands"),
    END_BARRENS("minecraft:the_end", "minecraft:end_barrens"),
    THE_VOID("minecraft:the_end", "minecraft:the_void"),
    // Other Biomes
    OTHER("minecraft:other");

    private final Identifier dimension;
    private final Identifier[] biomes;
    private static final Set<Identifier> dimensions = Collections.unmodifiableSet(initializeDimensions());
    private static final Map<Identifier, BiomeCategory> lookUp = Collections.unmodifiableMap(initializeLookUp());

    BiomeCategory(String dimension, String ... biomes) {
        this.dimension = new Identifier(dimension);
        this.biomes = new Identifier[biomes.length];
        for (int i = 0; i < biomes.length; i++)
            this.biomes[i] = new Identifier(biomes[i]);
    }

    public static Set<Identifier> getDimensions() {
        return dimensions;
    }

    public Identifier getDimension() {
        return dimension;
    }

    public Identifier[] getBiomes() {
        return biomes;
    }

    private static Set<Identifier> initializeDimensions() {
        Set<Identifier> dimensions = new LinkedHashSet<>();
        for (BiomeCategory category : BiomeCategory.values()) {
            dimensions.add(category.dimension);
        }
        return dimensions;
    }

    private static Map<Identifier, BiomeCategory> initializeLookUp() {
        Map<Identifier, BiomeCategory> lookUp = new HashMap<>();
        for (BiomeCategory category : BiomeCategory.values()) {
            for (Identifier biome : category.biomes) {
                lookUp.put(biome, category);
            }
        }
        return lookUp;
    }

    public static BiomeCategory find(Identifier biome) {
        return lookUp.getOrDefault(biome, OTHER);
    }
}
