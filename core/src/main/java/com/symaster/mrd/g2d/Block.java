package com.symaster.mrd.g2d;

import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/24
 */
public class Block {

    private final int x;
    private final int y;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float toX1(Scene scene) {
        float blockSize = scene.getBlockSize();
        return x * blockSize;
    }

    public float toX2(Scene scene) {
        return toX1(scene) + scene.getBlockSize();
    }

    public float toY1(Scene scene) {
        float blockSize = scene.getBlockSize();
        return y * blockSize;
    }

    public float toY2(Scene scene) {
        return toY1(scene) + scene.getBlockSize();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;
        return x == block.x && y == block.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Block{" + "x=" + x + ", y=" + y + '}';
    }

}
