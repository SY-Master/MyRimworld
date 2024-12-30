package com.symaster.mrd.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.data.GameGenerateData;
import com.symaster.mrd.game.data.Save;
import com.symaster.mrd.input.InputFactory;

import java.util.UUID;

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

    public Save getSave() {
        return save;
    }

    @Override
    public Save call() throws Exception {



        finished = true;
        return null;
    }

    // @Override
    // public void run() {
    //
    //     String mapSeed;
    //     if (gameGenerateData.mapSeed == null) {
    //         mapSeed = UUID.randomUUID().toString();
    //     } else {
    //         mapSeed = gameGenerateData.mapSeed;
    //     }
    //
    //     progress = 0.01f;
    //
    //     save = new Save();
    //
    //     Scene scene = gameGenerateData.scene;
    //     save.setScene(scene);
    //
    //     progress = 0.15f;
    // }
}
