package ladysnake.lightorbs.common.entities;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.awt.*;

@Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo", striprefs = true)
public class EntitySolarOrb extends AbstractCompanionOrb implements ILightProvider {

    public EntitySolarOrb(World world) {
        super(world);
    }

    public EntitySolarOrb(World world, double x, double y, double z, EntityPlayer player) {
        super(world, x, y, z, player.getUniqueID());
    }

    @Override
    public Light provideLight() {
        return Light.builder().pos(this).radius(8).color(243, 126, 74, 0.01f).build();
    }

}