package ladysnake.illuminations.client;

import ladysnake.illuminations.client.particle.FireflyParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {
    public static DefaultParticleType FIREFLY;

    @Override
    public void onInitializeClient() {
        FIREFLY = Registry.register(Registry.PARTICLE_TYPE, "illuminations:firefly", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(IlluminationsClient.FIREFLY, FireflyParticle.DefaultFactory::new);
    }

}
