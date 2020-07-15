package ladysnake.illuminations.client;

import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.renders.entities.FireflyRender;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static DefaultParticleType FIREFLY;

    @Override
    public void onInitializeClient() {
        registerRenders();
        registerColors();
        registerCutouts();

        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple());
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);
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
