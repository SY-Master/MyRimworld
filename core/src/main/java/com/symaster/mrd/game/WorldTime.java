package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.GameTime;

import java.util.Set;

/**
 * @author yinmiao
 * @since 2025/1/10
 */
public class WorldTime extends Node {

    private final Sprite sprite;
    private float a = 0.76f;

    public WorldTime(AssetManager assetManager) {
        sprite = new Sprite(assetManager.get("white.png", Texture.class));
        // sprite.setColor(0,0,0, 0.5f);
        add(new SpriteNode(sprite));
        setLayer(Layer.WEATHER.getLayer());
        setFusionRender(true);
        setForcedLogic(true);
        setVisible(true);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        sprite.setSize(width, getHeight());
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        sprite.setSize(getWidth(), height);
    }

    @Override
    public boolean logic(float delta) {
        boolean logic = super.logic(delta);

        Scene scene = getScene();
        if (scene == null) {
            return logic;
        }

        Set<Node> byGroup1 = scene.getByGroup(Groups.TIMER);
        if (byGroup1 == null || byGroup1.isEmpty()) {
            return logic;
        }
        GameTime next = (GameTime) byGroup1.iterator().next();


        if (next.getHour() < 6) {
            setVisible(true);
            sprite.setColor(0, 0, 0, a);
        } else if (next.getHour() < 8) {
            setVisible(true);
            double hour = (next.getHourD() - 6) / 2.0;
            sprite.setColor(0, 0, 0, a - a * (float) hour);
        } else if (next.getHour() < 18) {
            setVisible(false);
        } else if (next.getHour() < 20) {
            double hour = (next.getHourD() - 18) / 2.0;
            setVisible(true);
            sprite.setColor(0, 0, 0, 0 + a * (float) hour);
        } else {
            setVisible(true);
            sprite.setColor(0, 0, 0, a);
        }

        return logic;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }
}
