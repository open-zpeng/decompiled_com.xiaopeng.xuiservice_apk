package com.xiaopeng.xuiservice.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import com.xiaopeng.xuimanager.utils.LogUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/* loaded from: classes5.dex */
public class FileUtils {
    private static final int BUFFER_SIZE = 262144;
    private static final String TAG = "FileUtils";
    public static final int TYPE_NAME = 1;
    public static final int TYPE_PATH = 0;

    public static void unZip(File srcFile, String destDirPath) throws RuntimeException {
        long start = System.currentTimeMillis();
        LogUtil.i(TAG, "unZip " + srcFile.getName() + "  " + destDirPath);
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "  file not found!!");
        }
        File destdir = new File(destDirPath);
        try {
            if (!destdir.exists()) {
                destdir.mkdir();
            }
            ZipFile zipFile = null;
            try {
                try {
                    zipFile = new ZipFile(srcFile);
                    Enumeration<?> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (entry.isDirectory()) {
                            String dirPath = destDirPath + "/" + entry.getName();
                            File dir = new File(dirPath);
                            dir.mkdirs();
                        } else {
                            File targetFile = new File(destDirPath + "/" + entry.getName());
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }
                            targetFile.createNewFile();
                            InputStream is = zipFile.getInputStream(entry);
                            FileOutputStream fos = new FileOutputStream(targetFile);
                            byte[] buf = new byte[262144];
                            while (true) {
                                int len = is.read(buf);
                                if (len == -1) {
                                    break;
                                }
                                fos.write(buf, 0, len);
                            }
                            fos.close();
                            is.close();
                        }
                    }
                    long end = System.currentTimeMillis();
                    LogUtil.d(TAG, "unzip done  :" + (end - start) + " ms");
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable th) {
                    if (zipFile != null) {
                        try {
                            zipFile.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e3) {
                throw new RuntimeException("unzip error from ZipUtils", e3);
            }
        } catch (Exception e4) {
            LogUtil.e(TAG, "destdir:" + destdir + " " + e4);
        }
    }

    public static void copyFile(String fromPath, String toPath) {
        File srcFile = new File(fromPath);
        File destFile = new File(toPath);
        copyFile(srcFile, destFile);
    }

    public static void copyFile(File fromFile, File toFile) {
        LogUtil.d(TAG, "copyFile fromFileName:" + fromFile.getName() + " &toFileName:" + toFile.getName());
        if (!fromFile.exists()) {
            LogUtil.w(TAG, "fromFile not exist");
        } else if (!fromFile.isFile()) {
            LogUtil.w(TAG, "fromFile not file");
        } else if (!fromFile.canRead()) {
            LogUtil.w(TAG, "fromFile not read");
        } else {
            if (!toFile.getParentFile().exists()) {
                toFile.getParentFile().mkdirs();
            }
            if (toFile.exists()) {
                if (!isFileCompleted(fromFile, toFile)) {
                    toFile.delete();
                } else {
                    LogUtil.w(TAG, "toFile exists");
                    return;
                }
            }
            try {
                LogUtil.i(TAG, "copy file begin---");
                FileInputStream fosfrom = new FileInputStream(fromFile);
                FileOutputStream fosto = new FileOutputStream(toFile);
                byte[] bt = new byte[1024];
                while (true) {
                    int c = fosfrom.read(bt);
                    if (c > 0) {
                        fosto.write(bt, 0, c);
                    } else {
                        fosfrom.close();
                        fosto.close();
                        LogUtil.i(TAG, "copy file success---");
                        return;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void setTTSFilePermission(String destDirPath) {
        LogUtil.i(TAG, "setTTSFilePermission path:" + destDirPath);
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        File file = new File(destDirPath);
        setFilePermission(file, perms, perms);
        LogUtil.i(TAG, "setTTSFilePermission end");
    }

    public static void setOperationResourcePermission(String destDirPath) {
        LogUtil.i(TAG, "setOperationResourcePermission path:" + destDirPath);
        Set<PosixFilePermission> dirPerms = new HashSet<>();
        dirPerms.add(PosixFilePermission.OWNER_READ);
        dirPerms.add(PosixFilePermission.OWNER_WRITE);
        dirPerms.add(PosixFilePermission.OWNER_EXECUTE);
        dirPerms.add(PosixFilePermission.GROUP_READ);
        dirPerms.add(PosixFilePermission.GROUP_WRITE);
        dirPerms.add(PosixFilePermission.GROUP_EXECUTE);
        dirPerms.add(PosixFilePermission.OTHERS_READ);
        dirPerms.add(PosixFilePermission.OTHERS_WRITE);
        dirPerms.add(PosixFilePermission.OTHERS_EXECUTE);
        Set<PosixFilePermission> filePerms = new HashSet<>();
        filePerms.add(PosixFilePermission.OWNER_READ);
        filePerms.add(PosixFilePermission.OWNER_WRITE);
        filePerms.add(PosixFilePermission.OWNER_EXECUTE);
        filePerms.add(PosixFilePermission.GROUP_READ);
        filePerms.add(PosixFilePermission.GROUP_WRITE);
        filePerms.add(PosixFilePermission.GROUP_EXECUTE);
        filePerms.add(PosixFilePermission.OTHERS_READ);
        File file = new File(destDirPath);
        setFilePermission(file, dirPerms, filePerms);
        LogUtil.i(TAG, "setOperationResourcePermission end");
    }

    public static void setFilePermission(File file, Set<PosixFilePermission> dirPermissions, Set<PosixFilePermission> filePermissions) {
        try {
            if (file.isDirectory()) {
                Files.setPosixFilePermissions(Paths.get(file.getAbsolutePath(), new String[0]), dirPermissions);
                File[] items = file.listFiles();
                if (items.length > 0) {
                    for (File item : items) {
                        setFilePermission(item, dirPermissions, filePermissions);
                    }
                }
                return;
            }
            Files.setPosixFilePermissions(Paths.get(file.getAbsolutePath(), new String[0]), filePermissions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isFileCompleted(File fromFile, File toFile) {
        LogUtil.i(TAG, "isFileCompleted:");
        String aHashString = getMD5HashString(fromFile);
        LogUtil.i(TAG, "get fromFile" + fromFile.getAbsolutePath() + " md5 hash" + aHashString);
        String bHashString = getMD5HashString(toFile);
        LogUtil.i(TAG, "get toFile" + toFile.getAbsolutePath() + "md5 hash" + bHashString);
        return TextUtils.equals(aHashString, bHashString);
    }

    private static String getMD5HashString(File file) {
        try {
            String result = DigestUtils.getMD5HashString(file);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void parseAllFileInFolder(String path, int type, List<String> mList, String prefix) {
        LogUtil.d(TAG, "parseAllFileInFolder path:" + path + " type:" + type + " prefix:" + prefix);
        try {
            File mfile = new File(path);
            if (mfile.isDirectory()) {
                File file = new File(mfile.getAbsolutePath());
                File[] files = file.listFiles();
                LogUtil.d(TAG, "isDirectory");
                if (files == null) {
                    LogUtil.e(TAG, "empty folder");
                    return;
                }
                LogUtil.d(TAG, "files.length:" + files.length);
                for (File file2 : files) {
                    parseAllFileInFolder(file2.getAbsolutePath(), type, mList, prefix);
                }
            } else {
                String fileName = mfile.getName();
                String filePrefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                LogUtil.d(TAG, "filePrefix:" + filePrefix + " isFile:  " + fileName);
                if (filePrefix.equals(prefix)) {
                    if (type == 0) {
                        mList.add(mfile.getAbsolutePath());
                    } else if (1 == type) {
                        String mName = fileName.substring(0, fileName.lastIndexOf("."));
                        mList.add(mName);
                    }
                }
            }
            LogUtil.i(TAG, "mList.SIZE:" + mList.size());
        } catch (Exception e) {
            LogUtil.e(TAG, "parseAllFileInFolder FAIL :" + e);
        }
    }

    public static String readFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    String sb2 = sb.toString();
                    br.close();
                    return sb2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFromFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        return readFromFile(new File(filePath));
    }

    public static String readFromFile(InputStream inputStream) {
        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    br.close();
                    reader.close();
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTypeFilePath(String targetPath, String type) {
        File file = new File(targetPath);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File child : files) {
                if (child.getName().endsWith(type)) {
                    return child.getAbsolutePath();
                }
            }
            return "";
        }
        return "";
    }

    public static boolean isPathHaveChildren(String targetPath) {
        File[] files;
        File file = new File(targetPath);
        return file.exists() && (files = file.listFiles()) != null && files.length > 0;
    }

    public static String getFilePrefix(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            String fileName = file.getName();
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return "";
    }

    public static boolean deleteFile(String path) {
        LogUtil.d(TAG, "deleteFile path:" + path);
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.w(TAG, "deleteFile file not exist with path:" + path);
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] children = file.listFiles();
        if (children != null && children.length > 0) {
            for (File child : children) {
                deleteFile(child.getPath());
            }
        }
        return file.delete();
    }

    public static boolean isDataDiskSpaceEnough(String zipFilePath) {
        File file = new File(zipFilePath);
        return isDataDiskSpaceEnough(file);
    }

    public static boolean isDataDiskSpaceEnough(File zipFile) {
        File data = Environment.getDataDirectory();
        StatFs dataStat = new StatFs(data.getPath());
        long availSpace = dataStat.getAvailableBytes();
        return availSpace > getZipTrueSize(zipFile);
    }

    public static long getZipTrueSize(File zipFile) {
        long size = 0;
        try {
            ZipFile f = new ZipFile(zipFile.getAbsolutePath());
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static List<String> getAllDirs(String path) {
        List<String> subdirs = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    subdirs.add(f.getAbsolutePath());
                }
            }
        }
        return subdirs;
    }

    public static List<String> getAllFiles(String path, String suffix) {
        List<String> files = new ArrayList<>();
        parseAllFileInFolder(path, 0, files, suffix);
        return files;
    }
}
