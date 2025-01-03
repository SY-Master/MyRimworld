package com.symaster.mrd.game;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public enum UILayer {

    Gui(1),
    SceneFloat(2),
    SceneNode(3),

    ;

    private final int layer;

    UILayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }
}

