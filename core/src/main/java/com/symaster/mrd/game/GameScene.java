package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.input.InputFactory;
import com.symaster.mrd.util.UnitUtil;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class GameScene extends Scene {

    /**
     * 游戏时间
     */
    private GameTime gameTime;

    public GameScene(AssetManager assetManager, String mapSeed, InputFactory inputFactory) {
        super(assetManager, mapSeed, UnitUtil.ofM(SystemConfig.BLOCK_SIZE), inputFactory);
    }
}
