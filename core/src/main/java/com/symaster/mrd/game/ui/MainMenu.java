package com.symaster.mrd.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.symaster.mrd.api.BasePage;
import com.symaster.mrd.enums.BridgeInputProcessorEnum;
import com.symaster.mrd.g2d.StageProcessor;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.UILayer;
import com.symaster.mrd.game.ui.page.MainMenuBtn;
import com.symaster.mrd.game.ui.page.PlayGameSetting;
import com.symaster.mrd.input.BridgeInputProcessor;

import java.util.Collections;
import java.util.List;

/**
 * 主页
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class MainMenu extends BasePage {

    private StageProcessor stage;
    private Image bg;
    private MainMenuBtn mainMenuBtn;
    private PlayGameSetting playGameSetting;

    @Override
    public void created() {
        this.stage = new StageProcessor();
        this.stage.setViewport(new ScreenViewport());
        this.stage.setUiLayer(UILayer.Gui.getLayer());
        this.stage.setUiSort(0);
        this.stage.setGroup(BridgeInputProcessorEnum.PAGE.getCode());

        bg = new Image(getGlobalAsset("white", Texture.class));
        bg.setColor(0.19f, 0.56f, 0.79f, 1f);
        this.stage.addActor(bg);

        mainMenuBtn = new MainMenuBtn();
        mainMenuBtn.getPlayGameBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toPlayGameClick();
            }
        });
        this.stage.addActor(mainMenuBtn);

        this.stage.addActor((playGameSetting = new PlayGameSetting()));
        playGameSetting.setVisible(false);
        playGameSetting.getBackBtn().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backClick();
            }
        });

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void backClick() {
        mainMenuBtn.setVisible(true);
        playGameSetting.setVisible(false);
    }

    private void toPlayGameClick() {
        mainMenuBtn.setVisible(false);
        playGameSetting.setVisible(true);
    }

    public void logic(float delta) {

    }

    public void render() {
        this.stage.getViewport().apply();
        this.stage.draw();
    }

    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
        bg.setSize(width, height);
        mainMenuBtn.resize(width, height);
        playGameSetting.resize(width, height);
    }

    public Image getBg() {
        return bg;
    }

    public MainMenuBtn getMainMenuBtn() {
        return mainMenuBtn;
    }

    public PlayGameSetting getPlayGameSetting() {
        return playGameSetting;
    }

    @Override
    public List<BridgeInputProcessor> getInputProcessors() {
        return Collections.singletonList(stage);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }

}
