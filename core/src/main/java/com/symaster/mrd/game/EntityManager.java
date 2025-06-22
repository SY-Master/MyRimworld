package com.symaster.mrd.game;

import com.symaster.mrd.g2d.Node;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yinmiao
 * @since 2025/1/6
 */
public class EntityManager {

    private static final ConcurrentHashMap<String, Node> NODE_MAP = new ConcurrentHashMap<>();

    public static String nextId(Node node) {
        String newID = UUID.randomUUID().toString();

        if (NODE_MAP.containsKey(newID)) {
            return nextId(node);
        }

        NODE_MAP.put(newID, node);

        return newID;
    }

    public static boolean contains(String globalId) {
        return NODE_MAP.containsKey(globalId);
    }

    public static void remove(Node node) {
        NODE_MAP.remove(node.getGlobalId());
    }

    public static Node get(String globalId) {
        return NODE_MAP.get(globalId);
    }

    public static void put(Node node) {
        NODE_MAP.put(node.getGlobalId(), node);
    }

}
