package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.symaster.mrd.api.PositionConverter;
import com.symaster.mrd.g2d.scene.Scene;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yinmiao
 * @since 2024/12/22
 */
public class OrthographicCameraNode extends Node {

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private PositionConverter positionConverter;
    private Comparator<Node> comparator;

    private final List<Node> caches = new LinkedList<>();
    private final List<Node> caches2 = new LinkedList<>();
    private final Vector3 topRight = new Vector3();
    private final Vector3 bottomLeft = new Vector3();
    private final Rectangle worldRectangle = new Rectangle();

    public OrthographicCameraNode(String globalId) {
        super(globalId);
    }

    public OrthographicCameraNode() {
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void created() {
        if (camera== null) {
            this.camera = new OrthographicCamera();
        }

        if (this.spriteBatch == null) {
            this.spriteBatch = new SpriteBatch();
        }

        setForcedLogic(true);

        this.positionConverter = newConverter();
        this.comparator = (o1, o2) -> {
            if (o1.getLayer() == o2.getLayer()) {
                return Integer.compare(o1.getZIndex(), o2.getZIndex());
            }
            return Integer.compare(o1.getLayer(), o2.getLayer());
        };

        super.created();
    }

    public PositionConverter newConverter() {
        return new PositionConverter() {
            @Override
            public void toWorld(Vector2 screen) {
                Vector3 vector3 = new Vector3(screen.x, screen.y, 0);

                camera.unproject(vector3);

                screen.set(vector3.x, vector3.y);
            }

            @Override
            public void toScreen(Vector2 world) {
                Vector3 vector3 = new Vector3(world.x, world.y, 0);

                camera.project(vector3);

                world.set(vector3.x, vector3.y);
            }
        };
    }

    public PositionConverter getPositionConverter() {
        return positionConverter;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Rectangle getWorldRectangle() {
        topRight.set(camera.viewportWidth, 0, 0);
        camera.unproject(topRight);

        bottomLeft.set(0, camera.viewportHeight, 0);
        camera.unproject(bottomLeft);

        worldRectangle.set(bottomLeft.x, bottomLeft.y, topRight.x - bottomLeft.x, topRight.y - bottomLeft.y);
        return worldRectangle;
    }

    @Override
    public void setGdxNodePosition(float x, float y) {
        super.setGdxNodePosition(x, y);
        camera.position.set(x, y, 0);
    }

    public void beginDraw() {

    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void render() {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        caches.clear();
        caches2.clear();

        Rectangle worldRectangle = getWorldRectangle();
        scene.findNodes(worldRectangle, caches, true, 1);

        for (Node item : caches) {
            addTo(item, caches2);
        }

        caches2.sort(comparator);

        beginDraw();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Node node : caches2) {
            drawNode(node);
        }
        spriteBatch.end();
    }

    private void addTo(Node item, List<Node> caches2) {
        if (item.isVisible()) {
            caches2.add(item);

            if (!item.isFusionRender()) {
                for (Node node : item) {
                    addTo(node, caches2);
                }
            }
        }
    }

    private void drawNode(Node node) {
        if (node.isVisible()) {
            node.draw(spriteBatch);

            if (node.isFusionRender()) {
                for (Node child : node) {
                    drawNode(child);
                }
            }
        }
    }

}
