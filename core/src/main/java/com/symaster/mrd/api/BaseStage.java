package com.symaster.mrd.api;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.symaster.mrd.game.GameSingleData;


/**
 * @author yinmiao
 * @since 2025/6/21
 */
public abstract class BaseStage extends Stage {

    /**
     * Creates a stage with a {@link ScalingViewport} set to {@link Scaling#stretch}. The stage will use its own {@link Batch}
     * which will be disposed when the stage is disposed.
     */
    public BaseStage() {
    }

    public void created() {

    }

    public void logic(float delta) {

    }

    public void render() {

    }

    public void resize(int width, int height) {

    }

    public <T> T getAsset(String key, Class<T> clazz) {
        return GameSingleData.mrAssetManager.get(getClass().getName(), key, clazz);
    }

    public <T> T getGlobalAsset(String key, Class<T> clazz) {
        return GameSingleData.mrAssetManager.getGlobal(key, clazz);
    }

}
