package com.symaster.mrd.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.*;
import com.symaster.mrd.game.entity.map.TileMapFactory;
import com.symaster.mrd.game.service.DSS;
import com.symaster.mrd.util.UnitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.symaster.mrd.util.SceneUtil.addMoveSuit;

/**
 * @author yinmiao
 * @since 2024/12/29
 */
public class GameGenerateProcessor implements AsyncTask<Save> {

    private final GameGenerateData gameGenerateData;
    private float progress = 0f;
    private Save save;
    private boolean finished = false;

    public GameGenerateProcessor(GameGenerateData gameGenerateData) {
        this.gameGenerateData = gameGenerateData;
    }

    public float getProgress() {
        return progress;
    }

    public boolean update() {
        return finished;
    }

    public boolean update(int millis) {

        long endTime = TimeUtils.millis() + millis;
        while (true) {
            boolean done = update();
            if (done || TimeUtils.millis() > endTime) {
                return done;
            }
            ThreadUtils.yield();
        }
    }

    public Save getSave() {
        return save;
    }

    @Override
    public Save call() throws Exception {
        try {
            return generate();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    private Save generate() {

        Scene scene = new Scene(gameGenerateData.assetManager, gameGenerateData.mapSeed);

        // DSS
        DSS dss = addDss(scene);

        // Dss测试工具
        DSSTestMouse dssTestMouse = new DSSTestMouse();
        dssTestMouse.setDss(dss);
        dssTestMouse.created();
        scene.add(dssTestMouse);

        // 噪声算法
        Noise noise = addNoise(scene);

        // 地图工厂
        TileMapFactory tileMapFactory = addTileMapFactory(scene);

        // 游戏时间
        GameTime gameTime = addGameTime(scene);

        // 初始化区块
        initBlock(scene);

        // 主角
        Human maleHuman = new Human();
        maleHuman.setZIndex(100);
        maleHuman.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        maleHuman.setHp(new Measure(1, 100f));
        maleHuman.setGender(Gender.MALE);
        maleHuman.setDss(dss);
        maleHuman.setName("symaster");
        maleHuman.created();
        addMoveSuit(maleHuman, UnitUtil.ofM(5));

        dss.setPlayer(maleHuman);

        scene.add(maleHuman, Groups.PARTNER);

        Save save = new Save();
        save.setScene(scene);

        this.save = save;

        finished = true;
        return save;
    }

    private GameTime addGameTime(Scene scene) {
        GameTime gameTime = new GameTime(new Random().nextFloat() * 9999999 + 2000000);
        scene.add(gameTime, Groups.TIMER);
        return gameTime;
    }

    private TileMapFactory addTileMapFactory(Scene scene) {
        TileMapFactory tileMapFactory = new TileMapFactory(gameGenerateData.assetManager);
        scene.add(tileMapFactory, Groups.TILEMAP_FACTORY);
        return tileMapFactory;
    }

    private Noise addNoise(Scene scene) {
        Noise noise = new Noise(gameGenerateData.mapSeed.hashCode());
        scene.add(noise, Groups.NOISE);
        return noise;
    }

    private DSS addDss(Scene scene) {
        DSS dss = new DSS();
        scene.add(dss, Groups.DSS);
        return dss;
    }

    private void initBlock(Scene scene) {
        List<Block> blocks = new ArrayList<>();

        int initSize = 20; // 初始化区块大小
        for (int x = -initSize; x < initSize; x++) {
            for (int y = -initSize; y < initSize; y++) {
                blocks.add(new Block(x, y));
            }
        }
        scene.initBlocks(blocks, progress -> GameGenerateProcessor.this.progress = progress);
    }

}
