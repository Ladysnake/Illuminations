package ladysnake.illuminations.client.data;

import net.minecraft.particle.DefaultParticleType;

import java.util.Random;
import java.util.function.Supplier;

public record AuraData(DefaultParticleType particle, Supplier<AuraSettings> auraSettingsSupplier) {

    public boolean shouldAddParticle(Random random, int age) {
        AuraSettings settings = auraSettingsSupplier().get();
        if (settings.spawnRate() == 0) return false;
        float rand = random.nextFloat();
        return rand <= settings.spawnRate() && (age % settings.delay() == 0);
    }
}
