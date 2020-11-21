package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.particle.EyesParticle;
import ladysnake.illuminations.client.particle.FireflyParticle;
import ladysnake.illuminations.client.particle.GlowwormParticle;
import ladysnake.illuminations.client.particle.PlanktonParticle;
import ladysnake.illuminations.client.particle.aura.GhostlyParticle;
import ladysnake.illuminations.client.particle.aura.TwilightFireflyParticle;
import ladysnake.illuminations.client.particle.overhead.JackoParticle;
import ladysnake.illuminations.client.particle.overhead.OverheadParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Shadow
    protected ClientWorld world;

    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void createParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> ci) {
        //Force creation of custom particles
        if (parameters.getType() == IlluminationsClient.FIREFLY) {
            Particle p = FireflyParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.GLOWWORM) {
            Particle p = GlowwormParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.PLANKTON) {
            Particle p = PlanktonParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.EYES) {
            Particle p = EyesParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.TWILIGHT_AURA) {
            Particle p = TwilightFireflyParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.GHOSTLY_AURA) {
            Particle p = GhostlyParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.PRIDE_OVERHEAD) {
            Particle p = OverheadParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.TRANS_PRIDE_OVERHEAD) {
            Particle p = OverheadParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        } else if (parameters.getType() == IlluminationsClient.JACKO_OVERHEAD) {
            Particle p = JackoParticle.DefaultFactory.lastInstance.createParticle((DefaultParticleType)parameters, this.world, x, y, z, velocityX, velocityY, velocityZ);
            ci.setReturnValue(p);
        }
    }
}