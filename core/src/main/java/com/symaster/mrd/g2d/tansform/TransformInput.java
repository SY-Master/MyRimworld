package com.symaster.mrd.g2d.tansform;

import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class TransformInput extends Node {

    private final Vector2 vector2 = new Vector2();

    public TransformInput(String globalId) {
        super(globalId);
    }

    public TransformInput() {
    }

    public Vector2 getVector2() {
        return vector2;
    }

}
