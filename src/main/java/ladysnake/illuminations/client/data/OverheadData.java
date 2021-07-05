package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.entity.model.OverheadModel;
import net.minecraft.client.model.Model;
import net.minecraft.util.Identifier;

public class OverheadData {
    private final OverheadModel model;
    private final Identifier texture;

    public OverheadData(OverheadModel model, String textureName) {
        this.model = model;
        this.texture = new Identifier(IlluminationsClient.MODID, "textures/entity/" + textureName + ".png");
    }

    public OverheadModel getModel() {
        return model;
    }

    public Identifier getTexture() {
        return texture;
    }
}
