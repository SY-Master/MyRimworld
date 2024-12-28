package com.symaster.mrd.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.game.ui.page.MainMenuBtn;
import com.symaster.mrd.game.ui.page.PlayGameSetting;

/**
 * 主菜单
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class MainMenu extends Stage {

    private final Image bg;
    private final MainMenuBtn mainMenuBtn;
    private final PlayGameSetting playGameSetting;

    public MainMenu(Skin skin, AssetManager assetManager) {
        super(new ScreenViewport());

        bg = new Image(assetManager.get("white.png", Texture.class));
        bg.setColor(0.19f, 0.56f, 0.79f, 1f);
        addActor(bg);

        mainMenuBtn = new MainMenuBtn(skin, assetManager);
        mainMenuBtn.getPlayGameBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toPlayGameClick();
            }
        });
        addActor(mainMenuBtn);

        addActor((playGameSetting = new PlayGameSetting(skin, assetManager)));
        playGameSetting.setVisible(false);
        playGameSetting.getBackBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backClick();
            }
        });
        playGameSetting.getPlayGame().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playGameClick();
            }
        });


        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * 开始游戏
     */
    private void playGameClick() {

    }

    private void backClick() {
        mainMenuBtn.setVisible(true);
        playGameSetting.setVisible(false);
    }

    private void toPlayGameClick() {
        mainMenuBtn.setVisible(false);
        playGameSetting.setVisible(true);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        bg.setSize(width, height);
        mainMenuBtn.resize(width, height);
        playGameSetting.resize(width, height);
    }

    public void logic(float delta) {

    }

    public void render() {
        getViewport().apply();
        draw();
    }
}
