package com.xiaopeng.xuiservice.mediacenter.controller.info;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import com.xiaopeng.xuiservice.XUIConfig;
import com.xiaopeng.xuiservice.utils.UiHandler;
import com.xiaopeng.xuiservice.xapp.util.ResourceUtil;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class IconLoader {
    private static final String TAG = "IconLoader";
    private static Bitmap mDefaultMediaIcon;
    private static Callback mLoadCallback;
    public static CustomTarget<Bitmap> mLoadGameBitmapTarget;
    private static String mLoadUri;
    private static Map<String, Bitmap> mPkgBitmapMap = new HashMap();
    private static HashMap<String, Bitmap> mUriCacheBitmap = new HashMap<>();
    private static final int SIZE_ALBUM_ICON = ResourceUtil.getDimen(R.dimen.icon_album_size);
    private static Runnable mLoadUrlTask = new Runnable() { // from class: com.xiaopeng.xuiservice.mediacenter.controller.info.IconLoader.1
        @Override // java.lang.Runnable
        public void run() {
            if (!TextUtils.isEmpty(IconLoader.mLoadUri)) {
                try {
                    Glide.with(ActivityThread.currentActivityThread().getApplication()).asBitmap().load(IconLoader.mLoadUri).into((RequestBuilder<Bitmap>) IconLoader.mLoadGameBitmapTarget);
                } catch (Exception e) {
                    LogUtil.d(IconLoader.TAG, "mLoadUrlTask exception:" + e.getMessage());
                }
            }
        }
    };

    /* loaded from: classes5.dex */
    public interface Callback {
        void onBitmapLoaded(Bitmap bitmap);
    }

    static {
        int i = SIZE_ALBUM_ICON;
        mLoadGameBitmapTarget = new CustomTarget<Bitmap>(i, i) { // from class: com.xiaopeng.xuiservice.mediacenter.controller.info.IconLoader.2
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                IconLoader.mUriCacheBitmap.clear();
                IconLoader.mUriCacheBitmap.put(IconLoader.mLoadUri, bitmap);
                if (IconLoader.mLoadCallback != null) {
                    IconLoader.mLoadCallback.onBitmapLoaded(bitmap);
                }
            }

            @Override // com.bumptech.glide.request.target.Target
            public void onLoadCleared(Drawable drawable) {
            }

            @Override // com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
            public void onLoadFailed(Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                LogUtil.w(IconLoader.TAG, "onLoadFailed");
            }
        };
    }

    public static Bitmap getAppIconBitmap(String packageName) {
        if (XUIConfig.isInternationalEnable()) {
            return getDefaultMediaIcon();
        }
        if (mPkgBitmapMap.get(packageName) != null) {
            return mPkgBitmapMap.get(packageName);
        }
        Bitmap bmp = drawable2Bitmap(getAppIconDrawableByPackageName(packageName));
        mPkgBitmapMap.put(packageName, bmp);
        return bmp;
    }

    private static Bitmap getDefaultMediaIcon() {
        if (mDefaultMediaIcon == null) {
            mDefaultMediaIcon = BitmapFactory.decodeResource(ActivityThread.currentActivityThread().getApplication().getResources(), R.drawable.ic_default_music_album);
        }
        return mDefaultMediaIcon;
    }

    public static Drawable getAppIconDrawableByPackageName(String ApkTempPackageName) {
        try {
            PackageInfo pi = ActivityThread.currentActivityThread().getApplication().getPackageManager().getPackageInfo(ApkTempPackageName, 0);
            Context otherAppCtx = ActivityThread.currentActivityThread().getApplication().createPackageContext(ApkTempPackageName, 2);
            Drawable drawable = otherAppCtx.getResources().getDrawableForDensity(pi.applicationInfo.icon, 480);
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getCacheUriBitmap(String loadUri) {
        return mUriCacheBitmap.get(loadUri);
    }

    public static void loadUriBitmap(String uri, Callback callback) {
        mLoadUri = uri;
        mLoadCallback = callback;
        Glide.with(ActivityThread.currentActivityThread().getApplication()).clear(mLoadGameBitmapTarget);
        UiHandler.getInstance().removeCallbacks(mLoadUrlTask);
        UiHandler.getInstance().postDelayed(mLoadUrlTask, 500L);
    }
}
