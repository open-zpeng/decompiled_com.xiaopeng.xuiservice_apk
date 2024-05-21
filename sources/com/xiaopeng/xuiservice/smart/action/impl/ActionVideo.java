package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.action.util.WindowUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ActionVideo extends ActionMediaPlayer {
    private final double h;
    private SurfaceView surfaceView;
    private final double w;
    private final double x;
    private final double y;

    @Override // com.xiaopeng.xuiservice.smart.action.impl.ActionMediaPlayer, com.xiaopeng.xuiservice.smart.action.ActionBase
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public ActionVideo(String file, int repeat, int audioFocus, double x, double y, double w, double h) {
        super(file, repeat, audioFocus);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionVideo$r-MD7BuZATSxrbQVwdzBlI-Qsro
            @Override // java.lang.Runnable
            public final void run() {
                ActionVideo.this.showVideoViewAndPlay();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionVideo$Nff0DGgZs0WUR1tbITochHeecOA
            @Override // java.lang.Runnable
            public final void run() {
                ActionVideo.this.hideVideoViewAndStop();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showVideoViewAndPlay() {
        if (!initMediaPlayer()) {
            return;
        }
        this.mediaPlayer.prepareAsync();
        Context context = ActivityThread.currentActivityThread().getApplication();
        this.surfaceView = new SurfaceView(context) { // from class: com.xiaopeng.xuiservice.smart.action.impl.ActionVideo.1
            @Override // android.view.SurfaceView, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                Actions.INFO("surface view detached, stop player, action = " + ActionVideo.this);
                ActionVideo.this.stopAndReleaseMediaPlayer();
            }
        };
        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() { // from class: com.xiaopeng.xuiservice.smart.action.impl.ActionVideo.2
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                Actions.INFO("surfaceCreated");
                ActionVideo.this.mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Actions.INFO("surfaceChanged");
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                Actions.INFO("surfaceDestroyed");
            }
        });
        WindowManager windowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
        windowManager.addView(this.surfaceView, WindowUtil.createWindowLayoutParam(context, this.x, this.y, this.w, this.h));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideVideoViewAndStop() {
        if (this.surfaceView != null) {
            Context context = ActivityThread.currentActivityThread().getApplication();
            WindowManager windowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
            windowManager.removeView(this.surfaceView);
            this.surfaceView = null;
        }
    }
}
