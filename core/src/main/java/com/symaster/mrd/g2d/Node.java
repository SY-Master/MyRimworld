package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.Creation;
import com.symaster.mrd.api.NodePropertiesChangeExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.EntityManager;
import com.symaster.mrd.game.GameSingleData;
import org.apache.commons.lang3.StringUtils;

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

    private final String globalId;
    /**
     * 节点本地坐标x
     */
    private float positionX;
    /**
     * 节点本地坐标y
     */
    private float positionY;
    /**
     * 节点的宽度
     */
    private float width;
    /**
     * 节点的高度
     */
    private float height;
    /**
     * 当前组件的父级节点
     */
    private Node parent;
    /**
     * 当前组件是否可见
     */
    private boolean visible;
    /**
     * 激活附近区块半径，0表示不激活区块，1表示激活当前组件所在区块，2表示记过当前组件周围一圈，以此类推
     */
    private int activityBlockSize;
    /**
     * 坐标更新扩展
     */
    private Set<PositionUpdateExtend> pue;
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
     * 是否强制逻辑计算，如果强制逻辑计算的话：不管节点是否处在激活区块内，logic方法都会触发
     */
    private boolean forcedLogic;
    /**
     * 是否忽略时间速率缩放
     */
    private boolean ignoreTimeScale;
    /**
     * 渲染层
     * @see Layer
     */
    private int layer;

    private int logicId;
    /**
     * 融合渲染, 该组件的所有子组件全部合并在一起渲染
     */
    private boolean fusionRender;

    public Node() {
        this(false);
    }

    public Node(String globalId) {
        this(false, globalId);
    }

    public Node(boolean triggerCreate) {
        this(triggerCreate, null);
    }

    public Node(boolean triggerCreate, String globalId) {
        this.positionX = 0.0f;
        this.positionY = 0.0f;
        this.visible = true;
        this.limit2activityBlock = false;
        this.activityBlockSize = 0;
        this.width = 0.0f;
        this.height = 0.0f;
        this.zIndex = 0;

        if (StringUtils.isBlank(globalId)) {
            this.globalId = EntityManager.nextId(this);
        } else {
            if (EntityManager.contains(globalId)) {
                throw new IllegalArgumentException("全局ID冲突");
            }
            this.globalId = globalId;
            EntityManager.put(this);
        }

        this.layer = Layer.OBJECT.getLayer();
        this.fusionRender = false;
        if (triggerCreate) {
            created();
        }
    }

    public boolean isFusionRender() {
        return fusionRender;
    }

    public void setFusionRender(boolean fusionRender) {
        this.fusionRender = fusionRender;
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

    public void removePue(PositionUpdateExtend pue) {
        if (this.pue != null) {
            this.pue.remove(pue);
        }
    }

    public void addPue(PositionUpdateExtend pue) {
        if (this.pue == null) {
            this.pue = new HashSet<>();
        }
        this.pue.add(pue);
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
     *
     * @return 是否需要调用子节点
     */
    public boolean logic(float delta) {
        return true;
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
    }

    public void setPosition(float x, float y) {
        if (this.positionX == x && this.positionY == y) {
            return;
        }
        float oldX = this.positionX;
        float oldY = this.positionY;

        boolean moveOn = true;

        if (pue != null) {
            for (PositionUpdateExtend positionUpdateExtend : pue) {
                boolean b = positionUpdateExtend.beforeUpdate(this, oldX, oldY, x, y);
                if (!b) {
                    moveOn = false;
                }
            }
        }

        if (moveOn) {
            this.positionX = x;
            this.positionY = y;
        }

        if (pue != null) {
            for (PositionUpdateExtend positionUpdateExtend : pue) {
                positionUpdateExtend.afterUpdate(this, oldX, oldY, x, y);
            }
        }
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float y) {
        if (this.positionY == y) {
            return;
        }

        setPosition(positionX, y);
    }

    public void translate(float x, float y) {
        if (x == 0 && y == 0) {
            return;
        }

        setPosition(positionX + x, positionY + y);
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

    public String getGlobalId() {
        return globalId;
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
    public void add(int index, Node element) {
        super.add(index, element);
        element.parent = this;
        if (cue != null) {
            cue.afterAdd(this, element);
        }
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
    public boolean removeIf(Predicate<? super Node> filter) {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置显示组件的世界位置
     */
    public void setGdxNodePosition(float x, float y) {

    }

    /**
     * 绘制该节点
     */
    public void draw(SpriteBatch spriteBatch) {

    }

    /**
     * 释放该节点的资源
     */
    @Override
    public void dispose() {
        EntityManager.remove(this);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Node node = (Node) o;
        return globalId == node.globalId;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + globalId.hashCode();
        return result;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void created() {

    }

    public <T> T getAsset(String key, Class<T> clazz) {
        return GameSingleData.mrAssetManager.get(getClass().getName(), key, clazz);
    }

    public <T> T getGlobalAsset(String key, Class<T> clazz) {
        return GameSingleData.mrAssetManager.getGlobal(key, clazz);
    }

}
