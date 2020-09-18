package ladysnake.illuminations.common;

import ladysnake.illuminations.common.entities.BugBallEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class Illuminations implements ModInitializer {
    public static String MODID = "illuminations";

    public static EntityType<BugBallEntity> BUGBALL;

    @Override
    public void onInitialize() {
        BUGBALL = Registry.register(Registry.ENTITY_TYPE, MODID + ":bugball", FabricEntityTypeBuilder.<BugBallEntity>create(SpawnGroup.MISC, BugBallEntity::new).size(EntityDimensions.changing(0.25f, 0.25f)).trackable(64, 1, true).build());
        IlluminationsItems.registerItems();
    }
}
