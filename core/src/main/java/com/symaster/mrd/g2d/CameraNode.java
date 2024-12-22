package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.Camera;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class CameraNode extends Node {

    private final Camera camera;

    public CameraNode(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }
}
