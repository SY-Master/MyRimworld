package com.symaster.mrd.g2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * 场景节点
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Node extends ArrayList<Node> {

    private float positionX;
    private float positionY;
    /**
     * 当前组件的父级节点
     */
    private Node parent;
    /**
     * 当前组件是否可见
     */
    private boolean visible;
    /**
     * 激活附近区块半径，0表示不激活区块，1表示激活当前组件所在区域，2表示记过当前组件周围一圈，以此类推
     */
    private int activityBlockSize;
    /**
     * 坐标更新扩展
     */
    private PositionUpdateExtend pue;
    /**
     * 激活附近区块半径更新扩展
     */
    private ActivityBlockSizeExtend bse;

    public Node() {
        this.positionX = 0.0f;
        this.positionY = 0.0f;
        this.visible = false;
        this.activityBlockSize = 0;
    }

    public ActivityBlockSizeExtend getBse() {
        return bse;
    }

    public void setBse(ActivityBlockSizeExtend bse) {
        this.bse = bse;
    }

    public PositionUpdateExtend getPue() {
        return pue;
    }

    public void setPue(PositionUpdateExtend pue) {
        this.pue = pue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * 每帧调用
     */
    public void update() {

    }

    public int getActivityBlockSize() {
        return activityBlockSize;
    }

    public void setActivityBlockSize(int activityBlockSize) {
        if (this.activityBlockSize == activityBlockSize) {
            return;
        }

        int old = this.activityBlockSize;

        this.activityBlockSize = activityBlockSize;

        if (bse != null) {
            bse.update(this, old, activityBlockSize);
        }
    }

    /**
     * @return 父节点
     */
    public Node getParent() {
        return parent;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        if (this.positionX == positionX) {
            return;
        }
        float oldX = this.positionX;
        this.positionX = positionX;
        if (pue != null) {
            pue.update(this, oldX, this.positionY, this.positionX, this.positionY);
        }
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float y) {
        if (this.positionY == y) {
            return;
        }
        float oldY = this.positionY;
        this.positionY = y;
        if (pue != null) {
            pue.update(this, this.positionX, oldY, this.positionX, this.positionY);
        }
    }

    public void setPosition(float x, float y) {
        if (this.positionX == x && this.positionY == y) {
            return;
        }
        float oldX = this.positionX;
        float oldY = this.positionY;
        this.positionX = x;
        this.positionY = y;
        if (pue != null) {
            pue.update(this, oldX, oldY, this.positionX, this.positionY);
        }
    }

    public void translate(float x, float y) {
        if (x == 0 && y == 0) {
            return;
        }

        float oldX = this.positionX;
        float oldY = this.positionY;

        this.positionX += x;
        this.positionY += y;

        if (pue != null) {
            pue.update(this, oldX, oldY, this.positionX, this.positionY);
        }
    }

    public <T> List<T> getNode(Class<T> clazz) {
        List<T> rtn = new ArrayList<>();

        for (Node node : this) {
            if (clazz.isAssignableFrom(node.getClass())) {
                rtn.add(clazz.cast(node));
            }
        }

        return rtn;
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
