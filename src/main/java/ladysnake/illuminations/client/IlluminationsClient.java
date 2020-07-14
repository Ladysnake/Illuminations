package ladysnake.illuminations.client;

import ladysnake.illuminations.client.renders.entities.FireflyRender;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerRenders();
        registerColors();
        registerCutouts();
    }

    public static void registerRenders() {
        EntityRendererRegistry.INSTANCE.register(IlluminationsEntities.FIREFLY, (manager, context) -> new FireflyRender<>(manager));
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

    public static void registerCutouts() {
        BlockRenderLayerMap.INSTANCE.putBlock(IlluminationsBlocks.FIREFLY_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(IlluminationsBlocks.FIREFLY_TALL_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(IlluminationsBlocks.FIREFLY_IN_A_BOTTLE, RenderLayer.getCutout());
    }

}
