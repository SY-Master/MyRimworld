package com.symaster.mrd.gui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.symaster.mrd.game.ui.FooterMenu;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class FooterMenuContainer {
    private final FooterMenu footerMenu;
    private final TextButton menuBtn;
    private MenuActor menuActor;

    public FooterMenuContainer(FooterMenu footerMenu, TextButton menuBtn) {
        this.footerMenu = footerMenu;
        this.menuBtn = menuBtn;
    }

    public MenuActor getMenuActor() {
        return menuActor;
    }

    public void setMenuActor(MenuActor menuActor) {
        this.menuActor = menuActor;
    }

    public FooterMenu getFooterMenu() {
        return footerMenu;
    }

    public TextButton getMenuBtn() {
        return menuBtn;
    }
}
