package com.mojang.minecraft.mob;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.item.Item;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.model.AnimalModel;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.render.TextureManager;

public class Sheep extends QuadrupedMob {

    public static final long serialVersionUID = 0L;
    public boolean hasFur = true;
    public boolean grazing = false;
    public int grazingTime = 0;
    public float graze;
    public float grazeO;

    public Sheep(Level level, float posX, float posY, float posZ) {
        super(level, posX, posY, posZ);
        setSize(1.4F, 1.72F);
        this.setPos(posX, posY, posZ);
        heightOffset = 1.72F;
        modelName = "sheep";
        textureName = "/mob/sheep.png";
        ai = new Sheep$1(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        grazeO = graze;
        if (grazing) {
            graze += 0.2F;
        } else {
            graze -= 0.2F;
        }

        if (graze < 0F) {
            graze = 0F;
        }

        if (graze > 1F) {
            graze = 1F;
        }

    }

    @Override
    public void die(Entity killedBy) {
        if (killedBy != null) {
            killedBy.awardKillScore(this, 10);
        }

        int var2 = (int) (Math.random() + Math.random() + 1D);

        for (int var3 = 0; var3 < var2; ++var3) {
            level.addEntity(new Item(level, x, y, z, Block.BROWN_MUSHROOM.id));
        }

        super.die(killedBy);
    }

    @Override
    public void hurt(Entity entity, int amount) {
        if (hasFur && entity instanceof Player) {
            hasFur = false;
            int var3 = (int) (Math.random() * 3D + 1D);

            for (amount = 0; amount < var3; ++amount) {
                level.addEntity(new Item(level, x, y, z, Block.WHITE_WOOL.id));
            }

        } else {
            super.hurt(entity, amount);
        }
    }

    @Override
    public void renderModel(TextureManager var1, float var2, float var3, float var4,
            float yawDegrees, float pitchDegrees, float scale) {
        AnimalModel var8;
        float var9 = (var8 = (AnimalModel) modelCache.getModel("sheep")).head.y;
        float var10 = var8.head.z;
        var8.head.y += (grazeO + (graze - grazeO) * var3) * 8F;
        var8.head.z -= grazeO + (graze - grazeO) * var3;
        super.renderModel(var1, var2, var3, var4, yawDegrees, pitchDegrees, scale);
        if (hasFur || modelName.equals("sheep.fur")) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.load("/mob/sheep_fur.png"));
            GL11.glDisable(GL11.GL_CULL_FACE);
            AnimalModel var11;
            (var11 = (AnimalModel) modelCache.getModel("sheep.fur")).head.yaw = var8.head.yaw;
            var11.head.pitch = var8.head.pitch;
            var11.head.y = var8.head.y;
            var11.head.x = var8.head.x;
            var11.body.yaw = var8.body.yaw;
            var11.body.pitch = var8.body.pitch;
            var11.leg1.pitch = var8.leg1.pitch;
            var11.leg2.pitch = var8.leg2.pitch;
            var11.leg3.pitch = var8.leg3.pitch;
            var11.leg4.pitch = var8.leg4.pitch;
            var11.head.render(scale);
            var11.body.render(scale);
            var11.leg1.render(scale);
            var11.leg2.render(scale);
            var11.leg3.render(scale);
            var11.leg4.render(scale);
        }

        var8.head.y = var9;
        var8.head.z = var10;
    }
}
