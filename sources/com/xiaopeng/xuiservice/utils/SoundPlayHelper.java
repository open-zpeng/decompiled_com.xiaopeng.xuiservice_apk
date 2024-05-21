package com.xiaopeng.xuiservice.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.ArrayMap;
import com.xiaopeng.xuimanager.utils.LogUtil;
/* loaded from: classes5.dex */
public class SoundPlayHelper implements SoundPool.OnLoadCompleteListener {
    private static final String TAG = "SoundPlayHelper";
    private Context mContext;
    private ArrayMap<Integer, Integer> mSoundIdMap = new ArrayMap<>();
    private SoundPool mSoundPool;

    @Override // android.media.SoundPool.OnLoadCompleteListener
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        LogUtil.d(TAG, "onLoadComplete enter");
        if (this.mSoundPool != null) {
            LogUtil.d(TAG, "onLoadComplete play " + sampleId);
            this.mSoundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public SoundPlayHelper(Context context) {
        this.mContext = context;
        createSoundPoolIfNeeded();
    }

    private void createSoundPoolIfNeeded() {
        LogUtil.d(TAG, "createSoundPoolIfNeeded");
        if (this.mSoundPool == null) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(1).setContentType(2).build();
            this.mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
            this.mSoundPool.setOnLoadCompleteListener(this);
        }
    }

    public void playNote(int audioId) {
        if (!this.mSoundIdMap.containsKey(Integer.valueOf(audioId))) {
            playMayWait(audioId);
        } else {
            onLoadComplete(this.mSoundPool, this.mSoundIdMap.get(Integer.valueOf(audioId)).intValue(), 0);
        }
    }

    public void releaseSoundPool() {
        LogUtil.d(TAG, "releaseSoundPool");
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            soundPool.autoPause();
            for (Integer soundId : this.mSoundIdMap.keySet()) {
                this.mSoundPool.unload(soundId.intValue());
            }
            this.mSoundIdMap.clear();
        }
    }

    private void playMayWait(int audioId) {
        int sampleId = this.mSoundPool.load(this.mContext, audioId, 1);
        this.mSoundIdMap.put(Integer.valueOf(audioId), Integer.valueOf(sampleId));
        LogUtil.d(TAG, "playMayWait, audioId=" + audioId + ", sampleId=" + sampleId);
    }
}
