package com.symaster.mrd.game.entity;

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
