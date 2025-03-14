package com.symaster.mrd.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.drawable.SolidColorDrawable;

/**
 * 加载界面
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class Loading extends Stage {

    private final Texture texture;
    private final Texture borderTexture;
    private final Texture border1Texture;
    private final ProgressBar progressBar;
    private final Image bg;
    private final Image log;

    public Loading() {
        super(new ScreenViewport());

        texture = new Texture(Gdx.files.internal("log.png"));
        borderTexture = new Texture(Gdx.files.internal("border0.png"));
        border1Texture = new Texture(Gdx.files.internal("white.png"));

        SolidColorDrawable solidColorDrawable = new SolidColorDrawable(new Color(0.19f, 0.56f, 0.79f, 1f));
        bg = new Image(solidColorDrawable);

        addActor(bg);

        log = new Image(texture);
        log.setSize(texture.getWidth(), texture.getHeight());

        addActor(log);

        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = new NinePatchDrawable(new NinePatch(borderTexture, 1, 1, 1, 1));
        // style.knob = new NinePatchDrawable(new NinePatch(border1Texture, 1, 1, 1, 1));
        style.knobBefore = new NinePatchDrawable(new NinePatch(border1Texture));

        progressBar = new ProgressBar(0, 1, 0.1f, false, style);

        addActor(progressBar);
    }

    public void setProgressValue(float progress) {
        progressBar.setValue(progress);
    }

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
        borderTexture.dispose();
        border1Texture.dispose();
    }

    public void logic(float delta) {

    }

    public void render() {
        getViewport().apply();
        draw();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        bg.setSize(width, height);
        progressBar.setSize(width, 5);

        float i = width / 2f - log.getWidth() / 2;
        float j = height / 2f - log.getHeight() / 2;

        log.setPosition(i, j);
    }

}
