package com.symaster.mrd.game.core.ai;

/**
 * @author yinmiao
 * @since 2025/3/12
 */
public class AiResponse {

    private AiRequestType aiRequestType = AiRequestType.NOT_STARTED;
    /**
     * 思考过程
     */
    private StringBuilder reasoningContent;
    private StringBuilder content;

    public AiRequestType getAiRequestType() {
        return aiRequestType;
    }

    public void setAiRequestType(AiRequestType aiRequestType) {
        this.aiRequestType = aiRequestType;
    }

    public StringBuilder getReasoningContent() {
        return reasoningContent;
    }

    public void setReasoningContent(StringBuilder reasoningContent) {
        this.reasoningContent = reasoningContent;
    }

    public StringBuilder getContent() {
        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

}
