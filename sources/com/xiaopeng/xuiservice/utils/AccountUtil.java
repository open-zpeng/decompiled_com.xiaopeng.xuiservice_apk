package com.xiaopeng.xuiservice.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.ActivityThread;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes5.dex */
public class AccountUtil {
    private static AccountManager mAccountManager;
    private AccountsUpdateListener mAccountsUpdateListener;
    private static final String TAG = AccountUtil.class.getSimpleName();
    private static boolean inited = false;
    private static final ArrayList<AccountListener> mUpdateListeners = new ArrayList<>();

    /* loaded from: classes5.dex */
    public interface AccountListener {
        void onAccountsUpdated(Account[] accountArr);
    }

    /* loaded from: classes5.dex */
    public static class Constants {
        public static final String ACCOUNT_TYPE_XP_VEHICLE = "com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE";
        public static final String ACCOUNT_USER_TYPE = "user_type";
        public static final String ACTION_CAR_ACCOUNT_STATUS = "com.xiaopeng.xuiservice.ACTION_MITU_DEVICE_BINDSTATUS";
        public static final String AUTH_INFO_EXTRA_APP_ID = "app_id";
        public static final String AUTH_INFO_EXTRA_APP_SECRET = "app_secret";
        public static final String AUTH_TYPE_AUTH_CODE = "com.xiaopeng.accountservice.AUTH_TYPE_AUTH_CODE";
        public static final String AUTH_TYPE_AUTH_OTP = "com.xiaopeng.accountservice.AUTH_TYPE_AUTH_OTP";
        public static final String MESSAGE_MITU_VALID_TIME = "validTime";
        public static final String USER_DATA_EXTRA_AVATAR = "avatar";
        public static final String USER_DATA_EXTRA_MUSIC_VIP = "music_vip";
        public static final String USER_DATA_EXTRA_UID = "uid";
        public static final String USER_DATA_EXTRA_UPDATE = "update";
        public static final String USER_DATA_MITU_DEVICE_BIND_STATUS = "mitu_device_bind_status";
        public static final String USER_DATA_MITU_OPEN_ID = "mitu_openid";
        private static final int USER_TYPE_DRIVER = 4;
        private static final int USER_TYPE_TENANT = 3;
        private static final int USER_TYPE_TOWNER = 1;
        private static final int USER_TYPE_USER = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class AccountsUpdateListener implements OnAccountsUpdateListener {
        private AccountsUpdateListener() {
        }

        @Override // android.accounts.OnAccountsUpdateListener
        public void onAccountsUpdated(Account[] accounts) {
            if (!AccountUtil.mUpdateListeners.isEmpty()) {
                synchronized (AccountUtil.mUpdateListeners) {
                    Iterator it = AccountUtil.mUpdateListeners.iterator();
                    while (it.hasNext()) {
                        AccountListener l = (AccountListener) it.next();
                        l.onAccountsUpdated(accounts);
                    }
                }
                return;
            }
            LogUtil.d(AccountUtil.TAG, "onAccountsUpdated, but no listeners");
        }
    }

    /* loaded from: classes5.dex */
    private static class AccountUtilHolder {
        private static final AccountUtil instance = new AccountUtil();

        private AccountUtilHolder() {
        }
    }

    private AccountUtil() {
        this.mAccountsUpdateListener = new AccountsUpdateListener();
        mAccountManager = AccountManager.get(ActivityThread.currentActivityThread().getApplication());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: selfInit */
    public synchronized void lambda$init$0$AccountUtil() {
        if (inited) {
            return;
        }
        inited = true;
        mAccountManager.addOnAccountsUpdatedListener(this.mAccountsUpdateListener, XuiWorkHandler.getInstance(), true, new String[]{"com.xiaopeng.accountservice.ACCOUNT_TYPE_XP_VEHICLE"});
    }

    public static AccountUtil getInstance() {
        return AccountUtilHolder.instance;
    }

    public void init() {
        BroadcastManager.getInstance().addBootCompleteTask(new Runnable() { // from class: com.xiaopeng.xuiservice.utils.-$$Lambda$AccountUtil$Cj3AS_sih8Tt-mVFXI1UWN6s-bA
            @Override // java.lang.Runnable
            public final void run() {
                AccountUtil.this.lambda$init$0$AccountUtil();
            }
        });
        if (BroadcastManager.isBootComplete()) {
            lambda$init$0$AccountUtil();
        }
    }

    public Account getAccount(String type) {
        Account account = null;
        Account[] accounts = mAccountManager.getAccountsByType(type);
        if (accounts.length > 0) {
            account = accounts[0];
        }
        String str = TAG;
        LogUtil.d(str, "getAccount=" + account);
        return account;
    }

    public String getUserData(Account account, String key) {
        try {
            String value = mAccountManager.getUserData(account, key);
            return value;
        } catch (Exception e) {
            String str = TAG;
            LogUtil.w(str, "getUserData,e=" + e);
            return null;
        }
    }

    public void addAccountListener(AccountListener listener) {
        String str = TAG;
        LogUtil.i(str, "addAccountListener:" + listener);
        synchronized (mUpdateListeners) {
            mUpdateListeners.add(listener);
        }
    }

    public void removeAccountListener(AccountListener listener) {
        String str = TAG;
        LogUtil.i(str, "removeAccountListener:" + listener);
        synchronized (mUpdateListeners) {
            mUpdateListeners.remove(listener);
        }
    }
}
