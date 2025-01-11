package com.symaster.mrd.game.entity;

import com.symaster.mrd.g2d.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * 选择的内容
 *
 * @author yinmiao
 * @since 2025/1/10
 */
public class SelectData extends Node {
    private final Set<Node> nodes;
    private final Set<Node> preSelection;

    public SelectData() {
        this.nodes = new HashSet<>();
        this.preSelection = new HashSet<>();
    }

    public boolean isSelected(Node node) {
        return nodes.contains(node);
    }

    public void select(Node node) {
        nodes.add(node);
    }

    public void deselect(Node node) {
        nodes.remove(node);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public boolean isPreSelection(Node node) {
        return preSelection.contains(node);
    }

    public void preSelect(Node node) {
        preSelection.add(node);
    }

    public void dePreSelect(Node node) {
        preSelection.remove(node);
    }

    public Set<Node> getPreSelection() {
        return preSelection;
    }

    public void clearAll() {
        nodes.clear();
        preSelection.clear();
    }

    public void clearPreSelection() {
        preSelection.clear();
    }

    public void clearSelection() {
        nodes.clear();
    }
}
