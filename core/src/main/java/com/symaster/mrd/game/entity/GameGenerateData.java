package com.symaster.mrd.game.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.symaster.mrd.g2d.scene.Scene;

/**
 * @author yinmiao
 * @since 2024/12/29
 */
public class GameGenerateData {

    /**
     * 种子
     */
    public String mapSeed;
    /**
     * 指定场景
     */
    public Scene scene;
    /**
     * 资源管理器
     */
    public AssetManager assetManager;
    /**
     * 渲染批处理器
     */
    public SpriteBatch spriteBatch;
    /**
     * 皮肤
     */
    public Skin skin;

}
