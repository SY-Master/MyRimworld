package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2024/12/31
 */
public class GameTime extends Node {

    private final GameTimeConfig config;
    // 游戏总时间（秒）
    private double time;

    public GameTime() {
        this(0d);
    }

    public GameTime(double start) {
        this.time = start;
        this.config = new GameTimeConfig();
    }

    public double getTime() {
        return time;
    }

    public GameTimeConfig getConfig() {
        return config;
    }

    public int day() {
        return (int) Math.floor(time / 60);
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);
        time += delta;
    }
}
