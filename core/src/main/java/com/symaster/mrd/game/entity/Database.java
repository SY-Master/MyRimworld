package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.service.ai.HumanAction;
import com.symaster.mrd.game.service.ai.TimeAllocation;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public class Database extends Node {

    /**
     * 某个生物当前的动作
     */
    private final Map<Long, HumanAction> humanActionMap;
    /**
     * 时间分配
     */
    private final Map<Integer, TimeAllocation> periodMap;

    private final SelectData selectData;

    public Database() {
        this.periodMap = new HashMap<>();
        this.humanActionMap = new HashMap<>();
        this.selectData = new SelectData();
    }

    /**
     * 获取当前时间周期
     *
     * @param hour 当天的小时
     */
    public TimeAllocation getTimeAllocation(int hour) {
        return periodMap.get(hour);
    }

    /**
     * 设置时间周期
     */
    public void setTimeAllocation(int hour, TimeAllocation timeAllocation) {
        periodMap.put(hour, timeAllocation);
    }

    public HumanAction getHumanAction(long id) {
        return humanActionMap.get(id);
    }

    public void setHumanAction(long id, HumanAction humanAction) {
        humanActionMap.put(id, humanAction);
    }

    public SelectData getSelectData() {
        return selectData;
    }

}
