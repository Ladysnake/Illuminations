package ladysnake.illuminations.client;

import ladysnake.illuminations.client.renders.entities.RenderFirefly;
import ladysnake.illuminations.client.renders.entities.RenderLightningBug;
import ladysnake.illuminations.client.renders.entities.RenderWillOWisp;
import ladysnake.illuminations.common.entities.EntityFirefly;
import ladysnake.illuminations.common.entities.EntityLightningBug;
import ladysnake.illuminations.common.entities.EntityWillOWisp;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.client.render.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerRenders();
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(EntityFirefly.class, (manager, context) -> new RenderFirefly<>(manager));
        EntityRendererRegistry.INSTANCE.register(EntityLightningBug.class, (manager, context) -> new RenderLightningBug<>(manager));
        EntityRendererRegistry.INSTANCE.register(EntityWillOWisp.class, (manager, context) -> new RenderWillOWisp<>(manager));
    }

}
