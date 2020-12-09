package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class OverheadData {
    private final PlayerEntityModel<AbstractClientPlayerEntity> model;
    private final Identifier texture;

    public OverheadData(PlayerEntityModel<AbstractClientPlayerEntity> model, String textureName) {
        this.model = model;
        this.texture = new Identifier("textures/entity/steve.png");
    }

    public PlayerEntityModel<AbstractClientPlayerEntity> getModel() {
        return model;
    }

    public Identifier getTexture() {
        return texture;
    }
}
