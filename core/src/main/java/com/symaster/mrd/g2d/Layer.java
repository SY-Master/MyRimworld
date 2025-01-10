package com.symaster.mrd.g2d;

/**
 * @author yinmiao
 * @since 2025/1/3
 */
public enum Layer {

    MAP(0), // 地图层：地图
    OBJECT(1), // 物体层：角色、草木、建筑、动物
    WEATHER(2), // 天气层
    FLOAT(3), // 浮层
    SKY(4), // 天空层
    ;

    private final int layer;

    Layer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }
}
