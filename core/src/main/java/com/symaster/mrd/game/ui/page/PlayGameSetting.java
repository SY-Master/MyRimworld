package com.symaster.mrd.game.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.symaster.mrd.drawable.SolidColorDrawable;
import com.symaster.mrd.game.GameSingleData;

/**
 * @author yinmiao
 * @since 2024/12/28
 */
public class PlayGameSetting extends Group {

    private final ImageButton backBtn;
    private final TextButton playGame;
    private final Table table;
    private final ScrollPane scrollPane;

    public PlayGameSetting() {
        Texture left = GameSingleData.mrAssetManager.getGlobal("left", Texture.class);

        SolidColorDrawable up = new SolidColorDrawable(new TextureRegion(left), new Color(0, 0, 0, 1));
        SolidColorDrawable dn = new SolidColorDrawable(new TextureRegion(left), new Color(0.1f, 0.1f, 0.1f, 1));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(up, dn, up, up, up, up);

        // 返回按钮
        backBtn = new ImageButton(style);
        backBtn.setSize(45, 45f / left.getWidth() * left.getHeight());
        addActor(backBtn);

        addActor((scrollPane = new ScrollPane((table = new Table()))));

        table.add((playGame = new TextButton("开始游戏", GameSingleData.skinProxy.getSkin())));
    }

    public void resize(int width, int height) {
        backBtn.setPosition(10, height - backBtn.getHeight() - 10);

        float sW = width * 0.8f;
        scrollPane.setSize(sW, height - 55);
        scrollPane.setPosition(width / 2f - sW / 2, 5);

        // table.setWidth(sW - 1);
    }

    public ImageButton getBackBtn() {
        return backBtn;
    }

    public TextButton getPlayGame() {
        return playGame;
    }

    public Table getTable() {
        return table;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

}
