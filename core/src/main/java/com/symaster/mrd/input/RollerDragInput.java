package com.symaster.mrd.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.api.PositionConverter;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class RollerDragInput extends InputNode {
    private final Vector2 cache_vector2 = new Vector2();
    private final Node target;
    private PositionConverter positionConverter;

    private float nodeStartX;
    private float nodeStartY;
    private float startX;
    private float startY;
    private boolean dragging;

    public RollerDragInput(Node target) {
        this.target = target;
    }

    public PositionConverter getPositionConverter() {
        return positionConverter;
    }

    public void setPositionConverter(PositionConverter positionConverter) {
        this.positionConverter = positionConverter;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (positionConverter != null && dragging) {

            cache_vector2.set(screenX, screenY);
            positionConverter.toWorld(cache_vector2);

            float x = cache_vector2.x;
            float y = cache_vector2.y;

            cache_vector2.set(startX, startY);
            positionConverter.toWorld(cache_vector2);

            float xOffset = x - cache_vector2.x;
            float yOffset = y - cache_vector2.y;

            float desX = nodeStartX - xOffset;
            float desY = nodeStartY - yOffset;

            target.setPosition(desX, desY);
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
        if (Input.Buttons.MIDDLE == button && positionConverter != null) {
            draggedStart(screenX, screenY);
            return true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void draggedStart(int screenX, int screenY) {
        this.startX = screenX;
        this.startY = screenY;

        this.nodeStartX = target.getPositionX();
        this.nodeStartY = target.getPositionY();
        this.dragging = true;
    }
}
