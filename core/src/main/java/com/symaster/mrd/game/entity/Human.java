package com.symaster.mrd.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
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
    private float fontScale;

    public Human(AssetManager assetManager, Skin skin, float fontScale) {
        setRace(Race.Human);
        this.fontScale = fontScale;
        Texture texture = assetManager.get("user.png", Texture.class);

        float iconW = UnitUtil.ofM(0.7f);
        float iconH = UnitUtil.ofM(1f);

        Sprite sprite = new Sprite(texture);
        sprite.setSize(iconW, iconH);

        add((nodes = new SpriteNode(sprite)));

        float labelW = UnitUtil.ofM(1.5f);
        label = new Label(getName(), skin.get("nameLabel", Label.LabelStyle.class));
        label.setAlignment(Align.center);
        label.setFontScale(fontScale);
        label.setSize(labelW, 5f);

        float x = iconW / 2f - labelW / 2f;

        LabelNode nodes1 = new LabelNode(label);
        nodes1.setPosition(x, -5);
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

    public SpriteNode getNodes() {
        return nodes;
    }

    public Label getLabel() {
        return label;
    }

    public float getFontScale() {
        return fontScale;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
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
