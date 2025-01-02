package com.symaster.mrd.game.ui.footermenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.symaster.mrd.game.ui.MainStageUIItem;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.gui.UIPosition;
import com.symaster.mrd.util.GdxText;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public class PartnerMenu extends MainStageUIItem {

    private TextButton textButton;
    private PartnerPanel partnerPanel;

    @Override
    public void create() {
        super.create();

        textButton = new TextButton(GdxText.val("伙伴列表"), getSkin());
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPanel();
            }
        });

        partnerPanel = new PartnerPanel(this);
    }

    @Override
    public Actor key() {
        return textButton;
    }

    @Override
    public Actor panel() {
        return partnerPanel;
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);
        partnerPanel.logic(delta);
    }

    @Override
    public UIPosition uiPosition() {
        return UIPosition.LEFT_DOWN;
    }

    @Override
    public BTNPosition btnPosition() {
        return BTNPosition.BottomMenu;
    }

    @Override
    public int panelWidth(int sceneWidth, int sceneHeight) {
        return (int) (sceneWidth * 0.4f);
    }

    @Override
    public int panelHeight(int sceneWidth, int sceneHeight) {
        return (int) (sceneHeight * 0.9);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (partnerPanel != null) {
            partnerPanel.dispose();
        }
    }
}
