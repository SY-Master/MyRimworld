package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerateProcessor;

import java.util.Collections;
import java.util.Set;

/**
 * 生成地图处理器
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class BlockMapGenerateProcessorImpl implements BlockMapGenerateProcessor {

    private final AssetManager assetManager;

    public BlockMapGenerateProcessorImpl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {
        Texture mapTexture = assetManager.get("TX Tileset Grass.png", Texture.class);







        return Collections.emptySet();
    }
}
