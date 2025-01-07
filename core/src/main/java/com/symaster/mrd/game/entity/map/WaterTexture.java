package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class WaterTexture extends Node {

    private final TextureRegion textureRegion;

    public WaterTexture(AssetManager assetManager) {
        Texture texture = assetManager.get("Water.png", Texture.class);

        textureRegion = new TextureRegion(texture, 0, 0, 16, 16);
        setVisible(false);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}
