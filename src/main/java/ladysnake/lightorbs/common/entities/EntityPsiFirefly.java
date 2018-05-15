package ladysnake.lightorbs.common.entities;

import ladysnake.lightorbs.common.config.LightOrbsConfig;
import net.minecraft.world.World;

import java.util.Random;

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
        if (LightOrbsConfig.spawnPsiFireflies && LightOrbsConfig.psiFireflySwarmMinSize > 0 && LightOrbsConfig.psiFireflySwarmMaxSize >= LightOrbsConfig.psiFireflySwarmMinSize) {
            int swarmSize = new Random().nextInt(LightOrbsConfig.psiFireflySwarmMaxSize - LightOrbsConfig.psiFireflySwarmMinSize) + LightOrbsConfig.psiFireflySwarmMinSize;
            for (int i = 0; i < swarmSize; i++)
                this.world.spawnEntity(new EntityPsiFirefly(this.world, this.posX, this.posY, this.posZ));
            this.setDead();
            return true;
        } else return false;
    }

}
