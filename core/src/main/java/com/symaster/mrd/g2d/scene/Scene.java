package com.symaster.mrd.g2d.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.NodePropertiesChangeExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.api.ProgressProcessor;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.BlockArrayList;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerate;
import com.symaster.mrd.g2d.scene.impl.ChildUpdateExtendImpl;
import com.symaster.mrd.g2d.scene.impl.NodePropertiesChangeExtendImpl;
import com.symaster.mrd.g2d.scene.impl.PositionUpdateExtendImpl;
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
public class Scene implements Serializable, Disposable {

    private static final long serialVersionUID = 1L;

    /**
     * 区块大小
     */
    private final float blockSize;
    /**
     * 分区块Node表
     */
    private final BlockArrayList<Set<Node>> nodes;
    /**
     * 激活区块的节点
     */
    private final Map<Node, Set<Block>> activityBlockMap;

    private final Map<String, Set<Node>> nodeGroups;

    private final Set<Node> forceLogicNodes;

    /**
     * 激活的区块
     */
    private final Set<Block> activeBlocks;
    /**
     * 世界地图生成种子
     */
    private final String mapSeed;
    /**
     * 区块生成器
     */
    private final BlockMapGenerate blockMapGenerate;

    private int logicId = 0;

    private final NodePropertiesChangeExtend nodePropertiesChangeExtend;
    private final PositionUpdateExtend positionUpdateExtend;
    private final ChildUpdateExtend childUpdateExtend;
    private final Cache renderCache;

    public Scene(AssetManager assetManager) {
        this(assetManager, null, UnitUtil.ofM(SystemConfig.BLOCK_SIZE)); // 默认区块边长10米
    }

    public Scene(AssetManager assetManager, String mapSeed) {
        this(assetManager, mapSeed, UnitUtil.ofM(SystemConfig.BLOCK_SIZE)); // 默认区块边长10米
    }

    /**
     * 构建场景
     *
     * @param assetManager 资源管理器
     * @param mapSeed      种子
     * @param blockSize    区块大小
     */
    public Scene(AssetManager assetManager, String mapSeed, float blockSize) {
        this.blockSize = blockSize;
        this.nodes = new BlockArrayList<>();
        this.activityBlockMap = new HashMap<>();
        this.activeBlocks = new HashSet<>();
        this.renderCache = new Cache();
        this.nodeGroups = new HashMap<>();
        this.forceLogicNodes = new HashSet<>();
        if (mapSeed == null) {
            this.mapSeed = UUID.randomUUID().toString();
        } else {
            this.mapSeed = mapSeed;
        }
        this.nodePropertiesChangeExtend = new NodePropertiesChangeExtendImpl(this);
        this.positionUpdateExtend = new PositionUpdateExtendImpl(this);
        this.childUpdateExtend = new ChildUpdateExtendImpl(this);
        this.blockMapGenerate = new BlockMapGenerate(this, assetManager);
    }

    /**
     * 同步初始化区块
     *
     * @param initBlocks 指定区块
     */
    public void initBlocks(List<Block> initBlocks) {
        initBlocks(initBlocks, null);
    }

    /**
     * 同步初始化区块
     *
     * @param initBlocks 指定区块
     */
    public void initBlocks(List<Block> initBlocks, ProgressProcessor progressProcessor) {

        for (int i = 0; i < initBlocks.size(); i++) {
            Block initBlock = initBlocks.get(i);

            Set<Node> generate = this.blockMapGenerate.getBlockMapGenerateProcessor().generate(this, initBlock);
            nodes.computeIfAbsent(initBlock, k -> new HashSet<>()).addAll(generate);

            for (Node node : generate) {
                onScene(node);
            }

            if (progressProcessor != null) {
                progressProcessor.update(i + 1f / initBlocks.size());
            }
        }
    }

    public String getMapSeed() {
        return mapSeed;
    }

    public float getBlockSize() {
        return blockSize;
    }

