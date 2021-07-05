package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Config;
import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.data.AuraData;
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
        // if player has cosmetics
        if (IlluminationsClient.PLAYER_COSMETICS.containsKey(this.getUuid())) {
            // player aura
            String playerAura = IlluminationsClient.PLAYER_COSMETICS.get(this.getUuid()).getAura();
            if (playerAura != null && IlluminationsClient.AURAS_DATA.containsKey(playerAura)) {
                // do not render in first person or if the player is invisible
                //noinspection ConstantConditions
                if (((Config.getViewAurasFP() || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
                    if (IlluminationsClient.AURAS_DATA.containsKey(playerAura)) {
                        AuraData aura = IlluminationsClient.AURAS_DATA.get(playerAura);
                        if (IlluminationsClient.AURAS_DATA.get(playerAura).shouldAddParticle(this.random, this.age)) {
                            world.addParticle(aura.getParticle(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                        }
                    }
                }
            }

            // player pet
            String playerPet = IlluminationsClient.PLAYER_COSMETICS.get(this.getUuid()).getPet();
            if (playerPet != null && IlluminationsClient.PETS_DATA.containsKey(playerPet)) {
                // do not render in first person or if the player is invisible
                //noinspection ConstantConditions
                if (((Config.getViewAurasFP() || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
                    if (IlluminationsClient.PETS_DATA.containsKey(playerPet)) {
                        DefaultParticleType overhead = IlluminationsClient.PETS_DATA.get(playerPet);
                        if (this.age % 20 == 0) {
                            world.addParticle(overhead, this.getX() + Math.cos(this.bodyYaw / 50) * 0.5, this.getY() + this.getHeight() + 0.5f + Math.sin(this.age / 12f) / 12f, this.getZ() - Math.cos(this.bodyYaw / 50) * 0.5, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

}