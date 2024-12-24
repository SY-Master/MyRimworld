package com.symaster.mrd.g2d;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private final Map<Node, Block> activityBlock;

    private final ActivityBlockSizeExtend activityBlockSizeExtend;

    private final PositionUpdateExtend positionUpdateExtend;

    public Scene() {
        this(100f);
    }

    public Scene(float blockSize) {
        this.blockSize = blockSize;
        nodes = new HashMap<>();
        this.activityBlock = new HashMap<>();
        this.activityBlockSizeExtend = getActivityBlockSizeExtend();
        this.positionUpdateExtend = getPositionUpdateExtend();
    }

    private PositionUpdateExtend getPositionUpdateExtend() {
        return (node, oldX, oldY, newX, newY) -> {
            Block oldIndex = getBlockIndex(oldX, oldY);
            Block newIndex = getBlockIndex(newX, newY);
            if (!oldIndex.equals(newIndex)) {
                Set<Node> nodes1 = nodes.get(oldIndex);
                if (nodes1 != null) {
                    nodes1.remove(node);
                }
                nodes.computeIfAbsent(newIndex, k -> new HashSet<>()).add(node);
            }
        };
    }

    private ActivityBlockSizeExtend getActivityBlockSizeExtend() {
        return (node, oldSize, newSize) -> {
            if (newSize > 0) {
                // 添加
                Block block = activityBlock.get(node);
                if (block == null) {
                    activityBlock.put(node, getBlockIndex(node.getPositionX(), node.getPositionY()));
                }
            } else if (oldSize > 0) {
                // 移除
                activityBlock.remove(node);
            }
        };
    }

    public void add(Node node) {
        Block blockIndex = getBlockIndex(node.getPositionX(), node.getPositionY());

        nodes.computeIfAbsent(blockIndex, x -> new HashSet<>()).add(node);

        node.setBse(activityBlockSizeExtend);

        node.setPue(positionUpdateExtend);

        if (node.getActivityBlockSize() > 0) {
            activityBlock.put(node, blockIndex);
        }
    }

    public void remove(Node node) {
        Set<Node> nodes2 = nodes.get(getBlockIndex(node.getPositionX(), node.getPositionY()));
        if (nodes2 == null) {
            return;
        }

        node.setPue(null);
        node.setBse(null);

        nodes2.remove(node);
    }

    private Block getBlockIndex(float x, float y) {
        return new Block((int) Math.floor(x / blockSize), (int) Math.floor(y / blockSize));
    }

    /**
     * 逻辑处理
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {
        // for (Node node : this) {
        //     node.update();
        // }
    }

    public void render() {
    }

    private void drawCamera(CameraNode cameraNode) {
        SpriteBatch spriteBatch = cameraNode.getSpriteBatch();

        spriteBatch.setProjectionMatrix(cameraNode.getCamera().combined);
        spriteBatch.begin();


        // for (Node n2 : this) {
        //     if (n2 instanceof SpriteNode) {
        //         SpriteNode spriteNode = (SpriteNode) n2;
        //         Sprite sprite = spriteNode.getSprite();
        //
        //
        //     }
        // }
    }

    public void resize(int width, int height) {

    }

}
