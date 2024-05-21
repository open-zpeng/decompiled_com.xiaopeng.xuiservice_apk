package com.xiaopeng.xuiservice.carcontrol;

import android.util.ArraySet;
import com.xiaopeng.xuiservice.XUIConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes5.dex */
public class CarPropertyId {
    private static final ArraySet<Integer> mHvacPropSetMain = new ArraySet<>(Arrays.asList(557849120, 358614275, 559946242));
    private static final ArraySet<Integer> mHvacPropSetIoT = new ArraySet<>(Arrays.asList(557849143, 557914680, 557849145, 557849150, 557849151));
    private static final ArraySet<Integer> mHvacPropSetOpenSdk = new ArraySet<>(Arrays.asList(557849130, 557849129, 557849127, 356517120, 557849101, 358614275, 559946242, 356517128, 557849115, 356517121, 557849126, 557849092));
    private static final ArraySet<Integer> mVcuSpecialProps = new ArraySet<>(Arrays.asList(557847064, 559944229, 557847063));
    private static final ArraySet<Integer> mXpDiagnosticProps = new ArraySet<>(Arrays.asList(560993284));
    private static final ArraySet<Integer> mCarTboxProps = new ArraySet<>(Arrays.asList(557912113, 557846597, 557846552, 554700817, 554700825, 557912117, 557846611));
    private static final ArraySet<Integer> mCarBcmProps = new ArraySet<>(Arrays.asList(557915161, 557849760, 557849640, 557849623, 557849624, 557849648, 557849607, 557849713, 557849762, 557849647, 557915328, 557849602, 557849940, 557849889, 557849641, 557849642, 557849643, 557849610, 559946855, 559946856, 559946854, 559946857, 557849903));
    private static final Map<String, ArraySet<Integer>> mModuleHvacPropMap = new HashMap();

