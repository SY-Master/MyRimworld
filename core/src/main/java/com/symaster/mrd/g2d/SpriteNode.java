package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class SpriteNode extends Node {

    private final Sprite sprite;

    public SpriteNode(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
