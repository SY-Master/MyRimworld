package com.symaster.mrd.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.game.ui.GameUIItem;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class FooterMenuContainer {
    private final GameUIItem gameUIItem;
    private final Actor menuBtn;
    private MenuActor menuActor;

    public FooterMenuContainer(GameUIItem gameUIItem, Actor menuBtn) {
        this.gameUIItem = gameUIItem;
        this.menuBtn = menuBtn;
    }

    public MenuActor getMenuActor() {
        return menuActor;
    }

    public void setMenuActor(MenuActor menuActor) {
        this.menuActor = menuActor;
    }

    public GameUIItem getFooterMenu() {
        return gameUIItem;
    }

    public Actor getMenuBtn() {
        return menuBtn;
    }
}
