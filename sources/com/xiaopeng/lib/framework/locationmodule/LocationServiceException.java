package com.xiaopeng.lib.framework.locationmodule;

import com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationServiceException;
/* loaded from: classes.dex */
public class LocationServiceException extends ILocationServiceException {
    private static final long serialVersionUID = 100;
    private int mReasonCode;

    public LocationServiceException(int reasonCode) {
        super(null);
        this.mReasonCode = reasonCode;
    }

    public LocationServiceException(int reasonCode, String message) {
        super(message);
        this.mReasonCode = reasonCode;
    }

    @Override // com.xiaopeng.lib.framework.moduleinterface.locationmodule.ILocationServiceException
    public int getReasonCode() {
        return this.mReasonCode;
    }
}
