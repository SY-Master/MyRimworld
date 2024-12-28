package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author yinmiao
 * @since 2024/12/27
 */
public class NinePatchNode extends Node {

    private final NinePatchDrawable ninePatchDrawable;
    private float gdxNodePositionX = 0f;
    private float gdxNodePositionY = 0f;

    public NinePatchNode(NinePatchDrawable ninePatchDrawable) {
        this.ninePatchDrawable = ninePatchDrawable;
        setVisible(true);
    }

    public NinePatchDrawable getNinePatchDrawable() {
        return ninePatchDrawable;
    }

    /**
     * 设置显示组件的世界位置
     */
    @Override
    public void setGdxNodePosition(float x, float y) {
        super.setGdxNodePosition(x, y);
        this.gdxNodePositionX = x;
        this.gdxNodePositionY = y;
    }

    /**
     * 如果子节点需要绘制
     */
    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        ninePatchDrawable.draw(spriteBatch, gdxNodePositionX, gdxNodePositionY, getWidth(), getHeight());
    }
}
