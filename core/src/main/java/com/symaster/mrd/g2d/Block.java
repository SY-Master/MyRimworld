package com.symaster.mrd.g2d;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

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
        return "Block{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
