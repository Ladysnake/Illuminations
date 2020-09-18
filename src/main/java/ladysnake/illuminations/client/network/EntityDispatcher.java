package ladysnake.illuminations.client.network;

import ladysnake.illuminations.common.network.Packets;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.util.Optional;

public final class EntityDispatcher {
    private EntityDispatcher() {
    }

    public static void spawnFrom(final PacketContext ctx, final PacketByteBuf bytes) {
        Packets.readFrom(bytes, EntitySpawnS2CPacket::new).ifPresent(packet ->
                ctx.getTaskQueue().execute(() -> {
                    final ClientWorld world = MinecraftClient.getInstance().world;
                    Optional.ofNullable(packet.getEntityTypeId().create(world)).ifPresent(entity -> {
                        entity.updateTrackedPosition(packet.getX(), packet.getY(), packet.getZ());
                        entity.setVelocity(packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ());
                        entity.pitch = (float) (packet.getPitch() * 360) / 256.0F;
                        entity.yaw = (float) (packet.getYaw() * 360) / 256.0F;
                        entity.setEntityId(packet.getId());
                        entity.setUuid(packet.getUuid());
                        world.addEntity(packet.getId(), entity);
                    });
                }));
    }
}