package com.symaster.mrd.g2d;

/**
 * @author yinmiao
 * @since 2025/1/3
 */
public enum Layer {

    MAP(0), // 地图
    OBJECT(1), // 物体
    FLOAT(2), // 浮层
    SKY(3), // 天空
    ;

    private final int layer;

    Layer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }
}
