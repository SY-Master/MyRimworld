package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class CameraNode extends Node implements Disposable {

    private final Camera camera;
    private final SpriteBatch spriteBatch;

    public CameraNode(Camera camera) {
        this.camera = camera;
        this.spriteBatch = new SpriteBatch();
    }

    public Camera getCamera() {
        return camera;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
