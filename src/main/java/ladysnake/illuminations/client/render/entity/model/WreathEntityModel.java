// Made with Blockbench 3.8.2
package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;

public class WreathEntityModel extends OverheadEntityModel {
    private final ModelPart wreath;

    public WreathEntityModel() {
        textureWidth = 32;
        textureHeight = 16;
        head.setPivot(0.0F, 0.0F, 0.0F);

        wreath = new ModelPart(this);
        wreath.setPivot(0.0F, 24.0F, 0.0F);
        head.addChild(wreath);
        wreath.setTextureOffset(0, 0).addCuboid(-5.0F, -34.5F, -5.0F, 10.0F, 5.0F, 8.0F, 0F, false);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}