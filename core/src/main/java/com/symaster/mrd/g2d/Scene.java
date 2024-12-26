package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.api.ActivityBlockSizeExtend;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.util.UnitUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 场景
 *
 * @author yinmiao
 * @since 2024/12/22
 */
public class Scene {

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

    private final Set<CameraNode> cameraNodes;

    /// 节点扩展操作
    private final ActivityBlockSizeExtend activityBlockSizeExtend;
    private final PositionUpdateExtend positionUpdateExtend;
    private final ChildUpdateExtend childUpdateExtend;

    private final Cache renderCache;
    private final SpriteBatch spriteBatch;

    public Scene() {
        this(UnitUtil.ofM(SystemConfig.BLOCK_SIZE)); // 默认区块边长10米
    }

    public Scene(float blockSize) {
        this.blockSize = blockSize;
        nodes = new HashMap<>();
        this.activityBlockMap = new HashMap<>();
        this.activeBlocks = new HashSet<>();
        this.cameraNodes = new HashSet<>();
        this.renderCache = new Cache();
        this.spriteBatch = new SpriteBatch();
        this.activityBlockSizeExtend = getActivityBlockSizeExtend();
        this.positionUpdateExtend = getPositionUpdateExtend();
        this.childUpdateExtend = getChildUpdateExtend();
    }

    public ChildUpdateExtend getChildUpdateExtend() {
        return new ChildUpdateExtend() {

            @Override
            public void afterAdd(Node parent, Node child) {
                addExtend(child);

                if (child instanceof CameraNode) {
                    cameraNodes.add((CameraNode) child);
                }

            }

            @Override
            public void afterRemove(Node parent, Node child) {
                removeExtend(child);

                if (child instanceof CameraNode) {
                    cameraNodes.remove(child);
                }
            }
        };
    }

    public PositionUpdateExtend getPositionUpdateExtend() {
        // 当场景中的节点位置改变后会触发这段代码
        return (node, oldX, oldY, newX, newY) -> {
            if (node.getParent() != null) {
                return;
            }

            Block oldIndex = getBlockIndex(oldX, oldY);
            Block newIndex = getBlockIndex(newX, newY);
            if (!oldIndex.equals(newIndex)) {
                Set<Node> nodes1 = nodes.get(oldIndex);
                if (nodes1 != null) {
                    nodes1.remove(node);
                }
                nodes.computeIfAbsent(newIndex, k -> new HashSet<>()).add(node);

                if (activityBlockMap.get(node) != null) {
                    activityBlockMap.put(node, getNodeActivityBlocks(node, node.getActivityBlockSize()));
                    updateActivityBlockSize();
                }
            }
        };
    }

    public ActivityBlockSizeExtend getActivityBlockSizeExtend() {
        // 当场景中的节点设置了新的区块激活数量时会触发这段代码
        return (node, oldSize, newSize) -> {
            if (node.getParent() != null) {
                return;
            }
            blockUpdate(node, newSize);
        };
    }

    private void blockUpdate(Node node, int newSize) {
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

        addExtend(node);

        if (node.getActivityBlockSize() > 0) {
            activityBlockMap.put(node, getNodeActivityBlocks(node, node.getActivityBlockSize()));
            updateActivityBlockSize();
        }

        if (node instanceof CameraNode) {
            CameraNode cameraNode = (CameraNode) node;
            cameraNodes.add(cameraNode);
        }
    }

    public void remove(Node node) {
        Set<Node> nodes2 = nodes.get(getBlockIndex(node.getPositionX(), node.getPositionY()));
        if (nodes2 == null) {
            return;
        }

        node.setPue(null);
        node.setBse(null);
        removeExtend(node);

        nodes2.remove(node);

        if (node instanceof CameraNode) {
            cameraNodes.remove(node);
        }
    }

    private void addExtend(Node node) {
        node.setCue(childUpdateExtend);

        for (Node child : node) {
            child.setCue(childUpdateExtend);
        }
    }

    private void removeExtend(Node node) {
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
    public void act(float delta) {
        activeBlocks.stream()
                .flatMap(e -> {
                    Set<Node> nodes1 = nodes.get(e);
                    if (nodes1 == null) {
                        return Stream.empty();
                    } else {
                        return nodes1.stream();
                    }
                })
                .forEach(e -> {
                    e.act(delta);
                    e.updateViewPosition(0, 0);
                });
    }

    public void render() {
        Set<Node> nodeSet = renderCache.nodes;
        nodeSet.clear();

        CameraNode camera = null;

        for (CameraNode cameraNode : cameraNodes) {
            camera = cameraNode;
            Rectangle viewport = cameraNode.getWorldRectangle();

            int x = getBlockIndex(viewport.getX());
            int y = getBlockIndex(viewport.getY());

            int blockXNumber = (int) Math.ceil(viewport.getWidth() / blockSize);
            int blockYNumber = (int) Math.ceil(viewport.getHeight() / blockSize);

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

        if (nodeSet.isEmpty()) {
            return;
        }

        camera.beginDraw();
        spriteBatch.setProjectionMatrix(camera.getCamera().combined);
        spriteBatch.begin();
        nodeSet.stream().sorted(Comparator.comparingInt(Node::getZIndex)).forEach(node -> node.draw(spriteBatch));
        spriteBatch.end();
    }

    public void resize(int width, int height) {

    }

    private static final class Cache {
        public final Set<Node> nodes = new HashSet<>();
        public Block cacheBlock;
    }


}
