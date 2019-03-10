package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import ladysnake.illuminations.common.init.IlluminationsItems;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class WillOWispEntity extends ThrownLightOrbEntity {

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

        if (!beingThrown) {
            if (this.y > 300) this.invalidate();

            if (!this.world.isClient && !this.dead) {
                this.targetChangeCooldown -= (this.getPosVector().squaredDistanceTo(prevX, prevY, prevZ) < 0.0125) ? 10 : 1;

                if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPos().squaredDistanceToCenter(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                    selectBlockTarget();
                }

                Vec3d targetVector = new Vec3d(this.xTarget - x, this.yTarget - y, this.zTarget - z);
                double length = targetVector.length();
                targetVector = targetVector.multiply(0.5 / length);
                this.setVelocity(
                        (0.9) * getVelocity().x + (0.1) * targetVector.x,
                        (0.9) * getVelocity().y + (0.1) * targetVector.y,
                        (0.9) * getVelocity().z + (0.1) * targetVector.z
                );
                if (this.getPos() != this.getTargetPosition())
                    this.move(MovementType.SELF, this.getVelocity());
            }
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
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity_1 = ((EntityHitResult)hitResult).getEntity();
            int int_1 = entity_1 instanceof BlazeEntity ? 3 : 0;
            entity_1.damage(DamageSource.thrownProjectile(this, this.getOwner()), (float)int_1);
        }

        if (!this.world.isClient) {
            this.beingThrown = false;
        }
    }

    @Override
    public ActionResult interactAt(PlayerEntity playerEntity, Vec3d vec3d, Hand hand) {
        this.invalidate();
        playerEntity.inventory.insertStack(new ItemStack(IlluminationsItems.WILL_O_WISP));
        return super.interactAt(playerEntity, vec3d, hand);
    }

}
