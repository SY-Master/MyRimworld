package com.symaster.mrd.game.service.ai;

import com.symaster.mrd.g2d.Node;

import java.util.HashMap;
import java.util.Map;

/**
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

    public Database() {
        this.periodMap = new HashMap<>();
        this.humanActionMap = new HashMap<>();
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


}
