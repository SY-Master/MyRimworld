package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * @author yinmiao
 * @since 2025/6/22
 */
public class Progress extends Widget {

    private Style style;

    public Progress(Skin skin) {
        this(skin.get(Style.class));
    }

    public Progress(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public float getMinWidth() {
        return 10;
    }

    @Override
    public float getMinHeight() {
        return 3;
    }

    /**
     * If this method is overridden, the super method or {@link #validate()} should be called to ensure the widget is laid out.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public static class Style {

    }

}