    public BlockArrayList<Set<Node>> getNodes() {
        return nodes;
    }

    public Set<Node> getByGroup(String groupName) {
        return nodeGroups.get(groupName);
    }

    public Map<Node, Set<Block>> getActivityBlockMap() {
        return activityBlockMap;
    }

    public Set<Block> getActiveBlocks() {
        return activeBlocks;
    }

    public Cache getRenderCache() {
        return renderCache;
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
        add(node, null);
    }

    /**
     * 添加节点，只能添加跟节点
     */
    public void add(Node node, String group) {
        if (node.getParent() != null) {
            throw new RuntimeException("不能将子节点放置到场景中");
        }

        if (group != null && !group.isEmpty()) {
            nodeGroups.computeIfAbsent(group, k -> new HashSet<>()).add(node);
        }

        Block blockIndex = getBlockIndex(node.getPositionX(), node.getPositionY());

        if (!nodes.containsKey(blockIndex)) {
            blockMapGenerate.addGenerateQueue(blockIndex);
            nodes.put(blockIndex, new HashSet<>());
        }

        nodes.computeIfAbsent(blockIndex, x -> new HashSet<>()).add(node);

        node.setChangeExtend(nodePropertiesChangeExtend);
        node.setPue(positionUpdateExtend);

        addExtendEvent(node);

        if (node.getActivityBlockSize() > 0) {
            activityBlockMap.put(node, getNodeActivityBlocks(node, node.getActivityBlockSize()));
            updateActivityBlockSize();
        }

        // if (node instanceof OrthographicCameraNode) {
        //     orthographicCameraNodes.add(((OrthographicCameraNode) node));
        // }

        onScene(node);
    }

    public void addForcedLogic(Node node) {
        forceLogicNodes.add(node);
    }

    public void onScene(Node node) {
        node.onScene(this);

        for (Node node1 : node) {
            onScene(node1);
        }
    }

    public void extScene(Node node) {
        node.extScene(this);

        for (Node node1 : node) {
            extScene(node1);
        }
    }

