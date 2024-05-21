package com.loostone.libtuning.component.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.loostone.libtuning.component.provider.util.AppInfoProvider;
import com.loostone.libtuning.component.provider.util.AudioEffectProvider;
import com.loostone.libtuning.component.provider.util.MicInfoProvider;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000  2\u00020\u0001:\u0001 B\u0007¢\u0006\u0004\b\u001e\u0010\u001fJ\u000f\u0010\u0003\u001a\u00020\u0002H\u0016¢\u0006\u0004\b\u0003\u0010\u0004JQ\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0006\u001a\u00020\u00052\u0010\u0010\t\u001a\f\u0012\u0006\b\u0001\u0012\u00020\b\u0018\u00010\u00072\b\u0010\n\u001a\u0004\u0018\u00010\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\b\u0018\u00010\u00072\b\u0010\f\u001a\u0004\u0018\u00010\bH\u0016¢\u0006\u0004\b\u000e\u0010\u000fJ\u0019\u0010\u0010\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u0010\u0010\u0011J#\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u0012H\u0016¢\u0006\u0004\b\u0014\u0010\u0015J3\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\n\u001a\u0004\u0018\u00010\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\b\u0018\u00010\u0007H\u0016¢\u0006\u0004\b\u0017\u0010\u0018J=\u0010\u0019\u001a\u00020\u00162\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u00122\b\u0010\n\u001a\u0004\u0018\u00010\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\b\u0001\u0012\u00020\b\u0018\u00010\u0007H\u0016¢\u0006\u0004\b\u0019\u0010\u001aR\u0016\u0010\u001c\u001a\u00020\u001b8\u0002@\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001c\u0010\u001d¨\u0006!"}, d2 = {"Lcom/loostone/libtuning/component/provider/TuningProvider;", "Landroid/content/ContentProvider;", "", "onCreate", "()Z", "Landroid/net/Uri;", "uri", "", "", "projection", "selection", "selectionArgs", "sortOrder", "Landroid/database/Cursor;", "query", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "getType", "(Landroid/net/Uri;)Ljava/lang/String;", "Landroid/content/ContentValues;", "values", "insert", "(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;", "", "delete", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "Landroid/content/UriMatcher;", "sUriMatcher", "Landroid/content/UriMatcher;", "<init>", "()V", "Companion", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class TuningProvider extends ContentProvider {
    public static final int APP_INFO_ID = 3;
    @NotNull
    public static final String AUTHORITY = "com.loostone.tuning";
    @NotNull
    public static final Companion Companion = new Companion(null);
    public static final int EFFECT_ID = 1;
    @NotNull
    public static final String KEY = "key";
    public static final int MIC_INFO_ID = 2;
    @NotNull
    private static final String TAG = "TuningProvider";
    @NotNull
    public static final String URI_APP_INFO = "content://com.loostone.tuning/appInfo";
    @NotNull
    public static final String URI_AUDIO_EFFECT = "content://com.loostone.tuning/effect";
    @NotNull
    public static final String URI_MIC_INFO = "content://com.loostone.tuning/micInfo";
    @NotNull
    public static final String VALUE = "value";
    @NotNull
    private final UriMatcher sUriMatcher;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0003\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0016\u0010\u0006\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007R\u0016\u0010\b\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0016\u0010\t\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0007R\u0016\u0010\n\u001a\u00020\u00028\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0016\u0010\u000b\u001a\u00020\u00058\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0007R\u0016\u0010\f\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\u0007R\u0016\u0010\r\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\r\u0010\u0007R\u0016\u0010\u000e\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0007R\u0016\u0010\u000f\u001a\u00020\u00058\u0006@\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000f\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/loostone/libtuning/component/provider/TuningProvider$Companion;", "", "", "APP_INFO_ID", "I", "", "AUTHORITY", "Ljava/lang/String;", "EFFECT_ID", "KEY", "MIC_INFO_ID", "TAG", "URI_APP_INFO", "URI_AUDIO_EFFECT", "URI_MIC_INFO", "VALUE", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes4.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public TuningProvider() {
        UriMatcher uriMatcher = new UriMatcher(-1);
        uriMatcher.addURI(AUTHORITY, "effect", 1);
        uriMatcher.addURI(AUTHORITY, "micInfo", 2);
        uriMatcher.addURI(AUTHORITY, "appInfo", 3);
        this.sUriMatcher = uriMatcher;
    }

    @Override // android.content.ContentProvider
    public int delete(@NotNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return 0;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public String getType(@NotNull Uri uri) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return null;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Uri insert(@NotNull Uri uri, @Nullable ContentValues contentValues) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Cursor query(@NotNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        if (strArr != null) {
            if (!(strArr.length == 0)) {
                try {
                    int parseInt = Integer.parseInt(strArr[0]);
                    int match = this.sUriMatcher.match(uri);
                    if (match != 1) {
                        if (match != 2) {
                            if (match != 3) {
                                return null;
                            }
                            return AppInfoProvider.INSTANCE.getValue(parseInt);
                        }
                        return MicInfoProvider.INSTANCE.getValue(parseInt);
                    }
                    return AudioEffectProvider.INSTANCE.getValue(parseInt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(@NotNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        if (contentValues == null) {
            return -2;
        }
        try {
            Integer key = contentValues.getAsInteger("key");
            Integer asInteger = contentValues.getAsInteger("value");
            int intValue = asInteger == null ? 0 : asInteger.intValue();
            int match = this.sUriMatcher.match(uri);
            if (match == 1) {
                AudioEffectProvider audioEffectProvider = AudioEffectProvider.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(key, "key");
                return audioEffectProvider.setValue(key.intValue(), intValue);
            } else if (match != 2) {
                return -1;
            } else {
                MicInfoProvider micInfoProvider = MicInfoProvider.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(key, "key");
                return micInfoProvider.setValue(key.intValue(), intValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
