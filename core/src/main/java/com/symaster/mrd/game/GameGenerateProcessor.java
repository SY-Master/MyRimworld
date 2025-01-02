package com.symaster.mrd.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.api.ProgressProcessor;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.entity.*;

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
            if (done || TimeUtils.millis() > endTime) return done;
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
        List<Block> blocks = new ArrayList<>();

        int initSize = 10;
        for (int x = -initSize; x < initSize; x++) {
            for (int y = -initSize; y < initSize; y++) {
                blocks.add(new Block(x, y));
            }
        }

        Scene scene = new Scene(gameGenerateData.assetManager, gameGenerateData.mapSeed, gameGenerateData.inputFactory);
        scene.initBlocks(blocks, progress -> GameGenerateProcessor.this.progress = progress);

        // 游戏时间
        scene.add(new GameTime(new Random().nextFloat() * 9999999 + 2000000), Groups.TIMER);
        // scene.add(new GameTime(), Groups.TIMER);

        Human maleHuman = new Human(gameGenerateData.assetManager, gameGenerateData.skin, 0.2f);
        maleHuman.setZIndex(100);
        maleHuman.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        maleHuman.setHp(new Measure(1, 100f));
        maleHuman.setGender(Gender.MALE);
        maleHuman.setAi(gameGenerateData.ai);
        maleHuman.setName(NameGeneratorFactory.getNameGenerator(maleHuman.getRace()).generateName(Gender.MALE));
        scene.add(maleHuman, Groups.PARTNER);

        Human human = new Human(gameGenerateData.assetManager, gameGenerateData.skin, 0.2f);
        human.setZIndex(100);
        human.setActivityBlockSize(SystemConfig.PARTNER_ACTIVE_SIZE);
        human.setHp(new Measure(1, 100f));
        human.setGender(Gender.FEMALE);
        human.setAi(gameGenerateData.ai);
        human.setName(NameGeneratorFactory.getNameGenerator(human.getRace()).generateName(Gender.FEMALE));
        human.setPosition(20, 0);
        scene.add(human, Groups.PARTNER);

        Save save = new Save();
        save.setScene(scene);

        this.save = save;

        finished = true;
        return save;
    }

}
