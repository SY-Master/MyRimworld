package com.symaster.mrd.gui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class FooterMenuContainer {
    private final FooterMenu footerMenu;
    private final TextButton menuBtn;

    public FooterMenuContainer(FooterMenu footerMenu, TextButton menuBtn) {
        this.footerMenu = footerMenu;
        this.menuBtn = menuBtn;
    }

    public FooterMenu getFooterMenu() {
        return footerMenu;
    }

    public TextButton getMenuBtn() {
        return menuBtn;
    }
}
