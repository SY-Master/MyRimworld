package com.symaster.mrd.game.ui.page;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.symaster.mrd.util.GdxText;

/**
 * 主页按钮
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class MainMenuBtn extends Group {

    private final TextButton playGameBtn;
    private final TextButton loadGameBtn;
    private final TextButton settingBtn;
    private final TextButton modeManageBtn;
    private final TextButton extBtn;
    private final Image log;

    public MainMenuBtn(Skin skin, AssetManager assetManager) {
        log = new Image(assetManager.get("log.png", Texture.class));
        addActor(log);

        playGameBtn = new TextButton(GdxText.val("开始新游戏"), skin);
        playGameBtn.setSize(200, 45);
        addActor(playGameBtn);

        loadGameBtn = new TextButton(GdxText.val("加载游戏"), skin);
        loadGameBtn.setSize(200, 45);
        addActor(loadGameBtn);

        settingBtn = new TextButton(GdxText.val("设置"), skin);
        settingBtn.setSize(200, 45);
        addActor(settingBtn);

        modeManageBtn = new TextButton(GdxText.val("模组管理"), skin);
        modeManageBtn.setSize(200, 45);
        addActor(modeManageBtn);

        extBtn = new TextButton(GdxText.val("退出游戏"), skin);
        extBtn.setSize(200, 45);
        addActor(extBtn);
    }

    public void resize(int width, int height) {
        // float i = width / 2f - log.getWidth() / 2;
        float j = height / 2f - log.getHeight() / 2;
        log.setPosition(0, j);

        playGameBtn.setPosition(width - playGameBtn.getWidth() - 30, 250);
        loadGameBtn.setPosition(width - loadGameBtn.getWidth() - 30, 200);
        settingBtn.setPosition(width - settingBtn.getWidth() - 30, 150);
        modeManageBtn.setPosition(width - settingBtn.getWidth() - 30, 100);
        extBtn.setPosition(width - extBtn.getWidth() - 30, 50);
    }

    public TextButton getPlayGameBtn() {
        return playGameBtn;
    }

    public TextButton getLoadGameBtn() {
        return loadGameBtn;
    }

    public TextButton getSettingBtn() {
        return settingBtn;
    }

    public Image getLog() {
        return log;
    }
}
