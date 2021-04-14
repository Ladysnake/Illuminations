package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;

public class WorldweaverHaloEntityModel extends OverheadEntityModel {
    private final ModelPart crown;

    public WorldweaverHaloEntityModel() {
        textureWidth = 32;
        textureHeight = 48;
        head.setPivot(0.0F, 0.0F, 0.0F);
        head.setTextureOffset(0, 32);

        crown = new ModelPart(this);
        crown.setPivot(0.0F, -4.0F, 0.0F);
        head.addChild(crown);
        crown.setTextureOffset(0, 0).addCuboid(-8.0F, -11.0F, 5.0F, 16.0F, 16.0F, 0.0F, 0.0F, true);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}