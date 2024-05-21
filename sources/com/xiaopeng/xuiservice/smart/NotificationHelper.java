package com.xiaopeng.xuiservice.smart;

import android.app.ActivityThread;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.R;
/* loaded from: classes5.dex */
public final class NotificationHelper {
    private static final int CARD_TYPE_CAR_EGG = 28;
    private static final String CHANNEL_ID = "CarEgg Service";
    public static final int NOTIFY_ID_CAR_EGG = 257;
    private static final int NOTIFY_ID_INVALID = -1;
    private static final String TAG = NotificationHelper.class.getSimpleName();
    private static final String XP_CAR_EGG_LLU_INTENT = "com.xiaopeng.intent.action.caregg.llu";
    private IntentFilter mIntentFilter;
    private NotificationManager mNotificationMgr;
    private BroadcastReceiver mReceiver;

    /* loaded from: classes5.dex */
    public interface OnNotificationListener {
        void onCancel();

        void onConfirm();
    }

    private NotificationHelper() {
        Context context = ActivityThread.currentActivityThread().getApplication().getApplicationContext();
        this.mNotificationMgr = (NotificationManager) context.getSystemService("notification");
        createNotificationChannel(context);
    }

    public static NotificationHelper getInstance() {
        return InstanceHolder.sInstance;
    }

    private void registerBroadcastIfNeeded() {
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.smart.NotificationHelper.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                }
            };
            this.mIntentFilter = new IntentFilter();
        }
        ActivityThread.currentActivityThread().getApplication().registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = context.getString(R.string.xuimanager_llu_easter_egg_channel_name);
        String description = context.getString(R.string.xuimanager_llu_easter_egg_channel_description);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, 3);
        channel.setDescription(description);
        NotificationManager notificationManager = this.mNotificationMgr;
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showInfoCard(int cardType, int cardId, String ticker, String title, String content, String jumpAction) {
        Context context = ActivityThread.currentActivityThread().getApplication().getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID);
        builder.setTicker(ticker).setSmallIcon(R.drawable.ic_launcher).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(content).setDisplayFlag(32).setClearFlag(1).setAutoCancel(true);
        builder.getExtras().putInt("cardType", cardType);
        builder.getExtras().putString("cardJumpAction", jumpAction);
        this.mNotificationMgr.notify(cardId, builder.build());
    }

    public void cancelInfoflowCard(int id) {
        String str = TAG;
        LogUtil.d(str, "cancelInfoflowCard id=" + id);
        this.mNotificationMgr.cancel(id);
    }

    public void showEggInfoCard() {
        showInfoCard(28, 257, ActivityThread.currentActivityThread().getApplication().getApplicationContext().getString(R.string.xuimanager_llu_easter_egg_channel_name), ActivityThread.currentActivityThread().getApplication().getApplicationContext().getString(R.string.xuimanager_llu_easter_egg_title), ActivityThread.currentActivityThread().getApplication().getApplicationContext().getString(R.string.xuimanager_llu_easter_egg_content), XP_CAR_EGG_LLU_INTENT);
    }

    public void dismissEggInfoCard() {
        cancelInfoflowCard(257);
    }

    /* loaded from: classes5.dex */
    private static class InstanceHolder {
        private static final NotificationHelper sInstance = new NotificationHelper();

        private InstanceHolder() {
        }
    }
}
