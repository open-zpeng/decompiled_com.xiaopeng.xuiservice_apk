package com.xiaopeng.xuiservice.utils;

import com.xiaopeng.btservice.bluetooth.NfControlDef;
import com.xiaopeng.lib.framework.moduleinterface.carcontroller.IInputController;
import com.xiaopeng.speech.protocol.bean.stats.StatCommonBean;
import com.xiaopeng.speech.protocol.node.navi.bean.NaviPreferenceBean;
import com.xiaopeng.xuiservice.smart.condition.operator.Operators;
import com.xiaopeng.xuiservice.uvccamera.usb.UVCCamera;
import com.xiaopeng.xuiservice.xapp.miniprog.AlipayConstants;
import java.util.ArrayList;
import java.util.Collections;
import okhttp3.internal.http.StatusLine;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.Videoio;
/* loaded from: classes5.dex */
public final class MccTable {
    static final String LOG_TAG = "MccTable";
    static ArrayList<MccEntry> sTable = new ArrayList<>(240);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public static class MccEntry implements Comparable<MccEntry> {
        final String mIso;
        final int mMcc;
        final int mSmallestDigitsMnc;

        MccEntry(int mnc, String iso, int smallestDigitsMCC) {
            if (iso == null) {
                throw new NullPointerException();
            }
            this.mMcc = mnc;
            this.mIso = iso;
            this.mSmallestDigitsMnc = smallestDigitsMCC;
        }

        @Override // java.lang.Comparable
        public int compareTo(MccEntry o) {
            return this.mMcc - o.mMcc;
        }
    }

    private static MccEntry entryForMcc(int mcc) {
        MccEntry m = new MccEntry(mcc, "", 0);
        int index = Collections.binarySearch(sTable, m);
        if (index < 0) {
            return null;
        }
        return sTable.get(index);
    }

    public static String countryCodeForMcc(int mcc) {
        MccEntry entry = entryForMcc(mcc);
        if (entry == null) {
            return "";
        }
        return entry.mIso;
    }

