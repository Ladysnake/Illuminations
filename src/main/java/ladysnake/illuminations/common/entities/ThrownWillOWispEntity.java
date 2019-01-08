package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.util.HitResult;
import net.minecraft.world.World;

public class ThrownWillOWispEntity extends LivingThrownEntity {

    public ThrownWillOWispEntity(World world) {
        this(IlluminationsEntities.THROWN_WILL_O_WISP, world);
    }

    public ThrownWillOWispEntity(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    public ThrownWillOWispEntity(World world_1, LivingEntity livingEntity_1) {
        super(IlluminationsEntities.THROWN_WILL_O_WISP, livingEntity_1, world_1);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.entity != null) {
            int int_1 = 0;
            hitResult.entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)int_1);
        }

        if (!this.world.isClient) {
            this.invalidate();
            WillOWispEntity spawnedWisp = new WillOWispEntity(this.world);
            spawnedWisp.setPosition(this.x, this.y, this.z);
            this.world.createExplosion(this, this.x, this.y, this.z, 2f, true);
//            this.world.spawnEntity(spawnedWisp);
        }
    }

}
