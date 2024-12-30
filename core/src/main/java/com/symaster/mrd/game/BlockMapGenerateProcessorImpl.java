package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerateProcessor;

import java.util.HashSet;
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

    private static Set<Node> getMapSize(float mapSize, TextureRegion textureRegion) {
        Set<Node> rtn = new HashSet<>();

        for (int x = 0; x < SystemConfig.MAP_NUMBER; x++) {
            for (int y = 0; y < SystemConfig.MAP_NUMBER; y++) {
                float mapX = mapSize * x;
                float mapY = mapSize * y;

                Sprite sprite = new Sprite(textureRegion);
                sprite.setSize(mapSize, mapSize);

                SpriteNode map = new SpriteNode(sprite);
                map.setPosition(mapX, mapY);
                rtn.add(map);
            }
        }
        return rtn;
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {
        Texture mapTexture = assetManager.get("TX Tileset Grass.png", Texture.class);
        TextureRegion textureRegion = new TextureRegion(mapTexture, 0, 96, 32, 32);

        float blockSize = scene.getBlockSize();

        float startX = take.getX() * blockSize;
        float startY = take.getY() * blockSize;

        float mapSize = blockSize / SystemConfig.MAP_NUMBER;

        return getMapSize(mapSize, textureRegion);
    }
}
