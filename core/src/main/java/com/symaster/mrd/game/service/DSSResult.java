package com.symaster.mrd.game.service;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class DSSResult {

    // {type(动作类型，String，必填),dstId(交互目标ID，int，非必填),interactionContent(交互内容，Object，非必填，主要分两种类型说和做，如果说，请填写说什么，如果时做，请填写做什么):{type(说或做，String),val(说的内容或做的内容，String)},moveVector(移动向量，单位米，Object，非必填):{x(Double),y(Double)}}
    // 动作类型可选项(interaction：交互、move_to：向某个方向移动多少米，每次移动最少1米，最大100米)

    /**
     * interaction：交互、move_to：向某个方向移动多少米，每次移动最少1米，最大100米
     */
    private String type;
    private Long dstId;
    private DSSInteractionContent interactionContent;
    private DSSVector moveVector;

    public Long getDstId() {
        return dstId;
    }

    public void setDstId(Long dstId) {
        this.dstId = dstId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DSSInteractionContent getInteractionContent() {
        return interactionContent;
    }

    public void setInteractionContent(DSSInteractionContent interactionContent) {
        this.interactionContent = interactionContent;
    }

    public DSSVector getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(DSSVector moveVector) {
        this.moveVector = moveVector;
    }

}
