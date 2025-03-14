package com.symaster.mrd.game.core;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson2.JSONArray;
import com.symaster.mrd.game.core.ai.AiMessage;
import com.symaster.mrd.game.core.ai.AiResponse;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinmiao
 * @since 2025/3/13
 */
public class AliyunAiService extends AbstractAiService {

    private static final Logger logger = LoggerFactory.getLogger(AliyunAiService.class);

    @NotNull
    private static List<Message> toLocalMessage(List<AiMessage> contexts) {
        return contexts.stream().map(AiMessage::toAliyunAiMessage).collect(Collectors.toList());
    }

    private static void streamCheckParam(AiResponse aiResponse, String model) {
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("options == null");
        }
        if (aiResponse == null || aiResponse.getContent() == null) {
            throw new IllegalArgumentException(
                    "aiResponse == null || aiResponse.getContent() == null || aiResponse.getReasoningContent() == null");
        }
    }

    private Consumer<GenerationResult> getConsumer(AiResponse aiResponse) {
        return e -> {

            // System.out.println(e);

            GenerationOutput output = e.getOutput();
            if ("stop".equalsIgnoreCase(output.getFinishReason())) {
                return;
            }

            List<GenerationOutput.Choice> choices = output.getChoices();
            if (choices == null) {
                return;
            }
            for (GenerationOutput.Choice choice : choices) {
                Message message = choice.getMessage();
                if (message == null) {
                    continue;
                }

                String reasoningContent = message.getReasoningContent();
                if (reasoningContent != null && !reasoningContent.isEmpty() &&
                        aiResponse.getReasoningContent() != null) {
                    if (reasoningContent.startsWith(aiResponse.getReasoningContent().toString())) {
                        aiResponse.getReasoningContent().delete(0, aiResponse.getReasoningContent().length());
                    }

                    aiResponse.getReasoningContent().append(reasoningContent);
                }

                String content = message.getContent();
                if (content != null && !content.isEmpty()) {
                    if (content.startsWith(aiResponse.getContent().toString())) {
                        aiResponse.getContent().delete(0, aiResponse.getContent().length());
                    }

                    aiResponse.getContent().append(content);
                }
            }
        };
    }

    @Override
    public void blockStreamExec(AiResponse aiResponse, List<AiMessage> contexts, String model) {
        streamCheckParam(aiResponse, model);
        logger.info("contexts: {}", JSONArray.toJSONString(contexts));

        List<Message> msg = toLocalMessage(contexts);

        GenerationParam build = GenerationParam.builder()
                                               .apiKey("sk-00020fc401b44687ac66d5a0ff933a2e")
                                               .model(model)
                                               .messages(msg)
                                               .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                                               .build();

        Generation gen = new Generation();
        try {
            Flowable<GenerationResult> generationResultFlowable = gen.streamCall(build);
            GenerationResult generationResult = generationResultFlowable.doOnNext(getConsumer(aiResponse))
                                                                        .blockingLast();
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }
    }

}
