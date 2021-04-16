package ladysnake.illuminations.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ladysnake.illuminations.client.IlluminationsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class WispTrailParticleEffect implements ParticleEffect {
    public static final Codec<WispTrailParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.FLOAT.fieldOf("r").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.red;
        }), Codec.FLOAT.fieldOf("g").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.green;
        }), Codec.FLOAT.fieldOf("b").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.blue;
        })).apply(instance, WispTrailParticleEffect::new);
    });
    public static final ParticleEffect.Factory<WispTrailParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<WispTrailParticleEffect>() {
        public WispTrailParticleEffect read(ParticleType<WispTrailParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = (float)stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float)stringReader.readDouble();
            stringReader.expect(' ');
            float h = (float)stringReader.readDouble();
            return new WispTrailParticleEffect(f, g, h);
        }

        public WispTrailParticleEffect read(ParticleType<WispTrailParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new WispTrailParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };
    private final float red;
    private final float green;
    private final float blue;

    public WispTrailParticleEffect(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue);
    }

    public ParticleType<WispTrailParticleEffect> getType() {
        return IlluminationsClient.WISP_TRAIL;
    }

    @Environment(EnvType.CLIENT)
    public float getRed() {
        return this.red;
    }

    @Environment(EnvType.CLIENT)
    public float getGreen() {
        return this.green;
    }

    @Environment(EnvType.CLIENT)
    public float getBlue() {
        return this.blue;
    }
}
