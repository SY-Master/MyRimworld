package com.symaster.mrd.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.symaster.mrd.g2d.SpriteNode;

/**
 * 人类
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public class Human extends Creature {

    private Sprite sprite;

    public Human(AssetManager assetManager) {
        Texture texture = assetManager.get("user.png", Texture.class);

        sprite = new Sprite(texture);

        add(new SpriteNode(sprite));

        setVisible(true);

        if (getGender() != null) {
            updateSpriteColor();
        }
    }

    private void updateSpriteColor() {
        if (getGender() == Gender.FEMALE) {
            sprite.setColor(new Color(1f, 0, 0, 1));
        } else {
            sprite.setColor(new Color(0, 0, 1f, 1f));
        }
    }

    @Override
    public void setGender(Gender gender) {
        super.setGender(gender);
        updateSpriteColor();
    }
}
