package com.symaster.mrd.g2d.scene.impl;

import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;

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
    private boolean running = true;

    private BlockMapGenerateProcessor blockMapGenerateProcessor;

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

    public void setBlockMapGenerateProcessor(BlockMapGenerateProcessor blockMapGenerateProcessor) {
        this.blockMapGenerateProcessor = blockMapGenerateProcessor;
    }

    private Result generate(Block take) {
        if (blockMapGenerateProcessor == null) {
            return null;
        }
        Set<Node> generate = blockMapGenerateProcessor.generate(take);
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
