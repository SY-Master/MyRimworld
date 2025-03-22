package com.symaster.mrd.game.entity;

import com.symaster.mrd.game.core.ai.AiResponse;
import com.symaster.mrd.game.service.DSSResult;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class NodeActionData {

    private NodeActionEnum nodeActionEnum;
    /**
     * 如果是等待，则这里不为空，表是等待至多少游戏秒
     */
    private Double waitSecond;

    private Float moveToX;
    private Float moveToY;
    /**
     * 交互
     */
    private DSSResult dssResult;
    /**
     * 思考过程
     */
    private AiResponse aiResponse;
    /**
     * 记录节点记忆
     */
    private String nodeMemory;
    /**
     * 开始会话
     */
    private StartSessionObj startSessionObj;

    public NodeActionData(NodeActionEnum nodeActionEnum, AiResponse aiResponse) {
        this.nodeActionEnum = nodeActionEnum;
        this.aiResponse = aiResponse;
    }

    public NodeActionData(NodeActionEnum nodeActionEnum, Float moveToX, Float moveToY) {
        this.nodeActionEnum = nodeActionEnum;
        this.moveToX = moveToX;
        this.moveToY = moveToY;
    }

    public NodeActionData(NodeActionEnum nodeActionEnum, Double waitSecond) {
        this.nodeActionEnum = nodeActionEnum;
        this.waitSecond = waitSecond;
    }

    public NodeActionData(NodeActionEnum nodeActionEnum, DSSResult dssResult) {
        this.nodeActionEnum = nodeActionEnum;
        this.dssResult = dssResult;
    }

    public NodeActionData(NodeActionEnum nodeActionEnum, String nodeMemory) {
        this.nodeActionEnum = nodeActionEnum;
        this.nodeMemory = nodeMemory;
    }

    public NodeActionData(NodeActionEnum nodeActionEnum, StartSessionObj startSessionObj) {
        this.nodeActionEnum = nodeActionEnum;
        this.startSessionObj = startSessionObj;
    }

    public StartSessionObj getStartSessionObj() {
        return startSessionObj;
    }

    public void setStartSessionObj(StartSessionObj startSessionObj) {
        this.startSessionObj = startSessionObj;
    }

    public String getNodeMemory() {
        return nodeMemory;
    }

    public void setNodeMemory(String nodeMemory) {
        this.nodeMemory = nodeMemory;
    }

    public DSSResult getDssResult() {
        return dssResult;
    }

    public void setDssResult(DSSResult dssResult) {
        this.dssResult = dssResult;
    }

    public AiResponse getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(AiResponse aiResponse) {
        this.aiResponse = aiResponse;
    }

    public Float getMoveToX() {
        return moveToX;
    }

    public void setMoveToX(Float moveToX) {
        this.moveToX = moveToX;
    }

    public Float getMoveToY() {
        return moveToY;
    }

    public void setMoveToY(Float moveToY) {
        this.moveToY = moveToY;
    }

    public NodeActionEnum getNodeActionEnum() {
        return nodeActionEnum;
    }

    public void setNodeActionEnum(NodeActionEnum nodeActionEnum) {
        this.nodeActionEnum = nodeActionEnum;
    }

    public Double getWaitSecond() {
        return waitSecond;
    }

    public void setWaitSecond(Double waitSecond) {
        this.waitSecond = waitSecond;
    }

}
