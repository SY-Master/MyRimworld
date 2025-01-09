package com.symaster.mrd.game.entity.map.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.game.entity.map.TileMapTexture;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class GrassTexture extends TileMapTexture {

    private final TextureRegion textureRegion;

    public GrassTexture(AssetManager assetManager) {
        Texture grassTexture = assetManager.get("TX Tileset Grass.png", Texture.class);

        textureRegion = new TextureRegion(grassTexture, 0, 0, 32, 32);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public GrassTexture grass() {
        textureRegion.setRegion(0, 0, 32, 32);
        return this;
    }

    public GrassTexture grass2() {
        textureRegion.setRegion(0, 32, 32, 32);
        return this;
    }

    public GrassTexture grass3() {
        textureRegion.setRegion(0, 64, 32, 32);
        return this;
    }

    public GrassTexture flower() {
        textureRegion.setRegion(128, 32, 32, 32);
        return this;
    }

    public GrassTexture flower2() {
        textureRegion.setRegion(192, 32, 32, 32);
        return this;
    }

    public GrassTexture flower3() {
        textureRegion.setRegion(192, 64, 32, 32);
        return this;
    }
}
