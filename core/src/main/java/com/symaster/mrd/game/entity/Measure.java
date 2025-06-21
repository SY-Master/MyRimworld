package com.symaster.mrd.game.entity;

/**
 * @author yinmiao
 * @since 2025/1/1
 */
public class Measure {

    private float value;
    private float max;

    public Measure() {
        this.value = 1f;
        this.max = 100f;
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

    public String desc() {
        return String.format("%.2f/%.2f", value * max, max);
    }

}
