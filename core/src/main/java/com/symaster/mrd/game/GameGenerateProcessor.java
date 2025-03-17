package com.symaster.mrd.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.SelectNode;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.tansform.TransformInput;
import com.symaster.mrd.g2d.tansform.TransformMove;
import com.symaster.mrd.game.entity.*;
import com.symaster.mrd.game.entity.map.TileMapFactory;
import com.symaster.mrd.game.service.DSS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        DSS dss = new DSS();

        scene.add(dss);

        FrameSelector frameSelector = new FrameSelector(gameGenerateData.assetManager);

        // 选择器
        scene.add(frameSelector);

        // Dss测试工具
        DSSTestMouse dssTestMouse = new DSSTestMouse();
        dssTestMouse.setDss(dss);
        scene.add(dssTestMouse);

        // 噪声算法
        scene.add(new Noise(gameGenerateData.mapSeed.hashCode()), Groups.NOISE);

        // 地图工厂
        scene.add(new TileMapFactory(gameGenerateData.assetManager), Groups.TILEMAP_FACTORY);

        // 游戏时间
        scene.add(new GameTime(new Random().nextFloat() * 9999999 + 2000000), Groups.TIMER);

        // scene.add(new DSS(), );
        // scene.add(new GameTime(), Groups.TIMER);

        // 初始化区块
        initBlock(scene);

        Human maleHuman = new Human(gameGenerateData.assetManager, gameGenerateData.skin, 0.2f);
        maleHuman.add(new SelectNode(gameGenerateData.assetManager));
        maleHuman.setZIndex(100);
        maleHuman.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        maleHuman.setHp(new Measure(1, 100f));
        maleHuman.setGender(Gender.MALE);
        maleHuman.setDss(dss);

        TransformInput nodes1 = new TransformInput();
        maleHuman.add(nodes1);

        TransformMove transformMove = new TransformMove(nodes1.getVector2(), maleHuman);
        transformMove.setSpeed(30);
        maleHuman.add(transformMove);

        // maleHuman.setAi(gameGenerateData.ai);
        maleHuman.setName(NameGeneratorFactory.getNameGenerator(maleHuman.getRace()).generateName(Gender.MALE));
        scene.add(maleHuman, Groups.PARTNER);

        Human human = new Human(gameGenerateData.assetManager, gameGenerateData.skin, 0.2f);
        human.add(new SelectNode(gameGenerateData.assetManager));
        human.setZIndex(100);
        human.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        human.setHp(new Measure(1, 100f));
        human.setGender(Gender.FEMALE);

        TransformInput nodes = new TransformInput();
        human.add(nodes);

        TransformMove transformMove1 = new TransformMove(nodes.getVector2(), human);
        transformMove1.setSpeed(30);
        human.add(transformMove1);

        // human.setAi(gameGenerateData.ai);
        human.setDss(dss);
        human.setName(NameGeneratorFactory.getNameGenerator(human.getRace()).generateName(Gender.FEMALE));
        human.setPosition(20, 0);
        scene.add(human, Groups.PARTNER);

        Save save = new Save();
        save.setScene(scene);

        this.save = save;

        finished = true;
        return save;
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
