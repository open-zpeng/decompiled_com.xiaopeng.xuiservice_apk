package OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO00o.OooO00o;

import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.data.config.vendor.EnvironmentalSoundItem;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO0O0 implements IVolumeAdjustment {
    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef1ReverbOutGain() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO0O0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorAttack() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO0OO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorPregain() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO0o0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorRatio() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorRelease() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorThreshold() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ1() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ1Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ2() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ2Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ3() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ3Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ4() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ4Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ5() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOo00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ5Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOOoo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ6() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOo0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ6Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ7() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ7Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ8() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ8Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOoO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ9() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOoo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ9Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOoOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoFeedBack() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoGain() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo000().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoTime() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo00O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoType() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo00o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ1() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo0oO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ1Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ2() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ2Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ3() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ3Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooO00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ4() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ4Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ5() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o000oOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ5Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ6() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ6Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ7() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ7Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ8() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Ooooo00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ8Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OoooOoo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ9() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooooO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ9Frequency() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Ooooo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbMode() {
        Integer OooO00o2 = OooOO0.f398OooO00o.ooOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbTime() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0OoOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbVolume() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00O0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2VoiceMode() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00Ooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ1Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00oO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ2Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00oO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ3Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0ooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ4Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0ooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ5Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0ooOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ6Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0OOO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ7Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0Oo0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ8Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0OO00O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ9Type() {
        Integer OooO00o2 = OooOO0.f398OooO00o.oo0o0Oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoHicut() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00o0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoLowcut() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00ooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoStwide() {
        Integer OooO00o2 = OooOO0.f398OooO00o.oo000o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ1() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0O0O00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ2() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o000OOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ3() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o000000().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ4() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o000000O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ5() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o000000o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ6() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ7() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000O0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ8() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ9() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000OO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbFsEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Oooo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbHicut() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000Oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbHidamp() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000o0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbLowcut() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000Ooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbStwide() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000oO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4BypassEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00000oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4ReverbVolume() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000().OooO00o();
        return (OooO00o2 == null ? 0 : OooO00o2.intValue()) / 2;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4SceneEffectMode() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000O00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getBalance() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000O0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getCarOutPregainAdd() {
        Integer OooO00o2;
        OooOO0 oooOO0 = OooOO0.f398OooO00o;
        String OooO00o3 = oooOO0.o0000OO0().OooO00o();
        if (OooO00o3 == null) {
            OooO00o3 = "";
        }
        if (!StringsKt.contains$default((CharSequence) OooO00o3, (CharSequence) "54", false, 2, (Object) null) || (OooO00o2 = oooOO0.o0000O().OooO00o()) == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getEnvironmentalSound() {
        OooOO0.f398OooO00o.getClass();
        Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.o000O0O.getValue()).OooO00o();
        if (num == null) {
            return 1;
        }
        return num.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getMicVolume() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000OOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getMusicVolume() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000OOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getRecordVocalOffset() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000Oo().OooO00o();
        if (OooO00o2 == null) {
            return 480;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getSystemVolume() {
        OooOO0.f398OooO00o.getClass();
        Integer num = (Integer) ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.Oooo000.getValue()).OooO00o();
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef1ReverbEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO00o().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2CompressorEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooO0Oo().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2EQEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOoo().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2EchoEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooOooO().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2MusicEQEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.OooooOO().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2ReverbEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.Ooooooo().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2VoiceChangeEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o00Oo0().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isMicVolumeEnable() {
        Integer OooO00o2 = OooOO0.f398OooO00o.o0000OO().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void recoveryFromJson() {
        OooO oooO = OooO.f279OooO00o;
        Long OooO00o2 = oooO.OooO0O0().OooO00o();
        long longValue = OooO00o2 == null ? -1L : OooO00o2.longValue();
        Integer OooO00o3 = oooO.OooO0oO().OooO00o();
        int intValue = OooO00o3 == null ? 0 : OooO00o3.intValue();
        OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0 oooO0O0 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0O0.f311OooO00o;
        oooO0O0.OooO00o(longValue, intValue, true);
        oooO.OooOO0o();
        oooO0O0.OooO00o();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef1ReverbEnable(boolean z) {
        OooOO0.f398OooO00o.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef1ReverbOutGain(int i) {
        OooOO0.f398OooO00o.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorAttack(int i) {
        OooOO0.f398OooO00o.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorEnable(boolean z) {
        OooOO0.f398OooO00o.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorPregain(int i) {
        OooOO0.f398OooO00o.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorRatio(int i) {
        OooOO0.f398OooO00o.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorRelease(int i) {
        OooOO0.f398OooO00o.OooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorThreshold(int i) {
        OooOO0.f398OooO00o.OooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ1(int i) {
        OooOO0.f398OooO00o.OooOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ1Frequency(int i) {
        OooOO0.f398OooO00o.OooOO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ2(int i) {
        OooOO0.f398OooO00o.OooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ2Frequency(int i) {
        OooOO0.f398OooO00o.OooOOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ3(int i) {
        OooOO0.f398OooO00o.OooOOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ3Frequency(int i) {
        OooOO0.f398OooO00o.OooOOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ4(int i) {
        OooOO0.f398OooO00o.OooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ4Frequency(int i) {
        OooOO0.f398OooO00o.OooOOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ5(int i) {
        OooOO0.f398OooO00o.OooOo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ5Frequency(int i) {
        OooOO0.f398OooO00o.OooOOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ6(int i) {
        OooOO0.f398OooO00o.OooOo0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ6Frequency(int i) {
        OooOO0.f398OooO00o.OooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ7(int i) {
        OooOO0.f398OooO00o.OooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ7Frequency(int i) {
        OooOO0.f398OooO00o.OooOo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ8(int i) {
        OooOO0.f398OooO00o.OooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ8Frequency(int i) {
        OooOO0.f398OooO00o.OooOoO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ9(int i) {
        OooOO0.f398OooO00o.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ9Frequency(int i) {
        OooOO0.f398OooO00o.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQEnable(boolean z) {
        OooOO0.f398OooO00o.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoEnable(boolean z) {
        OooOO0.f398OooO00o.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoFeedBack(int i) {
        OooOO0.f398OooO00o.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoGain(int i) {
        OooOO0.f398OooO00o.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoTime(int i) {
        OooOO0.f398OooO00o.Oooo00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoType(int i) {
        OooOO0.f398OooO00o.Oooo00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ1(int i) {
        OooOO0.f398OooO00o.Oooo0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ1Frequency(int i) {
        OooOO0.f398OooO00o.Oooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ2(int i) {
        OooOO0.f398OooO00o.Oooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ2Frequency(int i) {
        OooOO0.f398OooO00o.Oooo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ3(int i) {
        OooOO0.f398OooO00o.OoooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ3Frequency(int i) {
        OooOO0.f398OooO00o.OoooO00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ4(int i) {
        OooOO0.f398OooO00o.OoooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ4Frequency(int i) {
        OooOO0.f398OooO00o.OoooO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ5(int i) {
        OooOO0.f398OooO00o.o000oOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ5Frequency(int i) {
        OooOO0.f398OooO00o.OoooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ6(int i) {
        OooOO0.f398OooO00o.OoooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ6Frequency(int i) {
        OooOO0.f398OooO00o.OoooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ7(int i) {
        OooOO0.f398OooO00o.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ7Frequency(int i) {
        OooOO0.f398OooO00o.OoooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ8(int i) {
        OooOO0.f398OooO00o.Ooooo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ8Frequency(int i) {
        OooOO0.f398OooO00o.OoooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ9(int i) {
        OooOO0.f398OooO00o.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ9Frequency(int i) {
        OooOO0.f398OooO00o.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQEnable(boolean z) {
        OooOO0.f398OooO00o.OooooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbEnable(boolean z) {
        OooOO0.f398OooO00o.Ooooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbMode(int i) {
        OooOO0.f398OooO00o.ooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbTime(int i) {
        OooOO0.f398OooO00o.o0OoOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbVolume(int i) {
        OooOO0.f398OooO00o.o00O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2VoiceChangeEnable(boolean z) {
        OooOO0.f398OooO00o.o00Oo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2VoiceMode(int i) {
        OooOO0.f398OooO00o.o00Ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ1Type(int i) {
        OooOO0.f398OooO00o.o00oO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ2Type(int i) {
        OooOO0.f398OooO00o.o00oO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ3Type(int i) {
        OooOO0.f398OooO00o.o0ooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ4Type(int i) {
        OooOO0.f398OooO00o.o0ooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ5Type(int i) {
        OooOO0.f398OooO00o.o0ooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ6Type(int i) {
        OooOO0.f398OooO00o.o0OOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ7Type(int i) {
        OooOO0.f398OooO00o.o0Oo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ8Type(int i) {
        OooOO0.f398OooO00o.o0OO00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ9Type(int i) {
        OooOO0.f398OooO00o.oo0o0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoHicut(int i) {
        OooOO0.f398OooO00o.o00o0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoLowcut(int i) {
        OooOO0.f398OooO00o.o00ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoStwide(int i) {
        OooOO0.f398OooO00o.oo000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ1(int i) {
        OooOO0.f398OooO00o.o0O0O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ2(int i) {
        OooOO0.f398OooO00o.o000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ3(int i) {
        OooOO0.f398OooO00o.o000000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ4(int i) {
        OooOO0.f398OooO00o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ5(int i) {
        OooOO0.f398OooO00o.o000000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ6(int i) {
        OooOO0.f398OooO00o.o00000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ7(int i) {
        OooOO0.f398OooO00o.o00000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ8(int i) {
        OooOO0.f398OooO00o.o00000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ9(int i) {
        OooOO0.f398OooO00o.o00000OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbFsEnable(int i) {
        OooOO0.f398OooO00o.Oooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (i == 1 ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbHicut(int i) {
        OooOO0.f398OooO00o.o00000Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbHidamp(int i) {
        OooOO0.f398OooO00o.o00000o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbLowcut(int i) {
        OooOO0.f398OooO00o.o0000Ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbStwide(int i) {
        OooOO0.f398OooO00o.o00000oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4BypassEnable(int i) {
        OooOO0.f398OooO00o.o00000oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4ReverbVolume(int i) {
        OooOO0.f398OooO00o.o0000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i * 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4SceneEffectMode(int i) {
        OooOO0.f398OooO00o.o0000O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setBalance(int i) {
        OooOO0.f398OooO00o.o0000O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setDeviceCard(@NotNull String deviceCard) {
        Intrinsics.checkNotNullParameter(deviceCard, "deviceCard");
        OooOO0.f398OooO00o.o0000OO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<String>) deviceCard);
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setEnvironmentalSound(int i, double d) {
        EnvironmentalSoundItem environmentalSoundItem;
        if (OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0 == null) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  环境音效没有初始化", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "AdjustBySO ->   环境音效没有初始化");
                return;
            }
            return;
        }
        OooOO0.f398OooO00o.getClass();
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.o000O0O.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(i));
        if (i == 0) {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList);
            environmentalSoundItem = arrayList.get(0);
        } else if (i == 1) {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList2 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList2);
            environmentalSoundItem = arrayList2.get(1);
        } else if (i == 2) {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList3 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList3);
            environmentalSoundItem = arrayList3.get(2);
        } else if (i == 3) {
            setAef2ReverbEnable(false);
            setAef2EchoEnable(false);
            ArrayList<EnvironmentalSoundItem> arrayList4 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList4);
            environmentalSoundItem = arrayList4.get(3);
        } else if (i == 4) {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList5 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList5);
            environmentalSoundItem = arrayList5.get(4);
        } else if (i != 5) {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList6 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList6);
            environmentalSoundItem = arrayList6.get(1);
        } else {
            setAef2ReverbEnable(true);
            setAef2EchoEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList7 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList7);
            environmentalSoundItem = arrayList7.get(5);
        }
        Intrinsics.checkNotNullExpressionValue(environmentalSoundItem, "when (mode) {\n            Constant.EnvironmentalSound.STUDIO -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![0]\n            }\n\n            Constant.EnvironmentalSound.KTV -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![1]\n            }\n\n            Constant.EnvironmentalSound.CONCERT -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![2]\n            }\n\n            Constant.EnvironmentalSound.ORIGIN -> {\n                setAef2ReverbEnable(false)\n                setAef2EchoEnable(false)\n\n                EnvironmentalSound.environmentalSounds!![3]\n            }\n\n            Constant.EnvironmentalSound.CAVE -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![4]\n            }\n\n            Constant.EnvironmentalSound.VOCAL_CONCERT -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![5]\n            }\n\n            else -> {\n                setAef2ReverbEnable(true)\n                setAef2EchoEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![1]\n            }\n        }");
        String msg = "soundItem(" + i + ")=" + environmentalSoundItem;
        Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
        Intrinsics.checkNotNullParameter(msg, "msg");
        if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
            Log.i("LogTuning", "AdjustBySO -> " + msg);
        }
        OooOO0.OooO0O0 = 0;
        setAef2ReverbMode(environmentalSoundItem.getAef2ReverbMode());
        setAef2ReverbVolume((int) (environmentalSoundItem.getAef2ReverbVolume() * d));
        setAef2ReverbTime(environmentalSoundItem.getAef2ReverbTime());
        OooOO0.OooO0OO = 0;
        setAef2EchoType(environmentalSoundItem.getAef2EchoMode());
        setAef2EchoGain((int) (environmentalSoundItem.getAef2EchoGain() * d));
        setAef2EchoTime(environmentalSoundItem.getAef2EchoTime());
        setAef2EchoFeedBack(environmentalSoundItem.getAef2EchoFeedback());
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMicVolume(int i) {
        OooOO0.f398OooO00o.o0000OOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMicVolumeEnable(boolean z) {
        OooOO0.f398OooO00o.o0000OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMusicVolume(int i) {
        OooOO0.f398OooO00o.o0000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setSystemVolume(int i) {
        OooOO0.f398OooO00o.getClass();
        ((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) OooOO0.Oooo000.getValue()).OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void volumeDown(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (i == 1) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  micVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   micVolumeDown");
            }
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            if (oooOO0.o0000OOO().OooO00o() == null) {
                return;
            }
            Integer OooO00o2 = oooOO0.o0000OOO().OooO00o();
            Intrinsics.checkNotNull(OooO00o2);
            int intValue = OooO00o2.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.f310OooO00o;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o0000OOO = oooOO0.o0000OOO();
            if (intValue >= 0) {
                i2 = Integer.valueOf(intValue);
            } else {
                i2 = 0;
            }
            o0000OOO.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i2);
        } else if (i == 2) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef1ReverbVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   aef1ReverbVolumeDown");
            }
            OooOO0 oooOO02 = OooOO0.f398OooO00o;
            if (oooOO02.OooO0O0().OooO00o() == null) {
                return;
            }
            Integer OooO00o3 = oooOO02.OooO0O0().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            int intValue2 = OooO00o3.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> OooO0O0 = oooOO02.OooO0O0();
            if (intValue2 >= 0) {
                i3 = Integer.valueOf(intValue2);
            } else {
                i3 = 0;
            }
            OooO0O0.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i3);
        } else if (i == 3) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef2ReverbVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   aef2ReverbVolumeDown");
            }
            OooOO0 oooOO03 = OooOO0.f398OooO00o;
            if (oooOO03.o00O0O().OooO00o() == null) {
                return;
            }
            Integer OooO00o4 = oooOO03.o00O0O().OooO00o();
            Intrinsics.checkNotNull(OooO00o4);
            int intValue3 = OooO00o4.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o00O0O = oooOO03.o00O0O();
            if (intValue3 >= 0) {
                i4 = Integer.valueOf(intValue3);
            } else {
                i4 = 0;
            }
            o00O0O.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i4);
        } else if (i != 4) {
        } else {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  laBaMicCompanyVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   laBaMicCompanyVolumeDown");
            }
            OooOO0 oooOO04 = OooOO0.f398OooO00o;
            if (oooOO04.o0000OOo().OooO00o() == null) {
                return;
            }
            Integer OooO00o5 = oooOO04.o0000OOo().OooO00o();
            Intrinsics.checkNotNull(OooO00o5);
            int intValue4 = OooO00o5.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0OO;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o0000OOo = oooOO04.o0000OOo();
            if (intValue4 >= 0) {
                i5 = Integer.valueOf(intValue4);
            } else {
                i5 = 0;
            }
            o0000OOo.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i5);
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void volumeUp(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (i == 1) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  micVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   micVolumeUp");
            }
            OooOO0 oooOO0 = OooOO0.f398OooO00o;
            if (oooOO0.o0000OOO().OooO00o() == null) {
                return;
            }
            Integer OooO00o2 = oooOO0.o0000OOO().OooO00o();
            Intrinsics.checkNotNull(OooO00o2);
            int intValue = OooO00o2.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.f310OooO00o;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o0000OOO = oooOO0.o0000OOO();
            if (intValue <= 100) {
                i2 = Integer.valueOf(intValue);
            } else {
                i2 = 100;
            }
            o0000OOO.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i2);
        } else if (i == 2) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef1ReverbVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   aef1ReverbVolumeUp");
            }
            OooOO0 oooOO02 = OooOO0.f398OooO00o;
            if (oooOO02.OooO0O0().OooO00o() == null) {
                return;
            }
            Integer OooO00o3 = oooOO02.OooO0O0().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            int intValue2 = OooO00o3.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> OooO0O0 = oooOO02.OooO0O0();
            if (intValue2 <= 100) {
                i3 = Integer.valueOf(intValue2);
            } else {
                i3 = 100;
            }
            OooO0O0.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i3);
        } else if (i == 3) {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef2ReverbVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   aef2ReverbVolumeUp");
            }
            OooOO0 oooOO03 = OooOO0.f398OooO00o;
            if (oooOO03.o00O0O().OooO00o() == null) {
                return;
            }
            Integer OooO00o4 = oooOO03.o00O0O().OooO00o();
            Intrinsics.checkNotNull(OooO00o4);
            int intValue3 = OooO00o4.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o00O0O = oooOO03.o00O0O();
            if (intValue3 <= 100) {
                i4 = Integer.valueOf(intValue3);
            } else {
                i4 = 100;
            }
            o00O0O.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i4);
        } else if (i != 4) {
        } else {
            Intrinsics.checkNotNullParameter("AdjustBySO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  laBaMicCompanyVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustBySO ->   laBaMicCompanyVolumeUp");
            }
            OooOO0 oooOO04 = OooOO0.f398OooO00o;
            if (oooOO04.o0000OOo().OooO00o() == null) {
                return;
            }
            Integer OooO00o5 = oooOO04.o0000OOo().OooO00o();
            Intrinsics.checkNotNull(OooO00o5);
            int intValue4 = OooO00o5.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0OO;
            OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer> o0000OOo = oooOO04.o0000OOo();
            if (intValue4 <= 100) {
                i5 = Integer.valueOf(intValue4);
            } else {
                i5 = 100;
            }
            o0000OOo.OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) i5);
        }
    }
}
