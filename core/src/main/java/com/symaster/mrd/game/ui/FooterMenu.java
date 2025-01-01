package com.symaster.mrd.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.gui.LayoutConfig;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public abstract class FooterMenu {

    private final MainStageUI mainStageUI;

    public FooterMenu(MainStageUI mainStageUI) {
        this.mainStageUI = mainStageUI;
    }

    public MainStageUI getMainStageUI() {
        return mainStageUI;
    }

    public abstract String name();

    public abstract Actor panel();

    public abstract LayoutConfig layoutConfig();

    public void logic(float delta) {

    }
}
