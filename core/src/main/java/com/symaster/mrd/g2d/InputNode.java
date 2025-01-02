package com.symaster.mrd.g2d;

import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.PageLayer;
import com.symaster.mrd.input.BridgeInputProcessor;
import com.symaster.mrd.game.GameSingleData;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class InputNode extends Node implements BridgeInputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void onScene(Scene scene) {
        GameSingleData.inputBridge.add(this);
    }

    @Override
    public void extScene(Scene scene) {
        GameSingleData.inputBridge.remove(this);
    }

    @Override
    public int layer() {
        return PageLayer.Scene.getLayer();
    }

    @Override
    public int sort() {
        return 0;
    }
}
