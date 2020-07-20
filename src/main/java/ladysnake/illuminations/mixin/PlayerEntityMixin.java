package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.particle.aura.FireflyAuraParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        // do not render in first person
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().gameRenderer != null) {
            //noinspection ConstantConditions
            if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || MinecraftClient.getInstance().player != (Object) this) {
                if (this.random.nextFloat() <= 0.1F)
                world.addParticle(IlluminationsClient.FIREFLY_AURA, this.getX() + FireflyAuraParticle.getWanderingDistance(this.random), this.getY() + this.random.nextFloat() + FireflyAuraParticle.getWanderingDistance(this.random), this.getZ() + FireflyAuraParticle.getWanderingDistance(this.random), 0, 0, 0);
            }
        }
    }

}