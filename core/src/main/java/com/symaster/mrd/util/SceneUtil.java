package com.symaster.mrd.util;

import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.Database;
import com.symaster.mrd.game.entity.SelectData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yinmiao
 * @since 2024/12/26
 */
public class SceneUtil {

    public static Node getTopParent(Node child) {
        if (child.getParent() == null) {
            return child;
        }

        return getTopParent(child.getParent());
    }

    public static SelectData getSelectData(Scene scene) {
        if (scene == null) {
            return null;
        }
        Set<Node> byGroup = scene.getByGroup(Groups.DATABASE);
        if (byGroup == null || byGroup.isEmpty()) {
            return null;
        }

        Database database = (Database) byGroup.iterator().next();

        return database.getSelectData();
    }

    /**
     * 通过圆查询节点
     *
     * @param centerX   圆中心点
     * @param centerY   圆中心点
     * @param radius    圆半径
     * @param inactBlks 是否包括未激活区块
     */
    public static List<Node> findNodesByCircle(Scene scene, float centerX, float centerY, float radius, boolean inactBlks) {
        int blockSize = (int) Math.ceil(radius / scene.getBlockSize()) + 2;

        int blockIndexX = scene.getBlockIndex(centerX);
        int blockIndexY = scene.getBlockIndex(centerY);

        List<Node> rtn = new ArrayList<>();

        for (int x = blockIndexX - blockSize; x < blockIndexX + blockSize; x++) {
            for (int y = blockIndexY - blockSize; y < blockIndexY + blockSize; y++) {

                Block block = new Block(x, y);

                boolean search;
                if (inactBlks) {
                    search = scene.getNodes().containsKey(block);
                } else {
                    search = scene.getActiveBlocks().contains(block);
                }

                if (search) {
                    findNodesByCircle(scene.getNodes().get(block), centerX, centerY, radius, rtn);
                }
            }
        }

        return rtn;
    }

    public static void findNodesByCircle(Set<Node> nodes, float centerX, float centerY, float radius, List<Node> rtn) {
        for (Node node : nodes) {
            float x1 = node.getPositionX();
            float y1 = node.getPositionY();
            float x2 = node.getPositionX() + node.getWidth();
            float y2 = node.getPositionY() + node.getHeight();

            if (GeomUtil.distance(centerX, centerY, x1, y1) <= radius) {
                rtn.add(node);
            } else if (GeomUtil.distance(centerX, centerY, x2, y2) <= radius) {
                rtn.add(node);
            } else if (GeomUtil.distance(centerX, centerY, x1, y2) < radius) {
                rtn.add(node);
            } else if (GeomUtil.distance(centerX, centerY, x2, y1) < radius) {
                rtn.add(node);
            }
        }
    }
}
