package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.xiaopeng.xuiservice.egg.EggLog;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.action.util.Repeater;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public abstract class ActionMediaPlayer extends ActionBase {
    private final int audioFocus;
    private AudioFocusRequest audioFocusRequest = null;
    private final String file;
    protected MediaPlayer mediaPlayer;
    private final Repeater repeater;
    private int requestAudioFocusResult;

    public ActionMediaPlayer(String file, int repeat, int audioFocus) {
        this.file = file;
        this.repeater = new Repeater(repeat, this);
        this.audioFocus = audioFocus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean initMediaPlayer() {
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionMediaPlayer$_ddgCZtR8ztGJsYsZNiUyNfSWJg
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    ActionMediaPlayer.this.lambda$initMediaPlayer$0$ActionMediaPlayer(mediaPlayer);
                }
            });
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionMediaPlayer$yIwoQq7wjtHGdhAWOPJJ_F9ecRc
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    ActionMediaPlayer.this.lambda$initMediaPlayer$1$ActionMediaPlayer(mediaPlayer);
                }
            });
            this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionMediaPlayer$BdF7Fs6GP5sK0KzmPcbnxVnsCxM
                @Override // android.media.MediaPlayer.OnErrorListener
                public final boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    return ActionMediaPlayer.this.lambda$initMediaPlayer$2$ActionMediaPlayer(mediaPlayer, i, i2);
                }
            });
            this.mediaPlayer.setAudioStreamType(audioSteamType());
            this.mediaPlayer.setDataSource(this.file);
            return true;
        } catch (Exception e) {
            Actions.ERROR("startAudio error", e);
            this.mediaPlayer = null;
            stop();
            return false;
        }
    }

    public /* synthetic */ void lambda$initMediaPlayer$0$ActionMediaPlayer(MediaPlayer mp) {
        Actions.INFO("MediaPlayer onPrepared: " + this.file);
        this.mediaPlayer.start();
        requestAudioFocus();
    }

    public /* synthetic */ void lambda$initMediaPlayer$1$ActionMediaPlayer(MediaPlayer mp) {
        Actions.INFO("MediaPlayer onCompletion: " + this.file);
        Repeater repeater = this.repeater;
        final MediaPlayer mediaPlayer = this.mediaPlayer;
        Objects.requireNonNull(mediaPlayer);
        repeater.repeat(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$MMXHIST66pe1ksc_ABl49qiicAI
            @Override // java.lang.Runnable
            public final void run() {
                mediaPlayer.start();
            }
        });
    }

    public /* synthetic */ boolean lambda$initMediaPlayer$2$ActionMediaPlayer(MediaPlayer mp, int what, int extra) {
        Actions.INFO("MediaPlayer onError: " + this.file);
        stop();
        return true;
    }

    protected int audioSteamType() {
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAndReleaseMediaPlayer() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    Actions.INFO("MediaPlayer stop(): " + this.file);
                    this.mediaPlayer.stop();
                }
                this.mediaPlayer.release();
                this.mediaPlayer = null;
                abandonAudioFocus();
            } catch (Exception e) {
                Actions.ERROR("release MediaPlayer error", e);
            }
        }
    }

    private void requestAudioFocus() {
        if (!isValidFocusGain(this.audioFocus)) {
            return;
        }
        Context context = ActivityThread.currentActivityThread().getApplication();
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager != null) {
            AudioFocusRequest.Builder builder = new AudioFocusRequest.Builder(this.audioFocus);
            builder.setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionMediaPlayer$sPeBJkJhgAF9JB3CzjNvlMPEZk8
                @Override // android.media.AudioManager.OnAudioFocusChangeListener
                public final void onAudioFocusChange(int i) {
                    ActionMediaPlayer.this.lambda$requestAudioFocus$3$ActionMediaPlayer(i);
                }
            }, XuiWorkHandler.getInstance());
            this.audioFocusRequest = builder.build();
            this.requestAudioFocusResult = audioManager.requestAudioFocus(this.audioFocusRequest);
            StringBuilder sb = new StringBuilder();
            sb.append("requestAudioFocus ");
            sb.append(this.requestAudioFocusResult == 1 ? "GRANTED" : "FAILED");
            sb.append(" for ");
            sb.append(this);
            EggLog.INFO(sb.toString());
        }
    }

    public /* synthetic */ void lambda$requestAudioFocus$3$ActionMediaPlayer(int focusChange) {
        EggLog.INFO("get onAudioFocusChange, focusChange = " + focusChange + ", " + this);
        if (focusChange == -1 || focusChange == -2) {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
                EggLog.INFO("AUDIOFOCUS_LOSS, pause mediaPlayer");
            }
        } else if (focusChange == 1) {
            MediaPlayer mediaPlayer2 = this.mediaPlayer;
            if (mediaPlayer2 != null && !mediaPlayer2.isPlaying() && !isDone()) {
                this.mediaPlayer.start();
                EggLog.INFO("AUDIOFOCUS_GAIN resume mediaPlayer");
            }
        } else {
            EggLog.INFO("ignore onAudioFocusChange, focusChange = " + focusChange);
        }
    }

    private void abandonAudioFocus() {
        if (!isValidFocusGain(this.audioFocus)) {
            return;
        }
        if (this.audioFocusRequest == null) {
            EggLog.INFO("audioFocusRequest is null, skip abandon audio focus");
        } else if (this.requestAudioFocusResult != 1) {
            EggLog.INFO("abandonAudioFocus skip, request not granted before");
        } else {
            Context context = ActivityThread.currentActivityThread().getApplication();
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (audioManager != null) {
                int abandonResult = audioManager.abandonAudioFocusRequest(this.audioFocusRequest);
                StringBuilder sb = new StringBuilder();
                sb.append("abandonAudioFocusRequest ");
                sb.append(abandonResult == 1 ? "GRANTED" : "FAILED");
                sb.append(" for ");
                sb.append(this);
                EggLog.INFO(sb.toString());
            }
        }
    }

    static boolean isValidFocusGain(int focusGain) {
        if (focusGain == 1 || focusGain == 2 || focusGain == 3 || focusGain == 4) {
            return true;
        }
        return false;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{file='" + this.file + "', repeater=" + this.repeater + ", audioFocus=" + this.audioFocus + '}';
    }
}
