package ladysnake.illuminations.client.enums;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.*;

public enum BiomeCategory {
    FOREST("minecraft:overworld", ConventionalBiomeTags.FOREST, "minecraft:forest", "minecraft:wooded_hills", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:birch_forest_hills", "minecraft:tall_birch_forest", "minecraft:tall_birch_hills", "minecraft:dark_forest", "minecraft:dark_forest_hills"),
    TAIGA("minecraft:overworld", ConventionalBiomeTags.TAIGA, "minecraft:taiga", "minecraft:taiga_hills", "minecraft:taiga_mountains", "minecraft:giant_tree_taiga", "minecraft:giant_tree_taiga_hills", "minecraft:giant_spruce_taiga", "minecraft:giant_spruce_taiga_hills"),
    SNOWY("minecraft:overworld", ConventionalBiomeTags.ICY, "minecraft:snowy_tundra", "minecraft:snowy_mountains", "minecraft:ice_spikes", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills", "minecraft:snowy_taiga_mountains", "minecraft:frozen_river", "minecraft:frozen_ocean", "minecraft:deep_frozen_ocean"),
    PLAINS("minecraft:overworld", ConventionalBiomeTags.PLAINS, "minecraft:plains", "minecraft:sunflower_plains"),
    DESERT("minecraft:overworld", ConventionalBiomeTags.DESERT, "minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"),
    SAVANNA("minecraft:overworld", ConventionalBiomeTags.SAVANNA, "minecraft:savanna", "minecraft:savanna_plateau", "minecraft:shattered_savanna", "minecraft:shattered_savanna_plateau"),
    JUNGLE("minecraft:overworld", ConventionalBiomeTags.JUNGLE, "minecraft:jungle", "minecraft:jungle_hills", "minecraft:modified_jungle", "minecraft:jungle_edge", "minecraft:modified_jungle_edge", "minecraft:bamboo_jungle", "minecraft:bamboo_jungle_hills"),
    BEACH("minecraft:overworld", ConventionalBiomeTags.BEACH, "minecraft:beach", "minecraft:stone_shore"),
    SWAMP("minecraft:overworld", ConventionalBiomeTags.SWAMP, "minecraft:swamp", "minecraft:swamp_hills", "minecraft:mangrove_swamp"),
    RIVER("minecraft:overworld", ConventionalBiomeTags.RIVER, "minecraft:river"),
    OCEAN("minecraft:overworld", ConventionalBiomeTags.OCEAN, "minecraft:ocean", "minecraft:deep_ocean", "minecraft:cold_ocean", "minecraft:deep_cold_ocean"),
    WARM_OCEAN("minecraft:overworld", null, "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:warm_ocean", "minecraft:deep_warm_ocean"),
    BADLANDS("minecraft:overworld", ConventionalBiomeTags.MESA, "minecraft:badlands", "minecraft:badlands_plateau", "minecraft:modified_badlands_plateau", "minecraft:wooded_badlands_plateau", "minecraft:modified_wooded_badlands_plateau", "minecraft:eroded_badlands"),
    MOUNTAINS("minecraft:overworld", ConventionalBiomeTags.EXTREME_HILLS, "minecraft:mountains", "minecraft:wooded_mountains", "minecraft:gravelly_mountains", "minecraft:modified_gravelly_mountains", "minecraft:mountain_edge"),
    DRIPSTONE_CAVES("minecraft:overworld", ConventionalBiomeTags.UNDERGROUND, "minecraft:dripstone_caves"),
    LUSH_CAVES("minecraft:overworld", null, "minecraft:lush_caves"),
    MUSHROOM("minecraft:overworld", ConventionalBiomeTags.MUSHROOM, "minecraft:mushroom_fields", "minecraft:mushroom_field_shore"),
    THE_VOID("minecraft:overworld", null, "minecraft:the_void"),
    // Nether Biomes
    NETHER_WASTES("minecraft:the_nether", ConventionalBiomeTags.IN_NETHER, "minecraft:nether_wastes"),
    CRIMSON_FOREST("minecraft:the_nether", null, "minecraft:crimson_forest"),
    WARPED_FOREST("minecraft:the_nether", null, "minecraft:warped_forest"),
    SOUL_SAND_VALLEY("minecraft:the_nether", null, "minecraft:soul_sand_valley"),
    BASALT_DELTAS("minecraft:the_nether", null, "minecraft:basalt_deltas"),
    // End Biomes
    THE_END("minecraft:the_end", ConventionalBiomeTags.IN_THE_END, "minecraft:the_end"),
    SMALL_END_ISLANDS("minecraft:the_end", null, "minecraft:small_end_islands"),
    END_MIDLANDS("minecraft:the_end", null, "minecraft:end_midlands"),
    END_HIGHLANDS("minecraft:the_end", null, "minecraft:end_highlands"),
    END_BARRENS("minecraft:the_end", null, "minecraft:end_barrens"),
    // Other Biomes
    OTHER("minecraft:other", ConventionalBiomeTags.IN_OVERWORLD);

    private static final Set<Identifier> dimensions = Collections.unmodifiableSet(initializeDimensions());
    private static final Map<Identifier, BiomeCategory> lookUp = Collections.unmodifiableMap(initializeLookUp());
    private static final Map<TagKey<Biome>, BiomeCategory> fallbackLookUp = Collections.unmodifiableMap(initializeFallbackLookUp());
    private final Identifier dimension;
    private final TagKey<Biome> fallback;
    private final Identifier[] biomes;

    BiomeCategory(String dimension, TagKey<Biome> fallback, String... biomes) {
        this.dimension = new Identifier(dimension);
        this.fallback = fallback;
        this.biomes = new Identifier[biomes.length];
        for (int i = 0; i < biomes.length; i++)
            this.biomes[i] = new Identifier(biomes[i]);
    }

    public static Set<Identifier> getDimensions() {
        return dimensions;
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

    private static Map<TagKey<Biome>, BiomeCategory> initializeFallbackLookUp() {
        Map<TagKey<Biome>, BiomeCategory> lookUp = new HashMap<>();
        for (BiomeCategory category : BiomeCategory.values()) {
            if (category.fallback != null) {
                lookUp.put(category.fallback, category);
            }
        }
        return lookUp;
    }

    public static BiomeCategory find(Identifier biome, TagKey<Biome> biomeCategory) {
        return lookUp.containsKey(biome)
                ? lookUp.get(biome)
                : fallbackLookUp.getOrDefault(biomeCategory, OTHER);
    }

    public Identifier getDimension() {
        return dimension;
    }

    public Identifier[] getBiomes() {
        return biomes;
    }
}
