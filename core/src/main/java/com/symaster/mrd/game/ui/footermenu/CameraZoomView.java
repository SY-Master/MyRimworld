package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.symaster.mrd.game.ui.MainStageUI;
import com.symaster.mrd.game.ui.MainStageUIItem;
import com.symaster.mrd.gui.UIPosition;

import java.awt.*;

/**
 * @author yinmiao
 * @since 2025/1/9
 */
public class CameraZoomView extends MainStageUIItem {

    ProgressBar progressBar;

    @Override
    public void create() {
        super.create();

        progressBar = new ProgressBar(0.1f, 5.0f, 0.1f, false, getSkin().get("default", ProgressBar.ProgressBarStyle.class));
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

        MainStageUI mainStageUI = getMainStageUI();
        if (mainStageUI == null) {
            return;
        }



    }
}
