package ladysnake.illuminations.client.data;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AuraData {
    private final DefaultParticleType particle;
    private final float chance;
    private final int delay;
    private final float spawnOffsetX;
    private final float spawnOffsetY;
    private final float spawnOffsetZ;

    public AuraData(DefaultParticleType particle, float chance, int delay, float spawnOffsetX, float spawnOffsetY, float spawnOffsetZ) {
        this.particle = particle;
        this.chance = chance;
        this.delay = delay;
        this.spawnOffsetX = spawnOffsetX;
        this.spawnOffsetY = spawnOffsetY;
        this.spawnOffsetZ = spawnOffsetZ;
    }

    public DefaultParticleType getParticle() {
        return particle;
    }

    public float getSpawnOffsetX() {
        return spawnOffsetX;
    }

    public float getSpawnOffsetY() {
        return spawnOffsetY;
    }

    public float getSpawnOffsetZ() {
        return spawnOffsetZ;
    }

    public boolean shouldAddParticle(Random random, int age) {
        float rand = random.nextFloat();
        return rand <= this.chance && (age % delay == 0);
    }
}
