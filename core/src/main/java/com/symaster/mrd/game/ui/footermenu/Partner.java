package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.game.ui.FooterMenu;
import com.symaster.mrd.game.ui.MainStageUI;
import com.symaster.mrd.gui.LayoutConfig;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class Partner extends FooterMenu {

    public Partner(MainStageUI mainStageUI) {
        super(mainStageUI);
    }

    @Override
    public String title() {
        return GdxText.val("伙伴列表");
    }

    @Override
    public Actor panel() {
        return null;
    }

    @Override
    public LayoutConfig layoutConfig() {
        return null;
    }

}
