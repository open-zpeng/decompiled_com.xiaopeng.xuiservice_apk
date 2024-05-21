package OooO00o.OooO00o.OooO00o.OooO00o;

import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class OooO0o implements Executor {
    @Override // java.util.concurrent.Executor
    public void execute(@NonNull Runnable runnable) {
        if (runnable != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run();
                return;
            } else {
                OooO0OO.f214OooO00o.post(runnable);
                return;
            }
        }
        throw new NullPointerException("Argument 'command' of type Runnable (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
    }
}
