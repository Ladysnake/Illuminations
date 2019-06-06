package ladysnake.illuminations.client;

import ladysnake.illuminations.client.renders.entities.BloodbeetleRender;
import ladysnake.illuminations.client.renders.entities.FireflyRender;
import ladysnake.illuminations.client.renders.entities.LightningBugRender;
import ladysnake.illuminations.client.renders.entities.WillOWispRender;
import ladysnake.illuminations.common.entities.*;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerRenders();
        registerColors();
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(FireflyEntity.class, (manager, context) -> new FireflyRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(BloodbeetleEntity.class, (manager, context) -> new BloodbeetleRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(LightningBugEntity.class, (manager, context) -> new LightningBugRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(WillOWispEntity.class, (manager, context) -> new WillOWispRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(ThrownPouchEntity.class, (manager, context) -> new FlyingItemEntityRenderer<>(manager, context.getItemRenderer()));
    }

    public static void registerColors() {
        ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> {
            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
            return provider == null ? -1 : provider.getColor(block, pos, world, layer);
        }, IlluminationsBlocks.FIREFLY_GRASS);
        ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> {
            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
            return provider == null ? -1 : provider.getColor(block, pos, world, layer);
        }, IlluminationsBlocks.FIREFLY_TALL_GRASS);
    }

}
