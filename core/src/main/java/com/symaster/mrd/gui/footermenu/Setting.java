package com.symaster.mrd.gui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.gui.FooterMenu;
import com.symaster.mrd.gui.LayoutConfig;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class Setting implements FooterMenu {

    @Override
    public String title() {
        return GdxText.val("设置");
    }

    @Override
    public Actor panel() {
        return null;
    }

    @Override
    public LayoutConfig layoutConfig() {
        return null;
    }

    @Override
    public int sort() {
        return 99;
    }
}
