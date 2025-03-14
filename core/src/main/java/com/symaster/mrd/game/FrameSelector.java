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
import com.symaster.mrd.game.entity.SelectData;
import com.symaster.mrd.util.SceneUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    private boolean dragged;
    private boolean down;
    private final List<Node> cache1;
    private final List<Node> cache2;
    private final Set<Integer> selectLayer;

    public FrameSelector(AssetManager assetManager) {
        dragged = false;
        down = false;
        this.cache1 = new LinkedList<>();
        this.cache2 = new LinkedList<>();
        this.selectLayer = new HashSet<>();
        this.selectLayer.add(Layer.OBJECT.getLayer());

        sprite = new Sprite(assetManager.get("white.png", Texture.class));
        sprite.setColor(new Color(0.129f, 0.847f, 0.427f, 0.3f));

        add(new SpriteNode(sprite));

        setLayer(Layer.FLOAT.getLayer());
        setVisible(false);
        setFusionRender(true);
    }

    public void addSelectLayer(final int layer) {
        selectLayer.add(layer);
    }

    public void removeSelectLayer(final int layer) {
        selectLayer.remove(layer);
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
        if (button == Input.Buttons.LEFT && down) {
            down = false;
            dragged = false;
            draggedOver(screenX, screenY);
            return true;
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (dragged) {
            dragged(screenX, screenY);
            return true;
        } else if (down) {
            draggedStart(screenX, screenY);
            return true;
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    private void findNodes(float x, float y, float w, float h) {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        float x1 = Math.min(x, x + w);
        float y1 = Math.min(y, y + h);
        float x2 = Math.max(x, x + w);
        float y2 = Math.max(y, y + h);

        cache1.clear();
        cache2.clear();
        scene.findNodes(x1, y1, x2 - x1, y2 - y1, cache2, true, 0);

        for (Node node : cache2) {
            if (selectLayer.contains(node.getLayer())) {
                float nodeX1 = Math.min(node.getPositionX(), node.getPositionX() + node.getWidth());
                float nodeY1 = Math.min(node.getPositionY(), node.getPositionY() + node.getHeight());
                float nodeX2 = Math.max(node.getPositionX(), node.getPositionX() + node.getWidth());
                float nodeY2 = Math.max(node.getPositionY(), node.getPositionY() + node.getHeight());

                if (nodeX1 >= x1 && nodeX2 <= x2 && nodeY1 >= y1 && nodeY2 <= y2) {
                    cache1.add(node);
                }
            }
        }
    }

    /**
     * 拖动开始
     *
     * @param screenX 屏幕坐标
     * @param screenY 屏幕坐标
     */
    private void draggedStart(int screenX, int screenY) {
        SelectData selectData = SceneUtil.getSelectData(getScene());
        if (selectData == null) {
            return;
        }

        // 如果没有按住左Shift则清空上次选择的内容
        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            selectData.clearAll();
        }

        // 算出鼠标的世界坐标(不能使用屏幕坐标, 会有问腿)
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        startX = cache.x;
        startY = cache.y;

        // 开始拖动
        dragged = true;

        // 初始化坐标
        setPosition(startX, startY);
        // 初始化sprite的尺寸, 为了防止上次选择时残留的尺寸, 初始化之后才设置Visible为true
        sprite.setSize(0, 0);
        setVisible(true);
    }

    /**
     * 拖动中...
     *
     * @param screenX 屏幕坐标
     * @param screenY 屏幕坐标
     */
    private void dragged(int screenX, int screenY) {

        // 世界坐标
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        // 拖动的矩形尺寸
        float w = cache.x - startX;
        float h = cache.y - startY;
        sprite.setSize(w, h);

        // 选择预览
        preSelect(cache.x, cache.y);
    }

    private void draggedOver(int screenX, int screenY) {
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        dragged = false;
        down = false;
        setVisible(false);
        startSelect(cache.x, cache.y);
    }

    private void preSelect(float worldX, float worldY) {
        findNodes(startX, startY, worldX - startX, worldY - startY);

        SelectData selectData = SceneUtil.getSelectData(getScene());
        if (selectData == null) {
            return;
        }

        selectData.clearPreSelection();

        if (!cache1.isEmpty()) {
            for (Node node : cache1) {
                selectData.preSelect(node);
            }
        }
    }

    /**
     * 开始选择内容
     */
    private void startSelect(float worldX, float worldY) {
        SelectData selectData = SceneUtil.getSelectData(getScene());
        if (selectData == null) {
            return;
        }

        selectData.clearPreSelection();

        findNodes(startX, startY, worldX - startX, worldY - startY);

        for (Node node : cache1) {
            selectData.select(node);
        }
    }

    @Override
    public int uiLayer() {
        return UILayer.SceneFloat.getLayer();
    }

}
