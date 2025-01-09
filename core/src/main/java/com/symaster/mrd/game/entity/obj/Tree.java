package com.symaster.mrd.game.entity.obj;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SpriteNode;

/**
 * @author yinmiao
 * @since 2025/1/8
 */
public class Tree extends Node {

    public Tree(AssetManager assetManager) {
        Texture plant = assetManager.get("TX Plant.png", Texture.class);
        Texture shadow = assetManager.get("TX Shadow Plant.png", Texture.class);

        Sprite shadowSprite = new Sprite(shadow, 20, 10, 120, 146);
        Sprite plantSprite = new Sprite(new TextureRegion(plant, 20, 10, 120, 146));

        SpriteNode nodes = new SpriteNode(shadowSprite);
        nodes.setLayer(Layer.MAP.getLayer());
        add(nodes);
        add(new SpriteNode(plantSprite));

        setVisible(true);
    }
}
