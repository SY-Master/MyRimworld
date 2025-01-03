package com.symaster.mrd.g2d;

/**
 * @author yinmiao
 * @since 2025/1/3
 */
public enum Layer {

    MAP(0), // 地图层
    OBJECT(1), // 物体层
    FLOAT(2), // 浮层
    SKY(3), // 天空层
    ;

    private final int layer;

    Layer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }
}
