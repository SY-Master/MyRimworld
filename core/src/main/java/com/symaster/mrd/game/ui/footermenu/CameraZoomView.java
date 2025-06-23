package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.symaster.mrd.game.ui.SceneUI;
import com.symaster.mrd.game.ui.GameUIItem;
import com.symaster.mrd.gui.UIPosition;

import java.awt.*;

/**
 * 摄像头缩放显示
 *
 * @author yinmiao
 * @since 2025/1/9
 */
public class CameraZoomView extends GameUIItem {

    ProgressBar progressBar;

    @Override
    public void created() {
        super.created();

        progressBar = new ProgressBar(0.1f, 5.0f, 0.1f, false,
                                      getSkin().get("default", ProgressBar.ProgressBarStyle.class));
        setPanelNormallyOpen(true);
    }

    @Override
    public Actor key() {
        return null;
    }

    @Override
    public Actor panel() {
        return progressBar;
    }

    @Override
    public UIPosition uiPosition() {
        return UIPosition.RIGHT_DOWN;
    }

    @Override
    public void updateMarge(Insets insets, int sceneWidth, int sceneHeight) {
        super.updateMarge(insets, sceneWidth, sceneHeight);

        insets.bottom = 32;
    }

    @Override
    public int panelHeight(int sceneWidth, int sceneHeight) {
        return 10;
    }

    @Override
    public int panelWidth(int sceneWidth, int sceneHeight) {
        return 300;
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);

        SceneUI sceneUI = getMainStageUI();
        if (sceneUI == null) {
        }


    }

}
