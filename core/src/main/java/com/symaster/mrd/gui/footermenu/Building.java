package com.symaster.mrd.gui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.gui.FooterMenu;
import com.symaster.mrd.gui.LayoutConfig;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class Building implements FooterMenu {
    @Override
    public String title() {
        return GdxText.val("建筑规划");
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
        return -1;
    }
}
