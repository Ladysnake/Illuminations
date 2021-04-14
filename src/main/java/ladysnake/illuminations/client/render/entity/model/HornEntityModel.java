package ladysnake.illuminations.client.render.entity.model;// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelPart;

public class HornEntityModel extends OverheadEntityModel {
    private final ModelPart crown;
    private final ModelPart west_r1;
    private final ModelPart east_r1;
    private final ModelPart south_r1;

    public HornEntityModel() {
        textureWidth = 32;
        textureHeight = 48;

        crown = new ModelPart(this);
        crown.setPivot(0.0F, -4.0F, 0.0F);
        head.addChild(crown);


        west_r1 = new ModelPart(this);
        west_r1.setPivot(6.0F, 2.0F, 3.0F);
        crown.addChild(west_r1);
        setRotationAngle(west_r1, 0.0F, 2.5307F, 0.0F);
        west_r1.setTextureOffset(0, 39).addCuboid(-11.0F, -9.0F, 3.0F, 16.0F, 9.0F, 0.0F, 0.0F, true);

        east_r1 = new ModelPart(this);
        east_r1.setPivot(-6.0F, 2.0F, 3.0F);
        crown.addChild(east_r1);
        setRotationAngle(east_r1, 0.0F, -2.5307F, 0.0F);
        east_r1.setTextureOffset(0, 22).addCuboid(-5.0F, -9.0F, 3.0F, 16.0F, 9.0F, 0.0F, 0.0F, true);

        south_r1 = new ModelPart(this);
        south_r1.setPivot(0.0F, -4.0F, 0.0F);
        crown.addChild(south_r1);
        setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
        south_r1.setTextureOffset(7, 30).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}