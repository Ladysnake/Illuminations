package ladysnake.lightorbs.common.entities;

import com.google.common.base.Optional;
import ladylib.LadyLib;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class AbstractCompanionOrb extends AbstractLightOrb {
    // Attributes
    private EntityPlayer player;
    private NBTTagCompound entCompound;

    // Constructors
    public AbstractCompanionOrb(World world) {
        super(world);
    }

    public AbstractCompanionOrb(World world, double x, double y, double z, EntityPlayer player) {
        this(world);
        this.setPosition(x, y, z);
        this.player = player;
    }

    // Getters & setters
    public EntityPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    // NBT
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (!compound.getString("OwnerUUID").isEmpty()) {
            try {
                this.player = this.world.getPlayerEntityByUUID(UUID.fromString(compound.getString("OwnerUUID")));
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
        }

        this.entCompound = compound;
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if (this.player != null) {
            compound.setString("OwnerUUID", this.player.getUniqueID().toString());
        }
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
        return this.getPlayer() != null;
    }

    // Behaviour
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        // if no player, searching NBT
        if (this.getPlayer() == null && this.entCompound != null) if (!this.entCompound.getString("OwnerUUID").isEmpty()) {
            this.setPlayer(this.world.getPlayerEntityByUUID(UUID.fromString(this.entCompound.getString("OwnerUUID"))));
        }

        if (this.getPlayer() == null) this.setSize(0F, 0F); else this.setSize(0.5F, 0.5F);

        if (!this.world.isRemote && !this.isDead) {
            // if too far, teleporting to player
            if (this.posX > this.player.posX + 20 || this.posX < this.player.posX - 20
                    || this.posY > this.player.posY + 20 || this.posY < this.player.posY - 20
                    || this.posZ > this.player.posZ + 20 || this.posZ < this.player.posZ - 20)
                this.setPosition(this.player.posX, this.player.posY, this.player.posZ);

            this.targetChangeCooldown -= (this.getPositionVector().squareDistanceTo(lastTickPosX, lastTickPosY, lastTickPosZ) < 0.0125) ? 10 : 1;

            if ((xTarget == 0 && yTarget == 0 && zTarget == 0) || this.getPosition().distanceSq(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0) {
                selectBlockTarget();
            }

            Vec3d targetVector = new Vec3d(this.xTarget - posX, this.yTarget - posY, this.zTarget - posZ);
            double length = targetVector.lengthVector();
            targetVector = targetVector.scale(0.1 / length);
            motionX = (0.9) * motionX + (0.1) * targetVector.x;
            motionY = (0.9) * motionY + (0.1) * targetVector.y;
            motionZ = (0.9) * motionZ + (0.1) * targetVector.z;
            double speedModifier = this.player.getPositionVector().subtract(this.getPositionVector()).lengthVector();
            if (this.getPosition() != this.getTargetPosition())
                this.move(MoverType.SELF, this.motionX * speedModifier, this.motionY * speedModifier, this.motionZ * speedModifier);
        }
    }

    private void selectBlockTarget() {
        this.xTarget = this.player.posX + rand.nextGaussian();
        this.yTarget = this.player.posY + rand.nextGaussian() + 2;
        this.zTarget = this.player.posZ + rand.nextGaussian();

        targetChangeCooldown = rand.nextInt() % 100;
    }

    public BlockPos getTargetPosition() {
        return new BlockPos(this.xTarget, this.yTarget + 0.5, this.zTarget);
    }

}
