package com.symaster.mrd.game.core.ai;

import com.alibaba.dashscope.common.Message;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public abstract class AbstractAiMessage implements AiMessage {

    private final MessageRoleType role;
    private final String content;

    protected AbstractAiMessage(MessageRoleType role, String content) {
        this.role = role;
        this.content = content;
    }

    public MessageRoleType getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Message toAliyunAiMessage() {
        return role.toAliyunAiMessage(this);
    }

}
