package ladysnake.illuminations.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.sortme.Living;
import net.minecraft.world.World;

public abstract class AbstractLightOrb extends MobEntityWithAi implements Living {

    public AbstractLightOrb(EntityType entityType, World worldIn) {
        super(entityType, worldIn);
        this.setSize(0.5F, 0.5F);
        this.setUnaffectedByGravity(true);
        this.yaw = (float) (Math.random() * 360.0D);
        this.velocityX = (Math.random() * 0.2 - 0.1) * 2.0F;
        this.velocityY = (Math.random() * 0.2) * 2.0F;
        this.velocityZ = (Math.random() * 0.2 - 0.1) * 2.0F;
    }

    @Override
    public void update() {
        super.update();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean isUnaffectedByGravity() {
        return true;
    }

    @Override
    public boolean doesCollide() {
        return false;
    }
//
//    @Override
//    public boolean doesEntityNotTriggerPressurePlate() {
//        return true;
//    }

}
