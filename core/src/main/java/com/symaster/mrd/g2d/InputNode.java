package com.symaster.mrd.g2d;

import com.badlogic.gdx.InputProcessor;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class InputNode extends Node implements InputProcessor {

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
        scene.getInputFactory().add(this);
    }

    @Override
    public void extScene(Scene scene) {
        scene.getInputFactory().remove(this);
    }
}
