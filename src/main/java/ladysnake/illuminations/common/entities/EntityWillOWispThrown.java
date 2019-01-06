package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.util.HitResult;
import net.minecraft.world.World;

public class EntityWillOWispThrown extends ThrownEntity {

    public EntityWillOWispThrown(World world) {
        this(IlluminationsEntities.THROWN_WILL_O_WISP, world);
    }

    public EntityWillOWispThrown(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    public EntityWillOWispThrown(World world_1, LivingEntity livingEntity_1) {
        super(IlluminationsEntities.THROWN_WILL_O_WISP, livingEntity_1, world_1);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        this.world.spawnEntity(new EntityWillOWisp(this.world));
        this.destroy();
    }

    @Override
    protected void initDataTracker() {

    }

}
