package OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0OO;
import android.media.AudioManager;
import android.util.Log;
import com.loostone.libbottom.util.JniUtil;
import com.loostone.libtuning.data.config.load.effect.EffectModeData;
import com.loostone.libtuning.data.config.load.effect.EffectModeItem;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class OooO0O0 {
    @Nullable
    public static OooO00o OooO0OO;
    @NotNull

    /* renamed from: OooO00o  reason: collision with root package name */
    public static final OooO0O0 f551OooO00o = new OooO0O0();
    @NotNull
    public static final Lazy OooO0O0 = LazyKt.lazy(C0016OooO0O0.f552OooO00o);
    @NotNull
    public static final List<EffectModeData> OooO0Oo = new ArrayList();

    /* loaded from: classes.dex */
    public interface OooO00o {
        void OooO00o(int i);
    }

    /* renamed from: OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO0O0$OooO0O0  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0016OooO0O0 extends Lambda implements Function0<OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o> {

        /* renamed from: OooO00o  reason: collision with root package name */
        public static final C0016OooO0O0 f552OooO00o = new C0016OooO0O0();

        public C0016OooO0O0() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o invoke() {
            return OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o.f590OooO00o.OooO00o();
        }
    }

    public final void OooO00o() {
        Map<Integer, Integer> effectScene;
        OooO0Oo.clear();
        ArrayList<EffectModeItem> arrayList = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0O.OooO00o.OooO0OO;
        if (arrayList == null) {
            return;
        }
        for (EffectModeItem effectModeItem : arrayList) {
            EffectModeData effectModeData = null;
            for (EffectModeData effectModeData2 : OooO0Oo) {
                int effectId = effectModeData2.getEffectId();
                Integer id = effectModeItem.getId();
                if (id != null && effectId == id.intValue()) {
                    effectModeData = effectModeData2;
                }
            }
            if (effectModeData == null) {
                effectModeData = new EffectModeData();
            }
            String name = effectModeItem.getName();
            if (name != null) {
                String msg = Intrinsics.stringPlus("eff name = ", name);
                Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                    Log.i("LogTuning", "TransportConfig -> " + msg);
                }
            }
            String msg2 = Intrinsics.stringPlus("eff type = ", Integer.valueOf(effectModeItem.getType()));
            Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "TransportConfig -> " + msg2);
            }
            Integer id2 = effectModeItem.getId();
            String msg3 = Intrinsics.stringPlus("eff id = ", id2);
            Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg3, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "TransportConfig -> " + msg3);
            }
            if (id2 != null) {
                effectModeData.setEffectId(id2.intValue());
                if (effectModeItem.getType() == 1) {
                    effectScene = effectModeData.getEffectSurround();
                } else {
                    effectScene = effectModeData.getEffectScene();
                }
                Integer global_eq_on_off = effectModeItem.getGLOBAL_EQ_ON_OFF();
                if (global_eq_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.f227OooO00o.OooO0O0()), Integer.valueOf(global_eq_on_off.intValue()));
                }
                Integer global_eq_type1 = effectModeItem.getGLOBAL_EQ_TYPE1();
                if (global_eq_type1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0O0.OooO0O0()), Integer.valueOf(global_eq_type1.intValue()));
                }
                Integer global_eq_fc1 = effectModeItem.getGLOBAL_EQ_FC1();
                if (global_eq_fc1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0OO.OooO0O0()), Integer.valueOf(global_eq_fc1.intValue()));
                }
                Integer global_eq_gain1 = effectModeItem.getGLOBAL_EQ_GAIN1();
                if (global_eq_gain1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0Oo.OooO0O0()), Integer.valueOf(global_eq_gain1.intValue()));
                }
                Integer global_eq_q1 = effectModeItem.getGLOBAL_EQ_Q1();
                if (global_eq_q1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0o0.OooO0O0()), Integer.valueOf(global_eq_q1.intValue()));
                }
                Integer global_eq_type2 = effectModeItem.getGLOBAL_EQ_TYPE2();
                if (global_eq_type2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0o.OooO0O0()), Integer.valueOf(global_eq_type2.intValue()));
                }
                Integer global_eq_fc2 = effectModeItem.getGLOBAL_EQ_FC2();
                if (global_eq_fc2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0oO.OooO0O0()), Integer.valueOf(global_eq_fc2.intValue()));
                }
                Integer global_eq_gain2 = effectModeItem.getGLOBAL_EQ_GAIN2();
                if (global_eq_gain2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO0oo.OooO0O0()), Integer.valueOf(global_eq_gain2.intValue()));
                }
                Integer global_eq_q2 = effectModeItem.getGLOBAL_EQ_Q2();
                if (global_eq_q2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooO.OooO0O0()), Integer.valueOf(global_eq_q2.intValue()));
                }
                Integer global_eq_type3 = effectModeItem.getGLOBAL_EQ_TYPE3();
                if (global_eq_type3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOO0.OooO0O0()), Integer.valueOf(global_eq_type3.intValue()));
                }
                Integer global_eq_fc3 = effectModeItem.getGLOBAL_EQ_FC3();
                if (global_eq_fc3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOO0O.OooO0O0()), Integer.valueOf(global_eq_fc3.intValue()));
                }
                Integer global_eq_gain3 = effectModeItem.getGLOBAL_EQ_GAIN3();
                if (global_eq_gain3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOO0o.OooO0O0()), Integer.valueOf(global_eq_gain3.intValue()));
                }
                Integer global_eq_q3 = effectModeItem.getGLOBAL_EQ_Q3();
                if (global_eq_q3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOO0.OooO0O0()), Integer.valueOf(global_eq_q3.intValue()));
                }
                Integer global_eq_type4 = effectModeItem.getGLOBAL_EQ_TYPE4();
                if (global_eq_type4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOO.OooO0O0()), Integer.valueOf(global_eq_type4.intValue()));
                }
                Integer global_eq_fc4 = effectModeItem.getGLOBAL_EQ_FC4();
                if (global_eq_fc4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOOO.OooO0O0()), Integer.valueOf(global_eq_fc4.intValue()));
                }
                Integer global_eq_gain4 = effectModeItem.getGLOBAL_EQ_GAIN4();
                if (global_eq_gain4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOOo.OooO0O0()), Integer.valueOf(global_eq_gain4.intValue()));
                }
                Integer global_eq_q4 = effectModeItem.getGLOBAL_EQ_Q4();
                if (global_eq_q4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOo0.OooO0O0()), Integer.valueOf(global_eq_q4.intValue()));
                }
                Integer global_eq_type5 = effectModeItem.getGLOBAL_EQ_TYPE5();
                if (global_eq_type5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOo.OooO0O0()), Integer.valueOf(global_eq_type5.intValue()));
                }
                Integer global_eq_fc5 = effectModeItem.getGLOBAL_EQ_FC5();
                if (global_eq_fc5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOOoo.OooO0O0()), Integer.valueOf(global_eq_fc5.intValue()));
                }
                Integer global_eq_gain5 = effectModeItem.getGLOBAL_EQ_GAIN5();
                if (global_eq_gain5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOo00.OooO0O0()), Integer.valueOf(global_eq_gain5.intValue()));
                }
                Integer global_eq_q5 = effectModeItem.getGLOBAL_EQ_Q5();
                if (global_eq_q5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOo0.OooO0O0()), Integer.valueOf(global_eq_q5.intValue()));
                }
                Integer global_eq_type6 = effectModeItem.getGLOBAL_EQ_TYPE6();
                if (global_eq_type6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOo0O.OooO0O0()), Integer.valueOf(global_eq_type6.intValue()));
                }
                Integer global_eq_fc6 = effectModeItem.getGLOBAL_EQ_FC6();
                if (global_eq_fc6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOo0o.OooO0O0()), Integer.valueOf(global_eq_fc6.intValue()));
                }
                Integer global_eq_gain6 = effectModeItem.getGLOBAL_EQ_GAIN6();
                if (global_eq_gain6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOo.OooO0O0()), Integer.valueOf(global_eq_gain6.intValue()));
                }
                Integer global_eq_q6 = effectModeItem.getGLOBAL_EQ_Q6();
                if (global_eq_q6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOoO0.OooO0O0()), Integer.valueOf(global_eq_q6.intValue()));
                }
                Integer global_eq_type7 = effectModeItem.getGLOBAL_EQ_TYPE7();
                if (global_eq_type7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOoO.OooO0O0()), Integer.valueOf(global_eq_type7.intValue()));
                }
                Integer global_eq_fc7 = effectModeItem.getGLOBAL_EQ_FC7();
                if (global_eq_fc7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOoOO.OooO0O0()), Integer.valueOf(global_eq_fc7.intValue()));
                }
                Integer global_eq_gain7 = effectModeItem.getGLOBAL_EQ_GAIN7();
                if (global_eq_gain7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOoo0.OooO0O0()), Integer.valueOf(global_eq_gain7.intValue()));
                }
                Integer global_eq_q7 = effectModeItem.getGLOBAL_EQ_Q7();
                if (global_eq_q7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOoo.OooO0O0()), Integer.valueOf(global_eq_q7.intValue()));
                }
                Integer global_eq_type8 = effectModeItem.getGLOBAL_EQ_TYPE8();
                if (global_eq_type8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOooO.OooO0O0()), Integer.valueOf(global_eq_type8.intValue()));
                }
                Integer global_eq_fc8 = effectModeItem.getGLOBAL_EQ_FC8();
                if (global_eq_fc8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooOooo.OooO0O0()), Integer.valueOf(global_eq_fc8.intValue()));
                }
                Integer global_eq_gain8 = effectModeItem.getGLOBAL_EQ_GAIN8();
                if (global_eq_gain8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo000.OooO0O0()), Integer.valueOf(global_eq_gain8.intValue()));
                }
                Integer global_eq_q8 = effectModeItem.getGLOBAL_EQ_Q8();
                if (global_eq_q8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo00O.OooO0O0()), Integer.valueOf(global_eq_q8.intValue()));
                }
                Integer global_eq_type9 = effectModeItem.getGLOBAL_EQ_TYPE9();
                if (global_eq_type9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo00o.OooO0O0()), Integer.valueOf(global_eq_type9.intValue()));
                }
                Integer global_eq_fc9 = effectModeItem.getGLOBAL_EQ_FC9();
                if (global_eq_fc9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0.OooO0O0()), Integer.valueOf(global_eq_fc9.intValue()));
                }
                Integer global_eq_gain9 = effectModeItem.getGLOBAL_EQ_GAIN9();
                if (global_eq_gain9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0O0.OooO0O0()), Integer.valueOf(global_eq_gain9.intValue()));
                }
                Integer global_eq_q9 = effectModeItem.getGLOBAL_EQ_Q9();
                if (global_eq_q9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0OO.OooO0O0()), Integer.valueOf(global_eq_q9.intValue()));
                }
                Integer scene_eq_on_off = effectModeItem.getSCENE_EQ_ON_OFF();
                if (scene_eq_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0o0.OooO0O0()), Integer.valueOf(scene_eq_on_off.intValue()));
                }
                Integer scene_eq_type1 = effectModeItem.getSCENE_EQ_TYPE1();
                if (scene_eq_type1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0o.OooO0O0()), Integer.valueOf(scene_eq_type1.intValue()));
                }
                Integer scene_eq_fc1 = effectModeItem.getSCENE_EQ_FC1();
                if (scene_eq_fc1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0oO.OooO0O0()), Integer.valueOf(scene_eq_fc1.intValue()));
                }
                Integer scene_eq_gain1 = effectModeItem.getSCENE_EQ_GAIN1();
                if (scene_eq_gain1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo0oo.OooO0O0()), Integer.valueOf(scene_eq_gain1.intValue()));
                }
                Integer scene_eq_q1 = effectModeItem.getSCENE_EQ_Q1();
                if (scene_eq_q1 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooo.OooO0O0()), Integer.valueOf(scene_eq_q1.intValue()));
                }
                Integer scene_eq_type2 = effectModeItem.getSCENE_EQ_TYPE2();
                if (scene_eq_type2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooO00.OooO0O0()), Integer.valueOf(scene_eq_type2.intValue()));
                }
                Integer scene_eq_fc2 = effectModeItem.getSCENE_EQ_FC2();
                if (scene_eq_fc2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooO0.OooO0O0()), Integer.valueOf(scene_eq_fc2.intValue()));
                }
                Integer scene_eq_gain2 = effectModeItem.getSCENE_EQ_GAIN2();
                if (scene_eq_gain2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooO0O.OooO0O0()), Integer.valueOf(scene_eq_gain2.intValue()));
                }
                Integer scene_eq_q2 = effectModeItem.getSCENE_EQ_Q2();
                if (scene_eq_q2 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooO.OooO0O0()), Integer.valueOf(scene_eq_q2.intValue()));
                }
                Integer scene_eq_type3 = effectModeItem.getSCENE_EQ_TYPE3();
                if (scene_eq_type3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOO0.OooO0O0()), Integer.valueOf(scene_eq_type3.intValue()));
                }
                Integer scene_eq_fc3 = effectModeItem.getSCENE_EQ_FC3();
                if (scene_eq_fc3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000oOoO.OooO0O0()), Integer.valueOf(scene_eq_fc3.intValue()));
                }
                Integer scene_eq_gain3 = effectModeItem.getSCENE_EQ_GAIN3();
                if (scene_eq_gain3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOOO.OooO0O0()), Integer.valueOf(scene_eq_gain3.intValue()));
                }
                Integer scene_eq_q3 = effectModeItem.getSCENE_EQ_Q3();
                if (scene_eq_q3 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOOo.OooO0O0()), Integer.valueOf(scene_eq_q3.intValue()));
                }
                Integer scene_eq_type4 = effectModeItem.getSCENE_EQ_TYPE4();
                if (scene_eq_type4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOo0.OooO0O0()), Integer.valueOf(scene_eq_type4.intValue()));
                }
                Integer scene_eq_fc4 = effectModeItem.getSCENE_EQ_FC4();
                if (scene_eq_fc4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOoO.OooO0O0()), Integer.valueOf(scene_eq_fc4.intValue()));
                }
                Integer scene_eq_gain4 = effectModeItem.getSCENE_EQ_GAIN4();
                if (scene_eq_gain4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooOoo.OooO0O0()), Integer.valueOf(scene_eq_gain4.intValue()));
                }
                Integer scene_eq_q4 = effectModeItem.getSCENE_EQ_Q4();
                if (scene_eq_q4 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Ooooo00.OooO0O0()), Integer.valueOf(scene_eq_q4.intValue()));
                }
                Integer scene_eq_type5 = effectModeItem.getSCENE_EQ_TYPE5();
                if (scene_eq_type5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Ooooo0o.OooO0O0()), Integer.valueOf(scene_eq_type5.intValue()));
                }
                Integer scene_eq_fc5 = effectModeItem.getSCENE_EQ_FC5();
                if (scene_eq_fc5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooooO0.OooO0O0()), Integer.valueOf(scene_eq_fc5.intValue()));
                }
                Integer scene_eq_gain5 = effectModeItem.getSCENE_EQ_GAIN5();
                if (scene_eq_gain5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooooOO.OooO0O0()), Integer.valueOf(scene_eq_gain5.intValue()));
                }
                Integer scene_eq_q5 = effectModeItem.getSCENE_EQ_Q5();
                if (scene_eq_q5 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OooooOo.OooO0O0()), Integer.valueOf(scene_eq_q5.intValue()));
                }
                Integer scene_eq_type6 = effectModeItem.getSCENE_EQ_TYPE6();
                if (scene_eq_type6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooooo0.OooO0O0()), Integer.valueOf(scene_eq_type6.intValue()));
                }
                Integer scene_eq_fc6 = effectModeItem.getSCENE_EQ_FC6();
                if (scene_eq_fc6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Oooooo.OooO0O0()), Integer.valueOf(scene_eq_fc6.intValue()));
                }
                Integer scene_eq_gain6 = effectModeItem.getSCENE_EQ_GAIN6();
                if (scene_eq_gain6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.OoooooO.OooO0O0()), Integer.valueOf(scene_eq_gain6.intValue()));
                }
                Integer scene_eq_q6 = effectModeItem.getSCENE_EQ_Q6();
                if (scene_eq_q6 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.Ooooooo.OooO0O0()), Integer.valueOf(scene_eq_q6.intValue()));
                }
                Integer scene_eq_type7 = effectModeItem.getSCENE_EQ_TYPE7();
                if (scene_eq_type7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0OoOo0.OooO0O0()), Integer.valueOf(scene_eq_type7.intValue()));
                }
                Integer scene_eq_fc7 = effectModeItem.getSCENE_EQ_FC7();
                if (scene_eq_fc7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.ooOO.OooO0O0()), Integer.valueOf(scene_eq_fc7.intValue()));
                }
                Integer scene_eq_gain7 = effectModeItem.getSCENE_EQ_GAIN7();
                if (scene_eq_gain7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00O0O.OooO0O0()), Integer.valueOf(scene_eq_gain7.intValue()));
                }
                Integer scene_eq_q7 = effectModeItem.getSCENE_EQ_Q7();
                if (scene_eq_q7 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00Oo0.OooO0O0()), Integer.valueOf(scene_eq_q7.intValue()));
                }
                Integer scene_eq_type8 = effectModeItem.getSCENE_EQ_TYPE8();
                if (scene_eq_type8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00Ooo.OooO0O0()), Integer.valueOf(scene_eq_type8.intValue()));
                }
                Integer scene_eq_fc8 = effectModeItem.getSCENE_EQ_FC8();
                if (scene_eq_fc8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00o0O.OooO0O0()), Integer.valueOf(scene_eq_fc8.intValue()));
                }
                Integer scene_eq_gain8 = effectModeItem.getSCENE_EQ_GAIN8();
                if (scene_eq_gain8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00ooo.OooO0O0()), Integer.valueOf(scene_eq_gain8.intValue()));
                }
                Integer scene_eq_q8 = effectModeItem.getSCENE_EQ_Q8();
                if (scene_eq_q8 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.oo000o.OooO0O0()), Integer.valueOf(scene_eq_q8.intValue()));
                }
                Integer scene_eq_type9 = effectModeItem.getSCENE_EQ_TYPE9();
                if (scene_eq_type9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00oO0o.OooO0O0()), Integer.valueOf(scene_eq_type9.intValue()));
                }
                Integer scene_eq_fc9 = effectModeItem.getSCENE_EQ_FC9();
                if (scene_eq_fc9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00oO0O.OooO0O0()), Integer.valueOf(scene_eq_fc9.intValue()));
                }
                Integer scene_eq_gain9 = effectModeItem.getSCENE_EQ_GAIN9();
                if (scene_eq_gain9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0ooOO0.OooO0O0()), Integer.valueOf(scene_eq_gain9.intValue()));
                }
                Integer scene_eq_q9 = effectModeItem.getSCENE_EQ_Q9();
                if (scene_eq_q9 != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0ooOOo.OooO0O0()), Integer.valueOf(scene_eq_q9.intValue()));
                }
                Integer echo_on_off = effectModeItem.getECHO_ON_OFF();
                if (echo_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0ooOoO.OooO0O0()), Integer.valueOf(echo_on_off.intValue()));
                }
                Integer echo_type = effectModeItem.getECHO_TYPE();
                if (echo_type != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0OOO0o.OooO0O0()), Integer.valueOf(echo_type.intValue()));
                }
                Integer echo_time = effectModeItem.getECHO_TIME();
                if (echo_time != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0Oo0oo.OooO0O0()), Integer.valueOf(echo_time.intValue()));
                }
                Integer echo_vol = effectModeItem.getECHO_VOL();
                if (echo_vol != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0OO00O.OooO0O0()), Integer.valueOf(echo_vol.intValue()));
                }
                Integer echo_feedback = effectModeItem.getECHO_FEEDBACK();
                if (echo_feedback != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.oo0o0Oo.OooO0O0()), Integer.valueOf(echo_feedback.intValue()));
                }
                Integer echo_highcut = effectModeItem.getECHO_HIGHCUT();
                if (echo_highcut != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0O0O00.OooO0O0()), Integer.valueOf(echo_highcut.intValue()));
                }
                Integer echo_lowcut = effectModeItem.getECHO_LOWCUT();
                if (echo_lowcut != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OOo.OooO0O0()), Integer.valueOf(echo_lowcut.intValue()));
                }
                Integer echo_stwide = effectModeItem.getECHO_STWIDE();
                if (echo_stwide != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000000.OooO0O0()), Integer.valueOf(echo_stwide.intValue()));
                }
                Integer rev_on_off = effectModeItem.getREV_ON_OFF();
                if (rev_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000000O.OooO0O0()), Integer.valueOf(rev_on_off.intValue()));
                }
                Integer rev_type = effectModeItem.getREV_TYPE();
                if (rev_type != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000000o.OooO0O0()), Integer.valueOf(rev_type.intValue()));
                }
                Integer rev_time = effectModeItem.getREV_TIME();
                if (rev_time != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000.OooO0O0()), Integer.valueOf(rev_time.intValue()));
                }
                Integer rev_vol = effectModeItem.getREV_VOL();
                if (rev_vol != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000O0.OooO0O0()), Integer.valueOf(rev_vol.intValue()));
                }
                Integer rev_highdamp = effectModeItem.getREV_HIGHDAMP();
                if (rev_highdamp != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000O.OooO0O0()), Integer.valueOf(rev_highdamp.intValue()));
                }
                Integer rev_lowcut = effectModeItem.getREV_LOWCUT();
                if (rev_lowcut != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000OO.OooO0O0()), Integer.valueOf(rev_lowcut.intValue()));
                }
                Integer rev_highcut = effectModeItem.getREV_HIGHCUT();
                if (rev_highcut != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000Oo.OooO0O0()), Integer.valueOf(rev_highcut.intValue()));
                }
                Integer rev_stwide = effectModeItem.getREV_STWIDE();
                if (rev_stwide != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000o0.OooO0O0()), Integer.valueOf(rev_stwide.intValue()));
                }
                Integer rev_echo_gain = effectModeItem.getREV_ECHO_GAIN();
                if (rev_echo_gain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000Ooo.OooO0O0()), Integer.valueOf(rev_echo_gain.intValue()));
                }
                Integer comp_thres = effectModeItem.getCOMP_THRES();
                if (comp_thres != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000oO.OooO0O0()), Integer.valueOf(comp_thres.intValue()));
                }
                Integer comp_pregain = effectModeItem.getCOMP_PREGAIN();
                if (comp_pregain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o00000oo.OooO0O0()), Integer.valueOf(comp_pregain.intValue()));
                }
                Integer comp_attack = effectModeItem.getCOMP_ATTACK();
                if (comp_attack != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000.OooO0O0()), Integer.valueOf(comp_attack.intValue()));
                }
                Integer comp_release = effectModeItem.getCOMP_RELEASE();
                if (comp_release != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000O00.OooO0O0()), Integer.valueOf(comp_release.intValue()));
                }
                Integer comp_ratio = effectModeItem.getCOMP_RATIO();
                if (comp_ratio != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oo.OooO0O0()), Integer.valueOf(comp_ratio.intValue()));
                }
                Integer comp_volume = effectModeItem.getCOMP_VOLUME();
                if (comp_volume != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oO.OooO0O0()), Integer.valueOf(comp_volume.intValue()));
                }
                Integer comp_global_gain = effectModeItem.getCOMP_GLOBAL_GAIN();
                if (comp_global_gain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000O0.OooO0O0()), Integer.valueOf(comp_global_gain.intValue()));
                }
                Integer ps_on_off = effectModeItem.getPS_ON_OFF();
                if (ps_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000O0O.OooO0O0()), Integer.valueOf(ps_on_off.intValue()));
                }
                Integer ps_semi = effectModeItem.getPS_SEMI();
                if (ps_semi != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OO.OooO0O0()), Integer.valueOf(ps_semi.intValue()));
                }
                Integer ps_mode = effectModeItem.getPS_MODE();
                if (ps_mode != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000O.OooO0O0()), Integer.valueOf(ps_mode.intValue()));
                }
                Integer fbc_level = effectModeItem.getFBC_LEVEL();
                if (fbc_level != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000OO0.OooO0O0()), Integer.valueOf(fbc_level.intValue()));
                }
                Integer fbc_threshold = effectModeItem.getFBC_THRESHOLD();
                if (fbc_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000OO.OooO0O0()), Integer.valueOf(fbc_threshold.intValue()));
                }
                Integer dhs_on_off = effectModeItem.getDHS_ON_OFF();
                if (dhs_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000OOO.OooO0O0()), Integer.valueOf(dhs_on_off.intValue()));
                }
                Integer dhs_threshold = effectModeItem.getDHS_THRESHOLD();
                if (dhs_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000OOo.OooO0O0()), Integer.valueOf(dhs_threshold.intValue()));
                }
                Integer nsr_on_off = effectModeItem.getNSR_ON_OFF();
                if (nsr_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000Oo0.OooO0O0()), Integer.valueOf(nsr_on_off.intValue()));
                }
                Integer nsr_threshold = effectModeItem.getNSR_THRESHOLD();
                if (nsr_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000Oo.OooO0O0()), Integer.valueOf(nsr_threshold.intValue()));
                }
                Integer lofi_on_off = effectModeItem.getLOFI_ON_OFF();
                if (lofi_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000OoO.OooO0O0()), Integer.valueOf(lofi_on_off.intValue()));
                }
                Integer lofi_down_sample = effectModeItem.getLOFI_DOWN_SAMPLE();
                if (lofi_down_sample != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000o0.OooO0O0()), Integer.valueOf(lofi_down_sample.intValue()));
                }
                Integer lofi_type = effectModeItem.getLOFI_TYPE();
                if (lofi_type != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000o0O.OooO0O0()), Integer.valueOf(lofi_type.intValue()));
                }
                Integer lofi_8bit_type = effectModeItem.getLOFI_8BIT_TYPE();
                if (lofi_8bit_type != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000o0o.OooO0O0()), Integer.valueOf(lofi_8bit_type.intValue()));
                }
                Integer lofi_alaising = effectModeItem.getLOFI_ALAISING();
                if (lofi_alaising != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000o.OooO0O0()), Integer.valueOf(lofi_alaising.intValue()));
                }
                Integer vc_on_off = effectModeItem.getVC_ON_OFF();
                if (vc_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oO0.OooO0O0()), Integer.valueOf(vc_on_off.intValue()));
                }
                Integer vc_formant_semi = effectModeItem.getVC_FORMANT_SEMI();
                if (vc_formant_semi != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oOO.OooO0O0()), Integer.valueOf(vc_formant_semi.intValue()));
                }
                Integer vc_pitch_semi = effectModeItem.getVC_PITCH_SEMI();
                if (vc_pitch_semi != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oOo.OooO0O0()), Integer.valueOf(vc_pitch_semi.intValue()));
                }
                Integer vc_delaylen = effectModeItem.getVC_DELAYLEN();
                if (vc_delaylen != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000oo0.OooO0O0()), Integer.valueOf(vc_delaylen.intValue()));
                }
                Integer vc_robotpitch = effectModeItem.getVC_ROBOTPITCH();
                if (vc_robotpitch != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0000ooO.OooO0O0()), Integer.valueOf(vc_robotpitch.intValue()));
                }
                Integer vc_robotpitchonoff = effectModeItem.getVC_ROBOTPITCHONOFF();
                if (vc_robotpitchonoff != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000.OooO0O0()), Integer.valueOf(vc_robotpitchonoff.intValue()));
                }
                Integer vc_autotuneonoff = effectModeItem.getVC_AUTOTUNEONOFF();
                if (vc_autotuneonoff != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O000.OooO0O0()), Integer.valueOf(vc_autotuneonoff.intValue()));
                }
                Integer vc_drywet = effectModeItem.getVC_DRYWET();
                if (vc_drywet != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OoO.OooO0O0()), Integer.valueOf(vc_drywet.intValue()));
                }
                Integer nsgate_on_off = effectModeItem.getNSGATE_ON_OFF();
                if (nsgate_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0o.OooO0O0()), Integer.valueOf(nsgate_on_off.intValue()));
                }
                Integer nsgate_thres = effectModeItem.getNSGATE_THRES();
                if (nsgate_thres != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Ooo.OooO0O0()), Integer.valueOf(nsgate_thres.intValue()));
                }
                Integer nsgate_release = effectModeItem.getNSGATE_RELEASE();
                if (nsgate_release != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0O.OooO0O0()), Integer.valueOf(nsgate_release.intValue()));
                }
                Integer sweep_sin_start = effectModeItem.getSWEEP_SIN_START();
                if (sweep_sin_start != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oo0.OooO0O0()), Integer.valueOf(sweep_sin_start.intValue()));
                }
                Integer sweep_sin_left_vol = effectModeItem.getSWEEP_SIN_LEFT_VOL();
                if (sweep_sin_left_vol != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O00.OooO0O0()), Integer.valueOf(sweep_sin_left_vol.intValue()));
                }
                Integer sweep_sin_right_vol = effectModeItem.getSWEEP_SIN_RIGHT_VOL();
                if (sweep_sin_right_vol != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O00O.OooO0O0()), Integer.valueOf(sweep_sin_right_vol.intValue()));
                }
                Integer bandcomp_on_off = effectModeItem.getBANDCOMP_ON_OFF();
                if (bandcomp_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0.OooO0O0()), Integer.valueOf(bandcomp_on_off.intValue()));
                }
                Integer bandcomp_low_fc = effectModeItem.getBANDCOMP_LOW_FC();
                if (bandcomp_low_fc != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0Oo.OooO0O0()), Integer.valueOf(bandcomp_low_fc.intValue()));
                }
                Integer bandcomp_high_fc = effectModeItem.getBANDCOMP_HIGH_FC();
                if (bandcomp_high_fc != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OO0O.OooO0O0()), Integer.valueOf(bandcomp_high_fc.intValue()));
                }
                Integer bandcomp_low_pregain = effectModeItem.getBANDCOMP_LOW_PREGAIN();
                if (bandcomp_low_pregain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0O0.OooO0O0()), Integer.valueOf(bandcomp_low_pregain.intValue()));
                }
                Integer bandcomp_mid_pregain = effectModeItem.getBANDCOMP_MID_PREGAIN();
                if (bandcomp_mid_pregain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0o0.OooO0O0()), Integer.valueOf(bandcomp_mid_pregain.intValue()));
                }
                Integer bandcomp_high_pregain = effectModeItem.getBANDCOMP_HIGH_PREGAIN();
                if (bandcomp_high_pregain != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0oO.OooO0O0()), Integer.valueOf(bandcomp_high_pregain.intValue()));
                }
                Integer bandcomp_low_threshold = effectModeItem.getBANDCOMP_LOW_THRESHOLD();
                if (bandcomp_low_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O0oo.OooO0O0()), Integer.valueOf(bandcomp_low_threshold.intValue()));
                }
                Integer bandcomp_mid_threshold = effectModeItem.getBANDCOMP_MID_THRESHOLD();
                if (bandcomp_mid_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000O.OooO0O0()), Integer.valueOf(bandcomp_mid_threshold.intValue()));
                }
                Integer bandcomp_high_threshold = effectModeItem.getBANDCOMP_HIGH_THRESHOLD();
                if (bandcomp_high_threshold != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OO00.OooO0O0()), Integer.valueOf(bandcomp_high_threshold.intValue()));
                }
                Integer bandcomp_low_ratio = effectModeItem.getBANDCOMP_LOW_RATIO();
                if (bandcomp_low_ratio != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o0OoO0o.OooO0O0()), Integer.valueOf(bandcomp_low_ratio.intValue()));
                }
                Integer bandcomp_mid_ratio = effectModeItem.getBANDCOMP_MID_RATIO();
                if (bandcomp_mid_ratio != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OO0o.OooO0O0()), Integer.valueOf(bandcomp_mid_ratio.intValue()));
                }
                Integer bandcomp_high_ratio = effectModeItem.getBANDCOMP_HIGH_RATIO();
                if (bandcomp_high_ratio != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OOO.OooO0O0()), Integer.valueOf(bandcomp_high_ratio.intValue()));
                }
                Integer bandcomp_low_attack = effectModeItem.getBANDCOMP_LOW_ATTACK();
                if (bandcomp_low_attack != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OOo0.OooO0O0()), Integer.valueOf(bandcomp_low_attack.intValue()));
                }
                Integer bandcomp_mid_attack = effectModeItem.getBANDCOMP_MID_ATTACK();
                if (bandcomp_mid_attack != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OOoO.OooO0O0()), Integer.valueOf(bandcomp_mid_attack.intValue()));
                }
                Integer bandcomp_high_attack = effectModeItem.getBANDCOMP_HIGH_ATTACK();
                if (bandcomp_high_attack != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oo00.OooO0O0()), Integer.valueOf(bandcomp_high_attack.intValue()));
                }
                Integer bandcomp_low_release = effectModeItem.getBANDCOMP_LOW_RELEASE();
                if (bandcomp_low_release != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oo0O.OooO0O0()), Integer.valueOf(bandcomp_low_release.intValue()));
                }
                Integer bandcomp_mid_release = effectModeItem.getBANDCOMP_MID_RELEASE();
                if (bandcomp_mid_release != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oo0o.OooO0O0()), Integer.valueOf(bandcomp_mid_release.intValue()));
                }
                Integer bandcomp_high_release = effectModeItem.getBANDCOMP_HIGH_RELEASE();
                if (bandcomp_high_release != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oo.OooO0O0()), Integer.valueOf(bandcomp_high_release.intValue()));
                }
                Integer popr_on_off = effectModeItem.getPOPR_ON_OFF();
                if (popr_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OoOO.OooO0O0()), Integer.valueOf(popr_on_off.intValue()));
                }
                Integer popr_mute = effectModeItem.getPOPR_MUTE();
                if (popr_mute != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OoOo.OooO0O0()), Integer.valueOf(popr_mute.intValue()));
                }
                Integer popr_unmute = effectModeItem.getPOPR_UNMUTE();
                if (popr_unmute != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Ooo0.OooO0O0()), Integer.valueOf(popr_unmute.intValue()));
                }
                Integer popr_mute_time = effectModeItem.getPOPR_MUTE_TIME();
                if (popr_mute_time != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000OooO.OooO0O0()), Integer.valueOf(popr_mute_time.intValue()));
                }
                Integer popr_unmute_time = effectModeItem.getPOPR_UNMUTE_TIME();
                if (popr_unmute_time != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000Oooo.OooO0O0()), Integer.valueOf(popr_unmute_time.intValue()));
                }
                Integer clear_all = effectModeItem.getCLEAR_ALL();
                if (clear_all != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000o000.OooO0O0()), Integer.valueOf(clear_all.intValue()));
                }
                Integer autoadapt_manule_start = effectModeItem.getAUTOADAPT_MANULE_START();
                if (autoadapt_manule_start != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000o00.OooO0O0()), Integer.valueOf(autoadapt_manule_start.intValue()));
                }
                Integer auto_pregain_on_off = effectModeItem.getAUTO_PREGAIN_ON_OFF();
                if (auto_pregain_on_off != null) {
                    effectScene.put(Integer.valueOf(OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO00o.o000o00O.OooO0O0()), Integer.valueOf(auto_pregain_on_off.intValue()));
                }
                if (effectModeItem.getType() == 0) {
                    OooO0Oo.add(effectModeData);
                }
            }
        }
    }

    public final void OooO0O0() {
        int i = 0;
        for (EffectModeData effectModeData : OooO0Oo) {
            String msg = Intrinsics.stringPlus("send data, dataPer.effectId = ", Integer.valueOf(effectModeData.getEffectId()));
            Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "TransportConfig -> " + msg);
            }
            boolean z = !effectModeData.getEffectSurround().isEmpty();
            ArrayList arrayList = new ArrayList();
            int i2 = 0;
            int i3 = 1;
            for (Map.Entry<Integer, Integer> entry : effectModeData.getEffectScene().entrySet()) {
                arrayList.add(entry.getKey());
                arrayList.add(entry.getValue());
                if (i3 % 3 == 0) {
                    arrayList.add(0, Integer.valueOf(effectModeData.getEffectId()));
                    if (i3 == effectModeData.getEffectScene().size() && !z) {
                        arrayList.add(1, 255);
                    } else {
                        arrayList.add(1, Integer.valueOf(i2));
                    }
                    arrayList.add(2, 0);
                    f551OooO00o.OooO00o(arrayList);
                    arrayList.clear();
                    i2++;
                } else if (i3 == effectModeData.getEffectScene().size()) {
                    arrayList.add(0, Integer.valueOf(effectModeData.getEffectId()));
                    if (!z) {
                        arrayList.add(1, 255);
                    } else {
                        arrayList.add(1, Integer.valueOf(i2));
                    }
                    arrayList.add(2, 0);
                    f551OooO00o.OooO00o(arrayList);
                    arrayList.clear();
                    i2++;
                }
                i3++;
            }
            int i4 = 1;
            for (Map.Entry<Integer, Integer> entry2 : effectModeData.getEffectSurround().entrySet()) {
                arrayList.add(entry2.getKey());
                arrayList.add(entry2.getValue());
                if (i4 % 3 == 0) {
                    arrayList.add(0, Integer.valueOf(effectModeData.getEffectId()));
                    if (i4 == effectModeData.getEffectSurround().size()) {
                        arrayList.add(1, 255);
                    } else {
                        arrayList.add(1, Integer.valueOf(i2));
                    }
                    arrayList.add(2, 1);
                    f551OooO00o.OooO00o(arrayList);
                    arrayList.clear();
                    i2++;
                } else if (i4 == effectModeData.getEffectSurround().size()) {
                    arrayList.add(0, Integer.valueOf(effectModeData.getEffectId()));
                    arrayList.add(1, 255);
                    arrayList.add(2, 1);
                    f551OooO00o.OooO00o(arrayList);
                    arrayList.clear();
                    i2++;
                }
                i4++;
            }
            i += i2;
            String msg2 = Intrinsics.stringPlus("pkgSizeCount = ", Integer.valueOf(i));
            Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
            Intrinsics.checkNotNullParameter(msg2, "msg");
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
                Log.i("LogTuning", "TransportConfig -> " + msg2);
            }
        }
        String msg3 = Intrinsics.stringPlus("sendData finish, pkgSizeCount = ", Integer.valueOf(i));
        Intrinsics.checkNotNullParameter("TransportConfig", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg3, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 2) {
            Log.i("LogTuning", "TransportConfig -> " + msg3);
        }
        OooO00o oooO00o = OooO0OO;
        if (oooO00o == null) {
            return;
        }
        oooO00o.OooO00o(i);
    }

    public final void OooO00o(List<Integer> list) {
        String str;
        char c;
        String str2;
        List<Integer> list2 = list;
        OooO0OO.OooO00o version = OooO0OO.OooO00o.DEFAULT;
        Intrinsics.checkNotNullParameter(version, "version");
        String format = String.format("%02d", Arrays.copyOf(new Object[]{61}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        String format2 = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(list2.get(0).intValue())}, 1));
        Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
        String format3 = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(list2.get(1).intValue())}, 1));
        Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
        int i = 2;
        String format4 = String.format("%1d", Arrays.copyOf(new Object[]{Integer.valueOf(list2.get(2).intValue())}, 1));
        Intrinsics.checkNotNullExpressionValue(format4, "java.lang.String.format(format, *args)");
        IntProgression step = RangesKt.step(RangesKt.until(3, list.size()), 2);
        int first = step.getFirst();
        int last = step.getLast();
        int step2 = step.getStep();
        if ((step2 <= 0 || first > last) && (step2 >= 0 || last > first)) {
            str = "";
        } else {
            str = "";
            while (true) {
                int i2 = first + step2;
                int intValue = list2.get(first).intValue();
                int intValue2 = list2.get(first + 1).intValue();
                Object[] objArr = new Object[i];
                objArr[0] = Integer.valueOf(intValue);
                if (intValue2 < 0) {
                    if (intValue2 >= -999) {
                        String format5 = String.format("%1$,05d", Arrays.copyOf(new Object[]{Integer.valueOf(intValue2)}, 1));
                        Intrinsics.checkNotNullExpressionValue(format5, "java.lang.String.format(format, *args)");
                        str2 = StringsKt.replace$default(format5, "-", "n", false, 4, (Object) null);
                        c = 1;
                    } else {
                        str2 = intValue2 >= -9999 ? StringsKt.replace$default(String.valueOf(intValue2), "-", "n", false, 4, (Object) null) : "n9999";
                        c = 1;
                    }
                } else if (intValue2 <= 99999) {
                    c = 1;
                    str2 = String.format("%05d", Arrays.copyOf(new Object[]{Integer.valueOf(intValue2)}, 1));
                    Intrinsics.checkNotNullExpressionValue(str2, "java.lang.String.format(format, *args)");
                } else {
                    c = 1;
                    str2 = "99999";
                }
                objArr[c] = str2;
                String format6 = String.format("%03d%s", Arrays.copyOf(objArr, 2));
                Intrinsics.checkNotNullExpressionValue(format6, "java.lang.String.format(format, *args)");
                str = Intrinsics.stringPlus(str, format6);
                if (first == last) {
                    break;
                }
                list2 = list;
                first = i2;
                i = 2;
            }
        }
        OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o oooO00o = (OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.y1.OooO00o) OooO0O0.getValue();
        OooO0OO command = new OooO0OO();
        command.f235OooO00o = format;
        command.OooO0O0 = format2;
        command.OooO0OO = format3;
        command.OooO0Oo = format4;
        command.OooO0o0 = str;
        oooO00o.getClass();
        Intrinsics.checkNotNullParameter(command, "command");
        String code = OooO00o.OooO00o.OooO00o.OooO00o.OooO00o.OooO0o(oooO00o.OooO00o().getParameters("pm_kara_code"));
        Intrinsics.checkNotNullExpressionValue(code, "code");
        if (StringsKt.contains$default((CharSequence) code, (CharSequence) "pm_kara_code=", false, 2, (Object) null)) {
            Intrinsics.checkNotNullExpressionValue(code, "code");
            String seed = StringsKt.replace$default(StringsKt.replace$default(code, "pm_kara=", "", false, 4, (Object) null), "pm_kara_code=", "", false, 4, (Object) null);
            if (Intrinsics.areEqual(seed, "")) {
                String msg = Intrinsics.stringPlus("wrong with write: ", command.OooO00o());
                Intrinsics.checkNotNullParameter("AMCommunication", Progress.TAG);
                Intrinsics.checkNotNullParameter(msg, "msg");
                if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                    Log.e("LogTuning", "AMCommunication -> " + msg);
                    return;
                }
                return;
            }
            AudioManager OooO00o2 = oooO00o.OooO00o();
            String value = command.OooO00o();
            Intrinsics.checkNotNullExpressionValue(seed, "code");
            Intrinsics.checkNotNullParameter(value, "value");
            Intrinsics.checkNotNullParameter(seed, "seed");
            OooO00o2.setParameters(Intrinsics.stringPlus("pm_kara=", JniUtil.f600OooO00o.encryption(value, Integer.parseInt(seed))));
            return;
        }
        String msg2 = Intrinsics.stringPlus("wrong with write code: ", command.OooO00o());
        Intrinsics.checkNotNullParameter("AMCommunication", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg2, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
            Log.e("LogTuning", "AMCommunication -> " + msg2);
        }
    }
}
