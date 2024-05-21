package com.xiaopeng.lib.framework.locationmodule.consumer;

import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationEvent;
/* loaded from: classes.dex */
public class LocationChangedEvent implements ILocationEvent {
    private ILocation mLocation;

    public LocationChangedEvent location(ILocation location) {
        this.mLocation = location;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationEvent
    public ILocation location() {
        return this.mLocation;
    }
}
