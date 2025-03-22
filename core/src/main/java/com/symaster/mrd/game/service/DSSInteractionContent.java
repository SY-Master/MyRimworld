package com.symaster.mrd.game.service;

import com.symaster.mrd.api.Command;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class DSSInteractionContent {

    /**
     * 指定交互目标实体ID
     */
    @Command("指定交互目标实体ID")
    private Long dstId;
    /**
     * 指令交互类型
     */
    @Command("指令交互类型,‘说’、‘做’")
    private String type;
    /**
     * 会话的状态，“start”、“in_session”、“end”
     */
    @Command("会话的状态，“start”、“in_session”、“end”")
    private String status;
    /**
     * 指定交互内容
     */
    @Command("指定交互内容")
    private String val;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
