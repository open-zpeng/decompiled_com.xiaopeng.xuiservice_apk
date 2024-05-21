package com.xiaopeng.lib.framework.locationmodule;

import android.content.Context;
import android.support.annotation.NonNull;
import com.xiaopeng.lib.framework.locationmodule.consumer.ServiceProxy;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocation;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer;
import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationServiceException;
/* loaded from: classes.dex */
public class LocationConsumerImpl implements ILocationConsumer {
    private ServiceProxy mServiceProxy;

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public void init(@NonNull Context context, String remoteAppPackage) throws ILocationServiceException {
        ServiceProxy serviceProxy = this.mServiceProxy;
        if (serviceProxy == null) {
            this.mServiceProxy = new ServiceProxy(context, remoteAppPackage);
        } else if (!serviceProxy.connected()) {
            this.mServiceProxy.bindToLocationService();
        } else {
            throw new LocationServiceException(3);
        }
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public void init(@NonNull Context context) throws ILocationServiceException {
        init(context, null);
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public boolean connected() {
        ServiceProxy serviceProxy = this.mServiceProxy;
        return serviceProxy != null && serviceProxy.connected();
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public void subscribe(ILocation.Category category) throws ILocationServiceException {
        ServiceProxy serviceProxy = this.mServiceProxy;
        if (serviceProxy == null) {
            throw new LocationServiceException(5);
        }
        serviceProxy.subscribe(category);
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public void unsubscribe() throws ILocationServiceException {
        ServiceProxy serviceProxy = this.mServiceProxy;
        if (serviceProxy == null) {
            throw new LocationServiceException(5);
        }
        serviceProxy.unsubscribe();
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationConsumer
    public ILocation getLocation() throws ILocationServiceException {
        ServiceProxy serviceProxy = this.mServiceProxy;
        if (serviceProxy == null) {
            throw new LocationServiceException(5);
        }
        return serviceProxy.getLastDrLocation();
    }
}
