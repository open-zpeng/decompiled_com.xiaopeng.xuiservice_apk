package com.qiyi.video.qsrregister.listener;

import com.qiyi.video.qsrbase.QYSExecResult;
import com.qiyi.video.qsrbase.QYSExecType;
import com.qiyi.video.qsrbase.QYSVideoInfo;
import java.util.List;
/* loaded from: classes4.dex */
public interface QYSListener {
    void onQYSExecCallback(QYSExecType qYSExecType, QYSExecResult qYSExecResult, List<QYSVideoInfo> list);
}
