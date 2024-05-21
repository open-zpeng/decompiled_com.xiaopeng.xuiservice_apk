package OooO00o.OooO00o.OooO00o.OooO00o;
/* loaded from: classes.dex */
public class OooOO0O {
    public static boolean OooO00o(String str) {
        if (str != null) {
            int length = str.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
