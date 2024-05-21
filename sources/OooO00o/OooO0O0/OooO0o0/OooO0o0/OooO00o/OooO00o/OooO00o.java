package OooO00o.OooO0O0.OooO0o0.OooO0O0.OooO00o.OooO00o;

import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO0o;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.loostone.libtuning.data.config.vendor.EnvironmentalSoundItem;
import com.loostone.libtuning.inf.extern.IVolumeAdjustment;
import com.lzy.okgo.model.Progress;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class OooO00o implements IVolumeAdjustment {
    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef1ReverbOutGain() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0O0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorAttack() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0OO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorPregain() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0o0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorRatio() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorRelease() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0oO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2CompressorThreshold() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ1() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ1Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ2() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ2Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ3() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ3Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ4() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ4Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ5() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ5Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ6() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOo00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ6Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOOoo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ7() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOo0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ7Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ8() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ8Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ9() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EQ9Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOoO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoFeedBack() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOoo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoGain() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoTime() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2EchoType() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo000().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ1() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo00o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ1Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo00O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ2() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0O0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ2Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ3() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0o0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ3Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0OO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ4() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0oO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ4Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ5() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ5Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooo0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ6() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ6Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooO00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ7() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ7Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ8() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o000oOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ8Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ9() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2MusicEQ9Frequency() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbMode() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Ooooo00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbTime() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOoo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2ReverbVolume() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Ooooo0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef2VoiceMode() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooooO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ1Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ2Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooooO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ3Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Ooooooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ4Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0OoOo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ5Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.ooOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ6Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00O0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ7Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00Oo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ8Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00Ooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EQ9Type() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00o0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoHicut() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooooOO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoLowcut() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooooOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EchoStwide() {
        Integer OooO00o2 = OooO0o.f313OooO00o.Oooooo0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ1() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00ooo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ2() {
        Integer OooO00o2 = OooO0o.f313OooO00o.oo000o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ3() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00oO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ4() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o00oO0O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ5() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0ooOO0().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ6() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0ooOOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ7() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0ooOoO().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ8() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0OOO0o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3EqQ9() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0Oo0oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbFsEnable() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbHicut() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0OO00O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbHidamp() {
        Integer OooO00o2 = OooO0o.f313OooO00o.oo0o0Oo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbLowcut() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o0O0O00().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef3ReverbStwide() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o000OOo().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4BypassEnable() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4ReverbVolume() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getAef4SceneEffectMode() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getBalance() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getCarOutPregainAdd() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getEnvironmentalSound() {
        return 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getMicVolume() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o000000O().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getMusicVolume() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getRecordVocalOffset() {
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public int getSystemVolume() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o000000o().OooO00o();
        if (OooO00o2 == null) {
            return 0;
        }
        return OooO00o2.intValue();
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef1ReverbEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO00o().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2CompressorEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooO0Oo().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2EQEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOoOO().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2EchoEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OooOoo0().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2MusicEQEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOo0().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2ReverbEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.OoooOoO().OooO00o();
        return OooO00o2 != null && OooO00o2.intValue() == 1;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isAef2VoiceChangeEnable() {
        return true;
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public boolean isMicVolumeEnable() {
        Integer OooO00o2 = OooO0o.f313OooO00o.o000000().OooO00o();
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
        OooO0o.f313OooO00o.OooO00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef1ReverbOutGain(int i) {
        OooO0o.f313OooO00o.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorAttack(int i) {
        OooO0o.f313OooO00o.OooO0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorEnable(boolean z) {
        OooO0o.f313OooO00o.OooO0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorPregain(int i) {
        OooO0o.f313OooO00o.OooO0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorRatio(int i) {
        OooO0o.f313OooO00o.OooO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorRelease(int i) {
        OooO0o.f313OooO00o.OooO0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2CompressorThreshold(int i) {
        OooO0o.f313OooO00o.OooO0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ1(int i) {
        OooO0o.f313OooO00o.OooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ1Frequency(int i) {
        OooO0o.f313OooO00o.OooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ2(int i) {
        OooO0o.f313OooO00o.OooOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ2Frequency(int i) {
        OooO0o.f313OooO00o.OooOO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ3(int i) {
        OooO0o.f313OooO00o.OooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ3Frequency(int i) {
        OooO0o.f313OooO00o.OooOOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ4(int i) {
        OooO0o.f313OooO00o.OooOOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ4Frequency(int i) {
        OooO0o.f313OooO00o.OooOOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ5(int i) {
        OooO0o.f313OooO00o.OooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ5Frequency(int i) {
        OooO0o.f313OooO00o.OooOOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ6(int i) {
        OooO0o.f313OooO00o.OooOo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ6Frequency(int i) {
        OooO0o.f313OooO00o.OooOOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ7(int i) {
        OooO0o.f313OooO00o.OooOo0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ7Frequency(int i) {
        OooO0o.f313OooO00o.OooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ8(int i) {
        OooO0o.f313OooO00o.OooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ8Frequency(int i) {
        OooO0o.f313OooO00o.OooOo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ9(int i) {
        OooO0o.f313OooO00o.OooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQ9Frequency(int i) {
        OooO0o.f313OooO00o.OooOoO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EQEnable(boolean z) {
        OooO0o.f313OooO00o.OooOoOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoEnable(boolean z) {
        OooO0o.f313OooO00o.OooOoo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoFeedBack(int i) {
        OooO0o.f313OooO00o.OooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoGain(int i) {
        OooO0o.f313OooO00o.OooOooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoTime(int i) {
        OooO0o.f313OooO00o.OooOooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2EchoType(int i) {
        OooO0o.f313OooO00o.Oooo000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ1(int i) {
        OooO0o.f313OooO00o.Oooo00o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ1Frequency(int i) {
        OooO0o.f313OooO00o.Oooo00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ2(int i) {
        OooO0o.f313OooO00o.Oooo0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ2Frequency(int i) {
        OooO0o.f313OooO00o.Oooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ3(int i) {
        OooO0o.f313OooO00o.Oooo0o0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ3Frequency(int i) {
        OooO0o.f313OooO00o.Oooo0OO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ4(int i) {
        OooO0o.f313OooO00o.Oooo0oO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ4Frequency(int i) {
        OooO0o.f313OooO00o.Oooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ5(int i) {
        OooO0o.f313OooO00o.Oooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ5Frequency(int i) {
        OooO0o.f313OooO00o.Oooo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ6(int i) {
        OooO0o.f313OooO00o.OoooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ6Frequency(int i) {
        OooO0o.f313OooO00o.OoooO00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ7(int i) {
        OooO0o.f313OooO00o.OoooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ7Frequency(int i) {
        OooO0o.f313OooO00o.OoooO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ8(int i) {
        OooO0o.f313OooO00o.o000oOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ8Frequency(int i) {
        OooO0o.f313OooO00o.OoooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ9(int i) {
        OooO0o.f313OooO00o.OoooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQ9Frequency(int i) {
        OooO0o.f313OooO00o.OoooOOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2MusicEQEnable(boolean z) {
        OooO0o.f313OooO00o.OoooOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 2));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbEnable(boolean z) {
        OooO0o.f313OooO00o.OoooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbMode(int i) {
        OooO0o.f313OooO00o.Ooooo00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbTime(int i) {
        OooO0o.f313OooO00o.OoooOoo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2ReverbVolume(int i) {
        OooO0o.f313OooO00o.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2VoiceChangeEnable(boolean z) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef2VoiceMode(int i) {
        OooO0o.f313OooO00o.OooooO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ1Type(int i) {
        OooO0o.f313OooO00o.Oooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ2Type(int i) {
        OooO0o.f313OooO00o.OoooooO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ3Type(int i) {
        OooO0o.f313OooO00o.Ooooooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ4Type(int i) {
        OooO0o.f313OooO00o.o0OoOo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ5Type(int i) {
        OooO0o.f313OooO00o.ooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ6Type(int i) {
        OooO0o.f313OooO00o.o00O0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ7Type(int i) {
        OooO0o.f313OooO00o.o00Oo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ8Type(int i) {
        OooO0o.f313OooO00o.o00Ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EQ9Type(int i) {
        OooO0o.f313OooO00o.o00o0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoHicut(int i) {
        OooO0o.f313OooO00o.OooooOO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoLowcut(int i) {
        OooO0o.f313OooO00o.OooooOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EchoStwide(int i) {
        OooO0o.f313OooO00o.Oooooo0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ1(int i) {
        OooO0o.f313OooO00o.o00ooo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ2(int i) {
        OooO0o.f313OooO00o.oo000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ3(int i) {
        OooO0o.f313OooO00o.o00oO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ4(int i) {
        OooO0o.f313OooO00o.o00oO0O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ5(int i) {
        OooO0o.f313OooO00o.o0ooOO0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ6(int i) {
        OooO0o.f313OooO00o.o0ooOOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ7(int i) {
        OooO0o.f313OooO00o.o0ooOoO().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ8(int i) {
        OooO0o.f313OooO00o.o0OOO0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3EqQ9(int i) {
        OooO0o.f313OooO00o.o0Oo0oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbFsEnable(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbHicut(int i) {
        OooO0o.f313OooO00o.o0OO00O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbHidamp(int i) {
        OooO0o.f313OooO00o.oo0o0Oo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbLowcut(int i) {
        OooO0o.f313OooO00o.o0O0O00().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef3ReverbStwide(int i) {
        OooO0o.f313OooO00o.o000OOo().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4BypassEnable(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4ReverbVolume(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setAef4SceneEffectMode(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setBalance(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setDeviceCard(@NotNull String deviceCard) {
        Intrinsics.checkNotNullParameter(deviceCard, "deviceCard");
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setEnvironmentalSound(int i, double d) {
        EnvironmentalSoundItem environmentalSoundItem;
        if (OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0 == null) {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  环境音效没有初始化", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 5) {
                Log.e("LogTuning", "AdjustByKO ->   环境音效没有初始化");
                return;
            }
            return;
        }
        if (i == 0) {
            setAef2ReverbEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList);
            environmentalSoundItem = arrayList.get(0);
        } else if (i == 1) {
            setAef2ReverbEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList2 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList2);
            environmentalSoundItem = arrayList2.get(1);
        } else if (i == 2) {
            setAef2ReverbEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList3 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList3);
            environmentalSoundItem = arrayList3.get(2);
        } else if (i == 3) {
            setAef2ReverbEnable(false);
            ArrayList<EnvironmentalSoundItem> arrayList4 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList4);
            environmentalSoundItem = arrayList4.get(3);
        } else if (i != 4) {
            setAef2ReverbEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList5 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList5);
            environmentalSoundItem = arrayList5.get(1);
        } else {
            setAef2ReverbEnable(true);
            ArrayList<EnvironmentalSoundItem> arrayList6 = OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0o.OooO0OO.OooO0O0;
            Intrinsics.checkNotNull(arrayList6);
            environmentalSoundItem = arrayList6.get(4);
        }
        Intrinsics.checkNotNullExpressionValue(environmentalSoundItem, "when (mode) {\n            Constant.EnvironmentalSound.STUDIO -> {\n                setAef2ReverbEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![0]\n            }\n\n            Constant.EnvironmentalSound.KTV -> {\n                setAef2ReverbEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![1]\n            }\n\n            Constant.EnvironmentalSound.CONCERT -> {\n                setAef2ReverbEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![2]\n            }\n\n            Constant.EnvironmentalSound.ORIGIN -> {\n                setAef2ReverbEnable(false)\n\n                EnvironmentalSound.environmentalSounds!![3]\n            }\n\n            Constant.EnvironmentalSound.CAVE -> {\n                setAef2ReverbEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![4]\n            }\n\n            else -> {\n                setAef2ReverbEnable(true)\n\n                EnvironmentalSound.environmentalSounds!![1]\n            }\n        }");
        setAef2ReverbMode(environmentalSoundItem.getAef2ReverbMode());
        setAef2ReverbVolume((int) (environmentalSoundItem.getAef2ReverbVolume() * d));
        setAef2ReverbTime(environmentalSoundItem.getAef2ReverbTime());
        setAef2EchoType(environmentalSoundItem.getAef2EchoMode());
        setAef2EchoGain((int) (environmentalSoundItem.getAef2EchoGain() * d));
        setAef2EchoTime(environmentalSoundItem.getAef2EchoTime());
        setAef2EchoFeedBack(environmentalSoundItem.getAef2EchoFeedback());
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMicVolume(int i) {
        OooO0o.f313OooO00o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMicVolumeEnable(boolean z) {
        OooO0o.f313OooO00o.o000000().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(z ? 1 : 0));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setMusicVolume(int i) {
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void setSystemVolume(int i) {
        OooO0o.f313OooO00o.o000000o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) Integer.valueOf(i));
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void volumeDown(int i) {
        if (i == 1) {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  micVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   micVolumeDown");
            }
            OooO0o oooO0o = OooO0o.f313OooO00o;
            if (oooO0o.o000000O().OooO00o() == null) {
                return;
            }
            Integer OooO00o2 = oooO0o.o000000O().OooO00o();
            Intrinsics.checkNotNull(OooO00o2);
            int intValue = OooO00o2.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.f310OooO00o;
            oooO0o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue >= 0 ? Integer.valueOf(intValue) : 0));
        } else if (i == 2) {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef1ReverbVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   aef1ReverbVolumeDown");
            }
            OooO0o oooO0o2 = OooO0o.f313OooO00o;
            if (oooO0o2.OooO0O0().OooO00o() == null) {
                return;
            }
            Integer OooO00o3 = oooO0o2.OooO0O0().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            int intValue2 = OooO00o3.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            oooO0o2.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue2 >= 0 ? Integer.valueOf(intValue2) : 0));
        } else if (i != 3) {
        } else {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef2ReverbVolumeDown", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   aef2ReverbVolumeDown");
            }
            OooO0o oooO0o3 = OooO0o.f313OooO00o;
            if (oooO0o3.Ooooo0o().OooO00o() == null) {
                return;
            }
            Integer OooO00o4 = oooO0o3.Ooooo0o().OooO00o();
            Intrinsics.checkNotNull(OooO00o4);
            int intValue3 = OooO00o4.intValue() - OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            oooO0o3.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue3 >= 0 ? Integer.valueOf(intValue3) : 0));
        }
    }

    @Override // com.loostone.libtuning.inf.extern.IVolumeAdjustment
    public void volumeUp(int i) {
        if (i == 1) {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  micVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   micVolumeUp");
            }
            OooO0o oooO0o = OooO0o.f313OooO00o;
            if (oooO0o.o000000O().OooO00o() == null) {
                return;
            }
            Integer OooO00o2 = oooO0o.o000000O().OooO00o();
            Intrinsics.checkNotNull(OooO00o2);
            int intValue = OooO00o2.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.f310OooO00o;
            oooO0o.o000000O().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue <= 100 ? Integer.valueOf(intValue) : 100));
        } else if (i == 2) {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef1ReverbVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   aef1ReverbVolumeUp");
            }
            OooO0o oooO0o2 = OooO0o.f313OooO00o;
            if (oooO0o2.OooO0O0().OooO00o() == null) {
                return;
            }
            Integer OooO00o3 = oooO0o2.OooO0O0().OooO00o();
            Intrinsics.checkNotNull(OooO00o3);
            int intValue2 = OooO00o3.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            oooO0o2.OooO0O0().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue2 <= 100 ? Integer.valueOf(intValue2) : 100));
        } else if (i != 3) {
        } else {
            Intrinsics.checkNotNullParameter("AdjustByKO", Progress.TAG);
            Intrinsics.checkNotNullParameter("  aef2ReverbVolumeUp", NotificationCompat.CATEGORY_MESSAGE);
            if (OooO00o.OooO0O0.OooO0O0.OooO0O0.OooO0O0 <= 3) {
                Log.i("LogTuning", "AdjustByKO ->   aef2ReverbVolumeUp");
            }
            OooO0o oooO0o3 = OooO0o.f313OooO00o;
            if (oooO0o3.Ooooo0o().OooO00o() == null) {
                return;
            }
            Integer OooO00o4 = oooO0o3.Ooooo0o().OooO00o();
            Intrinsics.checkNotNull(OooO00o4);
            int intValue3 = OooO00o4.intValue() + OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooO00o.OooO0O0;
            oooO0o3.Ooooo0o().OooO00o((OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o<Integer>) (intValue3 <= 100 ? Integer.valueOf(intValue3) : 100));
        }
    }
}
