package com.xiaopeng.lib.framework.locationmodule;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.lib.framework.locationmodule.ILocationServiceCallback;
/* loaded from: classes.dex */
public interface ILocationServiceInterface extends IInterface {
    void subscribe(ILocationServiceCallback iLocationServiceCallback, int i) throws RemoteException;

    void unsubscribe(ILocationServiceCallback iLocationServiceCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ILocationServiceInterface {
        private static final String DESCRIPTOR = "com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface";
        static final int TRANSACTION_subscribe = 1;
        static final int TRANSACTION_unsubscribe = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILocationServiceInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILocationServiceInterface)) {
                return (ILocationServiceInterface) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                ILocationServiceCallback _arg0 = ILocationServiceCallback.Stub.asInterface(data.readStrongBinder());
                int _arg1 = data.readInt();
                subscribe(_arg0, _arg1);
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
                ILocationServiceCallback _arg02 = ILocationServiceCallback.Stub.asInterface(data.readStrongBinder());
                unsubscribe(_arg02);
                reply.writeNoException();
                return true;
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ILocationServiceInterface {
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

            @Override // com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface
            public void subscribe(ILocationServiceCallback callback, int category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(category);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.lib.framework.locationmodule.ILocationServiceInterface
            public void unsubscribe(ILocationServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
