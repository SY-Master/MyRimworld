package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.symaster.mrd.api.PositionConverter;

/**
 * @author yinmiao
 * @since 2024/12/24
 */
public class ViewportNodeOrthographic extends OrthographicCameraNode {

    private final Viewport viewport;
    private final Vector2 topRight = new Vector2();
    private final Vector2 btnLeft = new Vector2();
    private final Rectangle worldRectangle = new Rectangle();

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
    public PositionConverter newConverter() {
        return new PositionConverter() {
            @Override
            public void toWorld(Vector2 screen) {
                viewport.unproject(screen);
            }

            @Override
            public void toScreen(Vector2 world) {
                viewport.project(world);
            }
        };
    }

    @Override
    public Rectangle getWorldRectangle() {
        topRight.set(viewport.getScreenWidth(), 0);
        btnLeft.set(0, viewport.getScreenHeight());

        viewport.unproject(topRight);
        viewport.unproject(btnLeft);

        worldRectangle.set(btnLeft.x, btnLeft.y, topRight.x - btnLeft.x, topRight.y - btnLeft.y);

        return worldRectangle;
    }

    @Override
    public void beginDraw() {
        viewport.apply();
    }

}
