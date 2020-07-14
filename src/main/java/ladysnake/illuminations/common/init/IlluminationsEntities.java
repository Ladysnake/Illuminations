package ladysnake.illuminations.common.init;

import ladysnake.illuminations.common.entities.FireflyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;

public class IlluminationsEntities {

    public static EntityType<FireflyEntity> FIREFLY;

    public static void init() {
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.<FireflyEntity>create(SpawnGroup.AMBIENT, FireflyEntity::new)
        				  																				  .dimensions(EntityDimensions.changing(1.0f, 1.0f))
        				  																				  .trackable(64, 1, true)
        				  																				  .build());

        FabricDefaultAttributeRegistry.register(FIREFLY, MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0));
    }

}
