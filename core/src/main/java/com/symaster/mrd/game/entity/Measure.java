package com.symaster.mrd.game.entity;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class Measure {

    private float value;
    private float max;

    public Measure() {
    }

    public Measure(float value, float max) {
        this.value = value;
        this.max = max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
