package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class GrassTexture extends Node {

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

    public GrassTexture flower() {
        textureRegion.setRegion(128, 32, 32, 32);
        return this;
    }
}
