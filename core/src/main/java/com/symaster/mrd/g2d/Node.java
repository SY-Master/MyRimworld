package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.NodePropertiesChangeExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.EntityIdGenerator;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * 场景节点
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Node extends LinkedList<Node> implements Disposable, Serializable, Creation {
    private static final long serialVersionUID = 1L;

    private final long id;
    private float positionY;
    private float width;
    private float height;
    /// 节点基本信息
    private float positionX;
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
    private NodePropertiesChangeExtend changeExtend;
    /**
     * 子节点改变扩展
     */
    private ChildUpdateExtend cue;
    /**
     * 渲染顺序
     */
    private int zIndex;
    /**
     * 将这个组件的移动限制在激活区块内
     */
    private boolean limit2activityBlock;
    /**
     * 当前组件所在场景
     */
    private Scene scene;
    /**
     * 是否强制逻辑计算，如果强制逻辑计算的话：不管节点是否处在激活区块内，都会逻辑计算
     */
    private boolean forcedLogic;
    /**
     * 是否忽略时间速率缩放
     */
    private boolean ignoreTimeScale;
    /**
     * 渲染层
     */
    private int layer;

    private int logicId;

    public Node() {
        this.positionX = 0.0f;
        this.positionY = 0.0f;
        this.visible = false;
        this.limit2activityBlock = false;
        this.activityBlockSize = 0;
        this.width = 0.0f;
        this.height = 0.0f;
        this.zIndex = 0;
        this.id = EntityIdGenerator.nextId();
        this.layer = Layer.OBJECT.getLayer();
    }

    public int getLogicId() {
        return logicId;
    }

    public void setLogicId(int logicId) {
        this.logicId = logicId;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public boolean isIgnoreTimeScale() {
        return ignoreTimeScale;
    }

    public void setIgnoreTimeScale(boolean ignoreTimeScale) {
        this.ignoreTimeScale = ignoreTimeScale;
    }

    public boolean isLimit2activityBlock() {
        return limit2activityBlock;
    }

    public void setLimit2activityBlock(boolean limit2activityBlock) {
        this.limit2activityBlock = limit2activityBlock;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public NodePropertiesChangeExtend getChangeExtend() {
        return changeExtend;
    }

    public void setChangeExtend(NodePropertiesChangeExtend changeExtend) {
        this.changeExtend = changeExtend;
    }

    public PositionUpdateExtend getPue() {
        return pue;
    }

    public void setPue(PositionUpdateExtend pue) {
        this.pue = pue;
    }

    public ChildUpdateExtend getCue() {
        return cue;
    }

    public void setCue(ChildUpdateExtend cue) {
        this.cue = cue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isForcedLogic() {
        return forcedLogic;
    }

    public void setForcedLogic(boolean forcedLogic) {
        this.forcedLogic = forcedLogic;

        if (forcedLogic && scene != null) {
            scene.addForcedLogic(this);
        }
        if (!forcedLogic && scene != null) {
            scene.removeForcedLogic(this);
        }
    }

    /**
     * 每帧调用，节点逻辑处理
     */
    public void logic(float delta) {
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

        if (changeExtend != null) {
            changeExtend.activityBlockSizeAfterUpdate(this, old, activityBlockSize);
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
        setPosition(positionX, positionY);

        // float oldX = this.positionX;
        // this.positionX = positionX;
        // if (pue != null) {
        //     pue.afterUpdate(this, oldX, this.positionY, this.positionX, this.positionY);
        // }
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float y) {
        if (this.positionY == y) {
            return;
        }

        setPosition(positionX, y);
        // float oldY = this.positionY;
        // this.positionY = y;
        // if (pue != null) {
        //     pue.afterUpdate(this, this.positionX, oldY, this.positionX, this.positionY);
        // }
    }

    public void setPosition(float x, float y) {
        if (this.positionX == x && this.positionY == y) {
            return;
        }
        float oldX = this.positionX;
        float oldY = this.positionY;

        if (pue == null || pue.beforeUpdate(this, oldX, oldY, x, y)) {
            this.positionX = x;
            this.positionY = y;

            if (pue != null) {
                pue.afterUpdate(this, oldX, oldY, x, y);
            }
        }
    }

    public void translate(float x, float y) {
        if (x == 0 && y == 0) {
            return;
        }

        float oldX = this.positionX;
        float oldY = this.positionY;

        if (pue == null || pue.beforeUpdate(this, oldX, oldY, positionX + x, positionY + y)) {
            this.positionX += x;
            this.positionY += y;

            if (pue != null) {
                pue.afterUpdate(this, oldX, oldY, this.positionX, this.positionY);
            }
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

    /**
     * 当前节点被添加进场景事件
     *
     * @param scene 场景
     */
    public void onScene(Scene scene) {
        this.scene = scene;

        if (this.forcedLogic) {
            scene.addForcedLogic(this);
        }
    }

    /**
     * 当前场景被移除场景事件
     *
     * @param scene 场景
     */
    public void extScene(Scene scene) {
        this.scene = null;

        if (this.forcedLogic) {
            scene.removeForcedLogic(this);
        }
    }

    public long getId() {
        return id;
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public boolean add(Node node) {
        boolean add = super.add(node);
        if (add) {
            node.parent = this;
        }
        if (cue != null) {
            cue.afterAdd(this, node);
        }
        return add;
    }

    @Override
    public void add(int index, Node element) {
        super.add(index, element);
        element.parent = this;
        if (cue != null) {
            cue.afterAdd(this, element);
        }
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        boolean b = super.addAll(c);
        if (b) {
            for (Node node : c) {
                node.parent = this;
                if (cue != null) {
                    cue.afterAdd(this, node);
                }
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
                if (cue != null) {
                    cue.afterAdd(this, node);
                }
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
        if (cue != null) {
            cue.afterRemove(this, remove);
        }
        return remove;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = super.remove(o);

        if (remove && o instanceof Node) {
            Node node = (Node) o;
            node.parent = null;

            if (cue != null) {
                cue.afterRemove(this, node);
            }
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

                    if (cue != null) {
                        cue.afterRemove(this, node);
                    }
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

    /**
     * 设置显示组件的世界位置
     */
    public void setGdxNodePosition(float x, float y) {

    }

    /**
     * 如果子节点需要绘制
     */
    public void draw(SpriteBatch spriteBatch) {

    }

    /**
     * 如果子节点需要释放资源
     */
    @Override
    public void dispose() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Long.hashCode(id);
        return result;
    }

    @Override
    public void create() {

    }
}
