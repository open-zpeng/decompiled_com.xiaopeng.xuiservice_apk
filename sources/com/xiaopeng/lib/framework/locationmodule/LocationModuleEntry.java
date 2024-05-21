package com.xiaopeng.lib.framework.locationmodule;

import com.xiaopeng.lib.framework.module.IModuleEntry;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider;
/* loaded from: classes.dex */
public class LocationModuleEntry implements IModuleEntry {
    @Override // com.xiaopeng.lib.framework.module.IModuleEntry
    public Object get(Class interfaceClass) {
        if (interfaceClass != ILocationProvider.class) {
            if (interfaceClass != ILocationConsumer.class) {
                return null;
            }
            return LocationConsumerHolder.INSTANCE;
        }
        return LocationProviderHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class LocationConsumerHolder {
        private static final ILocationConsumer INSTANCE = new LocationConsumerImpl();

        private LocationConsumerHolder() {
        }
    }

    /* loaded from: classes.dex */
    private static final class LocationProviderHolder {
        private static final ILocationProvider INSTANCE = new LocationProviderImpl();

        private LocationProviderHolder() {
        }
    }
}