    public void removeForcedLogic(Node node) {
        forceLogicNodes.remove(node);
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

    public Block getBlockIndex(float x, float y) {
        return new Block(getBlockIndex(x), getBlockIndex(y));
    }

    public int getBlockIndex(float n) {
        return (int) Math.floor(n / blockSize);
    }

    /**
     * 逻辑处理
     *
     * @param delta Time in seconds since the last frame.
     */
    public void logic(float delta) {
        // 添加新的地图
        addToMap();

        // 调用所有激活区块内组件的逻辑方法
        nodesLogic(delta);

        // 更新移动过的组件的区块位置
        nodesUpdate();

        // 新区块申请创建，逻辑：地图表内不存在的激活区块
        addToGenerateQueue();
    }

    /**
     * 将生成完成的地图加载进场景
     */
    private void addToMap() {
        if (blockMapGenerate != null) {
            BlockMapGenerate.Result result = blockMapGenerate.getResult();
            if (result != null) {
                Set<Node> nodes2 = nodes.computeIfAbsent(result.block, k -> new HashSet<>());
                nodes2.addAll(result.nodes);
                for (Node node : nodes2) {
                    node.onScene(this);
                }
            }
        }
    }

    /**
     * 新区块申请创建
     */
    private void addToGenerateQueue() {
        if (blockMapGenerate != null) {
            for (Block activeBlock : activeBlocks) {
                if (!nodes.containsKey(activeBlock)) {
                    blockMapGenerate.addGenerateQueue(activeBlock);
                    nodes.put(activeBlock, new HashSet<>());
                }
            }
        }
    }

    public void remove(Node node) {
        Set<Node> nodes2 = nodes.get(getBlockIndex(node.getPositionX(), node.getPositionY()));
        if (nodes2 == null) {
            return;
        }

        node.setPue(null);
        node.setChangeExtend(null);
        removeExtendEvent(node);

        nodes2.remove(node);

        // if (node instanceof OrthographicCameraNode) {
        //     orthographicCameraNodes.remove(node);
        // }

        extScene(node);
    }

    private void nodesLogic(float delta) {
        logicId++;
        logicId = logicId % 100;

        for (Block block : activeBlocks) {
            Set<Node> nodes1 = nodes.get(block);
            if (nodes1 == null || nodes1.isEmpty()) {
                continue;
            }

            for (Node node : nodes1) {
                nodeLogic(node, delta, 0f, 0f, logicId);
            }
        }

        for (Node forceLogicNode : forceLogicNodes) {
            if (forceLogicNode.getLogicId() == logicId) {
                continue;
            }

            nodeLogic(forceLogicNode, delta, 0f, 0f, logicId);
        }
    }

    private void nodeLogic(Node node, float delta, float parentX, float parentY, int logicId) {

        boolean childLogic;

        // 处理每个节点的逻辑
        if (node.isIgnoreTimeScale()) {
            childLogic = node.logic(delta);
        } else {
            childLogic = node.logic(delta * SystemConfig.TIME_SCALE);
        }

        node.setLogicId(logicId);

        // 显示组件的世界坐标
        float worldX = node.getPositionX() + parentX;
        float worldY = node.getPositionY() + parentY;

        // 让节点更新显示组件的坐标
        node.setGdxNodePosition(worldX, worldY);

        // 所有子节点
        if (childLogic) {
            for (Node c : node) {
                nodeLogic(c, delta, worldX, worldY, logicId);
            }
        }
    }

    /**
     * 更新移动过的组件的区块位置
     */
    private void nodesUpdate() {
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
                    activityBlockMap.put(moveNode.node,
                                         getNodeActivityBlocks(moveNode.node, moveNode.node.getActivityBlockSize()));
                    updateActivityBlockSize();
                }
            }
        }

        renderCache.moveNodes.clear();
    }

    public void findNodes(Rectangle worldRect, List<Node> result, boolean limitVisible, int blockExtend) {
        findNodes(worldRect.x, worldRect.y, worldRect.width, worldRect.height, result, limitVisible, blockExtend);
    }

    public void findNodes(float worldX,
                          float worldY,
                          float worldW,
                          float worldH,
                          List<Node> result,
                          boolean limitVisible,
                          int blockExtend) {
        int xIndex = getBlockIndex(worldX);
        int yIndex = getBlockIndex(worldY);

        int blockXNumber = (int) Math.ceil(worldW / blockSize) + 1;
        int blockYNumber = (int) Math.ceil(worldH / blockSize) + 1;

        int i = blockExtend * 2;

        findNodesByBlock(blockXNumber + i, blockYNumber + i, xIndex - blockExtend, yIndex - blockExtend, result,
                         limitVisible);
    }

    public void findNodesByBlock(int blockXNumber,
                                 int blockYNumber,
                                 int x,
                                 int y,
                                 List<Node> result,
                                 boolean limitVisible) {
        for (int i = 0; i < blockXNumber; i++) {
            for (int j = 0; j < blockYNumber; j++) {
                renderCache.cacheBlock = new Block(x + i, y + j);
                findNodesByBlock(renderCache.cacheBlock, result, limitVisible);
            }
        }
    }

    public void findNodesByBlock(Block cacheBlock, List<Node> result, boolean limitVisible) {
        if (activeBlocks.contains(cacheBlock)) {
            Set<Node> nodes1 = nodes.get(cacheBlock);
            if (nodes1 != null) {
                if (limitVisible) {
                    for (Node node : nodes1) {
                        if (node.isVisible()) {
                            result.add(node);
                        }
                    }
                } else {
                    result.addAll(nodes1);
                }
            }
        }
    }

    public void resize(int width, int height) {

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        blockMapGenerate.dispose();
    }

}
