package ladysnake.lumen.common.entities;

import elucent.albedo.lighting.ILightProvider;
import ladysnake.lumen.common.config.LumenConfig;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo", striprefs = true)
public class EntityRadFlame extends EntityWillOWisp implements ILightProvider {

    // Constructors
    public EntityRadFlame(World world) {
        super(world);
        this.setSize(0.5f, 0.5f);
    }

    public EntityRadFlame(World world, double x, double y, double z) {
        this(world);
        this.setPosition(x, y, z);
    }

    // Properties
    @Override
    public boolean getCanSpawnHere() {
        return LumenConfig.spawnWillOWisps;
    }

    // Behaviour
    @Override
    public elucent.albedo.lighting.Light provideLight() {
        return elucent.albedo.lighting.Light.builder().pos(this).radius(10).color(255, 0, 50, 0.01f).build();
    }

}
