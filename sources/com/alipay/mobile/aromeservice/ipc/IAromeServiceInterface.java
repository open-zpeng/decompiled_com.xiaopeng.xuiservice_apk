package com.alipay.mobile.aromeservice.ipc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: classes4.dex */
public interface IAromeServiceInterface extends IInterface {
    void registerOnCustomClickListener(String[] strArr);

    Bundle sendRequest(App app, int i, Bundle bundle);

    void setInitOptions(InitOptions initOptions);

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IAromeServiceInterface {
        private static final String DESCRIPTOR = "com.alipay.mobile.aromeservice.ipc.IAromeServiceInterface";
        static final int TRANSACTION_registerOnCustomClickListener = 3;
        static final int TRANSACTION_sendRequest = 2;
        static final int TRANSACTION_setInitOptions = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAromeServiceInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAromeServiceInterface)) {
                return (IAromeServiceInterface) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
            InitOptions _arg0;
            App _arg02;
            Bundle _arg2;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = InitOptions.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                setInitOptions(_arg0);
                return true;
            } else if (code != 2) {
                if (code != 3) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                }
                data.enforceInterface(DESCRIPTOR);
                String[] _arg03 = data.createStringArray();
                registerOnCustomClickListener(_arg03);
                return true;
            } else {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg02 = App.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                int _arg1 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                Bundle _result = sendRequest(_arg02, _arg1, _arg2);
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            }
        }

        /* loaded from: classes4.dex */
        private static class Proxy implements IAromeServiceInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.alipay.mobile.aromeservice.ipc.IAromeServiceInterface
            public void setInitOptions(InitOptions options) {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.alipay.mobile.aromeservice.ipc.IAromeServiceInterface
            public Bundle sendRequest(App app, int what, Bundle bundle) {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(what);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.alipay.mobile.aromeservice.ipc.IAromeServiceInterface
            public void registerOnCustomClickListener(String[] eventList) {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(eventList);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
