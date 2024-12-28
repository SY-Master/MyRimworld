package com.symaster.mrd.game.data;

import com.symaster.mrd.g2d.scene.Scene;

import java.io.Serializable;

/**
 * 存档
 *
 * @author yinmiao
 * @since 2024/12/27
 */
public class Save implements Serializable {
    private static final long serialVersionUID = 1L;

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
