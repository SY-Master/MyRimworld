package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerateProcessor;
import com.symaster.mrd.game.entity.Noise;
import com.symaster.mrd.game.entity.map.TileMap;
import com.symaster.mrd.game.entity.map.TileMapFactory;
import com.symaster.mrd.game.entity.map.TileSet;
import com.symaster.mrd.game.entity.obj.*;

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

    private Set<Node> getTileSet(float tileSize,
                                 TileMapFactory tileMapFactory,
                                 float startX,
                                 float startY,
                                 Noise noise,
                                 float blockSize) {
        Set<Node> rtn = new HashSet<>();

        TileSet tileSet = new TileSet();
        rtn.add(tileSet);

        for (int x = 0; x < SystemConfig.MAP_NUMBER; x++) {
            for (int y = 0; y < SystemConfig.MAP_NUMBER; y++) {

                // 世界坐标
                float worldX = tileSize * x + startX;
                float worldY = tileSize * y + startY;

                // 噪声值[-1, 1]
                float noiseValue = noise.interpolatedNoise(worldX / tileSize / 60, worldY / tileSize / 60);

                TileMap tileMap;
                if (noiseValue < -0.15f) {
                    // 水
                    water(tileMapFactory, rtn, tileSet, worldX, worldY, tileSize);
                } else {

                    // 草地
                    flower(tileMapFactory, rtn, tileSet, worldX, worldY, tileSize);
                }
            }
        }

        WorldTime nodes = new WorldTime(assetManager);
        nodes.setPosition(startX, startY);
        nodes.setWidth(blockSize);
        nodes.setHeight(blockSize);
        rtn.add(nodes);

        TileMap grass = new TileMap(tileMapFactory.getGrassTexture().grass());
        grass.setZIndex(-1);
        grass.setSize(blockSize, blockSize);
        grass.setPosition(startX, startY);
        rtn.add(grass);

        return rtn;
    }

    private void flower(TileMapFactory tileMapFactory,
                        Set<Node> rtn,
                        TileSet tileSet,
                        float worldX,
                        float worldY,
                        float tileSize) {
        TileMap tileMap;

        float v = random.nextFloat();
        if (v < 0.05f) {

            float v1 = random.nextFloat();
            if (v1 < 0.33) {
                tileMap = new TileMap(tileMapFactory.getGrassTexture().flower());
            } else if (v1 < 0.66) {
                tileMap = new TileMap(tileMapFactory.getGrassTexture().flower2());
            } else {
                tileMap = new TileMap(tileMapFactory.getGrassTexture().flower3());
            }

        } else if (v < 0.1f) {
            if (random.nextFloat() < 0.5) {
                tileMap = new TileMap(tileMapFactory.getGrassTexture().grass2());
            } else {
                tileMap = new TileMap(tileMapFactory.getGrassTexture().grass3());
            }

        } else {

            tileMap = null;

            // tileMap = new TileMap(tileMapFactory.getGrassTexture().grass());

            if (random.nextFloat() < 0.001f) {
                if (random.nextFloat() < 0.5f) {
                    // 灌木丛
                    bushes(tileMapFactory, rtn, tileSet, worldX, worldY, tileSize);
                } else {
                    // 大树
                    tree(tileMapFactory, rtn, tileSet, worldX, worldY, tileSize);
                }
            }
        }

        if (tileMap != null) {
            tileMap.setSize(tileSize, tileSize);
            tileMap.setPosition(worldX, worldY);
            tileSet.add(tileMap);
        }
    }

    private void bushes(TileMapFactory tileMapFactory,
                        Set<Node> rtn,
                        TileSet tileSet,
                        float worldX,
                        float worldY,
                        float tileSize) {
        Bushes bushes;
        float v1 = random.nextFloat();
        if (v1 < 0.33f) {
            // 添加树
            bushes = new BushesType1(assetManager);
        } else {
            // 添加树
            bushes = new BushesType2(assetManager);
        }

        bushes.setScale(random.nextFloat() * 0.5f + 0.75f);
        bushes.setPosition(worldX - bushes.getWidth() / 2 + tileSize / 2, worldY);
        rtn.add(bushes);
    }

    private void tree(TileMapFactory tileMapFactory,
                      Set<Node> rtn,
                      TileSet tileSet,
                      float worldX,
                      float worldY,
                      float tileSize) {
        Tree tree;
        float v1 = random.nextFloat();
        if (v1 < 0.33f) {
            // 添加树
            tree = new TreeType1(assetManager);
        } else if (v1 < 0.66f) {
            // 添加树
            tree = new TreeType2(assetManager);
        } else {
            // 添加树
            tree = new TreeType3(assetManager);
        }

        tree.setScale(random.nextFloat() + 0.5f);
        tree.setPosition(worldX - tree.getWidth() / 2 + tileSize / 2, worldY);
        rtn.add(tree);
    }

    private void water(TileMapFactory tileMapFactory,
                       Set<Node> rtn,
                       TileSet tileSet,
                       float worldX,
                       float worldY,
                       float tileSize) {
        TileMap tileMap = new TileMap(tileMapFactory.getWaterTexture());
        tileMap.setSize(tileSize, tileSize);
        tileMap.setPosition(worldX, worldY);
        tileSet.add(tileMap);
    }

    @Override
    public Set<Node> generate(Scene scene, Block take) {

        Set<Node> grassSet = scene.getByGroup(Groups.TILEMAP_FACTORY);
        if (grassSet == null || grassSet.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Node> noiseGroup = scene.getByGroup(Groups.NOISE);
        if (noiseGroup == null || noiseGroup.isEmpty()) {
            return Collections.emptySet();
        }

        // 地图工厂
        TileMapFactory tileMapFactory = (TileMapFactory) grassSet.iterator().next();

        // 噪声算法
        Noise noise = (Noise) noiseGroup.iterator().next();

        // 地图种子
        String mapSeed = scene.getMapSeed();

        // 区块尺寸
        float blockSize = scene.getBlockSize();

        // 区块开始的世界坐标
        float startX = take.getX() * blockSize;
        float startY = take.getY() * blockSize;

        // 瓦片尺寸
        float tileSize = blockSize / SystemConfig.MAP_NUMBER;

        return getTileSet(tileSize, tileMapFactory, startX, startY, noise, blockSize);
    }

}
