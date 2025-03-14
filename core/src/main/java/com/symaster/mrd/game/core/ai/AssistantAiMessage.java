package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public class AssistantAiMessage extends AbstractAiMessage {

    public AssistantAiMessage(String content) {
        super(MessageRoleType.ASSISTANT, content);
    }

}
