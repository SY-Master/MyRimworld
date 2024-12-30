package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class LabelNode extends Node {

    private final Label label;

    public LabelNode(Label label) {
        this.label = label;
        setVisible(true);
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        label.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        label.draw(spriteBatch, 1f);
    }
}
