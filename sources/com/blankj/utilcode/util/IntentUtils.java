package com.blankj.utilcode.util;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.opencv.videoio.Videoio;
/* loaded from: classes4.dex */
public final class IntentUtils {
    private IntentUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isIntentAvailable(Intent intent) {
        return Utils.getApp().getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    public static Intent getInstallAppIntent(String filePath) {
        return getInstallAppIntent(UtilsBridge.getFileByPath(filePath));
    }

    public static Intent getInstallAppIntent(File file) {
        Uri uri;
        if (UtilsBridge.isFileExists(file)) {
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(file);
            } else {
                String authority = Utils.getApp().getPackageName() + ".utilcode.fileprovider";
                uri = FileProvider.getUriForFile(Utils.getApp(), authority, file);
            }
            return getInstallAppIntent(uri);
        }
        return null;
    }

    public static Intent getInstallAppIntent(Uri uri) {
        if (uri == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(1);
        }
        return intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
    }

    public static Intent getUninstallAppIntent(String pkgName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + pkgName));
        return intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
    }

    public static Intent getLaunchAppIntent(String pkgName) {
        String launcherActivity = UtilsBridge.getLauncherActivity(pkgName);
        if (UtilsBridge.isSpace(launcherActivity)) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setClassName(pkgName, launcherActivity);
        return intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
    }

    public static Intent getLaunchAppDetailsSettingsIntent(String pkgName) {
        return getLaunchAppDetailsSettingsIntent(pkgName, false);
    }

    public static Intent getLaunchAppDetailsSettingsIntent(String pkgName, boolean isNewTask) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + pkgName));
        return getIntent(intent, isNewTask);
    }

    public static Intent getShareTextIntent(String content) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", content);
        return getIntent(Intent.createChooser(intent, ""), true);
    }

    public static Intent getShareImageIntent(String imagePath) {
        return getShareTextImageIntent("", imagePath);
    }

    public static Intent getShareImageIntent(File imageFile) {
        return getShareTextImageIntent("", imageFile);
    }

    public static Intent getShareImageIntent(Uri imageUri) {
        return getShareTextImageIntent("", imageUri);
    }

    public static Intent getShareTextImageIntent(@Nullable String content, String imagePath) {
        return getShareTextImageIntent(content, UtilsBridge.getFileByPath(imagePath));
    }

    public static Intent getShareTextImageIntent(@Nullable String content, File imageFile) {
        return getShareTextImageIntent(content, UtilsBridge.file2Uri(imageFile));
    }

    public static Intent getShareTextImageIntent(@Nullable String content, Uri imageUri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.putExtra("android.intent.extra.STREAM", imageUri);
        intent.setType("image/*");
        return getIntent(Intent.createChooser(intent, ""), true);
    }

    public static Intent getShareImageIntent(LinkedList<String> imagePaths) {
        return getShareTextImageIntent("", imagePaths);
    }

    public static Intent getShareImageIntent(List<File> images) {
        return getShareTextImageIntent("", images);
    }

    public static Intent getShareImageIntent(ArrayList<Uri> uris) {
        return getShareTextImageIntent("", uris);
    }

    public static Intent getShareTextImageIntent(@Nullable String content, LinkedList<String> imagePaths) {
        List<File> files = new ArrayList<>();
        if (imagePaths != null) {
            Iterator<String> it = imagePaths.iterator();
            while (it.hasNext()) {
                String imagePath = it.next();
                File file = UtilsBridge.getFileByPath(imagePath);
                if (file != null) {
                    files.add(file);
                }
            }
        }
        return getShareTextImageIntent(content, files);
    }

    public static Intent getShareTextImageIntent(@Nullable String content, List<File> images) {
        ArrayList<Uri> uris = new ArrayList<>();
        if (images != null) {
            for (File image : images) {
                Uri uri = UtilsBridge.file2Uri(image);
                if (uri != null) {
                    uris.add(uri);
                }
            }
        }
        return getShareTextImageIntent(content, uris);
    }

    public static Intent getShareTextImageIntent(@Nullable String content, ArrayList<Uri> uris) {
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", uris);
        intent.setType("image/*");
        return getIntent(Intent.createChooser(intent, ""), true);
    }

    public static Intent getComponentIntent(String pkgName, String className) {
        return getComponentIntent(pkgName, className, null, false);
    }

    public static Intent getComponentIntent(String pkgName, String className, boolean isNewTask) {
        return getComponentIntent(pkgName, className, null, isNewTask);
    }

    public static Intent getComponentIntent(String pkgName, String className, Bundle bundle) {
        return getComponentIntent(pkgName, className, bundle, false);
    }

    public static Intent getComponentIntent(String pkgName, String className, Bundle bundle, boolean isNewTask) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ComponentName cn = new ComponentName(pkgName, className);
        intent.setComponent(cn);
        return getIntent(intent, isNewTask);
    }

    public static Intent getShutdownIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= 26) {
            intent = new Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN");
        } else {
            intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        }
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        return intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR);
    }

    public static Intent getDialIntent(@NonNull String phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + Uri.encode(phoneNumber)));
        return getIntent(intent, true);
    }

    @RequiresPermission("android.permission.CALL_PHONE")
    public static Intent getCallIntent(@NonNull String phoneNumber) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 1, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + Uri.encode(phoneNumber)));
        return getIntent(intent, true);
    }

    public static Intent getSendSmsIntent(@NonNull String phoneNumber, String content) {
        if (phoneNumber == null) {
            throw new NullPointerException("Argument 'phoneNumber' of type String (#0 out of 2, zero-based) is marked by @androidx.annotation.NonNull but got null for it");
        }
        Uri uri = Uri.parse("smsto:" + Uri.encode(phoneNumber));
        Intent intent = new Intent("android.intent.action.SENDTO", uri);
        intent.putExtra("sms_body", content);
        return getIntent(intent, true);
    }

    public static Intent getCaptureIntent(Uri outUri) {
        return getCaptureIntent(outUri, false);
    }

    public static Intent getCaptureIntent(Uri outUri, boolean isNewTask) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", outUri);
        intent.addFlags(1);
        return getIntent(intent, isNewTask);
    }

    private static Intent getIntent(Intent intent, boolean isNewTask) {
        return isNewTask ? intent.addFlags(Videoio.CAP_INTELPERC_IMAGE_GENERATOR) : intent;
    }
}
