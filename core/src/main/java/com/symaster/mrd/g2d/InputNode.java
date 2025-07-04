package com.symaster.mrd.g2d;

import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.GamePageStatus;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.UILayer;
import com.symaster.mrd.input.BridgeInputProcessor;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class InputNode extends Node implements BridgeInputProcessor {

    public InputNode(String globalId) {
        super(globalId);
    }

    public InputNode() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void onScene(Scene scene) {
        super.onScene(scene);
        GameSingleData.inputBridge.add(this);
    }

    @Override
    public void extScene(Scene scene) {
        super.extScene(scene);
        GameSingleData.inputBridge.remove(this);
    }

    @Override
    public String group() {
        return "NODE";
    }

    @Override
    public int uiLayer() {
        return UILayer.SceneNode.getLayer();
    }

    @Override
    public int uiSort() {
        return 0;
    }

    /**
     * @return 是否启用输入事件
     */
    @Override
    public boolean actionEnable() {
        return true;
        // return GameSingleData.gamePageStatus == GamePageStatus.Game;
    }

}
