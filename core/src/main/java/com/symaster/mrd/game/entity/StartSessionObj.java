package com.symaster.mrd.game.entity;

import java.util.List;

/**
 * @author yinmiao
 * @since 2025/3/18
 */
public class StartSessionObj {

    private List<String> dstIds;
    private String sessionId;

    public List<String> getDstIds() {
        return dstIds;
    }

    public void setDstIds(List<String> dstIds) {
        this.dstIds = dstIds;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

}
