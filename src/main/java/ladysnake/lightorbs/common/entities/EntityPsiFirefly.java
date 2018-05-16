package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.config.LightOrbsConfig;
import net.minecraft.world.World;

public class EntityPsiFirefly extends EntityFirefly {

    public EntityPsiFirefly(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityPsiFirefly(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        return LightOrbsConfig.spawnPsiFireflies && super.getCanSpawnHere();
    }

}
