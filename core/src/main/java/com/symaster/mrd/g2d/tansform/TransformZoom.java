package com.symaster.mrd.g2d.tansform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.input.ScrolledInput;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class TransformZoom extends InputNode {

    private final OrthographicCamera camera;
    private final ScrolledInput scrolledInput;
    /**
     * 缩放移动补偿
     */
    private final Node moveNode;

    public TransformZoom(OrthographicCamera camera) {
        this(camera, new ScrolledInput(), null);
    }

    public TransformZoom(OrthographicCamera camera, Node moveNode) {
        this(camera, new ScrolledInput(), moveNode);
    }

    public TransformZoom(OrthographicCamera camera, ScrolledInput scrolledInput, Node moveNode) {
        this.camera = camera;
        this.scrolledInput = scrolledInput;
        this.moveNode = moveNode;
    }

    public Camera getCamera() {
        return camera;
    }

    public ScrolledInput getScrolledInput() {
        return scrolledInput;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        Vector3 mv = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        // 计算缩放前后的差值
        float zoomFactor = 1.1f;

        float oldZoom = camera.zoom;
        if (amountY > 0) {
            camera.zoom *= zoomFactor;
        } else if (amountY < 0) {
            camera.zoom /= zoomFactor;
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.08f, 4.0f);

        // 更新相机以应用缩放
        camera.update();

        // 根据缩放前后的差值调整相机的位置
        Vector3 m2 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector3 difference = m2.cpy().sub(mv);

        // 平移相机以补偿差值
        moveNode.translate(-difference.x, -difference.y);

        return true;
    }

}
