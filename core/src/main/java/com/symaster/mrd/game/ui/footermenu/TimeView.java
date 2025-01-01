package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.symaster.mrd.game.ui.MainStageUIItem;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.gui.LayoutConfig;
import com.symaster.mrd.gui.UIPosition;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class TimeView extends MainStageUIItem {

    public TimeView() {
        setPanelNormallyOpen(true);
    }

    @Override
    public Actor key() {
        return null;
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
                return null;
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

    @Override
    public void logic(float delta) {
        super.logic(delta);
    }
}
