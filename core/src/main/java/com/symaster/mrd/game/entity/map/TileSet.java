package com.symaster.mrd.game.entity.map;

import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/7
 */
public class TileSet extends Node {

    public boolean initialized = false;

    public TileSet() {
        setLayer(Layer.MAP.getLayer());
        setVisible(true);
    }

    @Override
    public boolean logic(float delta) {
        if (initialized) {
            return false;
        }
        initialized = true;
        return true;
    }
}
