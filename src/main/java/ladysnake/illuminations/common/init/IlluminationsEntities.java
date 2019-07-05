package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.entities.*;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class IlluminationsEntities {

    public static EntityType<FireflyEntity> FIREFLY;
    public static EntityType<LightningBugEntity> LIGHTNING_BUG;
    public static EntityType<FairyEntity> FAIRY;
    public static EntityType<WillOWispEntity> WILL_O_WISP;
    public static EntityType<TamedWispEntity> TAMED_WISP;

    // block entities
    public static BlockEntityType<FairyBellBlockEntity> FAIRY_BELL;

    public static void init() {
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.<FireflyEntity>create(EntityCategory.AMBIENT, FireflyEntity::new).size(EntityDimensions.changing(1.0f, 1.0f)).trackable(64, 1, true).build());

        WILL_O_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:will_o_wisp", FabricEntityTypeBuilder.<WillOWispEntity>create(EntityCategory.AMBIENT, WillOWispEntity::new).size(EntityDimensions.changing(0.5f, 0.5f)).trackable(64, 1, true).build());
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() == Biome.Category.SWAMP) {
                biome.getEntitySpawnList(EntityCategory.CREATURE).add(new Biome.SpawnEntry(WILL_O_WISP, 20, 1, 1));
            }
        }

        FAIRY = Registry.register(Registry.ENTITY_TYPE, "illuminations:fairy", FabricEntityTypeBuilder.<FairyEntity>create(EntityCategory.AMBIENT, FairyEntity::new).size(EntityDimensions.changing(0.4f, 0.4f)).trackable(64, 1, true).build());

        TAMED_WISP = Registry.register(Registry.ENTITY_TYPE, "illuminations:tamed_wisp", FabricEntityTypeBuilder.<TamedWispEntity>create(EntityCategory.MISC, TamedWispEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());

        // block entities
        FAIRY_BELL = Registry.register(Registry.BLOCK_ENTITY, "illuminations:fairy_bell", BlockEntityType.Builder.create(FairyBellBlockEntity::new, IlluminationsBlocks.FAIRY_BELL).build(null));

    }

}
