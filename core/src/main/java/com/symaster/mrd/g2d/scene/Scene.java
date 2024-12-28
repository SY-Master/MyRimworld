package com.symaster.mrd.g2d.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.api.ActivityBlockSizeExtend;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.OrthographicCameraNode;
import com.symaster.mrd.g2d.Creation;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.impl.ActivityBlockSizeExtendImpl;
import com.symaster.mrd.g2d.scene.impl.ChildUpdateExtendImpl;
import com.symaster.mrd.g2d.scene.impl.PositionUpdateExtendImpl;
import com.symaster.mrd.input.InputFactory;
import com.symaster.mrd.util.UnitUtil;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 场景
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Scene implements Serializable, Creation {
    private static final long serialVersionUID = 1L;

    /**
     * 区块大小
     */
    private final float blockSize;
    /**
     * 分区块Node表
     */
    private final Map<Block, Set<Node>> nodes;
    private final Map<Node, Set<Block>> activityBlockMap;
    private final Set<Block> activeBlocks;
    private final Set<OrthographicCameraNode> orthographicCameraNodes;

    private InputFactory inputFactory;

    private final Cache renderCache;

    private transient ActivityBlockSizeExtend activityBlockSizeExtend;
    private transient PositionUpdateExtend positionUpdateExtend;
    private transient ChildUpdateExtend childUpdateExtend;
    private transient SpriteBatch spriteBatch;

    public Scene() {
        this(UnitUtil.ofM(SystemConfig.BLOCK_SIZE)); // 默认区块边长10米
    }

    public Scene(float blockSize) {
        this.blockSize = blockSize;
        nodes = new HashMap<>();
        this.activityBlockMap = new HashMap<>();
        this.activeBlocks = new HashSet<>();
        this.orthographicCameraNodes = new HashSet<>();
        this.renderCache = new Cache();
    }

    @Override
    public void create() {
        this.spriteBatch = new SpriteBatch();
        this.activityBlockSizeExtend = new ActivityBlockSizeExtendImpl(this);
        this.positionUpdateExtend = new PositionUpdateExtendImpl(this);
        this.childUpdateExtend = new ChildUpdateExtendImpl(this);
    }

    public float getBlockSize() {
        return blockSize;
    }

    public Map<Block, Set<Node>> getNodes() {
        return nodes;
    }

    public Map<Node, Set<Block>> getActivityBlockMap() {
        return activityBlockMap;
    }

    public Set<Block> getActiveBlocks() {
        return activeBlocks;
    }

    public Set<OrthographicCameraNode> getCameraNodes() {
        return orthographicCameraNodes;
    }

    public Cache getRenderCache() {
        return renderCache;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public InputFactory getInputFactory() {
        return inputFactory;
    }

    public void setInputFactory(InputFactory inputFactory) {
        this.inputFactory = inputFactory;
    }

    public void blockUpdate(Node node, int newSize) {
        if (newSize > 0) {
            // 添加
            activityBlockMap.put(node, getNodeActivityBlocks(node, newSize));
            updateActivityBlockSize();
        } else {
            // 移除
            activityBlockMap.remove(node);
        }
    }

    private void updateActivityBlockSize() {
        activeBlocks.clear();
        activeBlocks.addAll(activityBlockMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet()));
    }

    public Set<Block> getNodeActivityBlocks(Node node, int newSize) {
        Set<Block> blocks = new HashSet<>();

        Block centerBlockIndex = getBlockIndex(node.getPositionX(), node.getPositionY());

        int i1 = newSize - 1;
        int i2 = newSize * 2 - 1;

        int startX = centerBlockIndex.getX() - i1;
        int startY = centerBlockIndex.getY() - i1;

        for (int x = 0; x < i2; x++) {
            for (int y = 0; y < i2; y++) {
                blocks.add(new Block(startX + x, startY + y));
            }
        }
        return blocks;
    }

    /**
     * 添加节点，只能添加跟节点
     */
    public void add(Node node) {
        if (node.getParent() != null) {
            throw new RuntimeException("不能将子节点放置到场景中");
        }

        Block blockIndex = getBlockIndex(node.getPositionX(), node.getPositionY());

        nodes.computeIfAbsent(blockIndex, x -> new HashSet<>()).add(node);

        node.setBse(activityBlockSizeExtend);
        node.setPue(positionUpdateExtend);

        addExtendEvent(node);

        if (node.getActivityBlockSize() > 0) {
            activityBlockMap.put(node, getNodeActivityBlocks(node, node.getActivityBlockSize()));
            updateActivityBlockSize();
        }

        if (node instanceof OrthographicCameraNode) {
            orthographicCameraNodes.add(((OrthographicCameraNode) node));
        }

        node.onScene(this);
        for (Node node1 : node) {
            node1.onScene(this);
        }
    }

    public void remove(Node node) {
        Set<Node> nodes2 = nodes.get(getBlockIndex(node.getPositionX(), node.getPositionY()));
        if (nodes2 == null) {
            return;
        }

        node.setPue(null);
        node.setBse(null);
        removeExtendEvent(node);

        nodes2.remove(node);

        if (node instanceof OrthographicCameraNode) {
            orthographicCameraNodes.remove(node);
        }

        node.extScene(Scene.this);
        for (Node node2 : node) {
            node2.extScene(Scene.this);
        }
    }

    public void addExtendEvent(Node node) {
        node.setCue(childUpdateExtend);

        for (Node child : node) {
            child.setCue(childUpdateExtend);
        }
    }

    public void removeExtendEvent(Node node) {
        node.setCue(null);

        for (Node child : node) {
            child.setCue(null);
        }
    }

    private Block getBlockIndex(float x, float y) {
        return new Block(getBlockIndex(x), getBlockIndex(y));
    }

    private int getBlockIndex(float n) {
        return (int) Math.floor(n / blockSize);
    }

    /**
     * 逻辑处理
     *
     * @param delta Time in seconds since the last frame.
     */
    public void logic(float delta) {
        for (Block block : activeBlocks) {
            Set<Node> nodes1 = nodes.get(block);
            if (nodes1 == null || nodes1.isEmpty()) {
                continue;
            }

            for (Node node : nodes1) {
                actNodeLogic(node, delta, true);
            }
        }

        for (OrthographicCameraNode orthographicCameraNode : orthographicCameraNodes) {
            actNodeLogic(orthographicCameraNode, delta, false);
        }

        // 更新移动过的组件的区块位置
        blocksUpdate();
    }

    private void actNodeLogic(Node node, float delta, boolean skipCam) {
        if (node instanceof OrthographicCameraNode && skipCam) {
            return;
        }

        // 处理每个节点的逻辑
        node.logic(delta);
        // 让节点更新显示组件的坐标
        node.updateViewPosition(0, 0);

        for (Node c : node) {
            actNodeLogic(c, delta, skipCam);
        }
    }

    /**
     * 更新移动过的组件的区块位置
     */
    private void blocksUpdate() {
        for (MoveNodeCache moveNode : renderCache.moveNodes) {
            Block oldIndex = getBlockIndex(moveNode.oldX, moveNode.oldY);
            Block newIndex = getBlockIndex(moveNode.newX, moveNode.newY);
            if (!oldIndex.equals(newIndex)) {
                Set<Node> nodes1 = nodes.get(oldIndex);
                if (nodes1 != null) {
                    nodes1.remove(moveNode.node);
                }
                nodes.computeIfAbsent(newIndex, k -> new HashSet<>()).add(moveNode.node);

                if (activityBlockMap.get(moveNode.node) != null) {
                    activityBlockMap.put(moveNode.node, getNodeActivityBlocks(moveNode.node, moveNode.node.getActivityBlockSize()));
                    updateActivityBlockSize();
                }
            }
        }

        renderCache.moveNodes.clear();
    }

    public void render() {
        renderCache.nodes.clear();

        OrthographicCameraNode camera = null;

        for (OrthographicCameraNode orthographicCameraNode : orthographicCameraNodes) {
            camera = orthographicCameraNode;
            Rectangle viewport = orthographicCameraNode.getWorldRectangle();

            int x = getBlockIndex(viewport.getX());
            int y = getBlockIndex(viewport.getY());

            int blockXNumber = (int) Math.ceil(viewport.getWidth() / blockSize) + 1;
            int blockYNumber = (int) Math.ceil(viewport.getHeight() / blockSize) + 1;

            findAndAdd(blockXNumber, blockYNumber, x, y, renderCache.nodes);
        }

        if (camera != null) {
            camera.beginDraw();
            spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        }

        spriteBatch.begin();
        renderCache.nodes.stream().sorted(Comparator.comparingInt(Node::getZIndex)).forEach(node -> node.draw(spriteBatch));
        spriteBatch.end();
    }

    private void findAndAdd(int blockXNumber, int blockYNumber, int x, int y, List<Node> nodeSet) {
        for (int i = 0; i < blockXNumber; i++) {
            for (int j = 0; j < blockYNumber; j++) {
                renderCache.cacheBlock = new Block(x + i, y + j);
                if (activeBlocks.contains(renderCache.cacheBlock)) {
                    Set<Node> nodes1 = nodes.get(renderCache.cacheBlock);
                    if (nodes1 != null) {
                        for (Node node : nodes1) {
                            if (node.isVisible()) {
                                nodeSet.add(node);
                            }
                        }
                    }
                }
            }
        }
    }

    public void resize(int width, int height) {

    }

}
