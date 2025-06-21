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
    private final LabelNode label;
    // private float fontScale;
    private HumanBodyType bodyType;

    public Human() {
        setRace(Race.Human);
        // this.fontScale = fontScale;

        Texture texture = getAsset("user", Texture.class);

        float iconW = UnitUtil.ofM(0.7f);
        float iconH = UnitUtil.ofM(1f);
        setWidth(iconW);
        setHeight(iconH);

        Sprite sprite = new Sprite(texture);
        sprite.setSize(iconW, iconH);

        add((nodes = new SpriteNode(sprite)));

        float labelW = UnitUtil.ofM(1.5f);
        // label = new Label(getName(), skin.get("nameLabel", Label.LabelStyle.class));

        float x = iconW / 2f - labelW / 2f;

        label = new LabelNode(getName(), 35);
        label.setLabelAlignment(Align.center);
        label.setLabelSize(labelW, 5f);
        label.setPosition(x, iconH + UnitUtil.ofM(0.1f));
        add(label);

        // label = node.getLabel();

        setVisible(true);
        setFusionRender(true);

        if (getGender() != null) {
            updateSpriteColor();
        }
    }

    public HumanBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(HumanBodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        label.setText(name);
    }

    public SpriteNode getNodes() {
        return nodes;
    }

    // public float getFontScale() {
    //     return fontScale;
    // }
    //
    // public void setFontScale(float fontScale) {
    //     this.fontScale = fontScale;
    // }

    private void updateSpriteColor() {
        nodes.getSprite().setColor(new Color(0.7f, 0.7f, 0.7f, 1f));
    }

    @Override
    public void setGender(Gender gender) {
        super.setGender(gender);
        updateSpriteColor();
    }



}
