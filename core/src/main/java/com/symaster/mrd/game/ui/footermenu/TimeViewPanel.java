package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.game.ui.MainStageUI;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class TimeViewPanel extends Group {

    private final Skin skin;
    private final TimeView timeView;
    private final Label label;

    public TimeViewPanel(Skin skin, TimeView timeView) {
        this.skin = skin;
        this.timeView = timeView;

        // 年-月-日-时-分
        label = new Label("0000-00-00-00-00", skin.get("nameLabel", Label.LabelStyle.class));
        addActor(label);
    }


    public void logic(float delta) {
        MainStageUI mainStageUI = timeView.getMainStageUI();
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
