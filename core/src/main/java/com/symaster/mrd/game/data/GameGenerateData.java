package com.symaster.mrd.game.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.service.AI;
import com.symaster.mrd.input.InputFactory;

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
     * 输入处理器工厂
     */
    public InputFactory inputFactory;
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
    /**
     * ai
     */
    public AI ai;
}
