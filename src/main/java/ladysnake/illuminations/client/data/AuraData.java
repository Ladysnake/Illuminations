package ladysnake.illuminations.client.data;

import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

public class AuraData {
    private final DefaultParticleType particle;
    private final float chance;

    public AuraData(DefaultParticleType particle, float chance) {
        this.particle = particle;
        this.chance = chance;
    }

    public DefaultParticleType getParticle() {
        return particle;
    }

    public float getChance() {
        return chance;
    }

    public boolean shouldAddParticle(Random random) {
        float rand = random.nextFloat();
        return rand <= this.chance;
    }
}
