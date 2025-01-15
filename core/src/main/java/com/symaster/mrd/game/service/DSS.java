package com.symaster.mrd.game.service;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Creature;
import com.symaster.mrd.game.entity.Database;
import com.symaster.mrd.game.entity.GameTime;
import com.symaster.mrd.game.entity.Race;
import com.symaster.mrd.game.service.ai.HumanAction;
import com.symaster.mrd.game.service.ai.TimeAllocation;

import java.util.Set;

/**
 * 决策系统 (Decision Support System, DSS)
 * <p>小人的大脑</p>
 * <p>核心方法{@link DSS#logic(Creature, float)}</p>
 *
 * @author yinmiao
 * @since 2025/1/13
 */
public class DSS extends Node {

    /**
     * 当前场景的数据库
     */
    private Database database;
    /**
     * 当前场景的游戏时间
     */
    private GameTime gameTime;

    /**
     * 人类种族决策
     *
     * @param nodes 决策对象
     * @param delta 上一帧耗时
     */
    private void human(Creature nodes, float delta) {
        if (dssNotAvailable()) {
            return;
        }

        HumanAction humanAction1 = database.getHumanAction(nodes.getId());
        if (humanAction1 != null) {
            humanAction1.logic(getScene(), database, gameTime, nodes, delta);
            return;
        }

        // 时间分配设定
        TimeAllocation timeAllocation;
        if (database.getTimeAllocation(gameTime.getHour()) == null) { // null 为未定义（自由活动）
            timeAllocation = TimeAllocation.FreeActivities;
        } else {
            timeAllocation = database.getTimeAllocation(gameTime.getHour());
        }

        // 当前时间分配
        HumanAction[] humanAction = timeAllocation.getHumanAction();

        HumanAction action = null;
        for (HumanAction actionFor : humanAction) {
            if (actionFor.available(getScene(), database, gameTime, nodes, delta)) {
                action = actionFor;
                break;
            }
        }

        if (action != null) {
            action.logic(getScene(), database, gameTime, nodes, delta);
        }
    }

    /**
     * 小人每帧会调用该方法
     *
     * @param nodes 调用的对象
     * @param delta 上一帧耗时
     */
    public void logic(Creature nodes, float delta) {
        if (dssNotAvailable()) {
            return;
        }

        if (nodes.getRace() == Race.Human) {
            // 人类的AI
            human(nodes, delta);
        }
    }

    /**
     * DSS是否不可用，
     *
     * @return 是否不可用
     */
    public boolean dssNotAvailable() {
        return getScene() == null || gameTime == null || database == null;
    }

    /**
     * 当前节点被添加进场景事件
     *
     * @param scene 场景
     */
    @Override
    public void onScene(Scene scene) {
        super.onScene(scene);

        Set<Node> byGroup = getScene().getByGroup(Groups.DATABASE);
        if (byGroup == null) {
            getScene().add(new Database(), Groups.DATABASE);
        }

        database = (Database) getScene().getByGroup(Groups.DATABASE).iterator().next();

        Set<Node> byGroup1 = scene.getByGroup(Groups.TIMER);
        if (byGroup1 != null) {
            gameTime = (GameTime) byGroup1.iterator().next();
        }
    }

    /**
     * 当前场景被移除场景事件
     *
     * @param scene 场景
     */
    @Override
    public void extScene(Scene scene) {
        super.extScene(scene);
        this.database = null;
        this.gameTime = null;
    }
}
