package com.blankj.utilcode.util;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.Utils;
import java.util.List;
/* loaded from: classes4.dex */
public final class ShellUtils {
    private static final String LINE_SEP = System.getProperty("line.separator");

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Utils.Task<CommandResult> execCmdAsync(String command, boolean isRooted, Utils.Consumer<CommandResult> consumer) {
        return execCmdAsync(new String[]{command}, isRooted, true, consumer);
    }

    public static Utils.Task<CommandResult> execCmdAsync(List<String> commands, boolean isRooted, Utils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands == null ? null : (String[]) commands.toArray(new String[0]), isRooted, true, consumer);
    }

    public static Utils.Task<CommandResult> execCmdAsync(String[] commands, boolean isRooted, Utils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands, isRooted, true, consumer);
    }

    public static Utils.Task<CommandResult> execCmdAsync(String command, boolean isRooted, boolean isNeedResultMsg, Utils.Consumer<CommandResult> consumer) {
        return execCmdAsync(new String[]{command}, isRooted, isNeedResultMsg, consumer);
    }

    public static Utils.Task<CommandResult> execCmdAsync(List<String> commands, boolean isRooted, boolean isNeedResultMsg, Utils.Consumer<CommandResult> consumer) {
        return execCmdAsync(commands == null ? null : (String[]) commands.toArray(new String[0]), isRooted, isNeedResultMsg, consumer);
    }

    public static Utils.Task<CommandResult> execCmdAsync(final String[] commands, final boolean isRooted, final boolean isNeedResultMsg, @NonNull Utils.Consumer<CommandResult> consumer) {
        if (consumer == null) {
            throw new NullPointerException("Argument 'consumer' of type Utils.Consumer<CommandResult> (#3 out of 4, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        return UtilsBridge.doAsync(new Utils.Task<CommandResult>(consumer) { // from class: com.blankj.utilcode.util.ShellUtils.1
            @Override // com.blankj.utilcode.util.ThreadUtils.Task
            public CommandResult doInBackground() {
                return ShellUtils.execCmd(commands, isRooted, isNeedResultMsg);
            }
        });
    }

    public static CommandResult execCmd(String command, boolean isRooted) {
        return execCmd(new String[]{command}, isRooted, true);
    }

    public static CommandResult execCmd(String command, List<String> envp, boolean isRooted) {
        return execCmd(new String[]{command}, envp == null ? null : (String[]) envp.toArray(new String[0]), isRooted, true);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRooted) {
        return execCmd(commands == null ? null : (String[]) commands.toArray(new String[0]), isRooted, true);
    }

    public static CommandResult execCmd(List<String> commands, List<String> envp, boolean isRooted) {
        String[] strArr = null;
        String[] strArr2 = commands == null ? null : (String[]) commands.toArray(new String[0]);
        if (envp != null) {
            strArr = (String[]) envp.toArray(new String[0]);
        }
        return execCmd(strArr2, strArr, isRooted, true);
    }

    public static CommandResult execCmd(String[] commands, boolean isRooted) {
        return execCmd(commands, isRooted, true);
    }

    public static CommandResult execCmd(String command, boolean isRooted, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRooted, isNeedResultMsg);
    }

    public static CommandResult execCmd(String command, List<String> envp, boolean isRooted, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp == null ? null : (String[]) envp.toArray(new String[0]), isRooted, isNeedResultMsg);
    }

    public static CommandResult execCmd(String command, String[] envp, boolean isRooted, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp, isRooted, isNeedResultMsg);
    }

    public static CommandResult execCmd(List<String> commands, boolean isRooted, boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : (String[]) commands.toArray(new String[0]), isRooted, isNeedResultMsg);
    }

    public static CommandResult execCmd(String[] commands, boolean isRooted, boolean isNeedResultMsg) {
        return execCmd(commands, (String[]) null, isRooted, isNeedResultMsg);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:9|(9:10|11|(1:13)(1:89)|14|15|16|(3:18|(2:20|21)(2:23|24)|22)|25|26)|(4:28|(2:30|(2:31|(1:33)(1:34)))(0)|35|(11:37|(2:38|(1:40)(0))|43|(2:60|61)|(2:55|56)|46|47|(1:49)(1:54)|(1:51)|52|53)(0))(0)|42|43|(0)|(0)|46|47|(0)(0)|(0)|52|53) */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d0, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d1, code lost:
        r0.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x012f, code lost:
        if (r4 == null) goto L47;
     */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0175  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x00e6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x014b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0159 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0167 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x00d8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x013f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.blankj.utilcode.util.ShellUtils.CommandResult execCmd(java.lang.String[] r15, java.lang.String[] r16, boolean r17, boolean r18) {
        /*
            Method dump skipped, instructions count: 385
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blankj.utilcode.util.ShellUtils.execCmd(java.lang.String[], java.lang.String[], boolean, boolean):com.blankj.utilcode.util.ShellUtils$CommandResult");
    }

    /* loaded from: classes4.dex */
    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        public String toString() {
            return "result: " + this.result + "\nsuccessMsg: " + this.successMsg + "\nerrorMsg: " + this.errorMsg;
        }
    }
}
