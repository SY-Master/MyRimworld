package com.symaster.mrd.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.util.GdxText;

/**
 * 主菜单
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class MainMenu extends Stage {

    private final Image bg;
    private final Image log;
    private final TextButton playGame;
    private final TextButton loadGame;
    private final TextButton setting;

    public MainMenu(Skin skin, AssetManager assetManager) {
        super(new ScreenViewport());

        bg = new Image(assetManager.get("white.png", Texture.class));
        bg.setColor(0.19f, 0.56f, 0.79f, 1f);
        addActor(bg);

        log = new Image(assetManager.get("log.png", Texture.class));
        addActor(log);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        playGame = new TextButton(GdxText.val("开始新游戏"), skin);
        playGame.setSize(200, 45);
        addActor(playGame);

        loadGame = new TextButton(GdxText.val("加载游戏"), skin);
        loadGame.setSize(200, 45);
        addActor(loadGame);

        setting = new TextButton("设置", skin);
        setting.setSize(200, 45);
        addActor(setting);

        resize(width, height);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        bg.setSize(width, height);

        // float i = width / 2f - log.getWidth() / 2;
        float j = height / 2f - log.getHeight() / 2;
        log.setPosition(0, j);

        playGame.setPosition(width - playGame.getWidth() - 30, 200);
        loadGame.setPosition(width - loadGame.getWidth() - 30, 150);
        setting.setPosition(width - setting.getWidth() - 30, 100);
    }

    public void logic(float delta) {

    }

    public void render() {
        getViewport().apply();
        draw();
    }
}
