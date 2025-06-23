package com.symaster.mrd.input;

import com.badlogic.gdx.InputProcessor;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class InputBridge implements InputProcessor, Serializable {

    private static final long serialVersionUID = 1L;
    private final List<BridgeInputProcessor> listeners = new LinkedList<>();

    /**
     * 启用的组
     */
    private final Set<String> ENABLE_GROUP = new HashSet<>();

    /**
     * 是否启用事件处理
     */
    private boolean enable = true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void add(BridgeInputProcessor inputProcessor) {
        listeners.add(inputProcessor);
    }

    public void remove(BridgeInputProcessor processor) {
        listeners.remove(processor);
    }

    private List<BridgeInputProcessor> updateSort() {
        if (!enable) {
            return Collections.emptyList();
        }

        return listeners.stream().filter(e -> ENABLE_GROUP.contains(e.group()) && e.actionEnable()).sorted((o1, o2) -> {
            if (o1.uiLayer() == o2.uiLayer()) {
                return Integer.compare(o1.uiSort(), o2.uiSort());
            } else {
                return Integer.compare(o1.uiLayer(), o2.uiLayer());
            }
        }).collect(Collectors.toList());
    }

    public void enableGroup(String group) {
        ENABLE_GROUP.add(group);
    }

    public void disableGroup(String group) {
        ENABLE_GROUP.remove(group);
    }

    @Override
    public boolean keyDown(int keycode) {

        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.keyDown(keycode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.keyUp(keycode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.keyTyped(character)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.touchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.touchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.touchCancelled(screenX, screenY, pointer, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.touchDragged(screenX, screenY, pointer)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.mouseMoved(screenX, screenY)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        for (com.badlogic.gdx.InputProcessor inputProcessor : updateSort()) {
            if (inputProcessor.scrolled(amountX, amountY)) {
                return true;
            }
        }

        return false;
    }

    private static final class SortCacheItem {

        BridgeInputProcessor processor;
        int layer;
        int sort;

    }

    private static final class SortConfig {

        int layer;
        int sort;

    }

}
