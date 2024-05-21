package com.xiaopeng.xuiservice.xapp.mode.octopus.bean;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;
/* loaded from: classes5.dex */
public class EventMap {
    private Rect mNaviTouchArea;
    private SparseArray<Point> mSelectKeyMap;

    public Rect getNaviTouchArea() {
        return this.mNaviTouchArea;
    }

    public void setNaviTouchArea(Rect naviTouchArea) {
        this.mNaviTouchArea = naviTouchArea;
    }

    public SparseArray<Point> getSelectKeyMap() {
        return this.mSelectKeyMap;
    }

    public void setSelectKeyMap(SparseArray<Point> selectKeyMap) {
        this.mSelectKeyMap = selectKeyMap;
    }

    public String toString() {
        return "EventMap{mNaviTouchArea=" + this.mNaviTouchArea + ", mSelectKeyMap=" + this.mSelectKeyMap + '}';
    }
}
