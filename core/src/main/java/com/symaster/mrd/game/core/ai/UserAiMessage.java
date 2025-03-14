package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public class UserAiMessage extends AbstractAiMessage {

    public UserAiMessage(String content) {
        super(MessageRoleType.USER, content);
    }

}
