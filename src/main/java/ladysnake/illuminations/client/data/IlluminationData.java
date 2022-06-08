package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.config.Config;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public record IlluminationData(DefaultParticleType illuminationType,
                               BiPredicate<World, BlockPos> locationSpawnPredicate,
                               Supplier<Float> chanceSupplier) {

    public boolean shouldAddParticle(Random random) {
        float chance = chanceSupplier.get();
        if (chance <= 0f) return false;
        float density = Config.getDensity() / 100f;
        return random.nextFloat() <= chance * density;
    }
}
