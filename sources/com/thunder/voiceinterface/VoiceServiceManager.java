package com.thunder.voiceinterface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.thunder.voiceinterface.IVoiceInterface;
import com.xiaopeng.xuiservice.xapp.Constants;
/* loaded from: classes4.dex */
public class VoiceServiceManager {
    private static volatile VoiceServiceManager INSTANCE;
    private OnVoiceBindListener listener;
    private final ServiceConnection mConnection = new ServiceConnection() { // from class: com.thunder.voiceinterface.VoiceServiceManager.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            VoiceServiceManager.this.mVoiceServer = IVoiceInterface.Stub.asInterface(service);
            if (VoiceServiceManager.this.listener != null) {
                VoiceServiceManager.this.listener.onBindCompleted();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            VoiceServiceManager.this.mVoiceServer = null;
            VoiceServiceManager.this.listener = null;
        }
    };
    private IVoiceInterface mVoiceServer;

    public void exitApplication() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.exitApplication();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void searchSong(String songName, String singerName) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.searchSong(songName, singerName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void previousPage() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.previousPage();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextPage() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.nextPage();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void scrollTop() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.scrollTop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addSongOrdered(String songName) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.addSongOrdered(songName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void singRightNow(String songName) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.singRightNow(songName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void originalSinging(boolean isOpen) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.originalSinging(isOpen);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void nextSong() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.nextSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void playOrPause(boolean isPlay) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.playOrPause(isPlay);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void replay() {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.replay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void openAtmosphere(int type) {
        IVoiceInterface iVoiceInterface = this.mVoiceServer;
        if (iVoiceInterface != null) {
            try {
                iVoiceInterface.openAtmosphere(type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void bindServer(Context context, OnVoiceBindListener listener) {
        this.listener = listener;
        Intent intent = new Intent();
        intent.setClassName(Constants.PACKAGE_THUNDER, "com.thunder.voice_service.VoiceService");
        boolean isBind = context.bindService(intent, this.mConnection, 1);
        Log.i("BindService", " isBind = " + isBind);
    }

    public void unBindServer(Context context) {
        if (this.mVoiceServer != null) {
            context.unbindService(this.mConnection);
        }
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this) {
            z = this.mVoiceServer != null;
        }
        return z;
    }

    private VoiceServiceManager() {
    }

    public static VoiceServiceManager getInstance() {
        if (INSTANCE == null) {
            synchronized (VoiceServiceManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VoiceServiceManager();
                }
            }
        }
        return INSTANCE;
    }
}
