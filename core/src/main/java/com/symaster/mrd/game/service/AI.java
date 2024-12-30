package com.symaster.mrd.game.service;

import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.Creature;

/**
 * @author yinmiao
 * @since 2024/12/30
 */
public class AI {

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void logic(Creature nodes, float delta) {
        if (scene == null) {
            return;
        }

    }
}
