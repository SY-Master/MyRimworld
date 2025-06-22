package com.symaster.mrd.game.entity;

import java.util.*;

/**
 * @author yinmiao
 * @since 2025/3/14
 */
public class NodeMemory {

    private final Map<String, List<String>>  nodeMemoryMap;

    public NodeMemory() {
        this.nodeMemoryMap = new HashMap<>();
    }

    public void add(String id, String format) {
        nodeMemoryMap.computeIfAbsent(id, x -> new ArrayList<>()).add(format);
    }

    public List<String> getAll(String id) {
        List<String> strings = nodeMemoryMap.get(id);
        if (strings == null) {
            return Collections.emptyList();
        }

        return strings;
    }

}
