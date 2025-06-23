package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.symaster.mrd.api.Creation;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.input.BridgeInputProcessor;


/**
 * @author yinmiao
 * @since 2025/6/21
 */
public class StageProcessor extends Stage implements Creation, BridgeInputProcessor {

    private String group;
    private Integer uiLayer;
    private Integer uiSort;
    private boolean enable = true;

    /**
     * Creates a stage with a {@link ScalingViewport} set to {@link Scaling#stretch}. The stage will use its own {@link Batch}
     * which will be disposed when the stage is disposed.
     */
    public StageProcessor() {
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

    public void changePage(StageProcessor page) {

    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setUiLayer(Integer uiLayer) {
        this.uiLayer = uiLayer;
    }

    public void setUiSort(Integer uiSort) {
        this.uiSort = uiSort;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return 组
     */
    @Override
    public String group() {
        return group;
    }

    /**
     * @return 层
     */
    @Override
    public int uiLayer() {
        return uiLayer;
    }

    /**
     * @return 返回当前事件顺序
     */
    @Override
    public int uiSort() {
        return uiSort;
    }

    /**
     * @return 是否启用输入事件
     */
    @Override
    public boolean actionEnable() {
        return enable;
    }

}
