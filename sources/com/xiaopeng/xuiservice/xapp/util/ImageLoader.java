package com.xiaopeng.xuiservice.xapp.util;

import android.app.ActivityThread;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.utils.UiHandler;
/* loaded from: classes5.dex */
public class ImageLoader {
    public static final String TAG = "ImageLoader";
    private static final int SIZE_WIDTH_GAME_ICON = ResourceUtil.getDimen(R.dimen.dialog_game_mode_prompt_width);
    private static final int SIZE_HEIGHT_GAME_ICON = ResourceUtil.getDimen(R.dimen.dialog_game_mode_prompt_height);
    public static CustomTarget<Bitmap> mLoadGameBitmapTarget = new CustomTarget<Bitmap>(SIZE_WIDTH_GAME_ICON, SIZE_HEIGHT_GAME_ICON) { // from class: com.xiaopeng.xuiservice.xapp.util.ImageLoader.3
        @Override // com.bumptech.glide.request.target.Target
        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
            onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
        }

        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onLoadCleared(Drawable drawable) {
        }

        @Override // com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
        public void onLoadFailed(Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            LogUtil.w(ImageLoader.TAG, "onLoadFailed");
        }
    };

    public static void preloadGameImage(final String imageUri) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.ImageLoader.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Glide.with(ActivityThread.currentActivityThread().getApplication()).asBitmap().load(imageUri).into((RequestBuilder<Bitmap>) ImageLoader.mLoadGameBitmapTarget);
                } catch (Exception e) {
                    LogUtil.d(ImageLoader.TAG, "preloadGameImage exception:" + e.getMessage());
                }
            }
        });
    }

    public static void loadGameImage(final String imageUri, final ImageView view, final int defaultId) {
        UiHandler.getInstance().post(new Runnable() { // from class: com.xiaopeng.xuiservice.xapp.util.ImageLoader.2
            @Override // java.lang.Runnable
            public void run() {
                Glide.with(ActivityThread.currentActivityThread().getApplication()).asBitmap().load(imageUri).error(defaultId).into((RequestBuilder) new CustomTarget<Bitmap>(ImageLoader.SIZE_WIDTH_GAME_ICON, ImageLoader.SIZE_HEIGHT_GAME_ICON) { // from class: com.xiaopeng.xuiservice.xapp.util.ImageLoader.2.1
                    @Override // com.bumptech.glide.request.target.Target
                    public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                        onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
                    }

                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        view.setImageBitmap(bitmap);
                    }

                    @Override // com.bumptech.glide.request.target.Target
                    public void onLoadCleared(Drawable drawable) {
                    }

                    @Override // com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        LogUtil.w(ImageLoader.TAG, "loadGameImage onLoadFailed");
                        view.setImageResource(defaultId);
                    }
                });
            }
        });
    }
}
