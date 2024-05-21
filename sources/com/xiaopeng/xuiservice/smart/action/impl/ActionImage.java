package com.xiaopeng.xuiservice.smart.action.impl;

import android.app.ActivityThread;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.widget.ImageView;
import com.xiaopeng.xuiservice.smart.action.ActionBase;
import com.xiaopeng.xuiservice.smart.action.Actions;
import com.xiaopeng.xuiservice.smart.action.util.WindowUtil;
import com.xiaopeng.xuiservice.smart.condition.impl.ConditionWindowPos;
import com.xiaopeng.xuiservice.utils.XuiWorkHandler;
/* loaded from: classes5.dex */
public class ActionImage extends ActionBase {
    private Bitmap bitmap;
    private final String file;
    private final double h;
    private ImageView imageView;
    private final double w;
    private final double x;
    private final double y;

    public ActionImage(String file, double x, double y, double w, double h) {
        this.file = file;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStart() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionImage$aAii8VuDYGE8fS-byWYA1YaSYqE
            @Override // java.lang.Runnable
            public final void run() {
                ActionImage.this.showImage();
            }
        });
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    protected void onStop() {
        XuiWorkHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.smart.action.impl.-$$Lambda$ActionImage$2L2D6Asadk-zQWkZhBrv5SuQ3bw
            @Override // java.lang.Runnable
            public final void run() {
                ActionImage.this.hideImage();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showImage() {
        String str = this.file;
        if (str != null) {
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            this.bitmap = decodeFile;
            if (decodeFile != null) {
                Context context = ActivityThread.currentActivityThread().getApplication();
                this.imageView = new ImageView(context) { // from class: com.xiaopeng.xuiservice.smart.action.impl.ActionImage.1
                    @Override // android.widget.ImageView, android.view.View
                    protected void onDetachedFromWindow() {
                        super.onDetachedFromWindow();
                        Actions.INFO("image view detached, recycle bitmap, action = " + ActionImage.this);
                        ActionImage.this.bitmap.recycle();
                    }
                };
                this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                this.imageView.setImageBitmap(this.bitmap);
                WindowManager windowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
                windowManager.addView(this.imageView, WindowUtil.createWindowLayoutParam(context, this.x, this.y, this.w, this.h));
                Actions.INFO("display image success, file = " + this.file + ", bitmap resolution = " + this.bitmap.getWidth() + "x" + this.bitmap.getHeight());
                return;
            }
        }
        Actions.ERROR("load image bitmap fail, file = " + this.file);
        stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideImage() {
        if (this.imageView != null) {
            Context context = ActivityThread.currentActivityThread().getApplication();
            WindowManager windowManager = (WindowManager) context.getSystemService(ConditionWindowPos.TYPE);
            windowManager.removeView(this.imageView);
            this.imageView = null;
        }
    }

    @Override // com.xiaopeng.xuiservice.smart.action.ActionBase
    public String toString() {
        return super.toString() + "{file='" + this.file + "', x=" + this.x + ", y=" + this.y + ", w=" + this.w + ", h=" + this.h + '}';
    }
}
