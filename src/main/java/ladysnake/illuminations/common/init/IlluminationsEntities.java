package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.entities.LightningBugEntity;
import ladysnake.illuminations.common.entities.WillOWispEntity;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.biome.Biome.Category.*;

public class IlluminationsEntities {

    public static EntityType<FireflyEntity> FIREFLY;
    public static EntityType<LightningBugEntity> LIGHTNING_BUG;
    public static EntityType<WillOWispEntity> WILL_O_WISP;

    public static void init() {
        // Firefly + firefly spawns
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.create(FireflyEntity.class, FireflyEntity::new).trackable(64, 1, true).build());
        List<Biome.Category> fireflyAcceptableCategories = Arrays.asList(PLAINS, SWAMP, FOREST, JUNGLE, SAVANNA, RIVER);
        for (Biome biome : Registry.BIOME) {
            if (fireflyAcceptableCategories.contains(biome.getCategory())) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(FIREFLY, 40, 6, 24));
            }
        }
        // LB + LB spawns
        LIGHTNING_BUG = Registry.register(Registry.ENTITY_TYPE, "illuminations:lightning_bug", FabricEntityTypeBuilder.create(LightningBugEntity.class, LightningBugEntity::new).trackable(64, 1, true).build());
        for (Biome biome : Registry.BIOME) {
            if (fireflyAcceptableCategories.contains(biome.getCategory())) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(LIGHTNING_BUG, 40, 6, 24));
            }
        }
        // WoW + thrown WoW + WoW spawns
        WILL_O_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:will_o_wisp", FabricEntityTypeBuilder.create(WillOWispEntity.class, WillOWispEntity::new).trackable(64, 1, true).build());
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() == Biome.Category.SWAMP) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(WILL_O_WISP, 20, 1, 1));
            }
        }
    }

}
