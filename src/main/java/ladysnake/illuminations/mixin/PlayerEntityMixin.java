package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo callbackInfo) {
        // if player has an aura
        if (IlluminationsClient.PLAYER_AURAS.containsKey(this.getUuid())) {
            String playerAura = IlluminationsClient.PLAYER_AURAS.get(this.getUuid()).getAura();

            // do not render in first person or if the player is invisible
            //noinspection ConstantConditions
            if ((MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
                if (IlluminationsClient.AURAS_DATA.containsKey(playerAura)) {
                    DefaultParticleType auraParticle = IlluminationsClient.AURAS_DATA.get(playerAura).getParticle();
                    if (IlluminationsClient.AURAS_DATA.get(playerAura).shouldAddParticle(this.random)) {
                        world.addParticle(auraParticle, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                    }
                }
            }
        }
    }

}