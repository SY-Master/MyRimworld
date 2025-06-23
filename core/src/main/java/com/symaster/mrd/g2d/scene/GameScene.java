package com.symaster.mrd.g2d.scene;

import com.symaster.mrd.api.BasePage;
import com.symaster.mrd.g2d.ViewportNodeOrthographic;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.ui.SceneUI;
import com.symaster.mrd.input.BridgeInputProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * @author yinmiao
 * @since 2025/6/23
 */
public class GameScene extends BasePage {

    private Scene scene;
    private SceneUI sceneUI;
    private ViewportNodeOrthographic camera;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public SceneUI getSceneUI() {
        return sceneUI;
    }

    public void setSceneUI(SceneUI sceneUI) {
        this.sceneUI = sceneUI;
    }

    public ViewportNodeOrthographic getCamera() {
        return camera;
    }

    public void setCamera(ViewportNodeOrthographic camera) {
        this.camera = camera;
    }

    @Override
    public void logic(float delta) {
        scene.logic(delta);
        sceneUI.logic(delta);

        super.logic(delta);
    }

    @Override
    public void render() {

        // 绘制相机
        camera.render();
        // 绘制GUI
        sceneUI.render();

        super.render();
    }

    @Override
    public void resize(int width, int height) {
        sceneUI.resize(width, height);
        if (camera != null) {
            camera.getViewport().update(width, height);
        }
        scene.resize(width, height);
        super.resize(width, height);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        if (scene != null) {
            scene.dispose();
        }

        if (sceneUI != null) {
            sceneUI.dispose();
        }

        if (camera != null) {
            camera.dispose();
        }
    }

    @Override
    public List<BridgeInputProcessor> getInputProcessors() {
        return Arrays.asList(sceneUI, scene);
    }

}
