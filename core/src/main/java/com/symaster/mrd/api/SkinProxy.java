package com.symaster.mrd.api;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author yinmiao
 * @since 2025/3/21
 */
public class SkinProxy {
    private final Skin skin;

    public SkinProxy(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

}
