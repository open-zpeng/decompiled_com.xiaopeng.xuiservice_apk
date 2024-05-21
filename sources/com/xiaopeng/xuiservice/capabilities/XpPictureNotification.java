package com.xiaopeng.xuiservice.capabilities;

import android.app.ActivityThread;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.internal.view.SupportMenu;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
import java.util.HashMap;
/* loaded from: classes5.dex */
public class XpPictureNotification {
    private static final boolean DBG = true;
    private static final String TAG = "XpPictureNotification";
    private int NOTIFICATION_ID;
    private Context mContext;
    private NotificationManager mNotificationManager;

    private XpPictureNotification(Context context) {
        this.NOTIFICATION_ID = 234;
        this.mContext = context;
        this.mNotificationManager = NotificationManager.from(context.getApplicationContext());
    }

    public static XpPictureNotification getInstance() {
        LogUtil.d(TAG, "XpPictureNotification getInstance() called");
        return InstanceHolder.picNotification;
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final XpPictureNotification picNotification = new XpPictureNotification(ActivityThread.currentActivityThread().getApplication());

        private InstanceHolder() {
        }
    }

    public void generateBigPictureStyleNotification(int vId) {
        LogUtil.d(TAG, "generateBigPictureStyleNotification()");
        if (Build.VERSION.SDK_INT >= 26) {
            LogUtil.d(TAG, ">= android.os.Build.VERSION_CODES.O");
            Context packageContext = this.mContext.getApplicationContext();
            NotificationChannel mChannel = new NotificationChannel("my_channel_01", "my_channel", 4);
            mChannel.setDescription("This is my channel");
            mChannel.enableLights(true);
            mChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            this.mNotificationManager.createNotificationChannel(mChannel);
            Notification notif = new Notification.Builder(packageContext, "my_channel_01").setContentTitle(Constants.bigPicMap.get(Integer.valueOf(vId)).getmBigContentTitle()).setContentText(Constants.bigPicMap.get(Integer.valueOf(vId)).getmContentText()).setSmallIcon(R.drawable.car_ic_mode).setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(packageContext.getResources(), R.drawable.car_ic_mode))).build();
            this.mNotificationManager.notify(this.NOTIFICATION_ID, notif);
        }
    }

    /* loaded from: classes5.dex */
    private static class Constants {
        public static HashMap<Integer, BigPicStyleObj> bigPicMap = new HashMap<Integer, BigPicStyleObj>() { // from class: com.xiaopeng.xuiservice.capabilities.XpPictureNotification.Constants.1
            {
                put(557854211, new BigPicStyleObj("showoff灯语开关", "ESP off指示灯", 1, "Showoff_cId", "Showoff_channel", "ESP off指示灯状态变更", 4, true, 0, R.drawable.car_ic_mode, "内容大标题。。。", "总结文字。。。"));
                put(557847631, new BigPicStyleObj("充电灯语开关", "12V蓄电池充电系统指示灯", 1, "Charging_cId", "Charging_channel", "电池充电状态变更", 4, true, 0, R.drawable.car_ic_mode, "充电灯语开关", "总结文字。。。"));
                put(557851660, new BigPicStyleObj("电子刹车（EPB）故障指示灯", "电子刹车（EPB）发现故障，请及时修复", 1, "EPB_SysWarning_cId", "EPB_SysWarning_channel", "电子刹车（EPB）出现故障", 4, true, 0, R.drawable.car_ic_mode, "内容大标题。。。", "总结文字。。。"));
            }
        };

        private Constants() {
        }
    }

    /* loaded from: classes5.dex */
    protected static class BigPicStyleObj {
        protected String mBigContentTitle;
        protected int mBigImage;
        protected String mChannelDescription;
        protected boolean mChannelEnableVibrate;
        protected String mChannelId;
        protected int mChannelImportance;
        protected int mChannelLockscreenVisibility;
        protected CharSequence mChannelName;
        protected String mContentText;
        protected String mContentTitle;
        protected int mPriority;
        protected String mSummaryText;
        protected int vId;

        public BigPicStyleObj(int vId) {
            this.vId = vId;
        }

        public BigPicStyleObj(String mContentTitle, String mContentText, int mPriority, String mChannelId, CharSequence mChannelName, String mChannelDescription, int mChannelImportance, boolean mChannelEnableVibrate, int mChannelLockscreenVisibility, int mBigImage, String mBigContentTitle, String mSummaryText) {
            this.mContentTitle = mContentTitle;
            this.mContentText = mContentText;
            this.mPriority = mPriority;
            this.mChannelId = mChannelId;
            this.mChannelName = mChannelName;
            this.mChannelDescription = mChannelDescription;
            this.mChannelImportance = mChannelImportance;
            this.mChannelEnableVibrate = mChannelEnableVibrate;
            this.mChannelLockscreenVisibility = mChannelLockscreenVisibility;
            this.mBigImage = mBigImage;
            this.mBigContentTitle = mBigContentTitle;
            this.mSummaryText = mSummaryText;
        }

        public String getmContentTitle() {
            return this.mContentTitle;
        }

        public void setmContentTitle(String mContentTitle) {
            this.mContentTitle = mContentTitle;
        }

        public String getmContentText() {
            return this.mContentText;
        }

        public void setmContentText(String mContentText) {
            this.mContentText = mContentText;
        }

        public int getmPriority() {
            return this.mPriority;
        }

        public void setmPriority(int mPriority) {
            this.mPriority = mPriority;
        }

        public String getmChannelId() {
            return this.mChannelId;
        }

        public void setmChannelId(String mChannelId) {
            this.mChannelId = mChannelId;
        }

        public CharSequence getmChannelName() {
            return this.mChannelName;
        }

        public void setmChannelName(CharSequence mChannelName) {
            this.mChannelName = mChannelName;
        }

        public String getmChannelDescription() {
            return this.mChannelDescription;
        }

        public void setmChannelDescription(String mChannelDescription) {
            this.mChannelDescription = mChannelDescription;
        }

        public int getmChannelImportance() {
            return this.mChannelImportance;
        }

        public void setmChannelImportance(int mChannelImportance) {
            this.mChannelImportance = mChannelImportance;
        }

        public boolean ismChannelEnableVibrate() {
            return this.mChannelEnableVibrate;
        }

        public void setmChannelEnableVibrate(boolean mChannelEnableVibrate) {
            this.mChannelEnableVibrate = mChannelEnableVibrate;
        }

        public int getmChannelLockscreenVisibility() {
            return this.mChannelLockscreenVisibility;
        }

        public void setmChannelLockscreenVisibility(int mChannelLockscreenVisibility) {
            this.mChannelLockscreenVisibility = mChannelLockscreenVisibility;
        }

        public int getmBigImage() {
            return this.mBigImage;
        }

        public void setmBigImage(int mBigImage) {
            this.mBigImage = mBigImage;
        }

        public String getmBigContentTitle() {
            return this.mBigContentTitle;
        }

        public void setmBigContentTitle(String mBigContentTitle) {
            this.mBigContentTitle = mBigContentTitle;
        }

        public String getmSummaryText() {
            return this.mSummaryText;
        }

        public void setmSummaryText(String mSummaryText) {
            this.mSummaryText = mSummaryText;
        }

        public int getvId() {
            return this.vId;
        }
    }
}
