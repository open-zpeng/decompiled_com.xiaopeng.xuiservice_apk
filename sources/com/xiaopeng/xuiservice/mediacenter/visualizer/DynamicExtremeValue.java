package com.xiaopeng.xuiservice.mediacenter.visualizer;

import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes5.dex */
public class DynamicExtremeValue {
    private LinkedList<Float> mList = new LinkedList<>();
    private int mSize;

    public DynamicExtremeValue(int size) {
        this.mSize = size;
    }

    public void addValue(float value) {
        synchronized (this.mList) {
            while (this.mList.size() >= this.mSize) {
                this.mList.removeFirst();
            }
            this.mList.add(Float.valueOf(value));
        }
    }

    public void setSize(int size) {
        synchronized (this.mList) {
            this.mSize = size;
            while (this.mList.size() > this.mSize) {
                this.mList.removeFirst();
            }
        }
    }

    public float getAverageValue() {
        float size;
        synchronized (this.mList) {
            float sum = 0.0f;
            Iterator<Float> it = this.mList.iterator();
            while (it.hasNext()) {
                Float f = it.next();
                sum += f.floatValue();
            }
            size = sum / this.mList.size();
        }
        return size;
    }

    public float getMaxValue() {
        float max;
        synchronized (this.mList) {
            max = Float.MIN_VALUE;
            Iterator<Float> it = this.mList.iterator();
            while (it.hasNext()) {
                Float f = it.next();
                max = Math.max(f.floatValue(), max);
            }
        }
        return max;
    }

    public float getMinValue() {
        float min;
        synchronized (this.mList) {
            min = Float.MAX_VALUE;
            Iterator<Float> it = this.mList.iterator();
            while (it.hasNext()) {
                Float f = it.next();
                min = Math.min(f.floatValue(), min);
            }
        }
        return min;
    }
}
