package com.xiaopeng.xuiservice.utils;

import android.os.SystemProperties;
import android.util.Pair;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayDeque;
import java.util.Deque;
/* loaded from: classes5.dex */
public class TimingLog {
    private final String mTag;
    private final Deque<Pair<String, Long>> mStartTimes = new ArrayDeque();
    private TimeConfig DEFAULT_CONFIG = new TimeConfig();
    private TimeConfig mConfig = this.DEFAULT_CONFIG;
    private long mThreadId = Thread.currentThread().getId();

    public TimingLog(String tag) {
        this.mTag = tag;
    }

    public void setConfig(TimeConfig config) {
        this.mConfig = config;
    }

    public void startLog(String name) {
    }

    public void endLog() {
    }

    private void assertSameThread() {
        Thread currentThread = Thread.currentThread();
        if (currentThread.getId() != this.mThreadId && this.mConfig.isLogEnable()) {
            LogUtil.w(this.mTag, "TimingLog call start/end not same thread");
        }
    }

    private void logd(String name, long time) {
        if (this.mConfig.isLogEnable()) {
            String str = this.mTag;
            LogUtil.d(str, name + " curtime = " + time + "ms");
        }
    }

    public void logDuration(String name, long timeMs) {
        if (this.mConfig.isTimeoutLogEnable() && timeMs > this.mConfig.getMaxTime()) {
            String str = this.mTag;
            LogUtil.i(str, "May took too long time = " + timeMs + "ms at " + name);
        }
    }

    /* loaded from: classes5.dex */
    public class TimeConfig {
        private boolean mLogEnable;
        private boolean mLogTimeoutEnable;
        private long mMaxTime;

        public TimeConfig() {
            this.mLogEnable = false;
            this.mLogTimeoutEnable = true;
            this.mMaxTime = SystemProperties.getLong("persist.data.timinglog.maxtime", 800L);
        }

        private TimeConfig(Builder builder) {
            this.mLogEnable = false;
            this.mLogTimeoutEnable = true;
            this.mMaxTime = SystemProperties.getLong("persist.data.timinglog.maxtime", 800L);
            this.mLogEnable = builder.mLogEnable;
            this.mLogTimeoutEnable = builder.mLogTimeoutEnable;
        }

        public boolean isLogEnable() {
            return this.mLogEnable;
        }

        public boolean isTimeoutLogEnable() {
            return this.mLogTimeoutEnable;
        }

        public long getMaxTime() {
            return this.mMaxTime;
        }

        /* loaded from: classes5.dex */
        public final class Builder {
            private boolean mLogEnable;
            private boolean mLogTimeoutEnable;

            public Builder() {
            }

            public Builder mLogEnable(boolean val) {
                this.mLogEnable = val;
                return this;
            }

            public Builder mLogTimeoutEnable(boolean val) {
                this.mLogTimeoutEnable = val;
                return this;
            }

            public TimeConfig build() {
                return new TimeConfig(this);
            }
        }
    }
}
