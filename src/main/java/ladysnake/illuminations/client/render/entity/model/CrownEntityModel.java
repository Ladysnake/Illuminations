// Made with Blockbench 3.7.4

package ladysnake.illuminations.client.render.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class CrownEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	private final ModelPart crown;
	private final ModelPart crown_r1;
	private final ModelPart crown_r2;
	private final ModelPart crown_r3;
	private final ModelPart crown_r4;

	public CrownEntityModel() {
		textureWidth = 32;
		textureHeight = 48;
		crown = new ModelPart(this);
		crown.setPivot(0.0F, 23.3333F, 0.8333F);
		setRotationAngle(crown, -0.1745F, 0.0F, 0.0F);
		crown.setTextureOffset(0, 0).addCuboid(-4.0F, -8.3333F, -4.8333F, 8.0F, 8.0F, 8.0F, -0.5F, true);

		crown_r1 = new ModelPart(this);
		crown_r1.setPivot(0.0F, -2.3333F, -0.8333F);
		crown.addChild(crown_r1);
		setRotationAngle(crown_r1, -0.2618F, 1.5708F, 0.0F);
		crown_r1.setTextureOffset(7, 23).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r2 = new ModelPart(this);
		crown_r2.setPivot(0.0F, -2.3333F, -0.8333F);
		crown.addChild(crown_r2);
		setRotationAngle(crown_r2, -0.2618F, 3.1416F, 0.0F);
		crown_r2.setTextureOffset(7, 15).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r3 = new ModelPart(this);
		crown_r3.setPivot(0.0F, -2.3333F, -0.8333F);
		crown.addChild(crown_r3);
		setRotationAngle(crown_r3, -0.2618F, -1.5708F, 0.0F);
		crown_r3.setTextureOffset(7, 39).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);

		crown_r4 = new ModelPart(this);
		crown_r4.setPivot(0.0F, -2.3333F, -0.8333F);
		crown.addChild(crown_r4);
		setRotationAngle(crown_r4, -0.2618F, 0.0F, 0.0F);
		crown_r4.setTextureOffset(7, 31).addCuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true);
	}

	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
//		float k = 0.2617994F;
//		float l = -0.2617994F;
//		float m = 0.0F;
//		float n = 0.0F;
//		if (livingEntity.isFallFlying()) {
//			float o = 1.0F;
//			Vec3d vec3d = livingEntity.getVelocity();
//			if (vec3d.y < 0.0D) {
//				Vec3d vec3d2 = vec3d.normalize();
//				o = 1.0F - (float)Math.pow(-vec3d2.y, 1.5D);
//			}
//
//			k = o * 0.34906584F + (1.0F - o) * k;
//			l = o * -1.5707964F + (1.0F - o) * l;
//		} else if (livingEntity.isInSneakingPose()) {
//			k = 0.6981317F;
//			l = -0.7853982F;
//			m = 3.0F;
//			n = 0.08726646F;
//		}
//
//		this.field_3365.pivotX = 5.0F;
//		this.field_3365.pivotY = m;
//		if (livingEntity instanceof AbstractClientPlayerEntity) {
//			AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity)livingEntity;
//			abstractClientPlayerEntity.elytraPitch = (float)((double)abstractClientPlayerEntity.elytraPitch + (double)(k - abstractClientPlayerEntity.elytraPitch) * 0.1D);
//			abstractClientPlayerEntity.elytraYaw = (float)((double)abstractClientPlayerEntity.elytraYaw + (double)(n - abstractClientPlayerEntity.elytraYaw) * 0.1D);
//			abstractClientPlayerEntity.elytraRoll = (float)((double)abstractClientPlayerEntity.elytraRoll + (double)(l - abstractClientPlayerEntity.elytraRoll) * 0.1D);
//			this.field_3365.pitch = abstractClientPlayerEntity.elytraPitch;
//			this.field_3365.yaw = abstractClientPlayerEntity.elytraYaw;
//			this.field_3365.roll = abstractClientPlayerEntity.elytraRoll;
//		} else {
//			this.field_3365.pitch = k;
//			this.field_3365.roll = l;
//			this.field_3365.yaw = n;
//		}
//
//		this.field_3364.pivotX = -this.field_3365.pivotX;
//		this.field_3364.yaw = -this.field_3365.yaw;
//		this.field_3364.pivotY = this.field_3365.pivotY;
//		this.field_3364.pitch = this.field_3365.pitch;
//		this.field_3364.roll = -this.field_3365.roll;
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