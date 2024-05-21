package com.blankj.utilcode.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.blankj.utilcode.util.Utils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
/* loaded from: classes4.dex */
public class NotificationUtils {
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    public @interface Importance {
    }

    public static boolean areNotificationsEnabled() {
        return NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled();
    }

    public static void notify(int id, Utils.Consumer<NotificationCompat.Builder> consumer) {
        notify(null, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    public static void notify(String tag, int id, Utils.Consumer<NotificationCompat.Builder> consumer) {
        notify(tag, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    public static void notify(int id, ChannelConfig channelConfig, Utils.Consumer<NotificationCompat.Builder> consumer) {
        notify(null, id, channelConfig, consumer);
    }

    public static void notify(String tag, int id, ChannelConfig channelConfig, Utils.Consumer<NotificationCompat.Builder> consumer) {
        NotificationManagerCompat.from(Utils.getApp()).notify(tag, id, getNotification(channelConfig, consumer));
    }

    public static Notification getNotification(ChannelConfig channelConfig, Utils.Consumer<NotificationCompat.Builder> consumer) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = (NotificationManager) Utils.getApp().getSystemService("notification");
            nm.createNotificationChannel(channelConfig.getNotificationChannel());
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp());
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId(channelConfig.mNotificationChannel.getId());
        }
        if (consumer != null) {
            consumer.accept(builder);
        }
        return builder.build();
    }

    public static void cancel(String tag, int id) {
        NotificationManagerCompat.from(Utils.getApp()).cancel(tag, id);
    }

    public static void cancel(int id) {
        NotificationManagerCompat.from(Utils.getApp()).cancel(id);
    }

    public static void cancelAll() {
        NotificationManagerCompat.from(Utils.getApp()).cancelAll();
    }

    @RequiresPermission("android.permission.EXPAND_STATUS_BAR")
    public static void setNotificationBarVisibility(boolean isVisible) {
        String methodName = isVisible ? Build.VERSION.SDK_INT <= 16 ? "expand" : "expandNotificationsPanel" : Build.VERSION.SDK_INT <= 16 ? "collapse" : "collapsePanels";
        invokePanels(methodName);
    }

    private static void invokePanels(String methodName) {
        try {
            Object service = Utils.getApp().getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName, new Class[0]);
            expand.invoke(service, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes4.dex */
    public static class ChannelConfig {
        public static final ChannelConfig DEFAULT_CHANNEL_CONFIG = new ChannelConfig(Utils.getApp().getPackageName(), Utils.getApp().getPackageName(), 3);
        private NotificationChannel mNotificationChannel;

        public ChannelConfig(String id, CharSequence name, int importance) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel = new NotificationChannel(id, name, importance);
            }
        }

        public NotificationChannel getNotificationChannel() {
            return this.mNotificationChannel;
        }

        public ChannelConfig setBypassDnd(boolean bypassDnd) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setBypassDnd(bypassDnd);
            }
            return this;
        }

        public ChannelConfig setDescription(String description) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setDescription(description);
            }
            return this;
        }

        public ChannelConfig setGroup(String groupId) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setGroup(groupId);
            }
            return this;
        }

        public ChannelConfig setImportance(int importance) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setImportance(importance);
            }
            return this;
        }

        public ChannelConfig setLightColor(int argb) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setLightColor(argb);
            }
            return this;
        }

        public ChannelConfig setLockscreenVisibility(int lockscreenVisibility) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setLockscreenVisibility(lockscreenVisibility);
            }
            return this;
        }

        public ChannelConfig setName(CharSequence name) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setName(name);
            }
            return this;
        }

        public ChannelConfig setShowBadge(boolean showBadge) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setShowBadge(showBadge);
            }
            return this;
        }

        public ChannelConfig setSound(Uri sound, AudioAttributes audioAttributes) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setSound(sound, audioAttributes);
            }
            return this;
        }

        public ChannelConfig setVibrationPattern(long[] vibrationPattern) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.mNotificationChannel.setVibrationPattern(vibrationPattern);
            }
            return this;
        }
    }
}
