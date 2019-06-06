package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class BloodbeetleEntity extends FireflyEntity {
    public static final double RANGE = 8.0;

    // Constructors
    public BloodbeetleEntity(EntityType entityType, World world) {
        super(entityType, world);
//        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    public BloodbeetleEntity(World world, double x, double y, double z) {
        this(IlluminationsEntities.BLOODBEETLE, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeContainer().register(EntityAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient && !this.dead) {
            // despawn if players are too far away
            boolean arePlayersNear = world.isPlayerInRange(this.x, this.y, this.z, 48);
            if (!arePlayersNear) this.remove();

            // despawn on daytime
            float tod = this.world.getLevelProperties().getTimeOfDay();
            if (tod >= 1010 && tod < 12990) {
                this.remove();
            }

            // die in fire
            if (this.isOnFire()) {
                this.remove();
            }

            PlayerEntity playerTarget = this.world.getClosestPlayer(this.x, this.y, this.z);

            if (playerTarget != null) {
                this.setTarget(
                        playerTarget.x + new Random().nextGaussian()*5,
                        playerTarget.y + playerTarget.getHeight()/2 + new Random().nextGaussian()*5,
                        playerTarget.z + new Random().nextGaussian()*5);
            }

            Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.1 / length);

            this.setVelocity(
                    (0.9) * getVelocity().x + (0.5) * targetVector.x,
                    (0.9) * getVelocity().y + (0.5) * targetVector.y,
                    (0.9) * getVelocity().z + (0.5) * targetVector.z
            );
            this.move(MovementType.SELF, this.getVelocity());
        }
    }

    @Override
    public void pushAway(Entity entityIn) {
        if (!(entityIn instanceof BloodbeetleEntity) && entityIn instanceof LivingEntity) {
            entityIn.damage(new EntityDamageSource("bloodbeetle", this), 1.0F);
        }
    }

    @Override
    public void pushAwayFrom(Entity entityIn) {
        this.pushAway(entityIn);
    }

}
