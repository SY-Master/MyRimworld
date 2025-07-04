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

    /**
     * 获取当前时间加上秒数
     */
    public double getTimeBySecond(double second) {
        return time + second;
    }

    public double getTimeByMinute(double minute) {
        return time + minute * config.second;
    }

    public double getTimeByHour(double hour) {
        return time + hour * config.minute * config.second;
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
        return (int) Math.floor(time / config.month / config.day / config.hour / config.minute / config.second) + 1;
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
        return (int) Math.floor(getHour(time));
    }

    /**
     * 小时
     */
    public double getHourD() {
        return getHour(time);
    }

    /**
     * 小时
     */
    public double getHour(double time) {
        double v = time / (config.minute * config.second);

        double v1 = v % (config.month * config.day * config.hour);
        double v2 = v1 % (config.day * config.hour);
        return v2 % config.hour;
    }

    public int getMinute() {
        return getMinute(time);
    }

    public int getMinute(double time) {
        double v = time / config.second;

        double v1 = v % (config.month * config.day * config.hour * config.minute);
        double v2 = v1 % (config.day * config.hour * config.minute);
        double v3 = v2 % (config.hour * config.minute);
        double v4 = v3 % config.minute;

        return (int) Math.floor(v4);
    }


    @Override
    public boolean logic(float delta) {
        super.logic(delta);
        time += delta;
        return true;
    }

}
