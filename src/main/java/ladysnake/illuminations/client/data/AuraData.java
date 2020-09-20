package ladysnake.illuminations.client.data;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AuraData {
    private final DefaultParticleType particle;
    private final float chance;
    private final int delay;

    public AuraData(DefaultParticleType particle, float chance, int delay) {
        this.particle = particle;
        this.chance = chance;
        this.delay = delay;
    }

    public DefaultParticleType getParticle() {
        return particle;
    }

    public boolean shouldAddParticle(Random random, int age) {
        float rand = random.nextFloat();
        return rand <= this.chance && (age % delay == 0);
    }
}
