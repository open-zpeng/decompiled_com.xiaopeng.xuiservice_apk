package com.xiaopeng.lib.framework.locationmodule;

import android.content.Context;
import android.support.annotation.NonNull;
import com.xiaopeng.lib.framework.locationmodule.provider.ServiceProvider;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationServiceException;
/* loaded from: classes.dex */
public class LocationProviderImpl implements ILocationProvider {
    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider
    public void start(@NonNull Context context) throws ILocationServiceException {
        if (ServiceProvider.getInstance().started()) {
            throw new LocationServiceException(2);
        }
        ServiceProvider.getInstance().startService(context);
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider
    public void publishLocation(@NonNull ILocation location) throws ILocationServiceException {
        if (!ServiceProvider.getInstance().started()) {
            throw new LocationServiceException(1);
        }
        ServiceProvider.getInstance().publishLocation((LocationImpl) location);
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider
    public boolean serviceStarted() {
        return ServiceProvider.getInstance().started();
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationProvider
    public ILocation buildLocation() {
        return new LocationImpl();
    }
}
