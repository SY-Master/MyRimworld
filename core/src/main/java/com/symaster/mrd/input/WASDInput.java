package com.symaster.mrd.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;

/**
 * WASD 的输入
 *
 * @author yinmiao
 * @since 2024/12/26
 */
public class WASDInput extends InputNode {

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

    private boolean W = false;
    private boolean A = false;
    private boolean S = false;
    private boolean D = false;

    @Override
    public boolean keyUp(int keycode) {
        boolean rtn = false;

        if (Input.Keys.W == keycode) {
            W = false;
            rtn = true;
        }

        if (Input.Keys.A == keycode) {
            A = false;
            rtn = true;
        }

        if (Input.Keys.S == keycode) {
            S = false;
            rtn = true;
        }

        if (Input.Keys.D == keycode) {
            D = false;
            rtn = true;
        }

        update();

        return rtn;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean rtn = false;

        if (Input.Keys.W == keycode) {
            W = true;
            rtn = true;
        }

        if (Input.Keys.A == keycode) {
            A = true;
            rtn = true;
        }

        if (Input.Keys.S == keycode) {
            S = true;
        }

        if (Input.Keys.D == keycode) {
            D = true;
            rtn = true;
        }

        update();

        return rtn;
    }

    private void update() {
        vector2.x = 0;
        vector2.y = 0;

        if (W) {
            vector2.y += 1f;
        }
        if (S) {
            vector2.y -= 1f;
        }
        if (A) {
            vector2.x -= 1f;
        }
        if (D) {
            vector2.x += 1f;
        }

        vector2.limit(1);
    }

}
