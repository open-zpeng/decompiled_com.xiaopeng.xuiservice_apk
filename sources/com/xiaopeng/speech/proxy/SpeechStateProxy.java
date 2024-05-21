package com.xiaopeng.speech.proxy;

import android.os.IBinder;
import android.os.RemoteException;
import com.xiaopeng.speech.ConnectManager;
import com.xiaopeng.speech.ISpeechEngine;
import com.xiaopeng.speech.ISpeechState;
import com.xiaopeng.speech.common.util.IPCRunner;
import com.xiaopeng.speech.common.util.LogUtils;
/* loaded from: classes2.dex */
public class SpeechStateProxy extends ISpeechState.Stub implements ConnectManager.OnConnectCallback {
    private ISpeechState mSpeechState;
    private IPCRunner<ISpeechState> mIpcRunner = new IPCRunner<>("SpeechStateProxy");
    private Integer phoneCallStatusCache = null;

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isInitOK() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isInitOK();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setPhoneCallStatus(final int callStatus) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.1
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setPhoneCallStatus(callStatus);
                SpeechStateProxy.this.phoneCallStatusCache = Integer.valueOf(callStatus);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setPhoneCallStatusWithBinder(final IBinder binder, final int callStatus) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.2
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setPhoneCallStatusWithBinder(binder, callStatus);
                SpeechStateProxy.this.phoneCallStatusCache = Integer.valueOf(callStatus);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public int getPhoneCallStatus() {
        return this.phoneCallStatusCache.intValue();
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setShowSpeechDialog(final boolean isShow) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.3
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setShowSpeechDialog(isShow);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isShowSpeechDialog() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isShowSpeechDialog();
            } catch (RemoteException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    @Override // com.xiaopeng.speech.ConnectManager.OnConnectCallback
    public void onConnect(ISpeechEngine speechEngine) {
        try {
            if (speechEngine.getSpeechState() != null) {
                this.mSpeechState = speechEngine.getSpeechState();
                this.mIpcRunner.setProxy(this.mSpeechState);
                this.mIpcRunner.fetchAll();
                resumeSpeechState();
            } else {
                LogUtils.e("SpeechStateProxy", "isInitOK false, speechEngine.getSpeechState() = null");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.speech.ConnectManager.OnConnectCallback
    public void onDisconnect() {
        this.mIpcRunner.setProxy(null);
        this.mSpeechState = null;
    }

    private void resumeSpeechState() {
        StringBuilder sb = new StringBuilder();
        sb.append("resume speech state: ");
        Object obj = this.phoneCallStatusCache;
        if (obj == null) {
            obj = "null";
        }
        sb.append(obj);
        LogUtils.i("SpeechStateProxy", sb.toString());
        if (this.phoneCallStatusCache != null) {
            setPhoneCallStatusWithBinder(asBinder(), this.phoneCallStatusCache.intValue());
        }
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setOOBEStatus(final int callStatus) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.4
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setOOBEStatus(callStatus);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public int getOOBEStatus() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.getOOBEStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setCanExitFlag(final boolean flag) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.5
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setCanExitFlag(flag);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean canExit() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.canExit();
            } catch (RemoteException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isDMStarted() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isDMStarted();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void changeCurrentTTS(final String name) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.6
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.changeCurrentTTS(name);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public String getCurrentTTS() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.getCurrentTTS();
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public void setFuncState(final int func, final boolean flag) {
        this.mIpcRunner.runFunc(new IPCRunner.IIPCFunc<ISpeechState, Object>() { // from class: com.xiaopeng.speech.proxy.SpeechStateProxy.7
            @Override // com.xiaopeng.speech.common.util.IPCRunner.IIPCFunc
            public Object run(ISpeechState proxy) throws RemoteException {
                proxy.setFuncState(func, flag);
                return null;
            }
        });
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean getFuncState(int func) {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.getFuncState(func);
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isStateWaiting() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isStateWaiting();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean onWaiting() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.onWaiting();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isDMEndByPOISelect() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isDMEndByPOISelect();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isHotWordEngineReady() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isHotWordEngineReady();
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public boolean isMicrophoneMute() {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.isMicrophoneMute();
            } catch (RemoteException e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }

    @Override // com.xiaopeng.speech.ISpeechState
    public String canPerformASRToText(String packageName) {
        ISpeechState iSpeechState = this.mSpeechState;
        if (iSpeechState != null) {
            try {
                return iSpeechState.canPerformASRToText(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
