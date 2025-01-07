package com.symaster.mrd.g2d;

import java.util.function.Function;

/**
 * @author yinmiao
 * @since 2025/1/7
 */
public class BlockArrayList<T> {

    private final int initSize;
    private final float expansionThreshold;

    private MyItem[] positive;
    private MyItem[] negative;

    public BlockArrayList() {
        this(100);
    }

    public BlockArrayList(int initSize) {
        this.initSize = initSize;
        this.positive = new MyItem[initSize];
        this.negative = new MyItem[initSize];
        this.expansionThreshold = 0.75f;
    }

    private static boolean isExpansion(int targetIndex, int length, float expansionThreshold) {
        return targetIndex >= length * expansionThreshold;
    }

    private static MyItem[] expansionCopy(int newCapacity, MyItem[] src) {
        MyItem[] positive = new MyItem[newCapacity];

        System.arraycopy(src, 0, positive, 0, src.length);

        return positive;
    }

    private static Object[] expansionCopy(int newCapacity, Object[] src) {
        Object[] positive = new Object[newCapacity];

        System.arraycopy(src, 0, positive, 0, src.length);

        return positive;
    }

    private static int getNewCapacity(int target, int length) {
        int newCapacity = newCapacity(length);

        while (newCapacity <= target) {
            newCapacity = newCapacity(newCapacity);
        }
        return newCapacity;
    }

    private static int newCapacity(int l) {
        return l + (l >> 1);
    }

    public void put(Block block, T value) {
        int x = block.getX();
        if (x >= 0) {
            positivePut(block, value);
        } else {
            negativePut(block, value);
        }
    }

    private void negativePut(Block block, T value) {
        int index = Math.abs(block.getX()) - 1;

        if (isExpansion(index, negative.length, expansionThreshold)) {
            this.negative = expansionCopy(getNewCapacity(index, negative.length), negative);
        }

        MyItem myItem = negative[index];
        if (myItem == null) {
            myItem = new MyItem(expansionThreshold, initSize);
            negative[index] = myItem;
        }

        myItem.put(block.getY(), value);
    }

    private void positivePut(Block block, T value) {
        int index = block.getX();

        if (isExpansion(index, positive.length, expansionThreshold)) {
            this.positive = expansionCopy(getNewCapacity(index, positive.length), positive);
        }

        MyItem myItem = positive[index];
        if (myItem == null) {
            myItem = new MyItem(expansionThreshold, initSize);
            positive[index] = myItem;
        }

        myItem.put(block.getY(), value);
    }

    public T get(Block block) {
        int x = block.getX();
        int y = block.getY();

        if (x >= 0) {
            return get(x, y, positive);
        } else {

            int index = Math.abs(x) - 1;

            return get(index, y, negative);
        }
    }

    private T get(int x, int y, MyItem[] positive) {
        if (positive.length <= x) {
            return null;
        }
        MyItem myItem = positive[x];
        if (myItem == null) {
            return null;
        }
        Object o = myItem.get(y);
        if (o == null) {
            return null;
        }
        return (T) o;
    }

    public T computeIfAbsent(Block key, Function<? super Block, ? extends T> mappingFunction) {
        T t = get(key);
        if (t == null) {
            t = mappingFunction.apply(key);
            put(key, t);
        }
        return t;
    }

    public boolean containsKey(Block blockIndex) {
        return get(blockIndex) != null;
    }

    private static class MyItem {

        private final float expansionThreshold;
        public Object[] positive;
        public Object[] negative;

        public MyItem(float expansionThreshold, int initSize) {
            this.expansionThreshold = expansionThreshold;
            positive = new Object[initSize];
            negative = new Object[initSize];
        }

        public void put(int index, Object value) {
            if (index >= 0) {
                positivePut(index, value);
            } else {
                negativePut(index, value);
            }
        }

        public Object get(int index) {

            if (index >= 0) {
                if (positive.length <= index) {
                    return null;
                }

                return positive[index];
            } else {
                int i = Math.abs(index) - 1;
                if (negative.length <= i) {
                    return null;
                }
                return negative[i];
            }
        }

        private void negativePut(int index, Object value) {
            int i = Math.abs(index) - 1;

            if (isExpansion(i, negative.length, expansionThreshold)) {
                this.negative = expansionCopy(getNewCapacity(i, negative.length), negative);
            }

            negative[i] = value;
        }

        private void positivePut(int index, Object value) {
            if (isExpansion(index, positive.length, expansionThreshold)) {
                this.positive = expansionCopy(getNewCapacity(index, positive.length), positive);
            }

            positive[index] = value;
        }
    }

}
