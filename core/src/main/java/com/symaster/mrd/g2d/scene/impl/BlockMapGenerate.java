package com.symaster.mrd.g2d.scene.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.BlockMapGenerateProcessorImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author yinmiao
 * @since 2024/12/28
 */
public class BlockMapGenerate extends AsyncExecutor implements Disposable {

    private boolean running = true;

    /**
     * 结果队列
     */
    private final ConcurrentLinkedQueue<Result> res = new ConcurrentLinkedQueue<>();
    private final Scene scene;
    private final BlockMapGenerateProcessor blockMapGenerateProcessor;


    public BlockMapGenerate(Scene scene, AssetManager assetManager) {
        this(scene, new BlockMapGenerateProcessorImpl(assetManager));
    }

    public BlockMapGenerate(Scene scene, BlockMapGenerateProcessor blockMapGenerateProcessor) {
        super(1);
        this.scene = scene;
        this.blockMapGenerateProcessor = blockMapGenerateProcessor;
    }

    public ConcurrentLinkedQueue<Result> getRes() {
        return res;
    }

    public Scene getScene() {
        return scene;
    }

    public boolean isRunning() {
        return running;
    }

    public void addGenerateQueue(Block block) {
        if (!running) {
            throw new IllegalStateException("Not running");
        }

        submit(new Processor(block, res, scene, blockMapGenerateProcessor));
    }

    public Result getResult() {
        return res.poll();
    }

    public BlockMapGenerateProcessor getBlockMapGenerateProcessor() {
        return blockMapGenerateProcessor;
    }

    @Override
    public void dispose() {
        running = false;
    }

    public static final class Result {
        public Block block;
        public Set<Node> nodes;
    }

    public static final class Processor implements AsyncTask<Object> {

        private final Block take;
        private final ConcurrentLinkedQueue<Result> res;
        private final Scene scene;
        private final BlockMapGenerateProcessor blockMapGenerateProcessor;

        public Processor(Block take, ConcurrentLinkedQueue<Result> res, Scene scene, BlockMapGenerateProcessor blockMapGenerateProcessor) {
            this.take = take;
            this.res = res;
            this.scene = scene;
            this.blockMapGenerateProcessor = blockMapGenerateProcessor;
        }

        @Override
        public Object call() throws Exception {

            Set<Node> generate = blockMapGenerateProcessor.generate(scene, take);
            Result result = new Result();
            result.block = take;
            result.nodes = generate;
            res.add(result);

            return null;
        }
    }
}
