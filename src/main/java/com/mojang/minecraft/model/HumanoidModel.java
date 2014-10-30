package com.mojang.minecraft.model;

import com.mojang.util.MathHelper;

public class HumanoidModel extends Model {

    public ModelPart head;
    public ModelPart headwear;
    public ModelPart body;
    public ModelPart rightArm;
    public ModelPart leftArm;
    public ModelPart rightLeg;
    public ModelPart leftLeg;

    /**
     * Creates a new humanoid model with the default scaling.
     */
    public HumanoidModel() {
        this(0F);
    }

    /**
     * Creates a new humanoid model with the specified scaling. The scaling seems to make the model
     * wider but still making it occupy the same space.
     *
     * @param scale Scale value to use for the model.
     */
    public HumanoidModel(float scale) {
        head = new ModelPart(0, 0);
        head.setBounds(-4F, -8F, -4F, 8, 8, 8, scale);
        head.allowTransparency = false;
        headwear = new ModelPart(32, 0);
        headwear.setBounds(-4F, -8F, -4F, 8, 8, 8, scale + 0.5F);
        body = new ModelPart(16, 16);
        body.setBounds(-4F, 0F, -2F, 8, 12, 4, scale);
        body.allowTransparency = false;
        rightArm = new ModelPart(40, 16);
        rightArm.setBounds(-3F, -2F, -2F, 4, 12, 4, scale);
        rightArm.setPosition(-5F, 2F, 0F);
        rightArm.allowTransparency = false;
        leftArm = new ModelPart(40, 16);
        leftArm.mirror = true;
        leftArm.setBounds(-1F, -2F, -2F, 4, 12, 4, scale);
        leftArm.setPosition(5F, 2F, 0F);
        leftArm.allowTransparency = false;
        rightLeg = new ModelPart(0, 16);
        rightLeg.setBounds(-2F, 0F, -2F, 4, 12, 4, scale);
        rightLeg.setPosition(-2F, 12F, 0F);
        rightLeg.allowTransparency = false;
        leftLeg = new ModelPart(0, 16);
        leftLeg.mirror = true;
        leftLeg.setBounds(-2F, 0F, -2F, 4, 12, 4, scale);
        leftLeg.setPosition(2F, 12F, 0F);
        leftLeg.allowTransparency = false;
    }

    @Override
    public final void render(float var1, float var2, float var3,
            float yawDegrees, float pitchDegrees, float scale) {
        setRotationAngles(var1, var2, var3, yawDegrees, pitchDegrees, scale);
        head.render(scale);
        body.render(scale);
        rightArm.render(scale);
        leftArm.render(scale);
        rightLeg.render(scale);
        leftLeg.render(scale);
    }

    public void setRotationAngles(float var1, float var2, float var3,
            float yawDegrees, float pitchDegrees, float scale) {
        head.yaw = yawDegrees / (float) (180D / Math.PI);
        head.pitch = pitchDegrees / (float) (180D / Math.PI);
        rightArm.pitch = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 2F * var2;
        rightArm.roll = (MathHelper.cos(var1 * 0.2312F) + 1F) * var2;
        leftArm.pitch = MathHelper.cos(var1 * 0.6662F) * 2F * var2;
        leftArm.roll = (MathHelper.cos(var1 * 0.2812F) - 1F) * var2;
        rightLeg.pitch = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
        leftLeg.pitch = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.4F * var2;
        rightArm.roll += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
        leftArm.roll -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
        rightArm.pitch += MathHelper.sin(var3 * 0.067F) * 0.05F;
        leftArm.pitch -= MathHelper.sin(var3 * 0.067F) * 0.05F;
    }
}
