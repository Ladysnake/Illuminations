// Made with Blockbench 3.7.4

package ladysnake.illuminations.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class CrownEntityModel extends OverheadEntityModel {
    private final ModelPart crown;
    private final ModelPart west_r1;
    private final ModelPart north_r1;
    private final ModelPart east_r1;
    private final ModelPart south_r1;

    public CrownEntityModel() {
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
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}