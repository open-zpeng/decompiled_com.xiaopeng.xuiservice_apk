package com.blankj.utilcode.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.Utils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public class UtilsTransActivity extends AppCompatActivity {
    private static final Map<UtilsTransActivity, TransActivityDelegate> CALLBACK_MAP = new HashMap();
    protected static final String EXTRA_DELEGATE = "extra_delegate";

    public static void start(TransActivityDelegate delegate) {
        start(null, null, delegate, UtilsTransActivity.class);
    }

    public static void start(Utils.Consumer<Intent> consumer, TransActivityDelegate delegate) {
        start(null, consumer, delegate, UtilsTransActivity.class);
    }

    public static void start(Activity activity, TransActivityDelegate delegate) {
        start(activity, null, delegate, UtilsTransActivity.class);
    }

    public static void start(Activity activity, Utils.Consumer<Intent> consumer, TransActivityDelegate delegate) {
        start(activity, consumer, delegate, UtilsTransActivity.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void start(Activity activity, Utils.Consumer<Intent> consumer, TransActivityDelegate delegate, Class<?> cls) {
        if (delegate == null) {
            return;
        }
        Intent starter = new Intent(Utils.getApp(), cls);
        starter.putExtra(EXTRA_DELEGATE, delegate);
        if (consumer != null) {
            consumer.accept(starter);
        }
        if (activity == null) {
            starter.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
            Utils.getApp().startActivity(starter);
            return;
        }
        activity.startActivity(starter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        Serializable extra = getIntent().getSerializableExtra(EXTRA_DELEGATE);
        if (!(extra instanceof TransActivityDelegate)) {
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        TransActivityDelegate delegate = (TransActivityDelegate) extra;
        CALLBACK_MAP.put(this, delegate);
        delegate.onCreateBefore(this, savedInstanceState);
        super.onCreate(savedInstanceState);
        delegate.onCreated(this, savedInstanceState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onStarted(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onResumed(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onPaused(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onStopped(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onSaveInstanceState(this, outState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onDestroy(this);
        CALLBACK_MAP.remove(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions == null) {
            throw new NullPointerException("Argument 'permissions' of type String[] (#1 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        if (grantResults == null) {
            throw new NullPointerException("Argument 'grantResults' of type int[] (#2 out of 3, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return;
        }
        callback.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        TransActivityDelegate callback = CALLBACK_MAP.get(this);
        if (callback == null) {
            return super.dispatchTouchEvent(ev);
        }
        if (callback.dispatchTouchEvent(this, ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /* loaded from: classes4.dex */
    public static abstract class TransActivityDelegate implements Serializable {
        public void onCreateBefore(@NonNull UtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onCreated(@NonNull UtilsTransActivity activity, @Nullable Bundle savedInstanceState) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onStarted(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onDestroy(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onResumed(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onPaused(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onStopped(@NonNull UtilsTransActivity activity) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onSaveInstanceState(@NonNull UtilsTransActivity activity, Bundle outState) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onRequestPermissionsResult(@NonNull UtilsTransActivity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (permissions == null) {
                throw new NullPointerException("Argument 'permissions' of type String[] (#2 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            if (grantResults == null) {
                throw new NullPointerException("Argument 'grantResults' of type int[] (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public void onActivityResult(@NonNull UtilsTransActivity activity, int requestCode, int resultCode, Intent data) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
        }

        public boolean dispatchTouchEvent(@NonNull UtilsTransActivity activity, MotionEvent ev) {
            if (activity == null) {
                throw new NullPointerException("Argument 'activity' of type UtilsTransActivity (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
            }
            return false;
        }
    }
}
