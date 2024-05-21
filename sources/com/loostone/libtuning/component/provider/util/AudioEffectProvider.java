package com.loostone.libtuning.component.provider.util;

import OooO00o.OooO0O0.OooO0O0.OooO0O0;
import android.util.Log;
import com.loostone.libtuning.PuremicTuning;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b5\n\u0002\u0010\u000e\n\u0002\b \bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\bk\u0010lJ\u0015\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u001d\u0010\b\u001a\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002¢\u0006\u0004\b\b\u0010\tR\u0016\u0010\n\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u000bR\u0016\u0010\r\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u000bR\u0016\u0010\u000e\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u000bR\u0016\u0010\u000f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000f\u0010\u000bR\u0016\u0010\u0010\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0010\u0010\u000bR\u0016\u0010\u0011\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u000bR\u0016\u0010\u0012\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0012\u0010\u000bR\u0016\u0010\u0013\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0013\u0010\u000bR\u0016\u0010\u0014\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u000bR\u0016\u0010\u0015\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0015\u0010\u000bR\u0016\u0010\u0016\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0016\u0010\u000bR\u001d\u0010\u001c\u001a\u00020\u00178B@\u0002X\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u0016\u0010\u001d\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001d\u0010\u000bR\u0016\u0010\u001e\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001e\u0010\u000bR\u0016\u0010\u001f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u001f\u0010\u000bR\u0016\u0010 \u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b \u0010\u000bR\u0016\u0010!\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b!\u0010\u000bR\u0016\u0010\"\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\"\u0010\u000bR\u0016\u0010#\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b#\u0010\u000bR\u0016\u0010$\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b$\u0010\u000bR\u0016\u0010%\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b%\u0010\u000bR\u0016\u0010&\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b&\u0010\u000bR\u0016\u0010'\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b'\u0010\u000bR\u0016\u0010(\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b(\u0010\u000bR\u0016\u0010)\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b)\u0010\u000bR\u0016\u0010*\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b*\u0010\u000bR\u0016\u0010+\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b+\u0010\u000bR\u0016\u0010,\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b,\u0010\u000bR\u0016\u0010-\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b-\u0010\u000bR\u0016\u0010.\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b.\u0010\u000bR\u0016\u0010/\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b/\u0010\u000bR\u0016\u00100\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b0\u0010\u000bR\u0016\u00101\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b1\u0010\u000bR\u0016\u00102\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b2\u0010\u000bR\u0016\u00103\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b3\u0010\u000bR\u0016\u00104\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b4\u0010\u000bR\u0016\u00105\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b5\u0010\u000bR\u0016\u00106\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b6\u0010\u000bR\u0016\u00107\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b7\u0010\u000bR\u0016\u00108\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b8\u0010\u000bR\u0016\u00109\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b9\u0010\u000bR\u0016\u0010:\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b:\u0010\u000bR\u0016\u0010;\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b;\u0010\u000bR\u0016\u0010<\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b<\u0010\u000bR\u0016\u0010=\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b=\u0010\u000bR\u0016\u0010>\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b>\u0010\u000bR\u0016\u0010?\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b?\u0010\u000bR\u0016\u0010@\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b@\u0010\u000bR\u0016\u0010A\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bA\u0010\u000bR\u0016\u0010B\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bB\u0010\u000bR\u0016\u0010C\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bC\u0010\u000bR\u0016\u0010D\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bD\u0010\u000bR\u0016\u0010E\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bE\u0010\u000bR\u0016\u0010F\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bF\u0010\u000bR\u0016\u0010G\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bG\u0010\u000bR\u0016\u0010H\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bH\u0010\u000bR\u0016\u0010I\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bI\u0010\u000bR\u0016\u0010J\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bJ\u0010\u000bR\u0016\u0010K\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bK\u0010\u000bR\u0016\u0010L\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bL\u0010\u000bR\u0016\u0010N\u001a\u00020M8\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bN\u0010OR\u0016\u0010P\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bP\u0010\u000bR\u0016\u0010Q\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bQ\u0010\u000bR\u0016\u0010R\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bR\u0010\u000bR\u0016\u0010S\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bS\u0010\u000bR\u0016\u0010T\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bT\u0010\u000bR\u0016\u0010U\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bU\u0010\u000bR\u0016\u0010V\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bV\u0010\u000bR\u0016\u0010W\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bW\u0010\u000bR\u0016\u0010X\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bX\u0010\u000bR\u0016\u0010Y\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bY\u0010\u000bR\u0016\u0010Z\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bZ\u0010\u000bR\u0016\u0010[\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b[\u0010\u000bR\u0016\u0010\\\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b\\\u0010\u000bR\u0016\u0010]\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b]\u0010\u000bR\u0016\u0010^\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b^\u0010\u000bR\u0016\u0010_\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b_\u0010\u000bR\u0016\u0010`\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\b`\u0010\u000bR\u0016\u0010a\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\ba\u0010\u000bR\u0016\u0010b\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bb\u0010\u000bR\u0016\u0010c\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bc\u0010\u000bR\u0016\u0010d\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bd\u0010\u000bR\u0016\u0010e\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\be\u0010\u000bR\u0016\u0010f\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bf\u0010\u000bR\u0016\u0010g\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bg\u0010\u000bR\u0016\u0010h\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bh\u0010\u000bR\u0016\u0010i\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bi\u0010\u000bR\u0016\u0010j\u001a\u00020\u00028\u0002@\u0002X\u0082T¢\u0006\u0006\n\u0004\bj\u0010\u000b¨\u0006m"}, d2 = {"Lcom/loostone/libtuning/component/provider/util/AudioEffectProvider;", "", "", "keyIndex", "Landroid/database/Cursor;", "getValue", "(I)Landroid/database/Cursor;", "value", "setValue", "(II)I", "VOLUME_UP", "I", "SET_AEF2_REVERB_MODE", "SET_SYSTEM_VOLUME", "GET_AEF2_EQ1", "GET_AEF2_REVERB_MODE", "GET_MAX_AEF2_REVERB_VOLUME", "SET_AEF2_ECHO_ENABLE", "SET_AEF2_REVERB_TIME", "GET_AEF2_EQ7", "GET_AEF2_ECHO_TIME", "GET_AEF2_EQ6_FREQUENCY", "SET_AEF2_ECHO_GAIN", "Lcom/loostone/libtuning/inf/extern/IVolumeAdjustment;", "volumeAdjustment$delegate", "Lkotlin/Lazy;", "getVolumeAdjustment", "()Lcom/loostone/libtuning/inf/extern/IVolumeAdjustment;", "volumeAdjustment", "GET_AEF2_EQ5_FREQUENCY", "GET_SYSTEM_VOLUME", "SET_AEF2_ECHO_TYPE", "GET_AEF2_EQ5", "GET_AEF2_ECHO_FEEDBACK", "TYPE_SET_REVERB_MODE", "GET_MUSIC_VOLUME", "GET_AEF2_EQ4", "SET_AEF2_EQ2_FREQUENCY", "SET_AEF2_EQ9", "GET_AEF2_EQ9", "SET_AEF2_REVERB_VOLUME", "GET_MIC_VOLUME", "GET_AEF1_REVERB_ENABLE", "GET_AEF1_REVERB_OUT_GAIN", "SET_MIC_VOLUME", "GET_MAX_MIC_VOLUME", "TYPE_SET_ECHO", "SET_AEF2_EQ3_FREQUENCY", "TYPE_GET_MIC", "SET_AEF2_EQ4_FREQUENCY", "TYPE_GET_ECHO", "TYPE_SET_MIC", "GET_AEF2_REVERB_ENABLE", "GET_MIC_VOLUME_ENABLE", "SET_AEF2_REVERB_ENABLE", "GET_AEF2_EQ4_FREQUENCY", "SET_AEF2_EQ6", "SET_AEF2_VOICE_MODE", "SET_AEF2_EQ1_FREQUENCY", "SET_AEF2_EQ4", "GET_AEF2_EQ8_FREQUENCY", "GET_AEF2_REVERB_VOLUME", "SET_AEF2_ECHO_FEEDBACK", "SET_AEF2_EQ5", "GET_AEF2_ECHO_GAIN", "GET_AEF2_ECHO_TYPE", "GET_AEF2_EQ3", "GET_AEF2_EQ1_FREQUENCY", "SET_AEF1_REVERB_OUT_GAIN", "GET_AEF2_EQ9_FREQUENCY", "GET_AEF2_REVERB_TIME", "TYPE_SET_VOICE_MODE", "GET_AEF2_EQ2", "SET_MIC_VOLUME_ENABLE", "SET_AEF2_EQ7", "GET_AEF2_EQ6", "SET_AEF2_EQ2", "", "TAG", "Ljava/lang/String;", "SET_AEF2_EQ6_FREQUENCY", "GET_AEF2_VOICE_MODE", "SET_AEF1_REVERB_ENABLE", "SET_AEF2_EQ3", "TYPE_SUPPORT_ADVANCE", "SET_AEF2_EQ5_FREQUENCY", "SET_MUSIC_VOLUME", "GET_AEF2_EQ_ENABLE", "TYPE_GET_ECHO_MAX", "GET_AEF2_EQ8", "GET_AEF2_EQ3_FREQUENCY", "SET_AEF2_EQ8", "GET_SUPPORT_ADVANCE_EFFECT", "VOLUME_DOWN", "SET_AEF2_EQ7_FREQUENCY", "GET_AEF2_EQ2_FREQUENCY", "SET_AEF2_EQ9_FREQUENCY", "GET_AEF2_EQ7_FREQUENCY", "SET_AEF2_EQ8_FREQUENCY", "SET_AEF2_EQ_ENABLE", "SET_AEF2_EQ1", "GET_AEF2_ECHO_ENABLE", "GET_MAX_AEF1_REVERB_VOLUME", "TYPE_GET_REVERB_MODE", "SET_AEF2_ECHO_TIME", "TYPE_GET_VOICE_MODE", "TYPE_GET_MIC_MAX", "<init>", "()V", "libTuning_CAR_XIAOPENGRelease"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes4.dex */
public final class AudioEffectProvider {
    private static final int GET_AEF1_REVERB_ENABLE = 201;
    private static final int GET_AEF1_REVERB_OUT_GAIN = 203;
    private static final int GET_AEF2_ECHO_ENABLE = 243;
    private static final int GET_AEF2_ECHO_FEEDBACK = 251;
    private static final int GET_AEF2_ECHO_GAIN = 245;
    private static final int GET_AEF2_ECHO_TIME = 249;
    private static final int GET_AEF2_ECHO_TYPE = 247;
    private static final int GET_AEF2_EQ1 = 215;
    private static final int GET_AEF2_EQ1_FREQUENCY = 233;
    private static final int GET_AEF2_EQ2 = 216;
    private static final int GET_AEF2_EQ2_FREQUENCY = 234;
    private static final int GET_AEF2_EQ3 = 217;
    private static final int GET_AEF2_EQ3_FREQUENCY = 235;
    private static final int GET_AEF2_EQ4 = 218;
    private static final int GET_AEF2_EQ4_FREQUENCY = 236;
    private static final int GET_AEF2_EQ5 = 219;
    private static final int GET_AEF2_EQ5_FREQUENCY = 237;
    private static final int GET_AEF2_EQ6 = 220;
    private static final int GET_AEF2_EQ6_FREQUENCY = 238;
    private static final int GET_AEF2_EQ7 = 221;
    private static final int GET_AEF2_EQ7_FREQUENCY = 239;
    private static final int GET_AEF2_EQ8 = 222;
    private static final int GET_AEF2_EQ8_FREQUENCY = 240;
    private static final int GET_AEF2_EQ9 = 223;
    private static final int GET_AEF2_EQ9_FREQUENCY = 241;
    private static final int GET_AEF2_EQ_ENABLE = 205;
    private static final int GET_AEF2_REVERB_ENABLE = 253;
    private static final int GET_AEF2_REVERB_MODE = 255;
    private static final int GET_AEF2_REVERB_TIME = 259;
    private static final int GET_AEF2_REVERB_VOLUME = 257;
    private static final int GET_AEF2_VOICE_MODE = 261;
    private static final int GET_MAX_AEF1_REVERB_VOLUME = 273;
    private static final int GET_MAX_AEF2_REVERB_VOLUME = 274;
    private static final int GET_MAX_MIC_VOLUME = 272;
    private static final int GET_MIC_VOLUME = 101;
    private static final int GET_MIC_VOLUME_ENABLE = 103;
    private static final int GET_MUSIC_VOLUME = 107;
    private static final int GET_SUPPORT_ADVANCE_EFFECT = 262;
    private static final int GET_SYSTEM_VOLUME = 105;
    private static final int SET_AEF1_REVERB_ENABLE = 200;
    private static final int SET_AEF1_REVERB_OUT_GAIN = 202;
    private static final int SET_AEF2_ECHO_ENABLE = 242;
    private static final int SET_AEF2_ECHO_FEEDBACK = 250;
    private static final int SET_AEF2_ECHO_GAIN = 244;
    private static final int SET_AEF2_ECHO_TIME = 248;
    private static final int SET_AEF2_ECHO_TYPE = 246;
    private static final int SET_AEF2_EQ1 = 206;
    private static final int SET_AEF2_EQ1_FREQUENCY = 224;
    private static final int SET_AEF2_EQ2 = 207;
    private static final int SET_AEF2_EQ2_FREQUENCY = 225;
    private static final int SET_AEF2_EQ3 = 208;
    private static final int SET_AEF2_EQ3_FREQUENCY = 226;
    private static final int SET_AEF2_EQ4 = 209;
    private static final int SET_AEF2_EQ4_FREQUENCY = 227;
    private static final int SET_AEF2_EQ5 = 210;
    private static final int SET_AEF2_EQ5_FREQUENCY = 228;
    private static final int SET_AEF2_EQ6 = 211;
    private static final int SET_AEF2_EQ6_FREQUENCY = 229;
    private static final int SET_AEF2_EQ7 = 212;
    private static final int SET_AEF2_EQ7_FREQUENCY = 230;
    private static final int SET_AEF2_EQ8 = 213;
    private static final int SET_AEF2_EQ8_FREQUENCY = 231;
    private static final int SET_AEF2_EQ9 = 214;
    private static final int SET_AEF2_EQ9_FREQUENCY = 232;
    private static final int SET_AEF2_EQ_ENABLE = 204;
    private static final int SET_AEF2_REVERB_ENABLE = 252;
    private static final int SET_AEF2_REVERB_MODE = 254;
    private static final int SET_AEF2_REVERB_TIME = 258;
    private static final int SET_AEF2_REVERB_VOLUME = 256;
    private static final int SET_AEF2_VOICE_MODE = 260;
    private static final int SET_MIC_VOLUME = 100;
    private static final int SET_MIC_VOLUME_ENABLE = 102;
    private static final int SET_MUSIC_VOLUME = 106;
    private static final int SET_SYSTEM_VOLUME = 104;
    @NotNull
    private static final String TAG = "AudioEffectProvider";
    private static final int TYPE_GET_ECHO = 6;
    private static final int TYPE_GET_ECHO_MAX = 4;
    private static final int TYPE_GET_MIC = 5;
    private static final int TYPE_GET_MIC_MAX = 3;
    private static final int TYPE_GET_REVERB_MODE = 8;
    private static final int TYPE_GET_VOICE_MODE = 10;
    private static final int TYPE_SET_ECHO = 2;
    private static final int TYPE_SET_MIC = 1;
    private static final int TYPE_SET_REVERB_MODE = 7;
    private static final int TYPE_SET_VOICE_MODE = 9;
    private static final int TYPE_SUPPORT_ADVANCE = 11;
    private static final int VOLUME_DOWN = 271;
    private static final int VOLUME_UP = 270;
    @NotNull
    public static final AudioEffectProvider INSTANCE = new AudioEffectProvider();
    @NotNull
    private static final Lazy volumeAdjustment$delegate = LazyKt.lazy(new Function0<IVolumeAdjustment>() { // from class: com.loostone.libtuning.component.provider.util.AudioEffectProvider$volumeAdjustment$2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        public final IVolumeAdjustment invoke() {
            return PuremicTuning.Companion.getInstance().getVolumeAdjustment();
        }
    });

    private AudioEffectProvider() {
    }

    private final IVolumeAdjustment getVolumeAdjustment() {
        return (IVolumeAdjustment) volumeAdjustment$delegate.getValue();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0063, code lost:
        if (getVolumeAdjustment().isAef2ReverbEnable() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0097, code lost:
        if (getVolumeAdjustment().isAef2EchoEnable() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00a3, code lost:
        if (getVolumeAdjustment().isAef2EQEnable() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00af, code lost:
        if (getVolumeAdjustment().isAef1ReverbEnable() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00cf, code lost:
        if (getVolumeAdjustment().isMicVolumeEnable() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0290, code lost:
        if (r12 >= 4) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0294, code lost:
        r11 = 1;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x02d2  */
    @org.jetbrains.annotations.NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.database.Cursor getValue(int r23) {
        /*
            Method dump skipped, instructions count: 868
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loostone.libtuning.component.provider.util.AudioEffectProvider.getValue(int):android.database.Cursor");
    }

    public final int setValue(int i, int i2) {
        String msg = "set  keyIndex: " + i + " value: " + i2;
        Intrinsics.checkNotNullParameter(TAG, Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", TAG + " -> " + msg);
        }
        if (i != 1) {
            if (i != 2) {
                if (i == VOLUME_UP) {
                    getVolumeAdjustment().volumeUp(i2);
                } else if (i != VOLUME_DOWN) {
                    switch (i) {
                        case 7:
                        case SET_AEF2_REVERB_MODE /* 254 */:
                            getVolumeAdjustment().setAef2ReverbMode(i2);
                            break;
                        case 9:
                        case SET_AEF2_VOICE_MODE /* 260 */:
                            getVolumeAdjustment().setAef2VoiceMode(i2);
                            break;
                        case 100:
                            break;
                        case 102:
                            getVolumeAdjustment().setMicVolumeEnable(i2 == 1);
                            break;
                        case 104:
                            getVolumeAdjustment().setSystemVolume(i2);
                            break;
                        case 106:
                            getVolumeAdjustment().setMusicVolume(i2);
                            break;
                        case 200:
                            getVolumeAdjustment().setAef1ReverbEnable(i2 == 1);
                            break;
                        case 202:
                            break;
                        case 204:
                            getVolumeAdjustment().setAef2EQEnable(i2 == 1);
                            break;
                        case SET_AEF2_ECHO_ENABLE /* 242 */:
                            getVolumeAdjustment().setAef2EchoEnable(i2 == 1);
                            break;
                        case SET_AEF2_ECHO_GAIN /* 244 */:
                            getVolumeAdjustment().setAef2EchoGain(i2);
                            break;
                        case SET_AEF2_ECHO_TYPE /* 246 */:
                            getVolumeAdjustment().setAef2EchoType(i2);
                            break;
                        case SET_AEF2_ECHO_TIME /* 248 */:
                            getVolumeAdjustment().setAef2EchoTime(i2);
                            break;
                        case 250:
                            getVolumeAdjustment().setAef2EchoFeedBack(i2);
                            break;
                        case SET_AEF2_REVERB_ENABLE /* 252 */:
                            getVolumeAdjustment().setAef2ReverbEnable(i2 == 1);
                            break;
                        case 256:
                            getVolumeAdjustment().setAef2ReverbVolume(i2);
                            break;
                        case 258:
                            getVolumeAdjustment().setAef2ReverbTime(i2);
                            break;
                        default:
                            switch (i) {
                                case 206:
                                    getVolumeAdjustment().setAef2EQ1(i2);
                                    break;
                                case 207:
                                    getVolumeAdjustment().setAef2EQ2(i2);
                                    break;
                                case 208:
                                    getVolumeAdjustment().setAef2EQ3(i2);
                                    break;
                                case 209:
                                    getVolumeAdjustment().setAef2EQ4(i2);
                                    break;
                                case 210:
                                    getVolumeAdjustment().setAef2EQ5(i2);
                                    break;
                                case 211:
                                    getVolumeAdjustment().setAef2EQ6(i2);
                                    break;
                                case 212:
                                    getVolumeAdjustment().setAef2EQ7(i2);
                                    break;
                                case 213:
                                    getVolumeAdjustment().setAef2EQ8(i2);
                                    break;
                                case SET_AEF2_EQ9 /* 214 */:
                                    getVolumeAdjustment().setAef2EQ9(i2);
                                    break;
                                default:
                                    switch (i) {
                                        case SET_AEF2_EQ1_FREQUENCY /* 224 */:
                                            getVolumeAdjustment().setAef2EQ1Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ2_FREQUENCY /* 225 */:
                                            getVolumeAdjustment().setAef2EQ2Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ3_FREQUENCY /* 226 */:
                                            getVolumeAdjustment().setAef2EQ3Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ4_FREQUENCY /* 227 */:
                                            getVolumeAdjustment().setAef2EQ4Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ5_FREQUENCY /* 228 */:
                                            getVolumeAdjustment().setAef2EQ5Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ6_FREQUENCY /* 229 */:
                                            getVolumeAdjustment().setAef2EQ6Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ7_FREQUENCY /* 230 */:
                                            getVolumeAdjustment().setAef2EQ7Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ8_FREQUENCY /* 231 */:
                                            getVolumeAdjustment().setAef2EQ8Frequency(i2);
                                            break;
                                        case SET_AEF2_EQ9_FREQUENCY /* 232 */:
                                            getVolumeAdjustment().setAef2EQ9Frequency(i2);
                                            break;
                                    }
                            }
                    }
                } else {
                    getVolumeAdjustment().volumeDown(i2);
                }
                return 1;
            }
            getVolumeAdjustment().setAef1ReverbOutGain(i2);
            return 1;
        }
        getVolumeAdjustment().setMicVolume(i2);
        return 1;
    }
}
