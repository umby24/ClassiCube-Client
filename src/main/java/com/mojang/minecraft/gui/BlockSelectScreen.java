package com.mojang.minecraft.gui;

import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.SessionData;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.level.tile.BlockID;
import com.mojang.minecraft.render.ShapeRenderer;
import com.mojang.minecraft.render.TextureManager;
import com.mojang.minecraft.render.texture.Textures;

public final class BlockSelectScreen extends GuiScreen {

    private final Timer timer = new Timer();
    private final int milliseconds = 30;
    public TimerTask timertask;
    boolean defaultSizeBlocks = SessionData.allowedBlocks.size() <= 50;
    int blocksPerRow = 13;
    int spacing = 20;
    float lastRotation = 0;

    public BlockSelectScreen() {
        grabsMouse = true;
        start();
        if (defaultSizeBlocks) {
            blocksPerRow = 11;
            spacing = 24;
        }
    }

    String getBlockName(int id) {
        String s;
        if (id < 0 || id > 255) {
            return "";
        }
        try {
            Block b = SessionData.allowedBlocks.get(id);
            if (b == null) {
                return "";
            }
            int ID = b.id;
            BlockID bid = BlockID.values()[ID + 1];
            if (bid == null) {
                s = "";
            } else {
                s = bid.name();
            }
        } catch (Exception e) {
            return "";
        }
        return s;
    }

    private int getBlockOnScreen(int x, int y) {
        for (int i = 0; i < SessionData.allowedBlocks.size(); ++i) {
            int var4 = width / 2 + i % blocksPerRow * spacing + -128 - 3;
            int var5 = height / 2 + i / blocksPerRow * spacing + -60 + 3;
            if (x >= var4 && x <= var4 + 22 && y >= var5 - blocksPerRow
                    && y <= var5 + blocksPerRow) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected final void onMouseClick(int x, int y, int clickType) {
        if (clickType == 0) {
            minecraft.player.inventory.replaceSlot(getBlockOnScreen(x, y));
            minecraft.setCurrentScreen(null);
        }
    }

    @Override
    public final void render(int var1, int var2) {
        var1 = getBlockOnScreen(var1, var2);
        if (defaultSizeBlocks) {
            drawFadingBox(width / 2 - 140, 30, width / 2 + 140, 195, -1878719232, -1070583712);
        } else {
            drawFadingBox(width / 2 - 140, 30, width / 2 + 140, 180, -1878719232, -1070583712);
        }
        if (var1 >= 0) {
            var2 = width / 2 + var1 % blocksPerRow * spacing + -128;
            if (defaultSizeBlocks) {
                drawCenteredString(fontRenderer, getBlockName(var1), width / 2, 180, 16777215);
            } else {
                drawCenteredString(fontRenderer, getBlockName(var1), width / 2, 165, 16777215);
            }
        }

        drawCenteredString(fontRenderer, "Select block", width / 2, 40, 16777215);
        TextureManager textureManager = minecraft.textureManager;
        ShapeRenderer shapeRenderer = ShapeRenderer.instance;
        var2 = textureManager.load(Textures.TERRAIN);
        GL11.glBindTexture(3553, var2);

        for (int i = 0; i < SessionData.allowedBlocks.size(); ++i) {
            Block block = SessionData.allowedBlocks.get(i);
            if (block != null) {
                GL11.glPushMatrix();
                int var5 = width / 2 + i % blocksPerRow * spacing + -128;
                int var6 = height / 2 + i / blocksPerRow * spacing + -60;
                GL11.glTranslatef(var5, var6, 0F);
                GL11.glScalef(9F, 9F, 9F);
                GL11.glTranslatef(1F, 0.5F, 8F);
                GL11.glRotatef(-30F, 1F, 0F, 0F);
                GL11.glRotatef(45F, 0F, 1F, 0F);
                if (var1 == i) {
                    GL11.glScalef(1.6F, 1.6F, 1.6F);
                    GL11.glRotatef(lastRotation, 0F, 1F, 0F);
                }

                GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
                GL11.glScalef(-1F, -1F, -1F);
                shapeRenderer.begin();
                block.renderFullBrightness(shapeRenderer);
                shapeRenderer.end();
                GL11.glPopMatrix();
            }
        }

    }

    void rotate() {
        lastRotation += 2.7F;
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rotate();
            }
        }, milliseconds, milliseconds);
    }
}