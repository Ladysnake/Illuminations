package ladysnake.illuminations.client;

import ladysnake.illuminations.client.renders.entities.FireflyRender;
import ladysnake.illuminations.client.renders.entities.LightningBugRender;
import ladysnake.illuminations.client.renders.entities.WillOWispRender;
import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.entities.LightningBugEntity;
import ladysnake.illuminations.common.entities.WillOWispEntity;
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
        EntityRendererRegistry.INSTANCE.register(FireflyEntity.class, (manager, context) -> new FireflyRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(LightningBugEntity.class, (manager, context) -> new LightningBugRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(WillOWispEntity.class, (manager, context) -> new WillOWispRender<>(manager));
    }

}
