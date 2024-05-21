package com.xiaopeng.lib.framework.locationmodule.consumer;

import com.xiaopeng.lib.framework.moduleinterface.locationmodule.IStateEvent;
/* loaded from: classes.dex */
public class ServiceStateEvent implements IStateEvent {
    private IStateEvent.TYPE mType;

    public ServiceStateEvent type(IStateEvent.TYPE value) {
        this.mType = value;
        return this;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.IStateEvent
    public IStateEvent.TYPE type() {
        return this.mType;
    }
}
