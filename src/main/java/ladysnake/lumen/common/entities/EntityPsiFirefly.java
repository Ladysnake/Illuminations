package ladysnake.lumen.common.entities;

import ladysnake.lumen.common.config.LumenConfig;
import net.minecraft.world.World;

public class EntityPsiFirefly extends EntityFirefly {

    public EntityPsiFirefly(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityPsiFirefly(World worldIn) {
        super(worldIn);

        this.despawnOnDaytime = false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        return LumenConfig.spawnPsiFireflies && super.getCanSpawnHere();
    }

}
