package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;

/**
 * 噪声算法
 */
public class Noise extends Node {

    private final int seed;

    public Noise() {
        this.seed = 0;
    }

    public Noise(int seed) {
        this.seed = seed;
    }

    public float interpolatedNoise(float x, float y) {
        // 使用 Math.floor 来确保我们总是向下取整。
        int integerX = (int) Math.floor(x);
        // 计算小数部分，保证即使是负数也能正确处理。
        float fractionalX = x - (float) integerX;

        int integerY = (int) Math.floor(y);
        float yx = y - (float) integerY;

        float v1 = smoothNoise(integerX, integerY);
        float v2 = smoothNoise(integerX + 1, integerY);
        float v3 = smoothNoise(integerX, integerY + 1);
        float v4 = smoothNoise(integerX + 1, integerY + 1);

        float i1 = interpolateCosine(v1, v2, fractionalX);
        float i2 = interpolateCosine(v3, v4, fractionalX);

        return interpolateCosine(i1, i2, yx);
    }

    private float noise(int x, int y) {
        int n = x + y * 57 + seed;
        n = (n << 13) ^ n;
        return 1 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824f;
    }

    private float smoothNoise(int x, int y) {
        float corners = (noise(x + 1, y + 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x - 1, y - 1)) / 16f;
        float sides = (noise(x, y + 1) + noise(x, y - 1) + noise(x - 1, y) + noise(x + 1, y)) / 8f;
        float center = noise(x, y) / 4f;

        return corners + sides + center;
    }

    // x must be in the range [0,1]
    private float interpolateLinear(float a, float b, float x) {
        return a * (1 - x) + b * x;
    }

    // x must be in the range [0,1]
    private float interpolateCosine(float a, float b, float x) {
        float ft = x * (float) Math.PI;
        float f = (1 - ((float) Math.cos(ft))) * 0.5f;
        return a * (1 - f) + b * f;
    }

}
