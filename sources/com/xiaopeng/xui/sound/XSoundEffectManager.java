package com.xiaopeng.xui.sound;

import android.media.AudioManager;
import androidx.annotation.NonNull;
import com.xiaopeng.xui.Xui;
import com.xiaopeng.xui.utils.XLogUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes5.dex */
public class XSoundEffectManager {
    private static final String TAG = "xpui-XSoundManager";
    private ExecutorService mExecutorService;
    private boolean mIsDestroy;
    private ConcurrentHashMap<Integer, XSoundEffect> mPoolHashMap;
    private ConcurrentHashMap<Integer, Boolean> mReleaseMap;

    /* loaded from: classes5.dex */
    private static class SingletonHolder {
        private static final XSoundEffectManager INSTANCE = new XSoundEffectManager();

        private SingletonHolder() {
        }
    }

    private XSoundEffectManager() {
        this.mPoolHashMap = new ConcurrentHashMap<>();
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mReleaseMap = new ConcurrentHashMap<>();
    }

    public static XSoundEffectManager get() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void play(int resource) {
        int autoManagerId = useAudioManager(resource);
        if (autoManagerId > 0) {
            XAudioManager.get().playSoundEffect(autoManagerId);
        } else {
            playLocal(resource);
        }
    }

    private void playLocal(final int resource) {
        this.mIsDestroy = false;
        this.mReleaseMap.put(Integer.valueOf(resource), false);
        this.mExecutorService.execute(new Runnable() { // from class: com.xiaopeng.xui.sound.-$$Lambda$XSoundEffectManager$bCpQWhKM74bwPueztuwQSwNsLTs
            @Override // java.lang.Runnable
            public final void run() {
                XSoundEffectManager.this.lambda$playLocal$0$XSoundEffectManager(resource);
            }
        });
    }

    public /* synthetic */ void lambda$playLocal$0$XSoundEffectManager(int resource) {
        if (this.mIsDestroy) {
            log(String.format("play-not for destroy resource:%s", Integer.valueOf(resource)));
            return;
        }
        Boolean isRelease = this.mReleaseMap.get(Integer.valueOf(resource));
        if (isRelease != null && isRelease.booleanValue()) {
            log(String.format("play-not for release resource:%s", Integer.valueOf(resource)));
            return;
        }
        System.currentTimeMillis();
        XSoundEffect soundEffect = this.mPoolHashMap.get(Integer.valueOf(resource));
        if (soundEffect == null) {
            soundEffect = SoundEffectFactory.create(resource);
            this.mPoolHashMap.put(Integer.valueOf(resource), soundEffect);
        }
        soundEffect.play();
    }

    private int useAudioManager(int id) {
        if (id != 2) {
            if (id != 3) {
                if (id != 4) {
                    if (id == 5) {
                        return 14;
                    }
                    return -1;
                }
                return 15;
            }
            return 16;
        }
        return 17;
    }

    public synchronized void release(final int resource) {
        this.mReleaseMap.put(Integer.valueOf(resource), true);
        this.mExecutorService.execute(new Runnable() { // from class: com.xiaopeng.xui.sound.-$$Lambda$XSoundEffectManager$wIXGXmGDQzq-cwWNmTR5CyWKfoQ
            @Override // java.lang.Runnable
            public final void run() {
                XSoundEffectManager.this.lambda$release$1$XSoundEffectManager(resource);
            }
        });
    }

    public /* synthetic */ void lambda$release$1$XSoundEffectManager(int resource) {
        XSoundEffect soundEffect = this.mPoolHashMap.get(Integer.valueOf(resource));
        if (soundEffect != null) {
            soundEffect.release();
        }
        log(String.format("release resource:%s", Integer.valueOf(resource)));
    }

    public synchronized void destroy() {
        this.mIsDestroy = true;
        this.mExecutorService.execute(new Runnable() { // from class: com.xiaopeng.xui.sound.-$$Lambda$XSoundEffectManager$gAnWfvgV_N-93OUZ2dykrfal-MM
            @Override // java.lang.Runnable
            public final void run() {
                XSoundEffectManager.this.lambda$destroy$2$XSoundEffectManager();
            }
        });
    }

    public /* synthetic */ void lambda$destroy$2$XSoundEffectManager() {
        long time = System.currentTimeMillis();
        for (XSoundEffect soundEffect : this.mPoolHashMap.values()) {
            if (soundEffect != null) {
                soundEffect.release();
            }
        }
        this.mPoolHashMap.clear();
        this.mReleaseMap.clear();
        log("destroy time : " + (System.currentTimeMillis() - time));
    }

    public synchronized void resetResource(int resource, @NonNull String path, int location) {
        resetResource(resource, path, location, 5);
    }

    public synchronized void resetResource(final int resource, @NonNull final String path, final int location, final int streamType) {
        this.mExecutorService.execute(new Runnable() { // from class: com.xiaopeng.xui.sound.-$$Lambda$XSoundEffectManager$zBcAqtI6-Ez0rykCABVb4QC0jtQ
            @Override // java.lang.Runnable
            public final void run() {
                XSoundEffectManager.this.lambda$resetResource$3$XSoundEffectManager(resource, path, location, streamType);
            }
        });
    }

    public /* synthetic */ void lambda$resetResource$3$XSoundEffectManager(int resource, String path, int location, int streamType) {
        SoundEffectFactory.resetResource(resource, path, location, streamType);
        XSoundEffect soundEffect = this.mPoolHashMap.get(Integer.valueOf(resource));
        if (soundEffect != null) {
            soundEffect.release();
            this.mPoolHashMap.remove(Integer.valueOf(resource));
        }
        log(String.format("resetResource--resource:%s,path:%s,location:%s,streamType:%s", Integer.valueOf(resource), path, Integer.valueOf(location), Integer.valueOf(streamType)));
    }

    public synchronized void restoreResource(final int resource) {
        this.mExecutorService.execute(new Runnable() { // from class: com.xiaopeng.xui.sound.-$$Lambda$XSoundEffectManager$xVTBBr68UkEjCh3o-q9_G_PQBcY
            @Override // java.lang.Runnable
            public final void run() {
                XSoundEffectManager.this.lambda$restoreResource$4$XSoundEffectManager(resource);
            }
        });
    }

    public /* synthetic */ void lambda$restoreResource$4$XSoundEffectManager(int resource) {
        SoundEffectFactory.restoreResource(resource);
        XSoundEffect soundEffect = this.mPoolHashMap.get(Integer.valueOf(resource));
        if (soundEffect != null) {
            soundEffect.release();
            this.mPoolHashMap.remove(Integer.valueOf(resource));
        }
        log(String.format("restoreResource resource:%s", Integer.valueOf(resource)));
    }

    private void log(String msg) {
        XLogUtils.d(TAG, msg);
    }

    /* loaded from: classes5.dex */
    public static class XAudioManager {
        public static final int FX_DEL = 14;
        public static final int FX_SWITCH_OFF = 15;
        public static final int FX_SWITCH_ON = 16;
        public static final int FX_WHEEL_SCROLL = 17;
        private AudioManager mAudioManager;

        private XAudioManager() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes5.dex */
        public static class SingletonHolder {
            private static final XAudioManager INSTANCE = new XAudioManager();

            private SingletonHolder() {
            }
        }

        public static XAudioManager get() {
            return SingletonHolder.INSTANCE;
        }

        private AudioManager getAudioManager() {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) Xui.getContext().getSystemService("audio");
            }
            return this.mAudioManager;
        }

        public void playSoundEffect(int effectType) {
            getAudioManager().playSoundEffect(effectType);
        }
    }
}
