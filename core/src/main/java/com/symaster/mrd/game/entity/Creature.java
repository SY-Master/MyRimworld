package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.service.AI;

/**
 * 生物实体
 *
 * @author yinmiao
 * @since 2024/12/27
 */
public class Creature extends Node {

    /**
     * 当前生命值（0-1）
     */
    private float hp;
    /**
     * 最大生命值
     */
    private float hpMax;
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
    private final Race race;
    /**
     * ai
     */
    private AI ai;

    public Creature(Race race) {
        this.race = race;
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

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getHpMax() {
        return hpMax;
    }

    public void setHpMax(float hpMax) {
        this.hpMax = hpMax;
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
