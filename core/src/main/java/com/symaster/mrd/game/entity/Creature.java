package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.service.ai.AI;

/**
 * 生物实体
 *
 * @author yinmiao
 * @since 2024/12/27
 */
public class Creature extends Node {

    /**
     * 生命值
     */
    private Measure hp;
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
     * ai
     */
    private AI ai;
    /**
     * 是否是活着的
     */
    private boolean alive;
    /**
     * 生物的创建时间（降生日）
     */
    private GameTime createTime;
    /**
     * 生物的最大寿命（年）
     */
    private int lifetime;

    public Measure getHp() {
        return hp;
    }

    public void setHp(Measure hp) {
        this.hp = hp;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public GameTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(GameTime createTime) {
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

    public AI getAi() {
        return ai;
    }

    public void setAi(AI ai) {
        this.ai = ai;
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
    public void logic(float delta) {
        super.logic(delta);
        ai.logic(this, delta);
    }
}
