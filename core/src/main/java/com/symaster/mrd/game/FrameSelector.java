package com.symaster.mrd.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.Database;
import com.symaster.mrd.game.entity.SelectData;

import java.util.Set;

/**
 * 选择器
 *
 * @author yinmiao
 * @since 2025/1/3
 */
public class FrameSelector extends InputNode {

    private final Sprite sprite;
    private final Vector2 cache = new Vector2();
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private boolean dragged;
    private boolean down;

    public FrameSelector(AssetManager assetManager) {
        dragged = false;
        down = false;

        sprite = new Sprite(assetManager.get("white.png", Texture.class));
        sprite.setColor(new Color(0.129f, 0.847f, 0.427f, 0.3f));

        add(new SpriteNode(sprite));

        setLayer(Layer.FLOAT.getLayer());
        setVisible(false);
        setFusionRender(true);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && GameSingleData.positionConverter != null) {
            down = true;
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && GameSingleData.positionConverter != null) {
            down = false;
            draggedOver(screenX, screenY);
            return true;
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (down) {
            draggedStart(screenX, screenY);
            return true;
        }

        if (dragged) {
            dragged(screenX, screenY);
            return true;
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    private void dragged(int screenX, int screenY) {
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        float w = cache.x - startX;
        float h = cache.y - startY;

        sprite.setSize(w, h);
    }

    private void draggedStart(int screenX, int screenY) {
        SelectData selectData = getSelectData();
        if (selectData == null) {
            return;
        }

        // 如果没有按住左Shift则清空上次选择的内容
        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            selectData.clearAll();
        }

        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        startX = cache.x;
        startY = cache.y;
        dragged = true;
        down = false;

        setPosition(startX, startY);
        sprite.setSize(0, 0);
        setVisible(true);
    }

    private SelectData getSelectData() {
        Scene scene = getScene();
        if (scene == null) {
            return null;
        }

        Set<Node> byGroup = scene.getByGroup(Groups.DATABASE);
        if (byGroup == null || byGroup.isEmpty()) {
            return null;
        }

        Database database = (Database) byGroup.iterator().next();

        return database.getSelectData();
    }

    private void draggedOver(int screenX, int screenY) {
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        endX = cache.x;
        endY = cache.y;
        dragged = false;
        setVisible(false);
        startSelect();
    }

    /**
     * 开始选择内容
     */
    private void startSelect() {


    }

    @Override
    public int uiLayer() {
        return UILayer.SceneFloat.getLayer();
    }
}
