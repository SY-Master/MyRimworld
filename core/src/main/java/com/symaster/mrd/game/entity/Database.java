package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.game.core.ai.AiResponse;
import com.symaster.mrd.game.service.ai.TimeAllocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数据库
 *
 * @author yinmiao
 * @since 2024/12/30
 */
public class Database extends Node {

    private final Map<Long, Queue<NodeActionData>> nodeActionDataMap;

    /**
     * 时间分配
     */
    private final Map<Integer, TimeAllocation> periodMap;

    private final Map<Long, NodeStatusEnum> nodeStatusMap;

    private final Map<Long, AiResponse> aiResponseMap;

    private final SelectData selectData;

    public Database() {
        this.periodMap = new HashMap<>();
        this.selectData = new SelectData();
        this.aiResponseMap = new HashMap<>();
        this.nodeStatusMap = new HashMap<>();
        this.nodeActionDataMap = new HashMap<>();
    }

    public AiResponse getAiResponse(long nodeId) {
        return aiResponseMap.get(nodeId);
    }

    public void setAiResponse(long nodeId, AiResponse aiResponse) {
        aiResponseMap.put(nodeId, aiResponse);
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

    public Queue<NodeActionData> getNodeActionData(long id) {
        Queue<NodeActionData> nodeActionData = nodeActionDataMap.get(id);
        if (nodeActionData == null) {
            LinkedBlockingQueue<NodeActionData> rtn = new LinkedBlockingQueue<>();
            nodeActionDataMap.put(id, rtn);
            return rtn;
        }

        return nodeActionData;
    }

    public void clearNodeActionData(long id) {
        nodeActionDataMap.remove(id);
    }

    public void putNodeActionData(long id, NodeActionData nodeActionData) {
        nodeActionDataMap.computeIfAbsent(id, k -> new LinkedBlockingQueue<>()).add(nodeActionData);
    }

    public NodeStatusEnum getNodeStatus(long id) {
        return nodeStatusMap.get(id);
    }

    public void setNodeStatus(long id, NodeStatusEnum nodeStatus) {
        nodeStatusMap.put(id, nodeStatus);
    }


    public SelectData getSelectData() {
        return selectData;
    }

}
