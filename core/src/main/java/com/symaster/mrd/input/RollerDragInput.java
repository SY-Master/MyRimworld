package com.symaster.mrd.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class RollerDragInput extends InputAdapter {

    private final Node target;

    private float nodeStartX;
    private float nodeStartY;
    private float startX;
    private float startY;
    private boolean dragging;

    public RollerDragInput(Node target) {
        this.target = target;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (dragging) {
            float desX = nodeStartX + screenX - startX;
            float desY = nodeStartY + screenY - startY;
            target.setPosition(desX, desY);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.MIDDLE == button) {
            draggedStart(screenX, screenY);
            return true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.MIDDLE == button) {
            dragging = false;
            return true;
        }
        return false;
    }

    private void draggedStart(int screenX, int screenY) {
        this.startX = screenX;
        this.startY = screenY;
        this.nodeStartX = target.getPositionX();
        this.nodeStartY = target.getPositionY();
        this.dragging = true;
    }
}
