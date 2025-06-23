package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.symaster.mrd.game.ui.GameUIItem;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.util.GdxText;

/**
 * 野生动物面板
 *
 * @author yinmiao
 * @since 2024/12/16
 */
public class CreatureMenu extends GameUIItem {

    private TextButton textButton;
    private Panel panel;

    @Override
    public void created() {
        super.created();

        textButton = new TextButton(GdxText.val("野生动物"), getSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPanel();
            }
        });
        panel = new Panel();
    }

    @Override
    public Actor key() {
        return textButton;
    }

    @Override
    public Actor panel() {
        return panel;
    }

    @Override
    public BTNPosition btnPosition() {
        return BTNPosition.BottomMenu;
    }

    public static class Panel extends Actor {

    }

}
