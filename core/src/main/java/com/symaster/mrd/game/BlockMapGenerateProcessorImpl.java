package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Layer;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.SpriteNode;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerateProcessor;
import com.symaster.mrd.game.entity.Noise;

import java.util.Collections;
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

    private Set<Node> getTileSet(float tileSize, Texture grassTexture, Texture waterTexture, float startX, float startY, Noise noise) {
        Set<Node> rtn = new HashSet<>();

        for (int x = 0; x < SystemConfig.MAP_NUMBER; x++) {
            for (int y = 0; y < SystemConfig.MAP_NUMBER; y++) {

                float worldX = tileSize * x + startX;
                float worldY = tileSize * y + startY;

                float noiseValue = noise.interpolatedNoise(worldX / tileSize / 50, worldY / tileSize / 50);

                TextureRegion textureRegion;
                if (noiseValue < -0.1f) {
                    textureRegion = new TextureRegion(waterTexture, 0, 0, 16, 16);
                } else {

                    float v = random.nextFloat();

                    if (v < 0.25) {
                        textureRegion = new TextureRegion(grassTexture, 0, 0, 32, 32);
                    } else if (v < 0.5) {
                        textureRegion = new TextureRegion(grassTexture, 32, 0, 32, 32);
                    } else if (v < 0.75) {
                        textureRegion = new TextureRegion(grassTexture, 128, 0, 32, 32);
                    } else {
                        textureRegion = new TextureRegion(grassTexture, 160, 32, 32, 32);
                    }

                }

                Sprite sprite = new Sprite(textureRegion);
                sprite.setSize(tileSize, tileSize);

                SpriteNode tile = new SpriteNode(sprite);
                tile.setLayer(Layer.MAP.getLayer());
                tile.setZIndex(0);
                tile.setVisible(true);
                tile.setPosition(worldX, worldY);
                rtn.add(tile);
            }
        }
        return rtn;
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {
        Texture grassTexture = assetManager.get("TX Tileset Grass.png", Texture.class);
        Texture waterTexture = assetManager.get("Water.png", Texture.class);

        Set<Node> noiseGroup = scene.getByGroup(Groups.NOISE);
        if (noiseGroup == null || noiseGroup.isEmpty()) {
            return Collections.emptySet();
        }

        Noise noise = (Noise) noiseGroup.iterator().next();

        String mapSeed = scene.getMapSeed();

        float blockSize = scene.getBlockSize();

        float startX = take.getX() * blockSize;
        float startY = take.getY() * blockSize;

        float tileSize = blockSize / SystemConfig.MAP_NUMBER;

        return getTileSet(tileSize, grassTexture, waterTexture, startX, startY, noise);
    }
}
