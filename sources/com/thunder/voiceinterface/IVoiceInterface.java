package com.thunder.voiceinterface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IVoiceInterface extends IInterface {
    void addSongOrdered(String str) throws RemoteException;

    void exitApplication() throws RemoteException;

    void nextPage() throws RemoteException;

    void nextSong() throws RemoteException;

    void openAtmosphere(int i) throws RemoteException;

    void originalSinging(boolean z) throws RemoteException;

    void playOrPause(boolean z) throws RemoteException;

    void previousPage() throws RemoteException;

    void replay() throws RemoteException;

    void scrollTop() throws RemoteException;

    void searchSong(String str, String str2) throws RemoteException;

    void singRightNow(String str) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IVoiceInterface {
        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void exitApplication() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void searchSong(String songName, String singerName) throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void previousPage() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void nextPage() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void scrollTop() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void addSongOrdered(String songName) throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void singRightNow(String songName) throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void originalSinging(boolean isOpen) throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void nextSong() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void playOrPause(boolean isPlay) throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void replay() throws RemoteException {
        }

        @Override // com.thunder.voiceinterface.IVoiceInterface
        public void openAtmosphere(int type) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IVoiceInterface {
        private static final String DESCRIPTOR = "com.thunder.voiceinterface.IVoiceInterface";
        static final int TRANSACTION_addSongOrdered = 6;
        static final int TRANSACTION_exitApplication = 1;
        static final int TRANSACTION_nextPage = 4;
        static final int TRANSACTION_nextSong = 9;
        static final int TRANSACTION_openAtmosphere = 12;
        static final int TRANSACTION_originalSinging = 8;
        static final int TRANSACTION_playOrPause = 10;
        static final int TRANSACTION_previousPage = 3;
        static final int TRANSACTION_replay = 11;
        static final int TRANSACTION_scrollTop = 5;
        static final int TRANSACTION_searchSong = 2;
        static final int TRANSACTION_singRightNow = 7;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVoiceInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IVoiceInterface)) {
                return (IVoiceInterface) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    exitApplication();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg1 = data.readString();
                    searchSong(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    previousPage();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    nextPage();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    scrollTop();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    addSongOrdered(data.readString());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    singRightNow(data.readString());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    originalSinging(_arg0);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    nextSong();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    playOrPause(_arg0);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    replay();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    openAtmosphere(data.readInt());
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IVoiceInterface {
            public static IVoiceInterface sDefaultImpl;
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

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void exitApplication() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().exitApplication();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void searchSong(String songName, String singerName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(songName);
                    _data.writeString(singerName);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().searchSong(songName, singerName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void previousPage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().previousPage();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void nextPage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().nextPage();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void scrollTop() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scrollTop();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void addSongOrdered(String songName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(songName);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addSongOrdered(songName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void singRightNow(String songName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(songName);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().singRightNow(songName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void originalSinging(boolean isOpen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOpen ? 1 : 0);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().originalSinging(isOpen);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void nextSong() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().nextSong();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void playOrPause(boolean isPlay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isPlay ? 1 : 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playOrPause(isPlay);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void replay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().replay();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.thunder.voiceinterface.IVoiceInterface
            public void openAtmosphere(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().openAtmosphere(type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IVoiceInterface impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IVoiceInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
