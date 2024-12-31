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

    public RollerDragInput(Node target) {
        this.target = target;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchCancelled");
        return super.touchCancelled(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp");
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.MIDDLE == button) {
            draggedStart();
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void draggedStart() {

    }
}
