package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/7
 */
public class TileMapFactory extends Node {

    private final GrassTexture grassTexture;
    private final WaterTexture waterTexture;

    public TileMapFactory(AssetManager assetManager) {
        grassTexture = new GrassTexture(assetManager);
        waterTexture = new WaterTexture(assetManager);
    }

    public GrassTexture getGrassTexture() {
        return grassTexture;
    }

    public WaterTexture getWaterTexture() {
        return waterTexture;
    }
}
