package ladysnake.illuminations.common.init;

import ladysnake.illuminations.client.renders.entities.RenderFirefly;
import ladysnake.illuminations.common.entities.EntityFirefly;
import net.fabricmc.fabric.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

public class IlluminationsEntities {

    public static EntityType<EntityFirefly> FIREFLY;

    public static void init() {
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.create(EntityFirefly.class, EntityFirefly::new).trackable(64, 1, true).build());
        Biome.SpawnEntry fireflySpawnEntry = new Biome.SpawnEntry(FIREFLY, 10, 6, 24);
        Biomes.PLAINS.getEntitySpawnList(EntityCategory.CREATURE).add(fireflySpawnEntry);
        Biomes.SUNFLOWER_PLAINS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.SWAMP.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.SWAMP_HILLS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.FOREST.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.BIRCH_FOREST.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.DARK_FOREST.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.FLOWER_FOREST.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.TALL_BIRCH_FOREST.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.BIRCH_FOREST_HILLS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.DARK_FOREST_HILLS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.RIVER.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.JUNGLE.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.JUNGLE_EDGE.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.JUNGLE_HILLS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.BAMBOO_JUNGLE.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.BAMBOO_JUNGLE_HILLS.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.MODIFIED_JUNGLE.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
        Biomes.MODIFIED_JUNGLE_EDGE.getEntitySpawnList(EntityCategory.AMBIENT).add(fireflySpawnEntry);
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(EntityFirefly.class, (manager, context) -> new RenderFirefly(manager));
    }

}
