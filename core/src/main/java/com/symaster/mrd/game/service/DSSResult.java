package com.symaster.mrd.game.service;

import com.symaster.mrd.api.Command;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class DSSResult {

    // {type(动作类型，String，必填),dstId(交互目标ID，int，非必填),interactionContent(交互内容，Object，非必填，主要分两种类型说和做，如果说，请填写说什么，如果时做，请填写做什么):{type(说或做，String),val(说的内容或做的内容，String)},moveVector(移动向量，单位米，Object，非必填):{x(Double),y(Double)}}
    // 动作类型可选项(interaction：交互、move_to：向某个方向移动多少米，每次移动最少1米，最大100米)

    @Command("指令类型，interaction：交互、move_to：向某个方向移动多少米，每次移动最少1米，最大100米")
    private String type;
    @Command("交互内容，如果指令类型为‘交互’，则必填")
    private DSSInteractionContent interactionContent;
    @Command("移动向量，如果指令类型为‘移动’，则必填")
    private DSSVector moveVector;
    @Command("用来发出是否同意的指令，true为同意，false为不同意")
    private Boolean actionAgree;

    public Boolean getActionAgree() {
        return actionAgree;
    }

    public void setActionAgree(Boolean actionAgree) {
        this.actionAgree = actionAgree;
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
