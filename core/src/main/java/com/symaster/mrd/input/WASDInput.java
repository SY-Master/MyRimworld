package com.symaster.mrd.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.tansform.TransformInput;

/**
 * WASD 的输入
 *
 * @author yinmiao
 * @since 2024/12/26
 */
public class WASDInput extends InputNode {

    private TransformInput transformInput;

    public WASDInput() {
    }

    public TransformInput getTransformInput() {
        return transformInput;
    }

    public void setTransformInput(TransformInput transformInput) {
        this.transformInput = transformInput;
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
        if (transformInput == null) {
            return;
        }

        Vector2 vector2 = transformInput.getVector2();

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
