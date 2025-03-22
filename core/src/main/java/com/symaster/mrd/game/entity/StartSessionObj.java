package com.symaster.mrd.game.entity;

import java.util.List;

/**
 * @author yinmiao
 * @since 2025/3/18
 */
public class StartSessionObj {

    private List<Long> dstIds;
    private String sessionId;

    public List<Long> getDstIds() {
        return dstIds;
    }

    public void setDstIds(List<Long> dstIds) {
        this.dstIds = dstIds;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

}
