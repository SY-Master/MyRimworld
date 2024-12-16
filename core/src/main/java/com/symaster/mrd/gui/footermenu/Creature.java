package com.symaster.mrd.gui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.gui.FooterMenu;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class Creature implements FooterMenu {

    @Override
    public String title() {
        return GdxText.val("生物列表");
    }

    @Override
    public Actor panel() {
        return null;
    }
}
