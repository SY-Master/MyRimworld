package com.symaster.mrd.game.ui.page;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.symaster.mrd.drawable.SolidColorDrawable;

/**
 * @author yinmiao
 * @since 2024/12/28
 */
public class PlayGameSetting extends Group {

    private final ImageButton backBtn;
    private final TextButton playGame;
    private final Table table;
    private final ScrollPane scrollPane;

    public PlayGameSetting(Skin skin, AssetManager assetManager) {
        Texture left = assetManager.get("left.png", Texture.class);

        SolidColorDrawable up = new SolidColorDrawable(new TextureRegion(left), new Color(0, 0, 0, 1));
        SolidColorDrawable dn = new SolidColorDrawable(new TextureRegion(left), new Color(0.1f, 0.1f, 0.1f, 1));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(up, dn, up, up, up, up);

        // 返回按钮
        backBtn = new ImageButton(style);
        backBtn.setSize(45, 45f / left.getWidth() * left.getHeight());
        addActor(backBtn);

        addActor((scrollPane = new ScrollPane((table = new Table()))));

        table.add((playGame = new TextButton("开始游戏", skin)));
    }

    public void resize(int width, int height) {
        backBtn.setPosition(10, height - backBtn.getHeight() - 10);

        float sW = width * 0.8f;
        scrollPane.setSize(sW, height - 55);
        scrollPane.setPosition(width / 2f - sW / 2, 5);
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
