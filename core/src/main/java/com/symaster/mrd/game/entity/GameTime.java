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

    /**
     * 年份
     */
    public int getYear() {
        return getYear(time);
    }

    /**
     * 年份
     */
    public int getYear(double time) {
        return (int) Math.floor(time / config.month / config.day / config.hour / config.minute / config.second);
    }

    /**
     * 月份
     */
    public int getMonth() {
        return getMonth(time);
    }

    /**
     * 月份
     */
    public int getMonth(double time) {

        int i = config.day * config.hour * config.minute * config.second;
        double v1 = time / i % config.month;

        return (int) Math.floor(v1 + 1);
    }

    /**
     * 天
     */
    public int getDay() {
        return getDay(time);
    }

    /**
     * 天
     */
    public int getDay(double time) {

        // 每天多少秒
        int i = config.hour * config.minute * config.second;

        // 一共多少天
        double v = time / i;

        double v1 = v % (config.month * config.day);

        double v2 = v1 % config.day;

        return (int) Math.floor(v2) + 1;
    }

    /**
     * 小时
     */
    public int getHour() {
        return getHour(time);
    }

    /**
     * 小时
     */
    private int getHour(double time) {
        double v = time / (config.minute * config.second);

        double v1 = v % (config.month * config.day * config.hour);
        double v2 = v1 % (config.day * config.hour);
        double v3 = v2 % config.hour;

        return (int) Math.floor(v3) + 1;
    }

    @Override
    public void logic(float delta) {
        super.logic(delta);
        time += delta;
    }
}
