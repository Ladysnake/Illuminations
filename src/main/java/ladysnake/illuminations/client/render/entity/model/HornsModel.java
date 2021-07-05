//// Made with Model Converter by Globox_Z
//// Generate all required imports
//// Made with Blockbench 3.8.4
//// Exported for Minecraft version 1.15
//// Paste this class into your mod and generate all required imports
//package ladysnake.illuminations.client.render.entity.model;
//import net.minecraft.client.model.Model;
//public class HornsModel extends Model {
//    private final ModelPart head;
//    private final ModelPart crown;
//    private final ModelPart west_r1;
//    private final ModelPart east_r1;
//    private final ModelPart south_r1;
//    public HornsModel(ModelPart root) {
//this.head = root.getChild("head");
//this.crown = this.head.getChild("crown");
//this.south_r1 = this.crown.getChild("south_r1");
//this.east_r1 = this.crown.getChild("east_r1");
//this.west_r1 = this.crown.getChild("west_r1");
//}
//public static TexturedModelData getTexturedModelData() {
//ModelData modelData = new ModelData();
//ModelPartData modelPartData = modelData.getRoot();
//ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0,6).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true), ModelTransform.pivot(0.0F,0.0F,0.0F));
//ModelPartData modelPartData2 = modelPartData1.addChild("crown", ModelPartBuilder.create(), ModelTransform.pivot(0.0F,-4.0F,0.0F));
//modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(0,39).cuboid(-11.0F, -9.0F, 3.0F, 16.0F, 9.0F, 0.0F, 0.0F, true), ModelTransform.pivot(6.0F,2.0F,3.0F));
//modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(0,22).cuboid(-5.0F, -9.0F, 3.0F, 16.0F, 9.0F, 0.0F, 0.0F, true), ModelTransform.pivot(-6.0F,2.0F,3.0F));
//modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(7,30).cuboid(-4.0F, -8.0F, 3.0F, 8.0F, 8.0F, 1.0F, -0.5F, true), ModelTransform.pivot(0.0F,-4.0F,0.0F));
//return TexturedModelData.of(modelData,32,32);
//        setRotationAngle(west_r1, 0.0F, 2.5307F, 0.0F);
//        setRotationAngle(east_r1, 0.0F, -2.5307F, 0.0F);
//        setRotationAngle(south_r1, -0.2618F, 0.0F, 0.0F);
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