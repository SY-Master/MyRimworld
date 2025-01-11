package com.symaster.mrd.game.entity.obj;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SelectNode;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.util.UnitUtil;

/**
 * @author yinmiao
 * @since 2025/1/8
 */
public class Tree extends Node {

    private final Sprite shadowSprite;
    private final Sprite plantSprite;
    private final int srcWidth, srcHeight;
    private final float defaultWidth;
    private float scale = 1f;

    public Tree(AssetManager assetManager, int x, int y, int width, int height, float defaultWidth) {
        Texture plant = assetManager.get("TX Plant.png", Texture.class);
        Texture shadow = assetManager.get("TX Shadow Plant.png", Texture.class);

        srcWidth = width;
        srcHeight = height;
        this.defaultWidth = defaultWidth;

        this.shadowSprite = new Sprite(shadow, x, y, width, height);
        this.shadowSprite.setColor(1, 1, 1, 0.7f);
        this.plantSprite = new Sprite(plant, x, y, width, height);

        SpriteNode nodes = new SpriteNode(shadowSprite);
        nodes.setLayer(Layer.MAP.getLayer());
        nodes.setZIndex(1);
        add(nodes);

        SpriteNode nodes1 = new SpriteNode(plantSprite) {
            @Override
            public int getZIndex() {
                return (int) -Tree.this.getPositionY();
            }
        };
        add(nodes1);

        updateSize();

        add(new SelectNode(assetManager));

        setVisible(true);
    }

    private void updateSize() {
        float pw = defaultWidth * scale;
        float ph = pw / srcWidth * srcHeight;
        setWidth(pw);
        setHeight(ph);

        shadowSprite.setSize(pw, ph);
        plantSprite.setSize(pw, ph);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        updateSize();
    }
}
