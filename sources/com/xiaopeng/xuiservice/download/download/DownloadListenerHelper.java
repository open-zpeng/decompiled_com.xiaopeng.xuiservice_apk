package com.xiaopeng.xuiservice.download.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.xiaopeng.xuimanager.download.IDownloadListenerInterface;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.download.bean.DispatchStateHandler;
import com.xiaopeng.xuiservice.download.bean.DownloadStateBean;
import com.xiaopeng.xuiservice.download.util.DownloadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
/* loaded from: classes5.dex */
public class DownloadListenerHelper {
    private static final String DOWNLOAD_IN_PROGRESS_URI = "content://downloads/my_downloads";
    private static final int DOWNLOAD_OPERATION_TYPE_CLEAR = 2;
    private static final int DOWNLOAD_OPERATION_TYPE_PAUSE = 1;
    private static final int DOWNLOAD_OPERATION_TYPE_START = 0;
    private static final int ILLEGAL_DOWNLOAD_STATE = -1;
    private static final String TAG = DownloadListenerHelper.class.getSimpleName();
    private static RemoteCallbackList<IDownloadListenerInterface> sCallbackList = null;
    private static DownloadManager sDownloadManager = null;
    private static ContentObserver sDownloadProgressObserver = null;
    private static BroadcastReceiver sFileDownloadListener = null;
    private static DispatchStateHandler sHandler = new DispatchStateHandler();
    private static HandlerThread mHandlerThread = null;
    private static ArrayList<Long> mCanceledTaskIds = new ArrayList<>();

    public static void init(Context context) {
        sCallbackList = new RemoteCallbackList<>();
        sDownloadManager = (DownloadManager) context.getSystemService("download");
        mHandlerThread = new HandlerThread("DownloadManager_ContentObserver");
        registerContentReceiver(context);
    }

    public static long enqueue(String downloadUri) {
        return enqueueWithHeaders(downloadUri, null, null, null);
    }

