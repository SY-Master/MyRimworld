package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public enum MessageRoleType {
    USER(1, "user") {
        @Override
        public com.alibaba.dashscope.common.Message toAliyunAiMessage(AiMessage message) {
            com.alibaba.dashscope.common.Message message1 = new com.alibaba.dashscope.common.Message();
            message1.setContent(message.getContent());
            message1.setRole(message.getRole().code);
            return message1;
        }
    },
    ASSISTANT(2, "assistant") {
        @Override
        public com.alibaba.dashscope.common.Message toAliyunAiMessage(AiMessage message) {
            com.alibaba.dashscope.common.Message message1 = new com.alibaba.dashscope.common.Message();
            message1.setContent(message.getContent());
            message1.setRole(message.getRole().code);
            return message1;
        }
    },
    SYSTEM(3, "system") {
        @Override
        public com.alibaba.dashscope.common.Message toAliyunAiMessage(AiMessage message) {
            com.alibaba.dashscope.common.Message message1 = new com.alibaba.dashscope.common.Message();
            message1.setContent(message.getContent());
            message1.setRole(message.getRole().code);
            return message1;
        }
    };

    private final int type;
    private final String code;

    MessageRoleType(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public abstract com.alibaba.dashscope.common.Message toAliyunAiMessage(AiMessage message);

    public int getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}
