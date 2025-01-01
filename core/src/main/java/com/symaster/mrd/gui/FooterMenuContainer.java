package com.symaster.mrd.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.symaster.mrd.game.ui.MainStageUIItem;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class FooterMenuContainer {
    private final MainStageUIItem mainStageUIItem;
    private final Actor menuBtn;
    private MenuActor menuActor;

    public FooterMenuContainer(MainStageUIItem mainStageUIItem, Actor menuBtn) {
        this.mainStageUIItem = mainStageUIItem;
        this.menuBtn = menuBtn;
    }

    public MenuActor getMenuActor() {
        return menuActor;
    }

    public void setMenuActor(MenuActor menuActor) {
        this.menuActor = menuActor;
    }

    public MainStageUIItem getFooterMenu() {
        return mainStageUIItem;
    }

    public Actor getMenuBtn() {
        return menuBtn;
    }
}
