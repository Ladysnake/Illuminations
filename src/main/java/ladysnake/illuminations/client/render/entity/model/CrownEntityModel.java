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
	public final ModelPart crown_r1;
	public final ModelPart crown_r2;
	public final ModelPart crown_r3;
	public final ModelPart crown_r4;

	public CrownEntityModel() {
		textureWidth = 32;
		textureHeight = 48;
		crown = new ModelPart(this);
		crown.setPivot(0.0F, 18.0F, 0.0F);
		crown.setTextureOffset(0, 0).addCuboid(-4.0F, -11.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, true);

		crown_r1 = new ModelPart(this);
		crown_r1.setPivot(0.0F, -5.0F, 0.0F);
		crown.addChild(crown_r1);
		setRotationAngle(crown_r1, -0.2618F, 1.5708F, 0.0F);
		crown_r1.setTextureOffset(7, 23).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r2 = new ModelPart(this);
		crown_r2.setPivot(0.0F, -5.0F, 0.0F);
		crown.addChild(crown_r2);
		setRotationAngle(crown_r2, -0.2618F, 3.1416F, 0.0F);
		crown_r2.setTextureOffset(7, 15).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r3 = new ModelPart(this);
		crown_r3.setPivot(0.0F, -5.0F, 0.0F);
		crown.addChild(crown_r3);
		setRotationAngle(crown_r3, -0.2618F, -1.5708F, 0.0F);
		crown_r3.setTextureOffset(7, 39).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r4 = new ModelPart(this);
		crown_r4.setPivot(0.0F, -5.0F, 0.0F);
		crown.addChild(crown_r4);
		setRotationAngle(crown_r4, -0.2618F, 0.0F, 0.0F);
		crown_r4.setTextureOffset(7, 31).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);
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
		return ImmutableList.of(this.crown, this.crown_r1);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of();
	}

}