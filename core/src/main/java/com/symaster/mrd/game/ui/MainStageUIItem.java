package com.symaster.mrd.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.gui.BTNPosition;
import com.symaster.mrd.gui.UIPosition;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/16
 */
public abstract class MainStageUIItem implements Disposable {

    private final Set<PanelOpenListener> panelOpenListeners;
    private Skin skin;
    private MainStageUI mainStageUI;
    private boolean panelNormallyOpen;
    private final Insets insets;

    public MainStageUIItem() {
        this.panelOpenListeners = new HashSet<>();
        this.panelNormallyOpen = false;
        this.insets = new Insets(0, 0, 0, 0);
    }

    public void create() {}

    public boolean isPanelNormallyOpen() {
        return panelNormallyOpen;
    }

    public void setPanelNormallyOpen(boolean panelNormallyOpen) {
        this.panelNormallyOpen = panelNormallyOpen;
    }

    public void setMainStageUI(MainStageUI mainStageUI) {
        this.mainStageUI = mainStageUI;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public MainStageUI getMainStageUI() {
        return mainStageUI;
    }

    public void openPanel() {
        for (PanelOpenListener panelOpenListener : panelOpenListeners) {
            panelOpenListener.open(this);
        }
    }

    public abstract Actor key();

    public abstract Actor panel();

    public UIPosition uiPosition() {
        return null;
    }

    public BTNPosition btnPosition() {
        return null;
    }

    public int panelWidth(int sceneWidth, int sceneHeight) {
        return 0;
    }

    public int panelHeight(int sceneWidth, int sceneHeight) {
        return 0;
    }

    public final Insets marge(int sceneWidth, int sceneHeight) {
        updateMarge(insets, sceneWidth, sceneHeight);
        return insets;
    }

    private void updateMarge(Insets insets, int sceneWidth, int sceneHeight) {

    }

    public void logic(float delta) {

    }

    public void addPanelOpenListener(PanelOpenListener listener) {
        panelOpenListeners.add(listener);
    }

    public void removePanelOpenListener(PanelOpenListener listener) {
        panelOpenListeners.remove(listener);
    }

    @Override
    public void dispose() {

    }
}
