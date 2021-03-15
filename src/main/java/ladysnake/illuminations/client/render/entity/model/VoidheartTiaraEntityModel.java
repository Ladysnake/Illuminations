// Made with Blockbench 3.8.2
package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;

public class VoidheartTiaraEntityModel extends OverheadEntityModel {
    private final ModelPart crown;
    private final ModelPart west_r1;
    private final ModelPart north_r1;
    private final ModelPart east_r1;
    private final ModelPart south_r1;

    public VoidheartTiaraEntityModel() {
        textureWidth = 32;
        textureHeight = 48;

        crown = new ModelPart(this);
        crown.setPivot(0.0F, -4.0F, 0.0F);
        head.addChild(crown);
        crown.setTextureOffset(0, 0).addCuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, true);

        west_r1 = new ModelPart(this);
        west_r1.setPivot(0.0F, -7.0F, 0.0F);
        crown.addChild(west_r1);
        setRotationAngle(west_r1, -0.2618F, 1.5708F, 0.0F);
        west_r1.setTextureOffset(0, 0).addCuboid(-7.0F, -11.0F, 3.0F, 11.0F, 11.0F, 1.0F, -0.5F, true);

        north_r1 = new ModelPart(this);
        north_r1.setPivot(0.5F, -7.0F, 0.0F);
        crown.addChild(north_r1);
        setRotationAngle(north_r1, -0.2618F, 3.1416F, 0.0F);
        north_r1.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, -0.5F, true);

        east_r1 = new ModelPart(this);
        east_r1.setPivot(0.0F, -6.0F, 1.0F);
        crown.addChild(east_r1);
        setRotationAngle(east_r1, -0.2618F, -1.5708F, 0.0F);
        east_r1.setTextureOffset(0, 0).addCuboid(-5.0F, -11.0F, 3.0F, 10.0F, 11.0F, 1.0F, -0.5F, true);

        south_r1 = new ModelPart(this);
        south_r1.setPivot(0.5F, -7.0F, 0.0F);
        crown.addChild(south_r1);
        setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
        south_r1.setTextureOffset(0, 0).addCuboid(-5.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, -0.5F, true);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}