package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class TileMapWater extends TileMap {
    private final Sprite sprite;

    public TileMapWater(WaterTexture waterTexture) {
        sprite = new Sprite(waterTexture.getTextureRegion());
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        super.setGdxNodePosition(x, y);
        sprite.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        sprite.draw(spriteBatch);
    }

    public void setSize(float w, float h) {
        sprite.setSize(w, h);
    }
}
