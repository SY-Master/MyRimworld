package com.symaster.mrd.game.core.ai;


/**
 * @author yinmiao
 * @since 2025/3/13
 */
public interface AiMessage {

    MessageRoleType getRole();

    String getContent();

    com.alibaba.dashscope.common.Message toAliyunAiMessage();

}
