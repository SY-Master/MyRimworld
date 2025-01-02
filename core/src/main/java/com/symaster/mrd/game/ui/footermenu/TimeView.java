package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.game.ui.MainStageUI;
import com.symaster.mrd.game.ui.MainStageUIItem;
import com.symaster.mrd.gui.UIPosition;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class TimeView extends MainStageUIItem {

    private Label label;

    public TimeView() {
        setPanelNormallyOpen(true);
    }

    @Override
    public void create() {
        super.create();

        label = new Label("0000-00-00-00-00", getSkin().get("nameLabel", Label.LabelStyle.class));
    }

    @Override
    public Actor key() {
        return null;
    }

    @Override
    public Actor panel() {
        return label;
    }

    @Override
    public UIPosition uiPosition() {
        return UIPosition.LEFT_UP;
    }

    @Override
    public int panelWidth(int sceneWidth, int sceneHeight) {
        return 200;
    }

    @Override
    public int panelHeight(int sceneWidth, int sceneHeight) {
        return 20;
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);

        MainStageUI mainStageUI = getMainStageUI();
        if (mainStageUI == null) {
            return;
        }

        if (mainStageUI.getScene() == null) {
            return;
        }

        Scene scene = mainStageUI.getScene();

        Set<Node> byGroup = scene.getByGroup(Groups.TIMER);
        if (byGroup == null) {
            return;
        }

        GameTime gameTime = (GameTime) byGroup.iterator().next();

        String format = String.format("%s年-%s月-%s日-%s时-%s分", gameTime.getYear(), gameTime.getMonth(), gameTime.getDay(), gameTime.getHour(), gameTime.getMinute());
        label.setText(format);

    }
}