    public static long enqueueWithHeaders(String downloadUri, String title, String description, Map<String, String> requestHeaders) {
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUri));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, DownloadUtils.getNameFromUrl(downloadUri));
        String str = TAG;
        LogUtil.d(str, "download file public dir  = " + Environment.DIRECTORY_DOWNLOADS);
        if (title != null) {
            request.setTitle(title);
        }
        if (description != null) {
            request.setDescription(description);
        }
        request.setNotificationVisibility(0);
        int operationType = 0;
        if (requestHeaders != null) {
            Objects.requireNonNull(request);
            requestHeaders.forEach(new BiConsumer() { // from class: com.xiaopeng.xuiservice.download.download.-$$Lambda$0GcK8clqlaNxVdb2_i9ReOROX9Q
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    request.addRequestHeader((String) obj, (String) obj2);
                }
            });
            String operationTypeStr = requestHeaders.get("android.download.type");
            if (operationTypeStr != null) {
                try {
                    operationType = Integer.parseInt(operationTypeStr);
                } catch (NumberFormatException e) {
                    String str2 = TAG;
                    LogUtil.d(str2, "parse operation type error , origin type from map is " + operationTypeStr);
                }
            }
        }
        long id = sDownloadManager.enqueue(request);
        dispatchCallbackByOperationType(id, operationType, downloadUri);
        return id;
    }

    private static void dispatchCallbackByOperationType(long id, int opreationType, String uri) {
        if (opreationType == 0) {
            dispatchDownloadStartCallback(id, uri);
            String str = TAG;
            LogUtil.d(str, "start download task id  = " + id + "  file uri  = " + uri);
        } else if (opreationType == 1) {
            dispatchDownloadPauseCallback(id, uri);
            String str2 = TAG;
            LogUtil.d(str2, "pause download task id  = " + id + "  file uri  = " + uri);
        } else if (opreationType == 2) {
            dispatchDownloadCancelCallback(id, uri);
            String str3 = TAG;
            LogUtil.d(str3, "cancel download task id  = " + id + "  file uri  = " + uri);
        }
    }

    private static void registerContentReceiver(Context context) {
        mHandlerThread.start();
        Handler workHandler = new Handler(mHandlerThread.getLooper());
        Uri sDownloadObserver = Uri.parse(DOWNLOAD_IN_PROGRESS_URI);
        sDownloadProgressObserver = new ContentObserver(workHandler) { // from class: com.xiaopeng.xuiservice.download.download.DownloadListenerHelper.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                long taskId = DownloadListenerHelper.getTaskIdFromUri(uri);
                long downloadedBytes = DownloadListenerHelper.getDownloadedBytes(taskId);
                long totalBytes = DownloadListenerHelper.getTotalBytes(taskId);
                float percentage = ((float) downloadedBytes) / ((float) totalBytes);
                String url = DownloadListenerHelper.getDownloadStateStringByFields(taskId, "uri")[0];
                String str = DownloadListenerHelper.TAG;
                LogUtil.d(str, "ContentObserver onChange task id = " + taskId + " url = " + url + "   downloaded Bytes = " + downloadedBytes + "    totalBytes = " + totalBytes + "   percentage = " + percentage);
                DownloadListenerHelper.dispatchDownloadInProgressCallback(taskId, url, totalBytes, percentage, downloadedBytes);
            }
        };
        context.getContentResolver().registerContentObserver(sDownloadObserver, true, sDownloadProgressObserver);
        sFileDownloadListener = new BroadcastReceiver() { // from class: com.xiaopeng.xuiservice.download.download.DownloadListenerHelper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent != null) {
                    String intentAction = intent.getAction();
                    String str = DownloadListenerHelper.TAG;
                    LogUtil.d(str, "Karl log onReceive intent = " + intent);
                    if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                        long taskId = intent.getLongExtra("extra_download_id", -1L);
                        if (taskId != -1) {
                            String url = DownloadListenerHelper.getDownloadStateStringByFields(taskId, "uri")[0];
                            if (8 == DownloadListenerHelper.getDownloadState(taskId)) {
                                String str2 = DownloadListenerHelper.TAG;
                                LogUtil.d(str2, "BroadcastReceiver onReceive task id = " + taskId + "  intentAction = " + intentAction);
                                Uri uriForDownloadedFile = DownloadListenerHelper.sDownloadManager.getUriForDownloadedFile(taskId);
                                String fileUriString = "";
                                if (uriForDownloadedFile != null) {
                                    fileUriString = uriForDownloadedFile.toString();
                                }
                                String str3 = DownloadListenerHelper.TAG;
                                LogUtil.d(str3, "Karl log BroadcastReceiver onReceive task id = " + taskId + "   downloaded file uri = " + fileUriString);
                                DownloadListenerHelper.dispatchDownloadCompleteCallback(taskId, url, fileUriString);
                                return;
                            }
                            int failReason = DownloadListenerHelper.getDownloadStateIntByFields(taskId, "reason")[0];
                            String str4 = DownloadListenerHelper.TAG;
                            LogUtil.d(str4, "Karl log BroadcastReceiver onReceive task id = " + taskId + "   failReason = " + failReason);
                            DownloadListenerHelper.dispatchDownloadErrorCallback(taskId, url, String.valueOf(failReason));
                        }
                    }
                }
            }
        };
        context.registerReceiver(sFileDownloadListener, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }

    public static boolean registerDownloadCallback(IDownloadListenerInterface listener) {
        return sCallbackList.register(listener);
    }

    public static boolean unRegisterDownloadCallback(IDownloadListenerInterface listener) {
        return sCallbackList.unregister(listener);
    }

    public static void destroy(Context context) {
        context.getContentResolver().unregisterContentObserver(sDownloadProgressObserver);
        context.unregisterReceiver(sFileDownloadListener);
    }

    public static boolean dispatchDownloadStartCallback(long taskId, String uri) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(1, taskId, uri, 0L, 0.0f, 0L, "", "");
        return sHandler.sendMessage(message);
    }

    public static boolean dispatchDownloadInProgressCallback(long taskId, String uri, long totalByte, float percentage, long byteTillNow) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(2, taskId, uri, totalByte, percentage, byteTillNow, "", "");
        return sHandler.sendMessage(message);
    }

    public static boolean dispatchDownloadPauseCallback(long taskId, String uri) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(4, taskId, uri, 0L, 0.0f, 0L, "", "");
        return sHandler.sendMessage(message);
    }

    public static boolean dispatchDownloadCancelCallback(long taskId, String uri) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(5, taskId, uri, 0L, 0.0f, 0L, "", "");
        return sHandler.sendMessage(message);
    }

    public static boolean dispatchDownloadCompleteCallback(long taskId, String uri, String completedFileUri) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(3, taskId, uri, 0L, 0.0f, 0L, completedFileUri, "");
        return sHandler.sendMessage(message);
    }

    public static boolean dispatchDownloadErrorCallback(long taskId, String uri, String errorMessage) {
        Message message = Message.obtain();
        message.obj = new DownloadStateBean(6, taskId, uri, 0L, 0.0f, 0L, "", errorMessage);
        return sHandler.sendMessage(message);
    }

    private static int[] generateIntBlock(int length) {
        int[] statesResult = new int[length];
        Arrays.fill(statesResult, -1);
        return statesResult;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getTotalBytes(long id) {
        String totalByte = getDownloadStateStringByFields(id, "total_size")[0];
        if (totalByte == null) {
            return 0L;
        }
        long totalSizeByte = Long.parseLong(totalByte);
        return totalSizeByte;
    }

    public static long getDownloadedBytes(long id) {
        String downloadedByteStr = getDownloadStateStringByFields(id, "bytes_so_far")[0];
        String str = TAG;
        LogUtil.d(str, "getDownloadedBytes downloadedByte = " + downloadedByteStr);
        if (downloadedByteStr == null) {
            return 0L;
        }
        long downloadedSizeByte = Long.parseLong(downloadedByteStr);
        return downloadedSizeByte;
    }

    public static int getDownloadState(long id) {
        int downloadState = getDownloadStateIntByFields(id, "status")[0];
        String str = TAG;
        LogUtil.d(str, "getDownloadState downloadState = " + downloadState);
        return downloadState;
    }

    public static int[] getDownloadStateIntByTitle(String title, String stateKey) {
        Cursor cursor = generateDownloadManagerQueryCursorByString(title);
        ArrayList<Integer> statesResult = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                statesResult.add(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(stateKey))));
            }
            cursor.close();
        }
        return convertIntegers(statesResult);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0025, code lost:
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0028, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:3:0x0009, code lost:
        if (r0 != null) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r0.moveToNext() == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0011, code lost:
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0013, code lost:
        if (r2 >= r6.length) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
        r1[r2] = r0.getInt(r0.getColumnIndex(r6[r2]));
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int[] getDownloadStateIntByFields(long r4, java.lang.String... r6) {
        /*
            android.database.Cursor r0 = generateDownloadManagerQueryCursor(r4)
            int r1 = r6.length
            int[] r1 = generateIntBlock(r1)
            if (r0 == 0) goto L28
        Lb:
            boolean r2 = r0.moveToNext()
            if (r2 == 0) goto L25
            r2 = 0
        L12:
            int r3 = r6.length
            if (r2 >= r3) goto L24
            r3 = r6[r2]
            int r3 = r0.getColumnIndex(r3)
            int r3 = r0.getInt(r3)
            r1[r2] = r3
            int r2 = r2 + 1
            goto L12
        L24:
            goto Lb
        L25:
            r0.close()
        L28:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.download.download.DownloadListenerHelper.getDownloadStateIntByFields(long, java.lang.String[]):int[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0023, code lost:
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0026, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:3:0x0007, code lost:
        if (r0 != null) goto L3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000d, code lost:
        if (r0.moveToNext() == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x000f, code lost:
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0011, code lost:
        if (r2 >= r6.length) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0013, code lost:
        r1[r2] = r0.getString(r0.getColumnIndex(r6[r2]));
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String[] getDownloadStateStringByFields(long r4, java.lang.String... r6) {
        /*
            android.database.Cursor r0 = generateDownloadManagerQueryCursor(r4)
            int r1 = r6.length
            java.lang.String[] r1 = new java.lang.String[r1]
            if (r0 == 0) goto L26
        L9:
            boolean r2 = r0.moveToNext()
            if (r2 == 0) goto L23
            r2 = 0
        L10:
            int r3 = r6.length
            if (r2 >= r3) goto L22
            r3 = r6[r2]
            int r3 = r0.getColumnIndex(r3)
            java.lang.String r3 = r0.getString(r3)
            r1[r2] = r3
            int r2 = r2 + 1
            goto L10
        L22:
            goto L9
        L23:
            r0.close()
        L26:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.xuiservice.download.download.DownloadListenerHelper.getDownloadStateStringByFields(long, java.lang.String[]):java.lang.String[]");
    }

    private static Cursor generateDownloadManagerQueryCursor(long id) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        return sDownloadManager.query(query);
    }

    private static Cursor generateDownloadManagerQueryCursorByString(String title) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterByString(title);
        return sDownloadManager.query(query);
    }

    public static long getTaskIdFromUri(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        long taskId = -1;
        try {
            if (lastPathSegment != null) {
                taskId = Long.parseLong(lastPathSegment);
            } else {
                LogUtil.e(TAG, "Last path segment is null");
            }
        } catch (NumberFormatException e) {
            LogUtil.e(TAG, "Uri never contains task id");
        }
        return taskId;
    }

    public static boolean dispatchDownloadCallbackByType(DownloadStateBean downloadStateBean) {
        return dispatchDownloadCallbackByType(downloadStateBean.getType(), downloadStateBean.getTaskId(), downloadStateBean.getFileUri(), downloadStateBean.getTotalBytes(), downloadStateBean.getPercentage(), downloadStateBean.getByteTillNow(), downloadStateBean.getCompletedFileUri(), downloadStateBean.getErrorMessage());
    }

    public static boolean dispatchDownloadCallbackByType(int type, long taskId, String fileUri, long totalBytes, float percentage, long byteTillNow, String completedFileUri, String errorMessage) {
        RemoteCallbackList<IDownloadListenerInterface> remoteCallbackList = sCallbackList;
        if (remoteCallbackList == null) {
            LogUtil.d(TAG, "dispatchDownloadCallbackByType sCallbackList is null");
            return false;
        }
        switch (type) {
            case 1:
                int callbackCount = remoteCallbackList.beginBroadcast();
                boolean success = false;
                for (int i = 0; i < callbackCount; i++) {
                    IDownloadListenerInterface broadcastItem = sCallbackList.getBroadcastItem(i);
                    try {
                        broadcastItem.onDownloadStart(taskId, fileUri);
                        sCallbackList.finishBroadcast();
                        success = true;
                    } catch (RemoteException e) {
                        success = false;
                        e.printStackTrace();
                    } finally {
                    }
                }
                return success;
            case 2:
                int callbackCount2 = remoteCallbackList.beginBroadcast();
                boolean success2 = false;
                int i2 = 0;
                while (i2 < callbackCount2) {
                    IDownloadListenerInterface broadcastItem2 = sCallbackList.getBroadcastItem(i2);
                    int i3 = i2;
                    try {
                        broadcastItem2.onDownloadInProgress(taskId, fileUri, totalBytes, percentage, byteTillNow);
                        sCallbackList.finishBroadcast();
                        success2 = true;
                    } catch (RemoteException e2) {
                        success2 = false;
                        e2.printStackTrace();
                    } finally {
                    }
                    i2 = i3 + 1;
                }
                return success2;
            case 3:
                int callbackCount3 = remoteCallbackList.beginBroadcast();
                boolean success3 = false;
                for (int i4 = 0; i4 < callbackCount3; i4++) {
                    IDownloadListenerInterface broadcastItem3 = sCallbackList.getBroadcastItem(i4);
                    try {
                        broadcastItem3.onDownloadCompleted(taskId, fileUri, completedFileUri);
                        sCallbackList.finishBroadcast();
                        success3 = true;
                    } catch (RemoteException e3) {
                        success3 = false;
                        e3.printStackTrace();
                    } finally {
                    }
                }
                return success3;
            case 4:
                int callbackCount4 = remoteCallbackList.beginBroadcast();
                boolean success4 = false;
                for (int i5 = 0; i5 < callbackCount4; i5++) {
                    IDownloadListenerInterface broadcastItem4 = sCallbackList.getBroadcastItem(i5);
                    try {
                        broadcastItem4.onDownloadPause(taskId, fileUri);
                        sCallbackList.finishBroadcast();
                        success4 = true;
                    } catch (RemoteException e4) {
                        success4 = false;
                        e4.printStackTrace();
                    } finally {
                    }
                }
                return success4;
            case 5:
                int callbackCount5 = remoteCallbackList.beginBroadcast();
                boolean success5 = false;
                for (int i6 = 0; i6 < callbackCount5; i6++) {
                    IDownloadListenerInterface broadcastItem5 = sCallbackList.getBroadcastItem(i6);
                    try {
                        broadcastItem5.onDownloadCancel(taskId, fileUri);
                        sCallbackList.finishBroadcast();
                        success5 = true;
                    } catch (RemoteException e5) {
                        success5 = false;
                        e5.printStackTrace();
                    } finally {
                    }
                }
                return success5;
            case 6:
                int callbackCount6 = remoteCallbackList.beginBroadcast();
                boolean success6 = false;
                for (int i7 = 0; i7 < callbackCount6; i7++) {
                    IDownloadListenerInterface broadcastItem6 = sCallbackList.getBroadcastItem(i7);
                    try {
                        broadcastItem6.onDownloadError(taskId, fileUri, errorMessage);
                        sCallbackList.finishBroadcast();
                        success6 = true;
                    } catch (RemoteException e6) {
                        success6 = false;
                        e6.printStackTrace();
                    } finally {
                    }
                }
                return success6;
            default:
                return false;
        }
    }
}
