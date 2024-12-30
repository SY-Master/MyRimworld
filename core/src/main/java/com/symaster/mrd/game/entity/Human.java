package com.symaster.mrd.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.symaster.mrd.g2d.LabelNode;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.util.UnitUtil;

/**
 * 人类
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public class Human extends Creature {

    private final SpriteNode nodes;
    private final Label label;

    public Human(AssetManager assetManager, Skin skin) {
        Texture texture = assetManager.get("user.png", Texture.class);

        Sprite  sprite = new Sprite(texture);
        sprite.setSize(UnitUtil.ofM(0.7f), UnitUtil.ofM(1f));

        add((nodes = new SpriteNode(sprite)));

        label = new Label(getName(), skin.get("nameLabel", Label.LabelStyle.class));
        label.setFontScale(0.3f);

        LabelNode nodes1 = new LabelNode(label);
        nodes1.setPositionY(-5);
        add(nodes1);

        setVisible(true);

        if (getGender() != null) {
            updateSpriteColor();
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        label.setText(name);
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
