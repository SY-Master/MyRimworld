package com.symaster.mrd.g2d;

import java.util.ArrayList;

/**
 * 场景
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Scene extends ArrayList<Node> {

    public void render() {
        for (Node node : this) {
            node.update();
        }

        // handleInput();
    }

    // private void handleInput() {
    //     if (Gdx.input.isKeyPressed(Input.Keys.A)) {
    //         cam.zoom += 0.02;
    //     }
    //     if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
    //         cam.zoom -= 0.02;
    //     }
    //     if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
    //         cam.translate(-3, 0, 0);
    //     }
    //     if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
    //         cam.translate(3, 0, 0);
    //     }
    //     if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
    //         cam.translate(0, -3, 0);
    //     }
    //     if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
    //         cam.translate(0, 3, 0);
    //     }
    //
    //     cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100 / cam.viewportWidth);
    //
    //     float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
    //     float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
    //
    //     cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
    //     cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    // }

}
