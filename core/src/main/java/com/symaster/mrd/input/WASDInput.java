package com.symaster.mrd.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.Node;

/**
 * WASD 的输入
 *
 * @author yinmiao
 * @since 2024/12/26
 */
public class WASDInput extends Node {
    private final Vector2 vector2;

    public WASDInput() {
        this(new Vector2());
    }

    public WASDInput(Vector2 vector2) {
        this.vector2 = vector2;
    }

    public Vector2 getVector2() {
        return vector2;
    }

    /**
     * 每帧调用
     */
    @Override
    public void logic(float delta) {
        super.logic(delta);

        vector2.x = 0;
        vector2.y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            vector2.y += 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            vector2.y -= 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            vector2.x -= 1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            vector2.x += 1f;
        }

        vector2.limit(1);
    }
}
