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
    private TransformInput transformInput;
    /**
     * 移动目标Transform组件
     */
    private Node operate;

    public TransformMove() {
    }

    public TransformMove(TransformInput transformInput) {
        this(transformInput, null);
    }

    public TransformMove(TransformInput transformInput, Node operate) {
        this.transformInput = transformInput;
        this.operate = operate;
    }

    public TransformInput getTransformInput() {
        return transformInput;
    }

    public void setTransformInput(TransformInput transformInput) {
        this.transformInput = transformInput;
    }

    public Node getOperate() {
        return operate;
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

        if (speed <= 0) {
            return true;
        }

        Node node;
        if (this.operate == null) {
            node = getParent();
        } else {
            node = this.operate;
        }

        if (node == null) {
            return true;
        }

        if (transformInput == null) {
            return true;
        }

        Vector2 input = transformInput.getVector2();

        // 获取到当前速度(每秒移动速度)
        float speed = Math.min(input.len(), 1) * this.speed;

        // 移动距离
        float dis = delta * speed;

        node.translate(input.x * dis, input.y * dis);

        return true;
    }

}
