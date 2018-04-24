package ladysnake.lightorbs.common.init;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CommonProxy {

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public void spawnParticle(World world, float x, float y, float z, float vx, float vy, float vz, int r, int g, int b, int a, float scale, int lifetime) {
    }

    public abstract Side getSide();
}
