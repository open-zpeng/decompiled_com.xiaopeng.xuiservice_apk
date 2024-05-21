package com.xiaopeng.xuiservice.mediacenter.visualizer;

import java.util.List;
/* loaded from: classes5.dex */
public class SpectrumParams {
    private float[] mFrequencyValues;
    private List<Integer> mMediaType;
    private String mMediaTypeName;
    private double[] mWeightValues;
    private float ratioDelta;

    public List<Integer> getMediaType() {
        return this.mMediaType;
    }

    public void setMediaType(List<Integer> mediaType) {
        this.mMediaType = mediaType;
    }

    public String getMediaTypeName() {
        return this.mMediaTypeName;
    }

    public void setMediaTypeName(String mediaTypeName) {
        this.mMediaTypeName = mediaTypeName;
    }

    public double[] getWeightValues() {
        return this.mWeightValues;
    }

    public void setWeightValues(double[] weightValues) {
        this.mWeightValues = weightValues;
    }

    public float[] getFrequencyValues() {
        return this.mFrequencyValues;
    }

    public void setFrequencyValues(float[] frequencyValues) {
        this.mFrequencyValues = frequencyValues;
    }

    public float getRatioDelta() {
        return this.ratioDelta;
    }

    public void setRatioDelta(float ratioDelta) {
        this.ratioDelta = ratioDelta;
    }
}
