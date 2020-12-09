package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.client.render.entity.model.OverheadEntityModel;
import net.minecraft.util.Identifier;

public class OverheadData {
    private final OverheadEntityModel model;
    private final Identifier texture;

    public OverheadData(OverheadEntityModel model, String textureName) {
        this.model = model;
        this.texture = new Identifier(IlluminationsClient.MODID, "textures/entity/"+textureName+".png");
    }

    public OverheadEntityModel getModel() {
        return model;
    }

    public Identifier getTexture() {
        return texture;
    }
}