    static {
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_TUNNEL, "gr", 2));
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_FERRIES, "nl", 2));
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_CARPOOL, "be", 2));
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_HIGHWAY, "fr", 2));
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_COUNTRY_BORDER, "mc", 2));
        sTable.add(new MccEntry(NaviPreferenceBean.PATH_PREF_AVOID_COUNTRY_BORDER, "ad", 2));
        sTable.add(new MccEntry(214, "es", 2));
        sTable.add(new MccEntry(216, "hu", 2));
        sTable.add(new MccEntry(218, "ba", 2));
        sTable.add(new MccEntry(219, "hr", 2));
        sTable.add(new MccEntry(220, "rs", 2));
        sTable.add(new MccEntry(222, "it", 2));
        sTable.add(new MccEntry(225, "va", 2));
        sTable.add(new MccEntry(226, "ro", 2));
        sTable.add(new MccEntry(228, "ch", 2));
        sTable.add(new MccEntry(230, "cz", 2));
        sTable.add(new MccEntry(231, "sk", 2));
        sTable.add(new MccEntry(232, "at", 2));
        sTable.add(new MccEntry(234, "gb", 2));
        sTable.add(new MccEntry(235, "gb", 2));
        sTable.add(new MccEntry(238, "dk", 2));
        sTable.add(new MccEntry(240, "se", 2));
        sTable.add(new MccEntry(242, "no", 2));
        sTable.add(new MccEntry(244, "fi", 2));
        sTable.add(new MccEntry(246, Operators.OP_LT, 2));
        sTable.add(new MccEntry(247, "lv", 2));
        sTable.add(new MccEntry(248, "ee", 2));
        sTable.add(new MccEntry(250, "ru", 2));
        sTable.add(new MccEntry(255, "ua", 2));
        sTable.add(new MccEntry(257, "by", 2));
        sTable.add(new MccEntry(Imgcodecs.IMWRITE_TIFF_COMPRESSION, "md", 2));
        sTable.add(new MccEntry(260, "pl", 2));
        sTable.add(new MccEntry(262, "de", 2));
        sTable.add(new MccEntry(266, "gi", 2));
        sTable.add(new MccEntry(268, "pt", 2));
        sTable.add(new MccEntry(270, "lu", 2));
        sTable.add(new MccEntry(Imgcodecs.IMWRITE_JPEG2000_COMPRESSION_X1000, "ie", 2));
        sTable.add(new MccEntry(274, "is", 2));
        sTable.add(new MccEntry(276, "al", 2));
        sTable.add(new MccEntry(278, "mt", 2));
        sTable.add(new MccEntry(280, "cy", 2));
        sTable.add(new MccEntry(282, Operators.OP_GE, 2));
        sTable.add(new MccEntry(283, "am", 2));
        sTable.add(new MccEntry(284, "bg", 2));
        sTable.add(new MccEntry(286, "tr", 2));
        sTable.add(new MccEntry(288, "fo", 2));
        sTable.add(new MccEntry(289, Operators.OP_GE, 2));
        sTable.add(new MccEntry(290, "gl", 2));
        sTable.add(new MccEntry(292, "sm", 2));
        sTable.add(new MccEntry(293, "si", 2));
        sTable.add(new MccEntry(294, "mk", 2));
        sTable.add(new MccEntry(295, "li", 2));
        sTable.add(new MccEntry(297, "me", 2));
        sTable.add(new MccEntry(302, "ca", 3));
        sTable.add(new MccEntry(StatusLine.HTTP_PERM_REDIRECT, "pm", 2));
        sTable.add(new MccEntry(310, "us", 3));
        sTable.add(new MccEntry(311, "us", 3));
        sTable.add(new MccEntry(312, "us", 3));
        sTable.add(new MccEntry(313, "us", 3));
        sTable.add(new MccEntry(314, "us", 3));
        sTable.add(new MccEntry(315, "us", 3));
        sTable.add(new MccEntry(316, "us", 3));
        sTable.add(new MccEntry(NfControlDef.BOND_NONE, "pr", 2));
        sTable.add(new MccEntry(NfControlDef.BOND_BONDED, "vi", 2));
        sTable.add(new MccEntry(334, "mx", 3));
        sTable.add(new MccEntry(338, "jm", 3));
        sTable.add(new MccEntry(340, "gp", 2));
        sTable.add(new MccEntry(342, "bb", 3));
        sTable.add(new MccEntry(344, "ag", 3));
        sTable.add(new MccEntry(346, "ky", 3));
        sTable.add(new MccEntry(348, "vg", 3));
        sTable.add(new MccEntry(350, "bm", 2));
        sTable.add(new MccEntry(352, "gd", 2));
        sTable.add(new MccEntry(354, "ms", 2));
        sTable.add(new MccEntry(356, "kn", 2));
        sTable.add(new MccEntry(358, "lc", 2));
        sTable.add(new MccEntry(360, "vc", 2));
        sTable.add(new MccEntry(362, "ai", 2));
        sTable.add(new MccEntry(363, "aw", 2));
        sTable.add(new MccEntry(364, "bs", 2));
        sTable.add(new MccEntry(365, "ai", 3));
        sTable.add(new MccEntry(366, "dm", 2));
        sTable.add(new MccEntry(368, "cu", 2));
        sTable.add(new MccEntry(370, "do", 2));
        sTable.add(new MccEntry(372, "ht", 2));
        sTable.add(new MccEntry(374, "tt", 2));
        sTable.add(new MccEntry(376, "tc", 2));
        sTable.add(new MccEntry(400, "az", 2));
        sTable.add(new MccEntry(401, "kz", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_OFFSET_X, "bt", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TRG_SOURCE, Operators.OP_IN, 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TRG_SOFTWARE, Operators.OP_IN, 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_GPI_SELECTOR, Operators.OP_IN, 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_GPO_MODE, "pk", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_LED_MODE, "af", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_MANUAL_WB, "lk", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AUTO_WB, "mm", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AEAG, "lb", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_EXP_PRIORITY, "jo", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AE_MAX_LIMIT, "sy", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AG_MAX_LIMIT, "iq", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AEAG_LEVEL, "kw", 2));
        sTable.add(new MccEntry(420, "sa", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_EXPOSURE, "ye", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_EXPOSURE_BURST_COUNT, "om", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_GAIN_SELECTOR, "ps", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_GAIN, "ae", 2));
        sTable.add(new MccEntry(425, "il", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_DOWNSAMPLING_TYPE, "bh", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BINNING_SELECTOR, "qa", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BINNING_VERTICAL, "mn", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BINNING_HORIZONTAL, "np", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BINNING_PATTERN, "ae", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_DECIMATION_SELECTOR, "ae", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_DECIMATION_VERTICAL, "ir", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_DECIMATION_PATTERN, "uz", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_SHUTTER_TYPE, "tj", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_SENSOR_TAPS, "kg", 2));
        sTable.add(new MccEntry(438, "tm", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AEAG_ROI_OFFSET_Y, "jp", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AEAG_ROI_WIDTH, "jp", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_WB_KB, "kr", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_HEIGHT, "vn", 2));
        sTable.add(new MccEntry(454, "hk", 2));
        sTable.add(new MccEntry(455, "mo", 2));
        sTable.add(new MccEntry(456, "kh", 2));
        sTable.add(new MccEntry(457, "la", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_SENSOR_DATA_BIT_DEPTH, "cn", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_OUTPUT_DATA_BIT_DEPTH, "cn", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_COOLING, "tw", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TARGET_TEMP, "kp", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_CMS, "bd", 2));
        sTable.add(new MccEntry(472, "mv", 2));
        sTable.add(new MccEntry(IInputController.KEYCODE_KNOB_BACKLIGHT_UP, "my", 2));
        sTable.add(new MccEntry(IInputController.KEYCODE_KNOB_TEMP_DOWN, "au", 2));
        sTable.add(new MccEntry(510, "id", 2));
        sTable.add(new MccEntry(514, "tl", 2));
        sTable.add(new MccEntry(515, "ph", 2));
        sTable.add(new MccEntry(IInputController.KEYCODE_KNOB_VOL_UP, "th", 2));
        sTable.add(new MccEntry(525, "sg", 2));
        sTable.add(new MccEntry(528, "bn", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_IMAGE_PAYLOAD_SIZE, "nz", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_SENSOR_OUTPUT_CHANNEL_COUNT, "mp", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_FRAMERATE, "gu", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_COUNTER_SELECTOR, "nr", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_COUNTER_VALUE, "pg", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_AVAILABLE_BANDWIDTH, "to", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BUFFER_POLICY, "sb", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_LUT_EN, "vu", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_LUT_INDEX, "fj", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_LUT_VALUE, "wf", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TRG_DELAY, "as", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TS_RST_MODE, "ki", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_TS_RST_SOURCE, "nc", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_IS_DEVICE_EXIST, "pf", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_ACQ_BUFFER_SIZE, "ck", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_ACQ_BUFFER_SIZE_UNIT, "ws", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_ACQ_TRANSPORT_BUFFER_SIZE, "fm", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_BUFFERS_QUEUE_SIZE, "mh", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_ACQ_TRANSPORT_BUFFER_COMMIT, "pw", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_RECENT_FRAME, "tv", 2));
        sTable.add(new MccEntry(Videoio.CAP_PROP_XI_COLUMN_FPN_CORRECTION, "nu", 2));
        sTable.add(new MccEntry(602, "eg", 2));
        sTable.add(new MccEntry(StatCommonBean.EVENT_SCENE_SETTING_SWITCH_ID, "dz", 2));
        sTable.add(new MccEntry(604, "ma", 2));
        sTable.add(new MccEntry(605, "tn", 2));
        sTable.add(new MccEntry(606, "ly", 2));
        sTable.add(new MccEntry(607, "gm", 2));
        sTable.add(new MccEntry(608, "sn", 2));
        sTable.add(new MccEntry(609, "mr", 2));
        sTable.add(new MccEntry(AlipayConstants.CONFIG_WIDTH, "ml", 2));
        sTable.add(new MccEntry(611, "gn", 2));
        sTable.add(new MccEntry(612, "ci", 2));
        sTable.add(new MccEntry(613, "bf", 2));
        sTable.add(new MccEntry(614, "ne", 2));
        sTable.add(new MccEntry(615, "tg", 2));
        sTable.add(new MccEntry(616, "bj", 2));
        sTable.add(new MccEntry(617, "mu", 2));
        sTable.add(new MccEntry(618, "lr", 2));
        sTable.add(new MccEntry(619, "sl", 2));
        sTable.add(new MccEntry(620, "gh", 2));
        sTable.add(new MccEntry(621, "ng", 2));
        sTable.add(new MccEntry(622, "td", 2));
        sTable.add(new MccEntry(623, "cf", 2));
        sTable.add(new MccEntry(624, "cm", 2));
        sTable.add(new MccEntry(625, "cv", 2));
        sTable.add(new MccEntry(626, "st", 2));
        sTable.add(new MccEntry(627, "gq", 2));
        sTable.add(new MccEntry(628, "ga", 2));
        sTable.add(new MccEntry(629, "cg", 2));
        sTable.add(new MccEntry(630, "cd", 2));
        sTable.add(new MccEntry(631, "ao", 2));
        sTable.add(new MccEntry(632, "gw", 2));
        sTable.add(new MccEntry(633, "sc", 2));
        sTable.add(new MccEntry(634, "sd", 2));
        sTable.add(new MccEntry(635, "rw", 2));
        sTable.add(new MccEntry(636, "et", 2));
        sTable.add(new MccEntry(637, "so", 2));
        sTable.add(new MccEntry(638, "dj", 2));
        sTable.add(new MccEntry(639, "ke", 2));
        sTable.add(new MccEntry(UVCCamera.DEFAULT_PREVIEW_WIDTH, "tz", 2));
        sTable.add(new MccEntry(641, "ug", 2));
        sTable.add(new MccEntry(642, "bi", 2));
        sTable.add(new MccEntry(643, "mz", 2));
        sTable.add(new MccEntry(645, "zm", 2));
        sTable.add(new MccEntry(646, "mg", 2));
        sTable.add(new MccEntry(647, "re", 2));
        sTable.add(new MccEntry(648, "zw", 2));
        sTable.add(new MccEntry(649, "na", 2));
        sTable.add(new MccEntry(650, "mw", 2));
        sTable.add(new MccEntry(651, "ls", 2));
        sTable.add(new MccEntry(652, "bw", 2));
        sTable.add(new MccEntry(653, "sz", 2));
        sTable.add(new MccEntry(654, "km", 2));
        sTable.add(new MccEntry(655, "za", 2));
        sTable.add(new MccEntry(657, "er", 2));
        sTable.add(new MccEntry(658, "sh", 2));
        sTable.add(new MccEntry(659, "ss", 2));
        sTable.add(new MccEntry(702, "bz", 2));
        sTable.add(new MccEntry(704, Operators.OP_GT, 2));
        sTable.add(new MccEntry(706, "sv", 2));
        sTable.add(new MccEntry(708, "hn", 3));
        sTable.add(new MccEntry(710, "ni", 2));
        sTable.add(new MccEntry(712, "cr", 2));
        sTable.add(new MccEntry(714, "pa", 2));
        sTable.add(new MccEntry(716, "pe", 2));
        sTable.add(new MccEntry(722, ArchiveStreamFactory.AR, 3));
        sTable.add(new MccEntry(724, "br", 2));
        sTable.add(new MccEntry(730, "cl", 2));
        sTable.add(new MccEntry(732, "co", 3));
        sTable.add(new MccEntry(734, "ve", 2));
        sTable.add(new MccEntry(736, "bo", 2));
        sTable.add(new MccEntry(738, "gy", 2));
        sTable.add(new MccEntry(740, "ec", 2));
        sTable.add(new MccEntry(742, "gf", 2));
        sTable.add(new MccEntry(744, "py", 2));
        sTable.add(new MccEntry(746, "sr", 2));
        sTable.add(new MccEntry(748, "uy", 2));
        sTable.add(new MccEntry(750, "fk", 2));
        Collections.sort(sTable);
    }
}
