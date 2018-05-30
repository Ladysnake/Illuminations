package ladysnake.lightorbs.common.entities;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.List;
import java.util.UUID;

@Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo", striprefs = true)
public class EntityCompanionOrb extends AbstractLightOrb implements ILightProvider {
    // Attributes
    private UUID ownerUUID;
    private static final DataParameter<String> TYPE = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.STRING);
    private static final DataParameter<Integer> LIGHTINGR = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LIGHTINGG = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LIGHTINGB = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> LIGHTINGRADIUS = EntityDataManager.createKey(EntityCompanionOrb.class, DataSerializers.VARINT);

    // Constructors
    public EntityCompanionOrb(World world) {
        super(world);
    }

    public EntityCompanionOrb(World world, double x, double y, double z, UUID ownerUUID) {
        this(world);
        this.setPosition(x, y, z);
        this.ownerUUID = ownerUUID;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(TYPE, "");
        this.getDataManager().register(LIGHTINGR, 0);
        this.getDataManager().register(LIGHTINGG, 0);
        this.getDataManager().register(LIGHTINGB, 0);
        this.getDataManager().register(LIGHTINGRADIUS, 0);
    }

    // Getters & setters
    public EntityPlayer getOwner() {
        try {
            return this.ownerUUID == null ? null : this.world.getPlayerEntityByUUID(this.ownerUUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public String getType() {
        return this.getDataManager().get(TYPE);
    }

    public void setProperties(String type, int lightingR, int lightingG, int lightingB, int lightingRadius) {
        this.getDataManager().set(TYPE, type);
        this.getDataManager().set(LIGHTINGR, lightingR);
        this.getDataManager().set(LIGHTINGG, lightingG);
        this.getDataManager().set(LIGHTINGB, lightingB);
        this.getDataManager().set(LIGHTINGRADIUS, lightingRadius);
    }

    public String getEntityTexture() {
        return "textures/entities/"+this.getDataManager().get(TYPE)+".png";
    }

    // NBT
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.ownerUUID = compound.getUniqueId("ownerUUID");
        this.getDataManager().set(TYPE, compound.getString("type"));
        this.getDataManager().set(LIGHTINGR, compound.getInteger("lightingR"));
        this.getDataManager().set(LIGHTINGG, compound.getInteger("lightingG"));
        this.getDataManager().set(LIGHTINGB, compound.getInteger("lightingB"));
        this.getDataManager().set(LIGHTINGRADIUS, compound.getInteger("lightingRadius"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (this.ownerUUID != null) compound.setUniqueId("ownerUUID", this.ownerUUID);
        compound.setString("type", this.getDataManager().get(TYPE));
        compound.setInteger("lightingR", this.getDataManager().get(LIGHTINGR));
        compound.setInteger("lightingG", this.getDataManager().get(LIGHTINGG));
        compound.setInteger("lightingB", this.getDataManager().get(LIGHTINGB));
        compound.setInteger("lightingRadius", this.getDataManager().get(LIGHTINGRADIUS));

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
            double speedModifier = (this.getOwner().getPositionVector().subtract(this.getPositionVector()).lengthVector()-2)*10;
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
        List<Entity> companions = this.world.getEntities(EntityCompanionOrb.class, entity -> {
            return ((EntityCompanionOrb) entity).getOwner() == this.getOwner();
        });
        if (companions.size() > 1) this.setDead();
    }

    @Override
    public Light provideLight() {
        return Light.builder().pos(this)
                .radius(this.getDataManager().get(LIGHTINGRADIUS))
                .color(this.getDataManager().get(LIGHTINGR),
                        this.getDataManager().get(LIGHTINGG),
                        this.getDataManager().get(LIGHTINGB), 0.01f).build();
    }

}
