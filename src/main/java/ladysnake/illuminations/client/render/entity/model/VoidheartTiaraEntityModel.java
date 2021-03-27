// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;

public class VoidheartTiaraEntityModel extends CrownEntityModel {
    private final ModelPart crown;
    private final ModelPart west_r1;
    private final ModelPart north_r1;
    private final ModelPart east_r1;
    private final ModelPart south_r1;
    private final ModelPart crown2;
    private final ModelPart west_r2;
    private final ModelPart north_r2;
    private final ModelPart east_r2;
    private final ModelPart south_r2;

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
        west_r1.setTextureOffset(7, 39).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

        north_r1 = new ModelPart(this);
        north_r1.setPivot(0.0F, -7.0F, 0.0F);
        crown.addChild(north_r1);
        setRotationAngle(north_r1, -0.2618F, 3.1416F, 0.0F);
        north_r1.setTextureOffset(7, 15).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

        east_r1 = new ModelPart(this);
        east_r1.setPivot(0.0F, -7.0F, 0.0F);
        crown.addChild(east_r1);
        setRotationAngle(east_r1, -0.2618F, -1.5708F, 0.0F);
        east_r1.setTextureOffset(7, 23).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

        south_r1 = new ModelPart(this);
        south_r1.setPivot(0.0F, -7.0F, 0.0F);
        crown.addChild(south_r1);
        setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
        south_r1.setTextureOffset(7, 31).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

        crown2 = new ModelPart(this);
        crown2.setPivot(0.0F, -4.0F, 0.0F);
        head.addChild(crown2);
        crown2.setTextureOffset(0, 0).addCuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, true);

        west_r2 = new ModelPart(this);
        west_r2.setPivot(0.0F, -7.0F, 0.0F);
        crown2.addChild(west_r2);
        setRotationAngle(west_r2, -0.2618F, 1.5708F, 0.0F);
        west_r2.setTextureOffset(0, 36).addCuboid(-7.0F, -11.0F, 3.0F, 11.0F, 11.0F, 1.0F, -0.5F, true);

        north_r2 = new ModelPart(this);
        north_r2.setPivot(0.5F, -7.0F, 0.0F);
        crown2.addChild(north_r2);
        setRotationAngle(north_r2, -0.2618F, 3.1416F, 0.0F);
        north_r2.setTextureOffset(0, 16).addCuboid(-4.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, -0.5F, true);

        east_r2 = new ModelPart(this);
        east_r2.setPivot(0.0F, -6.0F, 1.0F);
        crown2.addChild(east_r2);
        setRotationAngle(east_r2, -0.2618F, -1.5708F, 0.0F);
        east_r2.setTextureOffset(0, 25).addCuboid(-5.0F, -11.0F, 3.0F, 10.0F, 11.0F, 1.0F, -0.5F, true);

        south_r2 = new ModelPart(this);
        south_r2.setPivot(0.5F, -7.0F, 0.0F);
        crown2.addChild(south_r2);
        setRotationAngle(south_r2, -0.2618F, 0.0F, 0.0F);
        south_r2.setTextureOffset(12, 39).addCuboid(-5.0F, -8.0F, 3.0F, 9.0F, 8.0F, 1.0F, -0.5F, true);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}