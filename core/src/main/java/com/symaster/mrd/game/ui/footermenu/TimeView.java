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

    private TimeViewPanel timeViewPanel;

    public TimeView() {
        setPanelNormallyOpen(true);
    }

    @Override
    public void create() {
        super.create();
        timeViewPanel = new TimeViewPanel(getSkin(), this);
    }

    @Override
    public Actor key() {
        return null;
    }

    @Override
    public Actor panel() {
        return timeViewPanel;
    }

    @Override
    public LayoutConfig layoutConfig() {
        return new LayoutConfig() {
            @Override
            public UIPosition uiPosition() {
                return UIPosition.LEFT_UP;
            }

            @Override
            public BTNPosition btnPosition() {
                return null;
            }

            @Override
            public int panelWidth(int sceneWidth) {
                return 10;
            }

            @Override
            public int panelHeight(int sceneHeight) {
                return 20;
            }
        };
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);
        timeViewPanel.logic(delta);
    }
}
