package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.symaster.mrd.game.ui.MainStageUIItem;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.gui.LayoutConfig;
import com.symaster.mrd.gui.UIPosition;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class Setting extends MainStageUIItem {
    private TextButton textButton;

    @Override
    public void create() {
        super.create();

        textButton = new TextButton(GdxText.val("设置"), getSkin());
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
    public LayoutConfig layoutConfig() {
        return new LayoutConfig() {
            @Override
            public UIPosition uiPosition() {
                return UIPosition.LEFT_DOWN;
            }

            @Override
            public BTNPosition btnPosition() {
                return BTNPosition.BottomMenu;
            }

            @Override
            public int panelWidth(int sceneWidth) {
                return 0;
            }

            @Override
            public int panelHeight(int sceneHeight) {
                return 0;
            }
        };
    }

}
