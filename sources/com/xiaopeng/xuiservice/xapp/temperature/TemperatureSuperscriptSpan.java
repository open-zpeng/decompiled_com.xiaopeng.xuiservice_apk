package com.xiaopeng.xuiservice.xapp.temperature;

import android.app.ActivityThread;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import com.xiaopeng.xuiservice.R;
/* loaded from: classes5.dex */
public class TemperatureSuperscriptSpan extends MetricAffectingSpan implements ParcelableSpan {
    private static final int OFFSET = ActivityThread.currentActivityThread().getApplication().getResources().getDimensionPixelOffset(R.dimen.qc_temperature_suffix_offset);

    public TemperatureSuperscriptSpan() {
    }

    public TemperatureSuperscriptSpan(Parcel src) {
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 14;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.baselineShift -= OFFSET;
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint textPaint) {
        textPaint.baselineShift -= OFFSET;
    }
}