    static {
        mModuleHvacPropMap.put(XUIConfig.BusinessModule.MODULE_MAIN, mHvacPropSetMain);
        mModuleHvacPropMap.put(XUIConfig.BusinessModule.MODULE_IOT, mHvacPropSetIoT);
        mModuleHvacPropMap.put(XUIConfig.BusinessModule.MODULE_OPENCAR, mHvacPropSetOpenSdk);
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForMcu() {
        char c;
        String businessModule = XUIConfig.BusinessModule.getBusinessModule();
        int hashCode = businessModule.hashCode();
        if (hashCode == -1263190710) {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_OPENCAR)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 104462) {
            if (hashCode == 3343801 && businessModule.equals(XUIConfig.BusinessModule.MODULE_MAIN)) {
                c = 2;
            }
            c = 65535;
        } else {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_IOT)) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return null;
            }
            return new ArraySet<>(Arrays.asList(557847561, 557847614, 557847635, 557849724, 557847651, 557847652));
        }
        return new ArraySet<>(Arrays.asList(557847561));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForHvac() {
        return mModuleHvacPropMap.get(XUIConfig.BusinessModule.getBusinessModule());
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForXpu() {
        return new ArraySet<>(Arrays.asList(557856777, 557856809, 557856792, 561002513));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForCiu() {
        return new ArraySet<>(Arrays.asList(557852712, 557852700));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForBcm() {
        char c;
        String businessModule = XUIConfig.BusinessModule.getBusinessModule();
        int hashCode = businessModule.hashCode();
        if (hashCode == -1263190710) {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_OPENCAR)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 104462) {
            if (hashCode == 3343801 && businessModule.equals(XUIConfig.BusinessModule.MODULE_MAIN)) {
                c = 2;
            }
            c = 65535;
        } else {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_IOT)) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return null;
            }
            return mCarBcmProps;
        }
        return new ArraySet<>(Arrays.asList(559946855, 559946856, 559946854, 559946857, 557915161, 557849610, 557849641, 557849642, 557849607, 557849638, 356517139, 557849701, 557849665));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForVcu() {
        char c;
        String businessModule = XUIConfig.BusinessModule.getBusinessModule();
        int hashCode = businessModule.hashCode();
        if (hashCode == -1263190710) {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_OPENCAR)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 104462) {
            if (hashCode == 3343801 && businessModule.equals(XUIConfig.BusinessModule.MODULE_MAIN)) {
                c = 2;
            }
            c = 65535;
        } else {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_IOT)) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return null;
            }
            return new ArraySet<>(Arrays.asList(559944229, 557847064, 557847042, 557847063, 559944315, 559944314, 559944326, 559944335, 557847045, 557847080, 557847056, 1, 557847081, 557847161));
        }
        return new ArraySet<>(Arrays.asList(557847056, 557847045, 559944229, 557847127, 557847057, 557847042, 557847064, 557847063));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForVcuSpecialIds() {
        return mVcuSpecialProps;
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForScu() {
        return new ArraySet<>(Arrays.asList(557917818, 557852199, 557852295, 557852201, 557852197, 557852198, 557852286, 557852287, 557852212, 557852299, 557852200, 557852187, 557852235, 557852302, 557852262, 557852326, 557852332, 557852331, 557852368, 557852370, 557852274, 557852302, 557852288, 557917782, 557917780, 557852394, 557852409, 557852300));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForTbox() {
        return mCarTboxProps;
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForSrs() {
        char c;
        String businessModule = XUIConfig.BusinessModule.getBusinessModule();
        int hashCode = businessModule.hashCode();
        if (hashCode == -1263190710) {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_OPENCAR)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 104462) {
            if (hashCode == 3343801 && businessModule.equals(XUIConfig.BusinessModule.MODULE_MAIN)) {
                c = 2;
            }
            c = 65535;
        } else {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_IOT)) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return null;
            }
            return new ArraySet<>(Arrays.asList(557849612, 557849613, 557849614, 557849615, 557849616, 557849679, 557849800, 557849801, 557849802));
        }
        return new ArraySet<>(Arrays.asList(557849679, 557849800, 557849801, 557849802, 557849612, 557849613, 557849614, 557849615, 557849616));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForEsp() {
        return new ArraySet<>(Arrays.asList(559948801, 557851651));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForInput() {
        return new ArraySet<>(Arrays.asList(557851186, 557851187, 557851188, 557851189, 557851176));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForAtl() {
        return new ArraySet<>(Arrays.asList(557848586));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForAmp() {
        return new ArraySet<>(Arrays.asList(557850649));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForEps() {
        return new ArraySet<>(Arrays.asList(559948806));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForIcm() {
        return new ArraySet<>(Arrays.asList(557848078, 554702353, 554702360));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForLlu() {
        return new ArraySet<>(Arrays.asList(557854209, 557854229, 557854217, 557847625, 557847631, 557847645, 557847627));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForVpm() {
        return new ArraySet<>(Arrays.asList(557852376, 557852375, 557852253, 557852252));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForXpDiagnostic() {
        return mXpDiagnosticProps;
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForTpms() {
        char c;
        String businessModule = XUIConfig.BusinessModule.getBusinessModule();
        int hashCode = businessModule.hashCode();
        if (hashCode == -1263190710) {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_OPENCAR)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode != 104462) {
            if (hashCode == 3343801 && businessModule.equals(XUIConfig.BusinessModule.MODULE_MAIN)) {
                c = 1;
            }
            c = 65535;
        } else {
            if (businessModule.equals(XUIConfig.BusinessModule.MODULE_IOT)) {
                c = 2;
            }
            c = 65535;
        }
        if (c == 0) {
            return new ArraySet<>(Arrays.asList(559947266, 559947267, 559947268, 559947269));
        }
        return new ArraySet<>(Arrays.asList(559947266, 559947267, 559947268, 559947269));
    }

    public static ArraySet<Integer> getRegisterPropertyIdsForMsm() {
        return new ArraySet<>(Arrays.asList(557849914, 557849917, 557849918, 557849920, 557849915, 557849916, 557849919, 557849921, 557849953, 557849954, 557849955, 557849956));
    }
}
