package com.symaster.mrd.game.entity.map.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.game.entity.map.TileMapTexture;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class PlantTexture extends TileMapTexture {

    private final TextureRegion textureRegion;

    public PlantTexture(AssetManager assetManager) {
        Texture texture = assetManager.get("TX Plant.png", Texture.class);
        textureRegion = new TextureRegion(texture, 20, 10, 120, 146);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

}
