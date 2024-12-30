package com.symaster.mrd.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.data.GameGenerateData;
import com.symaster.mrd.game.data.Save;
import com.symaster.mrd.game.entity.Gender;
import com.symaster.mrd.game.entity.Human;

import java.util.ArrayList;
import java.util.List;

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

        Scene scene = new Scene(gameGenerateData.assetManager, gameGenerateData.mapSeed, blocks, gameGenerateData.spriteBatch);
        scene.setInputFactory(gameGenerateData.inputFactory);

        Human human = new Human(gameGenerateData.assetManager);
        human.setVisible(true);
        human.setActivityBlockSize(3);
        human.setHp(1f);
        human.setHpMax(100f);
        human.setGender(Gender.MALE);
        scene.add(human);

        Save save = new Save();
        save.setScene(scene);

        this.save = save;

        finished = true;
        return save;
    }

}
