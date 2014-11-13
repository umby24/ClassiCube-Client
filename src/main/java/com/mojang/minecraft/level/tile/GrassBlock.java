package com.mojang.minecraft.level.tile;

import java.util.Random;

import com.mojang.minecraft.level.Level;

public final class GrassBlock extends Block {

    protected GrassBlock(int id) {
        super(id);
        textureId = 3;
        setPhysics(true);
    }

    @Override
    public final int getDrop() {
        return DIRT.getDrop();
    }

    @Override
    public final int getTextureId(int side) {
        return side == 1 ? 0 : side == 0 ? 2 : 3;
    }

    @Override
    public final void update(Level level, int x, int y, int z, Random rand) {
        if (rand.nextInt(4) == 0) {
            if (!level.isLit(x, y, z)) {
                level.setTile(x, y, z, DIRT.id);
            } else {
                for (int var9 = 0; var9 < 4; ++var9) {
                    int var6 = x + rand.nextInt(3) - 1;
                    int var7 = y + rand.nextInt(5) - 3;
                    int var8 = z + rand.nextInt(3) - 1;
                    if (level.getTile(var6, var7, var8) == DIRT.id && level.isLit(var6, var7, var8)) {
                        level.setTile(var6, var7, var8, DIRT.id);
                    }
                }

            }
        }
    }
}
