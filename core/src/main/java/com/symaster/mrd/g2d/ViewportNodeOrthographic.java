package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * @author yinmiao
 * @since 2024/12/24
 */
public class ViewportNodeOrthographic extends OrthographicCameraNode {

    private final Viewport viewport;

    public ViewportNodeOrthographic(float worldWidth, float worldHeight, OrthographicCamera camera) {
        this(new FillViewport(worldWidth, worldHeight, camera));
    }

    public ViewportNodeOrthographic(float worldWidth, float worldHeight) {
        this(new FillViewport(worldWidth, worldHeight));
    }

    public ViewportNodeOrthographic(Viewport viewport) {
        super((OrthographicCamera) viewport.getCamera(), new SpriteBatch());
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public Rectangle getWorldRectangle() {

        Vector2 topRight = viewport.unproject(new Vector2(viewport.getScreenWidth(), 0));
        Vector2 btnLeft = viewport.unproject(new Vector2(0, viewport.getScreenHeight()));

        return new Rectangle(btnLeft.x, btnLeft.y, topRight.x - btnLeft.x, topRight.y - btnLeft.y);
    }

    @Override
    public void beginDraw() {
        viewport.apply();
    }
}
