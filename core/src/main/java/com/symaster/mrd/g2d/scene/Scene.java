package com.symaster.mrd.g2d.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.symaster.mrd.SystemConfig;
import com.symaster.mrd.api.ActivityBlockSizeExtend;
import com.symaster.mrd.api.ChildUpdateExtend;
import com.symaster.mrd.api.PositionUpdateExtend;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.OrthographicCameraNode;
import com.symaster.mrd.g2d.scene.impl.ActivityBlockSizeExtendImpl;
import com.symaster.mrd.g2d.scene.impl.BlockMapGenerate;
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
public class Scene implements Serializable, Disposable {
    private static final long serialVersionUID = 1L;

    /**
     * 区块大小
     */
    private final float blockSize;
    /**
     * 分区块Node表
     */
    private final Map<Block, Set<Node>> nodes;
    // /**
    //  * 分区块地图表
    //  */
    // private final Map<Block, Set<Node>> mapNodes;
    /**
     * 激活区块的节点
     */
    private final Map<Node, Set<Block>> activityBlockMap;
    /**
     * 激活的区块
     */
    private final Set<Block> activeBlocks;
    /**
     * 相机
     */
    private final Set<OrthographicCameraNode> orthographicCameraNodes;
    /**
     * 世界地图生成种子
     */
    private final String mapSeed;
    /**
     * 区块生成器
     */
    private final BlockMapGenerate blockMapGenerate;

    private final ActivityBlockSizeExtend activityBlockSizeExtend;
    private final PositionUpdateExtend positionUpdateExtend;
    private final ChildUpdateExtend childUpdateExtend;
    private final SpriteBatch spriteBatch;
    private final Cache renderCache;
    private InputFactory inputFactory;

    public Scene(AssetManager assetManager) {
        this(assetManager, UUID.randomUUID().toString(), UnitUtil.ofM(SystemConfig.BLOCK_SIZE), null); // 默认区块边长10米
    }

    public Scene(AssetManager assetManager, String mapSeed) {
        this(assetManager, mapSeed, UnitUtil.ofM(SystemConfig.BLOCK_SIZE), null); // 默认区块边长10米
    }

    public Scene(AssetManager assetManager, String mapSeed, List<Block> initBlocks) {
        this(assetManager, mapSeed, UnitUtil.ofM(SystemConfig.BLOCK_SIZE), initBlocks); // 默认区块边长10米
    }

    /**
     * 构建场景
     *
     * @param assetManager 资源管理器
     * @param mapSeed      种子
     * @param blockSize    区块大小
     * @param initBlocks   构建场景时初始化区块
     */
    public Scene(AssetManager assetManager, String mapSeed, float blockSize, List<Block> initBlocks) {
        this.blockSize = blockSize;
        this.nodes = new HashMap<>();
        this.activityBlockMap = new HashMap<>();
        this.activeBlocks = new HashSet<>();
        this.orthographicCameraNodes = new HashSet<>();
        this.renderCache = new Cache();
        this.mapSeed = mapSeed;
        this.spriteBatch = new SpriteBatch();
        this.activityBlockSizeExtend = new ActivityBlockSizeExtendImpl(this);
        this.positionUpdateExtend = new PositionUpdateExtendImpl(this);
        this.childUpdateExtend = new ChildUpdateExtendImpl(this);
        this.blockMapGenerate = new BlockMapGenerate(this, assetManager);

        // 同步初始化区块
        if (initBlocks != null) {
            initBlocks(initBlocks);
        }
    }

    /**
     * 同步初始化区块
     *
     * @param initBlocks 指定区块
     */
    private void initBlocks(List<Block> initBlocks) {
        for (Block initBlock : initBlocks) {
            Set<Node> generate = this.blockMapGenerate.getBlockMapGenerateProcessor().generate(this, initBlock);
            nodes.computeIfAbsent(initBlock, k -> new HashSet<>()).addAll(generate);
        }
    }

    public String getMapSeed() {
        return mapSeed;
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
        // 添加新的地图
        addToMap();

        // 调用所有激活区块内组件的逻辑方法
        actBlockNodes(delta);

        // 独立处理摄像头的逻辑方法
        actCamera(delta);

        // 更新移动过的组件的区块位置
        blocksUpdate();

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

    private void actCamera(float delta) {
        for (OrthographicCameraNode orthographicCameraNode : orthographicCameraNodes) {
            actNodeLogic(orthographicCameraNode, delta, false);
        }
    }

    private void actBlockNodes(float delta) {
        for (Block block : activeBlocks) {
            Set<Node> nodes1 = nodes.get(block);
            if (nodes1 == null || nodes1.isEmpty()) {
                continue;
            }

            for (Node node : nodes1) {
                actNodeLogic(node, delta, true);
            }
        }
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

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        blockMapGenerate.dispose();
        spriteBatch.dispose();
    }
}
