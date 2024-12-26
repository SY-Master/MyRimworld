package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class CameraNode extends Node {

    private final Camera camera;

    public CameraNode() {
        this(new OrthographicCamera());
    }

    public CameraNode(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public Rectangle getWorldRectangle() {
        Vector3 topRight = camera.unproject(new Vector3(camera.viewportWidth , 0 , 0));
        Vector3 bottomLeft = camera.unproject(new Vector3(0, camera.viewportHeight, 0));

        return new Rectangle(bottomLeft.x, bottomLeft.y, topRight.x - bottomLeft.x, topRight.y - bottomLeft.y);
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        camera.position.set(x, y, 0);
    }

    public void beginDraw() {

    }
}
