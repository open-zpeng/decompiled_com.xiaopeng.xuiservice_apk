package com.loostone.libtuning.channel.byd.light;

import OooO00o.OooO0O0.OooO0O0.OooO0Oo.OooO0O0.OooO0O0;
import OooO00o.OooO0O0.OooO0O0.OooO0o0.OooO0o0.OooO00o;
import OooO00o.OooO0O0.OooO0o0.OooO0OO.OooO00o.OooOO0;
import OooO00o.OooO0O0.OooO0o0.OooO0o0.OooO0O0.o0O0o000;
import com.loostone.libtuning.inf.extern.IAudioTempoProcess;
import com.loostone.libtuning.inf.extern.ITempoCallBack;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes4.dex */
public class SampleTempoProcess implements IAudioTempoProcess {
    private static final String TAG = "SampleTempo";
    private int brightness;
    private ITempoCallBack callBack;
    private boolean colorChange;
    private int colorIndex;
    private int[] colors;
    private final OooO0O0 command;
    private boolean shineFlag;

    public SampleTempoProcess() {
        OooO0O0.OooO0o version = OooO0O0.OooO0o.DEFAULT;
        Intrinsics.checkNotNullParameter(version, "version");
        String format = String.format("%03d", Arrays.copyOf(new Object[]{60}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
        String format2 = String.format("%03d", Arrays.copyOf(new Object[]{71}, 1));
        Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(format, *args)");
        String format3 = String.format("%02d", Arrays.copyOf(new Object[]{4}, 1));
        Intrinsics.checkNotNullExpressionValue(format3, "java.lang.String.format(format, *args)");
        OooO0O0.OooO0OO sign = OooO0O0.OooO0OO.READ;
        Intrinsics.checkNotNullParameter(sign, "sign");
        OooO0O0.EnumC0003OooO0O0 type = OooO0O0.EnumC0003OooO0O0.NONE;
        Intrinsics.checkNotNullParameter(type, "type");
        OooO0O0 oooO0O0 = new OooO0O0();
        oooO0O0.f230OooO00o = format;
        oooO0O0.OooO0O0 = format2;
        oooO0O0.OooO0OO = format3;
        oooO0O0.OooO0Oo = 1;
        oooO0O0.OooO0o0 = 0;
        oooO0O0.OooO0o = "000";
        oooO0O0.OooO0oO = "";
        this.command = oooO0O0;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void deinit() {
        o0O0o000.f584OooO00o.OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(71).OooO0O0(1).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o("3").OooO00o());
        this.shineFlag = false;
        if (this.callBack != null) {
            this.callBack = null;
        }
        OooOO0.f398OooO00o.OoooooO().OooO00o((OooO00o<Boolean>) Boolean.FALSE);
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public int getBrightness() {
        int[] iArr;
        o0O0o000 o0o0o000 = o0O0o000.f584OooO00o;
        OooO0O0 command = this.command;
        Intrinsics.checkNotNullParameter(command, "command");
        String OooO00o2 = o0o0o000.OooO00o().OooO00o(command);
        if (OooO00o2.length() < 14 || !"1".equals(String.valueOf(OooO00o2.charAt(0)))) {
            iArr = null;
        } else {
            String substring = OooO00o2.substring(1, 4);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            iArr = new int[]{Integer.parseInt(substring), Integer.parseInt(String.valueOf(OooO00o2.charAt(13)))};
        }
        if (iArr != null && iArr.length == 2) {
            this.brightness = iArr[0];
            this.colorChange = iArr[1] == 1;
        }
        return this.brightness;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public int getColor() {
        int[] iArr = this.colors;
        if (iArr != null) {
            if (this.colorIndex >= iArr.length) {
                this.colorIndex = 0;
            }
            return iArr[this.colorIndex];
        }
        return 0;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public boolean getColorChangeOrNot() {
        if (this.colorChange) {
            this.colorChange = false;
            int i = this.colorIndex + 1;
            this.colorIndex = i;
            if (i >= this.colors.length) {
                this.colorIndex = 0;
            }
            return true;
        }
        return false;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void init() {
        o0O0o000.f584OooO00o.OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(71).OooO0O0(1).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o("1").OooO00o());
        OooOO0.f398OooO00o.OoooooO().OooO00o((OooO00o<Boolean>) Boolean.TRUE);
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void initSong() {
        o0O0o000.f584OooO00o.OooO0O0(new OooO0O0.OooO00o().OooO00o(OooO0O0.OooO0o.DEFAULT).OooO00o(71).OooO0O0(1).OooO00o(OooO0O0.OooO0OO.WRITE).OooO00o(OooO0O0.EnumC0003OooO0O0.NONE).OooO00o("2").OooO00o());
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void setColors(int[] iArr) {
        this.colors = iArr;
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void setListener(final ITempoCallBack iTempoCallBack) {
        this.callBack = iTempoCallBack;
        if (this.shineFlag) {
            return;
        }
        this.shineFlag = true;
        new Thread(new Runnable() { // from class: com.loostone.libtuning.channel.byd.light.SampleTempoProcess.1
            @Override // java.lang.Runnable
            public void run() {
                while (SampleTempoProcess.this.shineFlag) {
                    ITempoCallBack iTempoCallBack2 = iTempoCallBack;
                    if (iTempoCallBack2 != null) {
                        iTempoCallBack2.setBrightness(SampleTempoProcess.this.getBrightness());
                        iTempoCallBack.setColor(SampleTempoProcess.this.getColor());
                        if (SampleTempoProcess.this.getColorChangeOrNot()) {
                            iTempoCallBack.colorChange();
                        }
                    }
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        SampleTempoProcess.this.shineFlag = false;
                        e.printStackTrace();
                    }
                }
                SampleTempoProcess.this.shineFlag = false;
            }
        }).start();
    }

    @Override // com.loostone.libtuning.inf.extern.IAudioTempoProcess
    public void setMode(int i, int i2) {
        int i3 = 0;
        if (i == 2) {
            this.colors = new int[ColorDefine.COLOR_GROUP2.length];
            while (true) {
                int[][] iArr = ColorDefine.COLOR_GROUP2;
                if (i3 >= iArr.length) {
                    return;
                }
                this.colors[i3] = ColorUtil.getColor(iArr[i3]);
                i3++;
            }
        } else if (i == 3) {
            this.colors = new int[ColorDefine.COLOR_GROUP3.length];
            while (true) {
                int[][] iArr2 = ColorDefine.COLOR_GROUP3;
                if (i3 >= iArr2.length) {
                    return;
                }
                this.colors[i3] = ColorUtil.getColor(iArr2[i3]);
                i3++;
            }
        } else if (i != 4) {
            this.colors = r3;
            int[] iArr3 = {ColorUtil.getColor(ColorDefine.COLOR_GROUP1[i2 % 31])};
        } else {
            this.colors = new int[ColorDefine.COLOR_GROUP4.length];
            while (true) {
                int[][] iArr4 = ColorDefine.COLOR_GROUP4;
                if (i3 >= iArr4.length) {
                    return;
                }
                this.colors[i3] = ColorUtil.getColor(iArr4[i3]);
                i3++;
            }
        }
    }
}
