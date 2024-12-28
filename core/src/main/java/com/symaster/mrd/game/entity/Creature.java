package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;

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

}
