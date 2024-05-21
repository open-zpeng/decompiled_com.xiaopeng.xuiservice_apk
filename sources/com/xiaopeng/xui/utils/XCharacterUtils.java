package com.xiaopeng.xui.utils;

import androidx.annotation.RestrictTo;
import com.blankj.utilcode.constant.RegexConstants;
@RestrictTo({RestrictTo.Scope.LIBRARY})
/* loaded from: classes5.dex */
public class XCharacterUtils {
    public static boolean isFullAngleUseRegex(char c) {
        return String.valueOf(c).matches(RegexConstants.REGEX_DOUBLE_BYTE_CHAR);
    }

    public static boolean isFullAngle(char c) {
        return c > 255;
    }

    public static boolean isFullAngleSymbol(char c) {
        return (c >= 65281 && c <= 65374) || c == 12288;
    }

    public static boolean isHalfAngleSymbol(char c) {
        return (c >= '!' && c <= '~') || c == ' ';
    }

    public static boolean isChinese(char c) {
        return c >= 19968 && c <= 40869;
    }

    public static boolean isNotEmoJi(char c) {
        return c == 0 || c == '\t' || c == '\n' || c == '\r' || (c >= ' ' && c <= 55295) || ((c >= 57344 && c <= 65533) || (c >= 0 && c <= 65535));
    }
}
