package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class SpriteNode extends Node {

    private final Sprite sprite;

    public SpriteNode(Sprite sprite) {
        this.sprite = sprite;
        setVisible(true);
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }
}
