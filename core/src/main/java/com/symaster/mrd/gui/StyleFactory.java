package com.symaster.mrd.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author yinmiao
 * @since 2024/12/20
 */
public class StyleFactory implements Disposable {

    private boolean setup = false;
    private BitmapFont font = null;
    private NinePatch ninePatch;
    private NinePatchDrawable ninePatchDrawable;
    private TextButton.TextButtonStyle defaultTextButtonStyle;

    public void setup(BitmapFont defaultFont) {
        this.font = defaultFont;
        setup = true;
    }

    public TextButton.TextButtonStyle defaultTextButtonStyle() {
        if (!setup) {
            return null;
        }

        if (ninePatch == null) {
            ninePatch = new NinePatch(new Texture(Gdx.files.internal("default.9.png")), 2, 2, 2, 2);
        }

        if (ninePatchDrawable == null) {
            ninePatchDrawable = new NinePatchDrawable(ninePatch);
        }

        if (defaultTextButtonStyle == null) {
            defaultTextButtonStyle = new TextButton.TextButtonStyle(ninePatchDrawable, ninePatchDrawable,
                                                                    ninePatchDrawable, font);
        }

        return defaultTextButtonStyle;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        if (setup) {
            font.dispose();
            setup = false;
        }
    }

}
