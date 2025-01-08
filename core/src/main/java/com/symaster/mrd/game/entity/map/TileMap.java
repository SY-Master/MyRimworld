package com.symaster.mrd.game.entity.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class TileMap extends Node {

    private final Sprite sprite;

    public TileMap(TileMapTexture texture) {
        setVisible(true);
        setLayer(Layer.MAP.getLayer());
        sprite = new Sprite(texture.getTextureRegion());
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
