package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public class SystemAiMessage extends AbstractAiMessage {

    public SystemAiMessage(String content) {
        super(MessageRoleType.SYSTEM, content);
    }

}
