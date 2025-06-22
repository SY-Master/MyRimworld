package com.symaster.mrd.game.entity;

import com.symaster.mrd.game.service.DSSInteractionContent;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class MessageResult {

    /**
     * 发送者ID
     */
    private String srcId;

    /**
     * 消息
     */
    private DSSInteractionContent dssInteractionContent;

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public DSSInteractionContent getDssInteractionContent() {
        return dssInteractionContent;
    }

    public void setDssInteractionContent(DSSInteractionContent dssInteractionContent) {
        this.dssInteractionContent = dssInteractionContent;
    }

}
