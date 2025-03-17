package com.symaster.mrd.game.entity;

import com.symaster.mrd.game.service.DSSInteractionContent;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class MessageQueue {

    private final Map<Long, Queue<MessageResult>> messageResultMap;

    public MessageQueue() {
        this.messageResultMap = new HashMap<>();
    }

    public void send(long id, long dstId, DSSInteractionContent interactionContent) {
        MessageResult messageResult = new MessageResult();
        messageResult.setSrcId(id);
        messageResult.setDssInteractionContent(interactionContent);
        messageResultMap.computeIfAbsent(dstId, k -> new LinkedBlockingQueue<>()).add(messageResult);
    }

    public MessageResult peek(long id) {
        Queue<MessageResult> messageResults = messageResultMap.get(id);
        if (messageResults == null) {
            return null;
        }

        return messageResults.peek();
    }

    public MessageResult poll(long id) {
        Queue<MessageResult> messageResults = messageResultMap.get(id);
        if (messageResults == null) {
            return null;
        }

        return messageResults.poll();
    }

}
