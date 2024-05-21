package com.xiaopeng.xuiservice.download.bean;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes5.dex */
public class DownloadStateBean implements Parcelable {
    public static final Parcelable.Creator<DownloadStateBean> CREATOR = new Parcelable.Creator<DownloadStateBean>() { // from class: com.xiaopeng.xuiservice.download.bean.DownloadStateBean.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DownloadStateBean createFromParcel(Parcel in) {
            return new DownloadStateBean(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DownloadStateBean[] newArray(int size) {
            return new DownloadStateBean[size];
        }
    };
    private long mByteTillNow;
    private String mCompletedFileUri;
    private String mErrorMessage;
    private String mFileUri;
    private float mPercentage;
    private long mTaskId;
    private long mTotalBytes;
    private int mType;

    public DownloadStateBean(int type, long taskId, String fileUri, long totalBytes, float percentage, long byteTillNow, String completedFileUri, String errorMessage) {
        this.mType = type;
        this.mTaskId = taskId;
        this.mFileUri = fileUri;
        this.mTotalBytes = totalBytes;
        this.mPercentage = percentage;
        this.mByteTillNow = byteTillNow;
        this.mCompletedFileUri = completedFileUri;
        this.mErrorMessage = errorMessage;
    }

    public DownloadStateBean() {
    }

    protected DownloadStateBean(Parcel in) {
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public long getTaskId() {
        return this.mTaskId;
    }

    public void setTaskId(long taskId) {
        this.mTaskId = taskId;
    }

    public String getFileUri() {
        return this.mFileUri;
    }

    public void setFileUri(String fileUri) {
        this.mFileUri = fileUri;
    }

    public long getTotalBytes() {
        return this.mTotalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.mTotalBytes = totalBytes;
    }

    public float getPercentage() {
        return this.mPercentage;
    }

    public void setPercentage(float percentage) {
        this.mPercentage = percentage;
    }

    public long getByteTillNow() {
        return this.mByteTillNow;
    }

    public void setByteTillNow(long byteTillNow) {
        this.mByteTillNow = byteTillNow;
    }

    public String getCompletedFileUri() {
        return this.mCompletedFileUri;
    }

    public void setCompletedFileUri(String completedFileUri) {
        this.mCompletedFileUri = completedFileUri;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    public static Parcelable.Creator<DownloadStateBean> getCREATOR() {
        return CREATOR;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
    }
}
