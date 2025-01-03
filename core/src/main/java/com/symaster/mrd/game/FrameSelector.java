package com.symaster.mrd.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.InputNode;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.SpriteNode;

/**
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

    private void draggedOver(int screenX, int screenY) {
        cache.x = screenX;
        cache.y = screenY;
        GameSingleData.positionConverter.toWorld(cache);

        endX = cache.x;
        endY = cache.y;
        dragged = false;
        setVisible(false);
    }

    @Override
    public int uiLayer() {
        return UILayer.SceneFloat.getLayer();
    }
}
