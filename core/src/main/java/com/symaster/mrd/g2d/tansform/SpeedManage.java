package com.symaster.mrd.g2d.tansform;

import com.symaster.mrd.g2d.Node;

/**
 * @author yinmiao
 * @since 2025/3/24
 */
public class SpeedManage extends Node {

    private float speed;
    private TransformMove transformMove;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public TransformMove getTransformMove() {
        return transformMove;
    }

    public void setTransformMove(TransformMove transformMove) {
        this.transformMove = transformMove;
    }

    /**
     * 计算实际速度
     */
    @Override
    public boolean logic(float delta) {
        boolean logic = super.logic(delta);

        // 获取

        //


        return logic;
    }

}
