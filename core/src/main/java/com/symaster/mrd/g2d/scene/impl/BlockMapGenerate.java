package com.symaster.mrd.g2d.scene.impl;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.BlockMapGenerateProcessorImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yinmiao
 * @since 2024/12/28
 */
public class BlockMapGenerate implements Runnable, Disposable {

    /**
     * 任务队列
     */
    private final LinkedBlockingQueue<Block> generateQueue = new LinkedBlockingQueue<>();
    /**
     * 结果队列
     */
    private final ConcurrentLinkedQueue<Result> res = new ConcurrentLinkedQueue<>();

    private final Scene scene;
    private final BlockMapGenerateProcessor blockMapGenerateProcessor;

    private boolean running = true;

    public BlockMapGenerate(Scene scene, AssetManager assetManager) {
        this(scene, new BlockMapGenerateProcessorImpl(assetManager));
    }

    public BlockMapGenerate(Scene scene, BlockMapGenerateProcessor blockMapGenerateProcessor) {
        this.scene = scene;
        this.blockMapGenerateProcessor = blockMapGenerateProcessor;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Block take = generateQueue.take();
                Result generate = generate(take);
                if (generate != null) {
                    res.add(generate);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void addGenerateQueue(Block block) {
        if (!running) {
            throw new IllegalStateException("Not running");
        }

        generateQueue.add(block);
    }

    public Result getResult() {
        return res.poll();
    }

    public BlockMapGenerateProcessor getBlockMapGenerateProcessor() {
        return blockMapGenerateProcessor;
    }

    private Result generate(Block take) {
        if (blockMapGenerateProcessor == null) {
            return null;
        }
        Set<Node> generate = blockMapGenerateProcessor.generate(scene, take);
        Result result = new Result();
        result.block = take;
        result.nodes = generate;
        return result;
    }

    @Override
    public void dispose() {
        running = false;
    }

    public static final class Result {
        public Block block;
        public Set<Node> nodes;
    }
}
