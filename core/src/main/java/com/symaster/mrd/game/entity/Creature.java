package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.service.DSS;
import com.symaster.mrd.game.service.ai.AI;

/**
 * 生物实体
 *
 * @author yinmiao
 * @since 2024/12/27
 */
public class Creature extends Node {

    /**
     * 生命值，自然回复或治疗
     */
    private Measure hp;
    /**
     * 食物，吃东西回复
     */
    private Measure food;
    /**
     * 精力，睡觉恢复
     */
    private Measure energy;
    /**
     * 性别
     */
    private Gender gender;
    /**
     * 生物的名称
     */
    private String name;
    /**
     * 种族
     */
    private Race race;
    /**
     * 是否是活着的
     */
    private boolean alive;
    /**
     * 生物的创建时间（降生日）（秒）
     */
    private double createTime;
    /**
     * 生物的最大寿命（年）
     */
    private int lifetime;
    /**
     * 决策系统
     */
    private DSS dss;

    public DSS getDss() {
        return dss;
    }

    public void setDss(DSS dss) {
        this.dss = dss;
    }

    public Measure getEnergy() {
        return energy;
    }

    public void setEnergy(Measure energy) {
        this.energy = energy;
    }

    public Measure getFood() {
        return food;
    }

    public void setFood(Measure food) {
        this.food = food;
    }

    public Measure getHp() {
        return hp;
    }

    public void setHp(Measure hp) {
        this.hp = hp;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Race getRace() {
        return race;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean logic(float delta) {
        super.logic(delta);
        dss.logic(this, delta);
        return true;
    }

    @Override
    public int getZIndex() {
        return (int) -getPositionY();
    }
}
