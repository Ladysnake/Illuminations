// Made with Blockbench 3.7.4

package ladysnake.illuminations.client.render.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class CrownEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	public final ModelPart crown;
	private final ModelPart west_r1;
	private final ModelPart north_r1;
	private final ModelPart east_r1;
	private final ModelPart south_r1;

	public CrownEntityModel() {
		textureWidth = 32;
		textureHeight = 48;
		crown = new ModelPart(this);
		crown.setPivot(0.0F, 24.0F, 0.0F);
		crown.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, true);

		west_r1 = new ModelPart(this);
		west_r1.setPivot(0.0F, -2.0F, 0.0F);
		crown.addChild(west_r1);
		setRotationAngle(west_r1, -0.2618F, 1.5708F, 0.0F);
		west_r1.setTextureOffset(7, 23).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		north_r1 = new ModelPart(this);
		north_r1.setPivot(0.0F, -2.0F, 0.0F);
		crown.addChild(north_r1);
		setRotationAngle(north_r1, -0.2618F, 3.1416F, 0.0F);
		north_r1.setTextureOffset(7, 15).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		east_r1 = new ModelPart(this);
		east_r1.setPivot(0.0F, -2.0F, 0.0F);
		crown.addChild(east_r1);
		setRotationAngle(east_r1, -0.2618F, -1.5708F, 0.0F);
		east_r1.setTextureOffset(7, 39).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		south_r1 = new ModelPart(this);
		south_r1.setPivot(0.0F, -2.0F, 0.0F);
		crown.addChild(south_r1);
		setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
		south_r1.setTextureOffset(7, 31).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);
	}

	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {

	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		crown.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.crown, this.north_r1, this.east_r1, this.south_r1, this.west_r1);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of();
	}

}