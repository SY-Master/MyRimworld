package com.symaster.mrd.api;

import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.input.BridgeInputProcessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/6/23
 */
public abstract class BasePage implements Creation, Disposable {

    private final Set<ChangeListener> changeListeners = new HashSet<>();

    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
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

    protected void changePage(BasePage page) {
        for (ChangeListener changeListener : changeListeners) {
            changeListener.change(this, page);
        }
    }

    protected void toHomePage() {
        for (ChangeListener changeListener : changeListeners) {
            changeListener.toHome(this);
        }
    }

    public List<BridgeInputProcessor> getInputProcessors() {
        return Collections.emptyList();
    }

}
