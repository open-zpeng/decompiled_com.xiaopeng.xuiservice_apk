package com.xiaopeng.xuiservice.smart.condition.impl;

import android.annotation.SuppressLint;
import android.app.ActivityThread;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue;
import com.xiaopeng.xuiservice.smart.condition.operator.OP;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ConditionLocation extends ConditionSingleValue<Location, Location> implements LocationListener {
    private volatile Location currentLocation;
    private final LocationManager locationManager;
    private final float minDistanceM;
    private final long minTimeMs;

    @SuppressLint({"MissingPermission"})
    public ConditionLocation(Location targetValue, long minTimeMs, float minDistanceM) {
        super(targetValue);
        this.minTimeMs = minTimeMs;
        this.minDistanceM = minDistanceM;
        this.locationManager = (LocationManager) ActivityThread.currentActivityThread().getApplication().getSystemService("location");
        boolean isGPSEnable = this.locationManager.isProviderEnabled("gps");
        this.currentLocation = isGPSEnable ? this.locationManager.getLastKnownLocation("gps") : null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    @SuppressLint({"MissingPermission"})
    public Location fetchCurrentValue() {
        return this.currentLocation;
    }

    public /* synthetic */ void lambda$onStartWatch$0$ConditionLocation() {
        this.locationManager.requestLocationUpdates("gps", this.minTimeMs, this.minDistanceM, this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    @SuppressLint({"MissingPermission"})
    protected void onStartWatch() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.condition.impl.-$$Lambda$ConditionLocation$ZdsgdTUQB2UG5AP1bWRVN1f8180
            @Override // java.lang.Runnable
            public final void run() {
                ConditionLocation.this.lambda$onStartWatch$0$ConditionLocation();
            }
        });
    }

    public /* synthetic */ void lambda$onStopWatch$1$ConditionLocation() {
        this.locationManager.removeUpdates(this);
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue, com.xiaopeng.xuiservice.smart.condition.ConditionBase
    protected void onStopWatch() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.condition.impl.-$$Lambda$ConditionLocation$mPctBYRvBM8J9O0nyXf6AcpuRpc
            @Override // java.lang.Runnable
            public final void run() {
                ConditionLocation.this.lambda$onStopWatch$1$ConditionLocation();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$operator$2(Location t, Location c) {
        return false;
    }

    @Override // com.xiaopeng.xuiservice.smart.condition.ConditionSingleValue
    protected OP<Location, Location> operator() {
        return new OP() { // from class: com.xiaopeng.xuiservice.smart.condition.impl.-$$Lambda$ConditionLocation$B7LHx6Gi_3VhLQOkBXBRpYEmUSY
            @Override // com.xiaopeng.xuiservice.smart.condition.operator.OP
            public final boolean isMatch(Object obj, Object obj2) {
                return ConditionLocation.lambda$operator$2((Location) obj, (Location) obj2);
            }
        };
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(@NonNull Location location) {
        this.currentLocation = location;
        updateCurrentValue(location);
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
