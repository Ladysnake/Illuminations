package ladysnake.illuminations.client;

import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

public class IlluminationData {
    private final DefaultParticleType illuminationType;
    private final float chance;

    public IlluminationData(DefaultParticleType illuminationType, float chance) {
        this.illuminationType = illuminationType;
        this.chance = chance;
    }

    public DefaultParticleType getIlluminationType() {
        return illuminationType;
    }

    public float getChance() {
        return chance;
    }

    public boolean shouldAddParticle(Random random) {
        return random.nextFloat() <= this.chance;
    }
}
