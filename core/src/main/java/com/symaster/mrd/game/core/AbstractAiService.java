package com.symaster.mrd.game.core;

import com.symaster.mrd.game.core.ai.AiMessage;
import com.symaster.mrd.game.core.ai.AiRequestType;
import com.symaster.mrd.game.core.ai.AiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public abstract class AbstractAiService implements AiService {

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public List<AiMessage> buildPrompt(List<AiMessage> prePrompt,
                                       List<AiMessage> mainPrompt,
                                       List<AiMessage> context,
                                       List<AiMessage> message,
                                       List<AiMessage> postPrompt) {
        List<AiMessage> rtn = new ArrayList<>();

        if (prePrompt != null && !prePrompt.isEmpty()) {
            rtn.addAll(prePrompt);
        }
        if (mainPrompt != null && !mainPrompt.isEmpty()) {
            rtn.addAll(mainPrompt);
        }
        if (context != null && !context.isEmpty()) {
            rtn.addAll(context);
        }
        if (message != null && !message.isEmpty()) {
            rtn.addAll(message);
        }
        if (postPrompt != null && !postPrompt.isEmpty()) {
            rtn.addAll(postPrompt);
        }

        return rtn;
    }

    /**
     * 阻塞流式掉用
     *
     * @param aiResponse 响应体
     * @param contexts   信息
     * @param model      大模型
     */
    @Override
    public final void blockStream(AiResponse aiResponse, List<AiMessage> contexts, String model) {
        aiResponse.setAiRequestType(AiRequestType.REQUESTING);

        try {
            blockStreamExec(aiResponse, contexts, model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            aiResponse.setAiRequestType(AiRequestType.FINISH);
        }
    }

    /**
     * 非阻塞流式掉用
     *
     * @param aiResponse 响应体
     * @param contexts   信息
     * @param model      大模型
     */
    @Override
    public void stream(AiResponse aiResponse, List<AiMessage> contexts, String model) {
        threadPool.execute(() -> blockStream(aiResponse, contexts, model));
    }

    public abstract void blockStreamExec(AiResponse aiResponse, List<AiMessage> contexts, String model);

    @Override
    public void close() throws IOException {
        threadPool.shutdownNow();
    }

}
