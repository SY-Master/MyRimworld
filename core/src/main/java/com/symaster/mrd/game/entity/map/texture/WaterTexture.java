package com.symaster.mrd.game.entity.map.texture;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.game.entity.map.TileMapTexture;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class WaterTexture extends TileMapTexture {

    private final TextureRegion textureRegion;

    public WaterTexture(AssetManager assetManager) {
        Texture texture = assetManager.get("Water.png", Texture.class);
        textureRegion = new TextureRegion(texture, 0, 0, 16, 16);
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

}
