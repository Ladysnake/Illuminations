package ladysnake.lightorbs.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public abstract class AbstractCompanionOrb extends AbstractLightOrb {
    // Attributes
    private UUID ownerUUID;

    // Constructors
    public AbstractCompanionOrb(World world) {
        super(world);
    }

    public AbstractCompanionOrb(World world, double x, double y, double z, UUID ownerUUID) {
        this(world);
        this.setPosition(x, y, z);
        this.ownerUUID = ownerUUID;
    }

    // Getters & setters
    public EntityPlayer getOwner() {
        try {
            return this.ownerUUID == null ? null : this.world.getPlayerEntityByUUID(this.ownerUUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    // NBT
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.ownerUUID = compound.getUniqueId("ownerUUID");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (this.ownerUUID != null) compound.setUniqueId("ownerUUID", this.ownerUUID);

        return compound;
    }

    // Properties
    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return this.getOwner() != null;
    }

    // Behaviour
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && !this.isDead && this.getOwner() != null) {
            // if too far, teleporting to player
            if (this.posX > this.getOwner().posX + 20 || this.posX < this.getOwner().posX - 20
                    || this.posY > this.getOwner().posY + 20 || this.posY < this.getOwner().posY - 20
                    || this.posZ > this.getOwner().posZ + 20 || this.posZ < this.getOwner().posZ - 20)
                this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);

            this.targetChangeCooldown -= (this.getPositionVector().squareDistanceTo(lastTickPosX, lastTickPosY, lastTickPosZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPosition().distanceSq(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
                verifyIfOnlyCompanion();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - posX, this.yTarget - posY, this.zTarget - posZ);
            double length = targetVector.lengthVector();
            targetVector = targetVector.scale(0.1 / length);
            motionX = (0.9) * motionX + (0.1) * targetVector.x;
            motionY = (0.9) * motionY + (0.1) * targetVector.y;
            motionZ = (0.9) * motionZ + (0.1) * targetVector.z;
            double speedModifier = this.getOwner().getPositionVector().subtract(this.getPositionVector()).lengthVector()-1;
            speedModifier *= speedModifier*speedModifier;
            if (this.getPosition() != this.getTargetPosition())
                this.move(MoverType.SELF, this.motionX * speedModifier, this.motionY * speedModifier, this.motionZ * speedModifier);
        }
    }

    private void selectBlockTarget() {
        this.xTarget = this.getOwner().posX + rand.nextGaussian();
        this.yTarget = this.getOwner().posY + rand.nextGaussian() + 2;
        this.zTarget = this.getOwner().posZ + rand.nextGaussian();

        targetChangeCooldown = rand.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

    public void verifyIfOnlyCompanion() {
        List<Entity> companions = this.world.getEntities(AbstractCompanionOrb.class, entity -> {
            return ((AbstractCompanionOrb) entity).getOwner() == this.getOwner();
        });
        if (companions.size() > 1) this.setDead();
    }

}
