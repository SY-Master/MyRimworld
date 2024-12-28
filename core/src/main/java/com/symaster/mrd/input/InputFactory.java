package com.symaster.mrd.input;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class InputFactory implements com.badlogic.gdx.InputProcessor, Serializable {
    private static final long serialVersionUID = 1L;

    private final Set<com.badlogic.gdx.InputProcessor> inputProcessors = new HashSet<>();

    public void add(com.badlogic.gdx.InputProcessor processor) {
        inputProcessors.add(processor);
    }

    public void remove(com.badlogic.gdx.InputProcessor processor) {
        inputProcessors.remove(processor);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.keyDown(keycode)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.keyUp(keycode)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean keyTyped(char character) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.keyTyped(character)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.touchDown(screenX, screenY, pointer, button)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.touchUp(screenX, screenY, pointer, button)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.touchCancelled(screenX, screenY, pointer, button)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.touchDragged(screenX, screenY, pointer)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.mouseMoved(screenX, screenY)) {
                rtn = true;
            }
        }

        return rtn;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (inputProcessors.isEmpty()) {
            return false;
        }

        boolean rtn = false;

        for (com.badlogic.gdx.InputProcessor inputProcessor : inputProcessors) {
            if (inputProcessor.scrolled(amountX, amountY)) {
                rtn = true;
            }
        }

        return rtn;
    }
}
