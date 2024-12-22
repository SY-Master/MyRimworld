package com.symaster.mrd.g2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * 场景节点
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Node extends ArrayList<Node> {

    private Node parent;

    /**
     * 每帧调用
     */
    public void update() {

    }

    /**
     * @return 父节点
     */
    public Node getParent() {
        return parent;
    }

    @Override
    public boolean add(Node node) {
        boolean add = super.add(node);
        if (add) {
            node.parent = this;
        }
        return add;
    }

    @Override
    public void add(int index, Node element) {
        super.add(index, element);
        element.parent = this;
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        boolean b = super.addAll(c);
        if (b) {
            for (Node node : c) {
                node.parent = this;
            }
        }
        return b;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Node> c) {
        boolean b = super.addAll(index, c);
        if (b) {
            for (Node node : c) {
                node.parent = this;
            }
        }
        return b;
    }

    @Override
    public Node remove(int index) {
        Node remove = super.remove(index);
        if (remove != null) {
            remove.parent = null;
        }
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);

        if (remove && o instanceof Node) {
            Node node = (Node) o;
            node.parent = null;
        }

        return remove;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = super.removeAll(c);
        if (b) {
            for (Object o : c) {
                if (o instanceof Node) {
                    Node node = (Node) o;
                    node.parent = null;
                }
            }
        }
        return b;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super Node> filter) {
        throw new UnsupportedOperationException();
    }
}
