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
import com.symaster.mrd.game.entity.map.*;

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

    private Set<Node> getTileSet(float tileSize, GrassTexture grassTexture, WaterTexture waterTexture, float startX, float startY, Noise noise) {
        Set<Node> rtn = new HashSet<>();

        for (int x = 0; x < SystemConfig.MAP_NUMBER; x++) {
            for (int y = 0; y < SystemConfig.MAP_NUMBER; y++) {

                float worldX = tileSize * x + startX;
                float worldY = tileSize * y + startY;

                float noiseValue = noise.interpolatedNoise(worldX / tileSize / 60, worldY / tileSize / 60);

                TileMap tileMap;
                if (noiseValue < -0.15f) {
                    tileMap = new TileMapWater(waterTexture);
                } else if (noiseValue < 0.95f) {
                    tileMap = new TileMapGrass(grassTexture.grass());
                } else {
                    tileMap = new TileMapGrass(grassTexture.flower());
                }

                tileMap.setSize(tileSize, tileSize);
                tileMap.setPosition(worldX, worldY);
                rtn.add(tileMap);
            }
        }
        return rtn;
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {

        Set<Node> grassSet = scene.getByGroup(Groups.TEXTURE_GRASS);
        if (grassSet == null || grassSet.isEmpty()) {
            return Collections.emptySet();
        }
        GrassTexture grassTexture = (GrassTexture) grassSet.iterator().next();


        Set<Node> byGroup = scene.getByGroup(Groups.TEXTURE_WATER);
        if (byGroup == null || byGroup.isEmpty()) {
            return Collections.emptySet();
        }
        WaterTexture waterTexture = (WaterTexture) byGroup.iterator().next();

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
