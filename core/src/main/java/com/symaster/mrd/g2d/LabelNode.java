package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.symaster.mrd.game.GameSingleData;

import static com.symaster.mrd.game.GameSingleData.skinProxy;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class LabelNode extends Node {

    private Label label;
    private float parentAlpha = 1.0f;

    public LabelNode(String text, int fontSize) {

        if (text == null || text.isEmpty()) {
            text = "EMPTY";
        }

        DynamicFontManager dynamicFontManager = skinProxy.getSkin().get(DynamicFontManager.class);

        BitmapFont font = dynamicFontManager.getFont(FontEnum.BASE_FONT, fontSize);

        Label.LabelStyle aDefault = new Label.LabelStyle(font,
                                                         skinProxy.getSkin().getColor("default"));

        this.label = new Label(text, aDefault);

        setVisible(true);
    }

    public float getParentAlpha() {
        return parentAlpha;
    }

    public void setParentAlpha(float parentAlpha) {
        this.parentAlpha = parentAlpha;
    }

    @Override
    public boolean logic(float delta) {
        boolean logic = super.logic(delta);

        setVisible(GameSingleData.rootCamZoom.getZoom() <= 1.3);

        return logic;
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        label.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        label.draw(spriteBatch, parentAlpha);
    }

    public void setText(String name) {
        label.setText(name);
    }

    public void setLabelAlignment(int center) {
        label.setAlignment(center);
    }

    public void setLabelSize(float labelW, float v) {
        label.setSize(labelW, v);
    }

}
