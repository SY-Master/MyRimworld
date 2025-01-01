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
import java.util.Random;
import java.util.Set;

/**
 * 生成地图处理器
 *
 * @author yinmiao
 * @since 2024/12/28
 */
public class BlockMapGenerateProcessorImpl implements BlockMapGenerateProcessor {

    private final AssetManager assetManager;
    private final Random random = new Random();

    public BlockMapGenerateProcessorImpl(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    private Set<Node> getMapSize(float mapSize, Texture mapTexture, float startX, float startY) {
        Set<Node> rtn = new HashSet<>();

        for (int x = 0; x < SystemConfig.MAP_NUMBER; x++) {
            for (int y = 0; y < SystemConfig.MAP_NUMBER; y++) {

                TextureRegion textureRegion;
                float v = random.nextFloat();

                if (v < 0.3f) {
                    textureRegion = new TextureRegion(mapTexture, 0, 0, 32, 32);
                } else if (v < 0.6f) {
                    textureRegion = new TextureRegion(mapTexture, 32, 0, 32, 32);
                } else if (v < 0.9f) {
                    textureRegion = new TextureRegion(mapTexture, 128, 0, 32, 32);
                } else {
                    textureRegion = new TextureRegion(mapTexture, 160, 32, 32, 32);
                }

                float mapX = mapSize * x + startX;
                float mapY = mapSize * y + startY;

                Sprite sprite = new Sprite(textureRegion);
                sprite.setSize(mapSize, mapSize);

                SpriteNode map = new SpriteNode(sprite);
                map.setZIndex(0);
                map.setVisible(true);
                map.setPosition(mapX, mapY);
                rtn.add(map);
            }
        }
        return rtn;
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {
        Texture mapTexture = assetManager.get("TX Tileset Grass.png", Texture.class);

        float blockSize = scene.getBlockSize();

        float startX = take.getX() * blockSize;
        float startY = take.getY() * blockSize;

        float mapSize = blockSize / SystemConfig.MAP_NUMBER;

        return getMapSize(mapSize, mapTexture, startX, startY);
    }
}
