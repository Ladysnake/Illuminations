//// Made with Model Converter by Globox_Z
//// Generate all required imports
//// Made with Blockbench 3.8.4
//// Exported for Minecraft version 1.15
//// Paste this class into your mod and generate all required imports
//package ladysnake.illuminations.client.render.entity.model;
//import net.minecraft.client.model.Model;
//public class HaloModel extends Model {
//    private final ModelPart head;
//    private final ModelPart crown;
//    public HaloModel(ModelPart root) {
//this.head = root.getChild("head");
//this.crown = this.head.getChild("crown");
//}
//public static TexturedModelData getTexturedModelData() {
//ModelData modelData = new ModelData();
//ModelPartData modelPartData = modelData.getRoot();
//ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0,32).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true), ModelTransform.pivot(0.0F,0.0F,0.0F));
//modelPartData1.addChild("crown", ModelPartBuilder.create().uv(0,0).cuboid(-8.0F, -11.0F, 5.0F, 16.0F, 16.0F, 0.0F, 0.0F, true), ModelTransform.pivot(0.0F,-4.0F,0.0F));
//return TexturedModelData.of(modelData,32,32);
//    }
//    @Override
//    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        //previously the render function, render code was moved to a method below
//    }
//    @Override
//    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        head.render(matrixStack, buffer, packedLight, packedOverlay);
//    }
//    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
//        bone.pitch = x;
//        bone.yaw = y;
//        bone.roll = z;
//    }
//}