package com.symaster.mrd.gui;

/**
 * @author yinmiao
 * @since 2024/12/19
 */
public class DefaultLayoutConfig extends LayoutConfig {

    @Override
    public int panelWidth(int sceneWidth) {
        return Math.round(sceneWidth * 0.5f);
    }

    @Override
    public int panelHeight(int sceneHeight) {
        return Math.round(sceneHeight * 0.5f);
    }
}
