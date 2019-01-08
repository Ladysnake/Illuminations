package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.entities.EntityFirefly;
import ladysnake.illuminations.common.entities.EntityLightningBug;
import ladysnake.illuminations.common.entities.EntityWillOWisp;
import ladysnake.illuminations.common.entities.EntityWillOWispThrown;
import net.fabricmc.fabric.entity.EntityTrackingRegistry;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.minecraft.client.network.packet.EntitySpawnClientPacket;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.biome.Biome.Category.*;

public class IlluminationsEntities {

    public static EntityType<EntityFirefly> FIREFLY;
    public static EntityType<EntityLightningBug> LIGHTNING_BUG;
    public static EntityType<EntityWillOWisp> WILL_O_WISP;
    public static EntityType<EntityWillOWispThrown> THROWN_WILL_O_WISP;

    public static void init() {
        // Firefly + firefly spawns
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.create(EntityFirefly.class, EntityFirefly::new).trackable(64, 1, true).build());
        List<Biome.Category> fireflyAcceptableCategories = Arrays.asList(PLAINS, SWAMP, FOREST, JUNGLE, SAVANNA, RIVER);
        for (Biome biome : Registry.BIOME) {
            if (fireflyAcceptableCategories.contains(biome.getCategory())) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(FIREFLY, 40, 6, 24));
            }
        }
        // LB + LB spawns
        LIGHTNING_BUG = Registry.register(Registry.ENTITY_TYPE, "illuminations:lightning_bug", FabricEntityTypeBuilder.create(EntityLightningBug.class, EntityLightningBug::new).trackable(64, 1, true).build());
        for (Biome biome : Registry.BIOME) {
            if (fireflyAcceptableCategories.contains(biome.getCategory())) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(LIGHTNING_BUG, 40, 6, 24));
            }
        }
        // WoW + thrown WoW + WoW spawns
        WILL_O_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:will_o_wisp", FabricEntityTypeBuilder.create(EntityWillOWisp.class, EntityWillOWisp::new).trackable(64, 1, true).build());
        THROWN_WILL_O_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:thrown_will_o_wisp", FabricEntityTypeBuilder.create(EntityWillOWispThrown.class, EntityWillOWispThrown::new).trackable(64, 1, true).build());
        EntityTrackingRegistry.INSTANCE.registerSpawnPacketProvider(THROWN_WILL_O_WISP, entity -> new EntitySpawnClientPacket(entity, Registry.ENTITY_TYPE.getRawId(THROWN_WILL_O_WISP)));
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() == Biome.Category.SWAMP) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(WILL_O_WISP, 20, 1, 1));
            }
        }
    }

}
