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
     * 保存所有节点的状态
     */
    private final Map<String, Status> statusMap;

    private final Map<Integer, Period> periodMap;

    public Database() {
        this.statusMap = new HashMap<>();
        this.periodMap = new HashMap<>();
    }

    /**
     * 获取节点的状态
     */
    public Status getStatus(String uid) {
        return statusMap.get(uid);
    }

    /**
     * 修改节点状态
     *
     * @param uid    节点id
     * @param status 状态
     */
    public void setStatus(String uid, Status status) {
        statusMap.put(uid, status);
    }

    /**
     * 获取当前时间周期
     *
     * @param hour 当天的小时
     */
    public Period getPeriod(int hour) {
        return periodMap.get(hour);
    }

    /**
     * 设置时间周期
     */
    public void setPeriod(int hour, Period period) {
        periodMap.put(hour, period);
    }


}
