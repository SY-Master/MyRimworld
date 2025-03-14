package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.entity.map.texture.GrassTexture;
import com.symaster.mrd.game.entity.map.texture.PlantTexture;
import com.symaster.mrd.game.entity.map.texture.WaterTexture;

/**
 * @author yinmiao
 * @since 2025/1/7
 */
public class TileMapFactory extends Node {

    private final GrassTexture grassTexture;
    private final WaterTexture waterTexture;
    private final PlantTexture plantTexture;

    public TileMapFactory(AssetManager assetManager) {
        grassTexture = new GrassTexture(assetManager);
        waterTexture = new WaterTexture(assetManager);
        plantTexture = new PlantTexture(assetManager);
    }

    public GrassTexture getGrassTexture() {
        return grassTexture;
    }

    public WaterTexture getWaterTexture() {
        return waterTexture;
    }

    public PlantTexture getPlantTexture() {
        return plantTexture;
    }

}
