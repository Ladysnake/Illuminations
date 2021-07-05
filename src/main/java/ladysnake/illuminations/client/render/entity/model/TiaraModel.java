//// Made with Model Converter by Globox_Z
//// Generate all required imports
//// Made with Blockbench 3.8.4
//// Exported for Minecraft version 1.15
//// Paste this class into your mod and generate all required imports
//package ladysnake.illuminations.client.render.entity.model;
//import net.minecraft.client.model.Model;
//public class TiaraModel extends Model {
//    private final ModelPart head;
//    private final ModelPart crown;
//    private final ModelPart west_r1;
//    private final ModelPart north_r1;
//    private final ModelPart east_r1;
//    private final ModelPart south_r1;
//    public TiaraModel(ModelPart root) {
//this.head = root.getChild("head");
//this.crown = this.head.getChild("crown");
//this.south_r1 = this.crown.getChild("south_r1");
//this.east_r1 = this.crown.getChild("east_r1");
//this.north_r1 = this.crown.getChild("north_r1");
//this.west_r1 = this.crown.getChild("west_r1");
//}
//public static TexturedModelData getTexturedModelData() {
//ModelData modelData = new ModelData();
//ModelPartData modelPartData = modelData.getRoot();
//ModelPartData modelPartData1 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0,48).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true), ModelTransform.pivot(0.0F,0.0F,0.0F));
//ModelPartData modelPartData2 = modelPartData1.addChild("crown", ModelPartBuilder.create().uv(0,4).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, -0.5F, true).uv(0,0).cuboid(-4.0F, -5.0F, -4.0F, 8.0F, 1.0F, 8.0F, -0.5F, false), ModelTransform.pivot(0.0F,-4.0F,0.0F));
//modelPartData2.addChild("west_r1", ModelPartBuilder.create().uv(6,39).cuboid(-5.1F, -6.1F, 3.03F, 10.0F, 8.0F, 1.0F, -0.5F, true), ModelTransform.pivot(0.0F,-6.0F,0.0F));
//modelPartData2.addChild("north_r1", ModelPartBuilder.create().uv(8,17).cuboid(0.0F, -4.3F, 3.2F, 6.0F, 6.0F, 1.0F, -0.5F, true), ModelTransform.pivot(3.0F,-6.0F,0.0F));
//modelPartData2.addChild("east_r1", ModelPartBuilder.create().uv(6,23).cuboid(-4.9F, -6.1F, 3.03F, 10.0F, 8.0F, 1.0F, -0.5F, true), ModelTransform.pivot(0.0F,-6.0F,0.0F));
//modelPartData2.addChild("south_r1", ModelPartBuilder.create().uv(7,32).cuboid(-4.0F, -6.3F, 3.2F, 8.0F, 8.0F, 1.0F, -0.75F, true), ModelTransform.pivot(0.0F,-6.0F,0.0F));
//return TexturedModelData.of(modelData,32,32);
//        setRotationAngle(west_r1, -0.0436F, 1.5708F, 0.0F);
//        setRotationAngle(north_r1, -0.0873F, 3.1416F, 0.0F);
//        setRotationAngle(east_r1, -0.0436F, -1.5708F, 0.0F);
//        setRotationAngle(south_r1, -0.0873F, 0.0F, 0.0F);
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