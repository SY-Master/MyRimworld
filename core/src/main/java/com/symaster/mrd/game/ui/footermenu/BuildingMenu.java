package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.symaster.mrd.game.ui.GameUIItem;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.util.GdxText;

/**
 * 建筑规划
 *
 * @author yinmiao
 * @since 2024/12/27
 */
public class BuildingMenu extends GameUIItem {

    private TextButton textButton;

    @Override
    public void create() {
        super.create();

        textButton = new TextButton(GdxText.val("建筑规划"), getSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPanel();
            }
        });
    }

    @Override
    public Actor key() {
        return textButton;
    }

    @Override
    public Actor panel() {
        return null;
    }

    @Override
    public BTNPosition btnPosition() {
        return BTNPosition.BottomMenu;
    }

}
