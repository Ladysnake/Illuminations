package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.Config;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class IlluminationData {
    private final DefaultParticleType illuminationType;
    private final Predicate<Long> timeSpawnPredicate;
    private final BiPredicate<World, BlockPos> locationSpawnPredicate;
    private final float chance;

    public IlluminationData(DefaultParticleType illuminationType, Predicate<Long> timeSpawnPredicate, BiPredicate<World, BlockPos> locationSpawnPredicate, float chance) {
        this.illuminationType = illuminationType;
        this.timeSpawnPredicate = timeSpawnPredicate;
        this.locationSpawnPredicate = locationSpawnPredicate;
        this.chance = chance;
    }

    public DefaultParticleType getIlluminationType() {
        return illuminationType;
    }

    public Predicate<Long> getTimeSpawnPredicate() {
        return timeSpawnPredicate;
    }

    public BiPredicate<World, BlockPos> getLocationSpawnPredicate() {
        return locationSpawnPredicate;
    }

    public float getChance() {
        return chance;
    }

    public boolean shouldAddParticle(Random random) {
        int density = Config.getDensity()/100;
        return random.nextFloat() <= this.chance * density;
    }
}
