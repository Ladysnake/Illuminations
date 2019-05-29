package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.entities.LightningBugEntity;
import ladysnake.illuminations.common.entities.WillOWispEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class IlluminationsEntities {

    public static EntityType<FireflyEntity> FIREFLY;
    public static EntityType<LightningBugEntity> LIGHTNING_BUG;
    public static EntityType<WillOWispEntity> WILL_O_WISP;

    public static void init() {
        // Firefly + firefly spawns
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.<FireflyEntity>create(EntityCategory.AMBIENT, FireflyEntity::new).size(EntitySize.resizeable(1.0f, 1.0f)).trackable(64, 1, true).build());

        // LB + LB spawns
//        LIGHTNING_BUG = Registry.register(Registry.ENTITY_TYPE, "illuminations:lightning_bug", FabricEntityTypeBuilder.<LightningBugEntity>create(EntityCategory.AMBIENT, LightningBugEntity::new).size(EntitySize.resizeable(1.0f, 1.0f)).trackable(64, 1, true).build());
//        for (Biome biome : Registry.BIOME) {
//            if (fireflyAcceptableCategories.contains(biome.getCategory())) {
//                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(LIGHTNING_BUG, 40, 6, 24));
//            }
//        }

        // WoW + thrown WoW + WoW spawns
        WILL_O_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:will_o_wisp", FabricEntityTypeBuilder.<WillOWispEntity>create(EntityCategory.AMBIENT, WillOWispEntity::new).size(EntitySize.resizeable(0.5f, 0.5f)).trackable(64, 1, true).build());
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() == Biome.Category.SWAMP) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(WILL_O_WISP, 20, 1, 1));
            }
        }
    }

}
