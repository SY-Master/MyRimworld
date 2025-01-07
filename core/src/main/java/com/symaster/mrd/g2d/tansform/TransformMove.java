package com.symaster.mrd.g2d.tansform;

import com.badlogic.gdx.math.Vector2;
import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @version 2023/11/16
 */
public class TransformMove extends Node {
    /**
     * 设定每秒移动距离
     */
    private float speed = 0f;
    /**
     * 输入向量
     */
    private final Vector2 input;
    /**
     * 移动目标Transform组件
     */
    private final Node operate;

    public TransformMove(Vector2 input) {
        this(input, null);
    }

    public TransformMove(Vector2 input, Node operate) {
        this.input = input;
        this.operate = operate;
    }

    public Node getOperate() {
        return operate;
    }

    public Vector2 getInput() {
        return input;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean logic(float delta) {
        super.logic(delta);

        Node node;
        if (this.operate == null) {
            node = getParent();
        } else {
            node = this.operate;
        }

        if (input == null || node == null) {
            return true;
        }

        // 获取到当前速度(每秒移动速度)
        float speed = Math.min(input.len(), 1) * this.speed;

        // 移动距离
        float dis = delta * speed;

        node.translate(input.x * dis, input.y * dis);

        return true;
    }

}
