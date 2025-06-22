package com.symaster.mrd.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.GameSingleData;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class RollerDragInput extends InputNode {

    private final Vector2 cache_vector2 = new Vector2();
    private Node target;

    private float nodeStartX;
    private float nodeStartY;
    private float startX;
    private float startY;
    private boolean dragging;

    public RollerDragInput(String globalId) {
        super(globalId);
    }

    public RollerDragInput() {
    }

    public RollerDragInput(Node target) {
        this.target = target;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (GameSingleData.positionConverter != null && dragging) {

            cache_vector2.set(screenX, screenY);
            GameSingleData.positionConverter.toWorld(cache_vector2);

            float x = cache_vector2.x;
            float y = cache_vector2.y;

            cache_vector2.set(startX, startY);
            GameSingleData.positionConverter.toWorld(cache_vector2);

            float xOffset = x - cache_vector2.x;
            float yOffset = y - cache_vector2.y;

            float desX = nodeStartX - xOffset;
            float desY = nodeStartY - yOffset;

            getDraggedTarget().setPosition(desX, desY);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.MIDDLE == button) {
            dragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (Input.Buttons.MIDDLE == button && GameSingleData.positionConverter != null) {
            draggedStart(screenX, screenY);
            return true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void draggedStart(int screenX, int screenY) {
        this.startX = screenX;
        this.startY = screenY;

        this.nodeStartX = getDraggedTarget().getPositionX();
        this.nodeStartY = getDraggedTarget().getPositionY();
        this.dragging = true;
    }

    private Node getDraggedTarget() {
        if (target != null) {
            return target;
        }

        return getParent();
    }

}
