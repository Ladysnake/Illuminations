package ladysnake.lumen.common.entities;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import ladysnake.lumen.common.Lumen;
import ladysnake.lumen.common.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo", striprefs = true)
public class EntityCompanionOrb extends AbstractLightOrb implements ILightProvider {
    // Attributes
    private UUID ownerUUID;
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.VARINT);

    // Constructors
    public EntityCompanionOrb(World world) {
        super(world);
    }

    public EntityCompanionOrb(World world, double x, double y, double z, UUID ownerUUID, ModEntities.Companion type) {
        this(world);
        this.setPosition(x, y, z);
        this.ownerUUID = ownerUUID;
        this.getDataManager().set(TYPE, type.ordinal());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(TYPE, 0);
    }

    // Getters & setters
    public EntityPlayer getOwner() {
        try {
            return this.ownerUUID == null ? null : this.world.getPlayerEntityByUUID(this.ownerUUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public ResourceLocation getEntityTexture() {
        return new ResourceLocation(Lumen.MOD_ID, "textures/entities/"+ModEntities.Companion.values()[this.dataManager.get(TYPE)].getName()+".png");
    }

    // NBT
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.ownerUUID = compound.getUniqueId("ownerUUID");
        this.getDataManager().set(TYPE, compound.getInteger("type"));
    }

    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (this.ownerUUID != null) compound.setUniqueId("ownerUUID", this.ownerUUID);
        compound.setInteger("type", this.getDataManager().get(TYPE));

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
    public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
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
            double length = targetVector.length();
            targetVector = targetVector.scale(0.1 / length);
            motionX = (0.9) * motionX + (0.1) * targetVector.x;
            motionY = (0.9) * motionY + (0.1) * targetVector.y;
            motionZ = (0.9) * motionZ + (0.1) * targetVector.z;
            double speedModifier = Math.max(this.getOwner().getPositionVector().subtract(this.getPositionVector()).length()-2, 0)*10;
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
        List<Entity> companions = this.world.getEntities(EntityCompanionOrb.class, entity -> ((EntityCompanionOrb) entity).getOwner() == this.getOwner());
        if (companions.size() > 1) this.setDead();
    }

    @Override
    public Light provideLight() {
        ModEntities.Companion type = ModEntities.Companion.values()[this.getDataManager().get(TYPE)];
        return Light.builder().pos(this).radius(type.getLightingRadius()).color(type.getLightingR(), type.getLightingG(), type.getLightingB(), 0.01f).build();
    }

}
