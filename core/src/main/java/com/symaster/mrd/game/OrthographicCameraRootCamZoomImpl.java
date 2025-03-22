package com.symaster.mrd.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.symaster.mrd.api.RootCamZoom;

/**
 * @author yinmiao
 * @since 2025/3/21
 */
public class OrthographicCameraRootCamZoomImpl implements RootCamZoom {

    private final OrthographicCamera camera;

    public OrthographicCameraRootCamZoomImpl(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public float getZoom() {
        return camera.zoom;
    }

}
