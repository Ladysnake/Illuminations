package ladysnake.illuminations.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ladysnake.illuminations.client.Illuminations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
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
        }), Codec.FLOAT.fieldOf("re").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.redEvolution;
        }), Codec.FLOAT.fieldOf("ge").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.greenEvolution;
        }), Codec.FLOAT.fieldOf("be").forGetter((wispTrailParticleEffect) -> {
            return wispTrailParticleEffect.blueEvolution;
        })).apply(instance, WispTrailParticleEffect::new);
    });
    public static final ParticleEffect.Factory<WispTrailParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<WispTrailParticleEffect>() {
        public WispTrailParticleEffect read(ParticleType<WispTrailParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float b = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float re = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float ge = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float be = (float) stringReader.readDouble();
            return new WispTrailParticleEffect(r, g, b, re, ge, be);
        }

        public WispTrailParticleEffect read(ParticleType<WispTrailParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new WispTrailParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };
    private final float red;
    private final float green;
    private final float blue;
    private final float redEvolution;
    private final float greenEvolution;
    private final float blueEvolution;

    public WispTrailParticleEffect(float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.redEvolution = redEvolution;
        this.greenEvolution = greenEvolution;
        this.blueEvolution = blueEvolution;
    }

    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.red);
        buf.writeFloat(this.green);
        buf.writeFloat(this.blue);
        buf.writeFloat(this.redEvolution);
        buf.writeFloat(this.greenEvolution);
        buf.writeFloat(this.blueEvolution);
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f", Registry.PARTICLE_TYPE.getId(this.getType()), this.red, this.green, this.blue, this.redEvolution, this.greenEvolution, this.blueEvolution);
    }

    public ParticleType<WispTrailParticleEffect> getType() {
        return Illuminations.WISP_TRAIL;
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

    @Environment(EnvType.CLIENT)
    public float getRedEvolution() {
        return redEvolution;
    }

    @Environment(EnvType.CLIENT)
    public float getGreenEvolution() {
        return greenEvolution;
    }

    @Environment(EnvType.CLIENT)
    public float getBlueEvolution() {
        return blueEvolution;
    }
}
