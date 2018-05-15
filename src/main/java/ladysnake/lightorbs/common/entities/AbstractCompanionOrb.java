package ladysnake.lightorbs.common.entities;

import com.google.common.base.Optional;
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
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);


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

    // NBT
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String s;

        if (compound.hasKey("OwnerUUID", 8)) s = compound.getString("OwnerUUID");
        else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
            } catch (Throwable var4) {}
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if (this.getOwnerId() == null) compound.setString("OwnerUUID", "");
        else compound.setString("OwnerUUID", this.getOwnerId().toString());
    }

    @Nullable
    public UUID getOwnerId() {
        return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
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
        return true;
    }

    // Behaviour
    private double xTarget;
    private double yTarget;
    private double zTarget;
    private int targetChangeCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        // if too far, teleporting to player
        if (this.player != null) {
            if (this.posX > this.player.posX + 20 || this.posX < this.player.posX - 20
                    || this.posY > this.player.posY + 20 || this.posY < this.player.posY - 20
                    || this.posZ > this.player.posZ + 20 || this.posZ < this.player.posZ - 20)
                this.setPosition(player.posX, player.posY, player.posZ);


            if (!this.world.isRemote && !this.isDead) {
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
