package ladysnake.illuminations.client;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
