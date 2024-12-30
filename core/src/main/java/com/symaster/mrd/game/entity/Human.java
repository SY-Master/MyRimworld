package com.symaster.mrd.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.util.GdxText;
import com.symaster.mrd.util.UnitUtil;

/**
 * 人类
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public class Human extends Creature {

    private SpriteNode nodes;

    public Human(AssetManager assetManager) {
        Texture texture = assetManager.get("user.png", Texture.class);

        Sprite  sprite = new Sprite(texture);
        sprite.setSize(UnitUtil.ofM(0.7f), UnitUtil.ofM(1f));

        add((nodes = new SpriteNode(sprite)));

        // Label label = new Label(GdxText.val(""), );



        setVisible(true);

        if (getGender() != null) {
            updateSpriteColor();
        }
    }

    private void updateSpriteColor() {
        if (getGender() == Gender.FEMALE) {
            nodes.getSprite().setColor(new Color(1f, 0, 0, 1));
        } else {
            nodes.getSprite().setColor(new Color(0, 0, 1f, 1f));
        }
    }

    @Override
    public void setGender(Gender gender) {
        super.setGender(gender);
        updateSpriteColor();
    }
}
