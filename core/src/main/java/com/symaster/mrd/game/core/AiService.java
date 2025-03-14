package com.symaster.mrd.game.core;

import com.symaster.mrd.game.core.ai.AiMessage;
import com.symaster.mrd.game.core.ai.AiResponse;

import java.io.Closeable;
import java.util.List;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public interface AiService extends Closeable {

    List<AiMessage> buildPrompt(List<AiMessage> prePrompt,
                                List<AiMessage> mainPrompt,
                                List<AiMessage> context,
                                List<AiMessage> message,
                                List<AiMessage> postPrompt);

    /**
     * 阻塞流式掉用
     *
     * @param aiResponse 响应体
     * @param contexts   信息
     * @param model      大模型
     */
    void blockStream(AiResponse aiResponse, List<AiMessage> contexts, String model);

    /**
     * 非阻塞流式掉用
     *
     * @param aiResponse 响应体
     * @param contexts   信息
     * @param model      大模型
     */
    void stream(AiResponse aiResponse, List<AiMessage> contexts, String model);
}
