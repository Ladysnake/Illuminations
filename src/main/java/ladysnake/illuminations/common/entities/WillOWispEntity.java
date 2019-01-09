package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class WillOWispEntity extends ThrownLightOrbEntity {

    public WillOWispEntity(World world) {
        this(IlluminationsEntities.WILL_O_WISP, world);
    }

    public WillOWispEntity(EntityType entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public WillOWispEntity(World world, LivingEntity livingEntity) {
        super(IlluminationsEntities.WILL_O_WISP, livingEntity, world);
    }

    // Behaviour
    private double groundLevel;
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void update() {
        super.update();

        if (this.y > 300) this.kill();

        if (!this.world.isClient && !this.method_5686()) {
            this.targetChangeCooldown -= (this.getPosVector().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceToCenter(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
            double length = targetVector.length();
            targetVector = targetVector.multiply(0.5 / length);
            velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
            velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
            velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
            if (this.getPos() != this.getTargetPosition()) this.move(MovementType.SELF, this.velocityX, this.velocityY, this.velocityZ);
        }
    }

    private void selectBlockTarget() {
        this.groundLevel = 0;
        for (int i = 0; i < 20; i++) {
            if (!this.world.getBlockState(new BlockPos(this.x, this.y - i, this.z)).getBlock().canMobSpawnInside())
                this.groundLevel = this.y - i;
            if (this.groundLevel != 0) break;
        }

        this.xTarget = this.x + random.nextGaussian() * 10;
        this.yTarget = Math.min(Math.max(this.y + random.nextGaussian() * 2, this.groundLevel), this.groundLevel + 4);
        this.zTarget = this.z + random.nextGaussian() * 10;

        while (!this.world.getBlockState(new BlockPos(this.xTarget, this.yTarget, this.zTarget)).getBlock().canMobSpawnInside())
            this.yTarget += 1;

        targetChangeCooldown = random.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    @Override
    public boolean canSpawn(IWorld world, SpawnType spawnType) {
        return !this.world.isDaylight();
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.entity != null) {
            int int_1 = 0;
            hitResult.entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)int_1);
        }

        if (!this.world.isClient) {
            this.world.createExplosion(this, this.x, this.y, this.z, 2f, true);
            this.beingThrown = false;
            this.invalidate();
        }
    }

}
