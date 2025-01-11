package com.symaster.mrd.g2d;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.symaster.mrd.game.entity.SelectData;
import com.symaster.mrd.util.SceneUtil;

/**
 * @author yinmiao
 * @since 2025/1/11
 */
public class SelectNode extends Node {

    private final NinePatch ninePatch;

    public SelectNode(AssetManager assetManager) {
        Texture texture = assetManager.get("select-1.png", Texture.class);

        ninePatch = new NinePatch(texture, 12, 12, 12, 12);
        ninePatch.scale(0.1f, 0.1f);
        ninePatch.setColor(Color.WHITE);

        setVisible(false);
    }

    @Override
    public boolean logic(float delta) {
        boolean logic = super.logic(delta);

        SelectData selectData = SceneUtil.getSelectData(getScene());
        if (selectData == null) {
            return logic;
        }

        Node topParent = SceneUtil.getTopParent(this);

        setVisible(selectData.isPreSelection(topParent) || selectData.isSelected(topParent));

        return logic;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        Node topParent = SceneUtil.getTopParent(this);
        ninePatch.setColor(Color.WHITE);
        ninePatch.draw(spriteBatch, topParent.getPositionX() - 2, topParent.getPositionY() - 2, topParent.getWidth() + 4, topParent.getHeight() + 4);
    }
}
