package ladysnake.lumen.common.entities;

import ladysnake.lumen.common.config.LumenConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class EntityEmber extends EntityFirefly {

    public EntityEmber(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);

        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;
    }

    public EntityEmber(World worldIn) {
        super(worldIn);

        this.colorModifier = 0.5F + new Random().nextFloat() * 0.5F;

        this.isAttractedByLight = false;
        this.despawnOnDaytime = false;
    }

    @Override
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        return LumenConfig.spawnEmbers && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }

    @Override
    public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
        return source == DamageSource.LAVA || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.HOT_FLOOR;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getHealth() > 0.0F) {
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());

            for (Entity entity : list) {
                if (!entity.isDead) {
                    entity.setFire(2);
                }
            }
        }
    }


}
