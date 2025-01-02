package com.symaster.mrd.game;

/**
 * @author yinmiao
 * @since 2025/1/2
 */
public enum PageLayer {

    Gui(1),
    Scene(2),

    ;

    private final int layer;

    PageLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }
}

