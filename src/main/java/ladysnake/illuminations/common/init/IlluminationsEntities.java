package ladysnake.illuminations.common.init;

import ladysnake.illuminations.client.renders.entities.RenderFirefly;
import ladysnake.illuminations.common.entities.EntityFirefly;
import net.fabricmc.fabric.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.biome.Biome.Category.*;

public class IlluminationsEntities {

    public static EntityType<EntityFirefly> FIREFLY;

    public static void init() {
        // Firefly + firefly spawns
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.create(EntityFirefly.class, EntityFirefly::new).trackable(64, 1, true).build());
        List<Biome.Category> acceptableCategories = Arrays.asList(PLAINS, SWAMP, FOREST, JUNGLE, SAVANNA, RIVER);
        for (Biome biome : Registry.BIOME) {
            if (acceptableCategories.contains(biome.getCategory())) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(FIREFLY, 40, 6, 24));
            }
        }
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(EntityFirefly.class, (manager, context) -> new RenderFirefly(manager));
    }

}
