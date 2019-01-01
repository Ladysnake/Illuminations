package ladysnake.illuminations.common.init;

import ladysnake.illuminations.client.renders.entities.RenderFirefly;
import ladysnake.illuminations.common.entities.EntityFirefly;
import net.fabricmc.fabric.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class IlluminationsEntities {

    public static EntityType<EntityFirefly> FIREFLY;

    public static void init() {
        FIREFLY = Registry.register(Registry.ENTITY_TYPE, "illuminations:firefly", FabricEntityTypeBuilder.create(EntityFirefly.class, EntityFirefly::new).build());
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(EntityFirefly.class, (manager, context) -> new RenderFirefly(manager));
    }

}
