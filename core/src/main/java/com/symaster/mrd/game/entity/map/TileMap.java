package com.symaster.mrd.game.entity.map;

import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class TileMap extends Node {

    public TileMap() {
        setVisible(true);
        setLayer(Layer.MAP.getLayer());
    }

    public void setSize(float w, float h) {

    }
}
