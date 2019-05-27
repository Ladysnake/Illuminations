package ladysnake.illuminations.client;

import ladysnake.illuminations.client.renders.entities.FireflyRender;
import ladysnake.illuminations.client.renders.entities.LightningBugRender;
import ladysnake.illuminations.client.renders.entities.WillOWispRender;
import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.entities.LightningBugEntity;
import ladysnake.illuminations.common.entities.WillOWispEntity;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import ladysnake.illuminations.common.init.IlluminationsItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerRenders();
        registerColors();
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(FireflyEntity.class, (manager, context) -> new FireflyRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(LightningBugEntity.class, (manager, context) -> new LightningBugRender<>(manager));
        EntityRendererRegistry.INSTANCE.register(WillOWispEntity.class, (manager, context) -> new WillOWispRender<>(manager));
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
