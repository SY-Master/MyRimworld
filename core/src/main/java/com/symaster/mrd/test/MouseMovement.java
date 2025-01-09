package com.symaster.mrd.test;

import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.Groups;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/1/9
 */
public class MouseMovement extends InputNode {

    private final Vector2 vector2 = new Vector2();

    public MouseMovement() {
        setLayer(Layer.FLOAT.getLayer());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return move(screenX, screenY);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return move(screenX, screenY);
    }

    private boolean move(int screenX, int screenY) {
        if (GameSingleData.positionConverter == null) {
            return false;
        }

        if (getScene() == null) {
            return false;
        }

        Set<Node> byGroup = getScene().getByGroup(Groups.MOUSE_MOVEMENT);
        if (byGroup == null) {
            return false;
        }

        vector2.set(screenX, screenY);
        GameSingleData.positionConverter.toWorld(vector2);

        boolean touched = false;
        for (Node node : byGroup) {
            node.setPosition(vector2.x, vector2.y);
            touched = true;
        }
        return touched;
    }
}
