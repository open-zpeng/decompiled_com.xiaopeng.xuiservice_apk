package com.qiyi.video.qys;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IQYSCallback extends IInterface {
    void onResponse(IQYSResponse iQYSResponse) throws RemoteException;

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IQYSCallback {
        private static final String DESCRIPTOR = "com.qiyi.video.qys.IQYSCallback";
        static final int TRANSACTION_onResponse = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IQYSCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IQYSCallback)) {
                return (IQYSCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IQYSResponse _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = IQYSResponse.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            onResponse(_arg0);
            reply.writeNoException();
            return true;
        }

        /* loaded from: classes4.dex */
        private static class Proxy implements IQYSCallback {
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

            @Override // com.qiyi.video.qys.IQYSCallback
            public void onResponse(IQYSResponse response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
