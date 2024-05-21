package com.qiyi.video.qys;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.qiyi.video.qys.IQYSCallback;
/* loaded from: classes4.dex */
public interface IQYS extends IInterface {
    void executeAsync(IQYSRequest iQYSRequest, IQYSCallback iQYSCallback) throws RemoteException;

    IQYSResponse executeSync(IQYSRequest iQYSRequest) throws RemoteException;

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IQYS {
        private static final String DESCRIPTOR = "com.qiyi.video.qys.IQYS";
        static final int TRANSACTION_executeAsync = 1;
        static final int TRANSACTION_executeSync = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IQYS asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IQYS)) {
                return (IQYS) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IQYSRequest _arg0;
            IQYSRequest _arg02;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = IQYSRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                IQYSCallback _arg1 = IQYSCallback.Stub.asInterface(data.readStrongBinder());
                executeAsync(_arg0, _arg1);
                reply.writeNoException();
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg02 = IQYSRequest.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                IQYSResponse _result = executeSync(_arg02);
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
        private static class Proxy implements IQYS {
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

            @Override // com.qiyi.video.qys.IQYS
            public void executeAsync(IQYSRequest message, IQYSCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.qiyi.video.qys.IQYS
            public IQYSResponse executeSync(IQYSRequest message) throws RemoteException {
                IQYSResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IQYSResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
