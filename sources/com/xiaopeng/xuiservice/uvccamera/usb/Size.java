package com.xiaopeng.xuiservice.uvccamera.usb;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes5.dex */
public class Size implements Parcelable, Cloneable {
    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.Size.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Size createFromParcel(Parcel in) {
            return new Size(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Size[] newArray(int size) {
            return new Size[size];
        }
    };
    public int fps;
    public List<Integer> fpsList;
    public int height;
    public int type;
    public int width;

    public Size(int _type, int _width, int _height, int _fps, List<Integer> _fpsList) {
        this.type = _type;
        this.width = _width;
        this.height = _height;
        this.fps = _fps;
        this.fpsList = _fpsList;
    }

    protected Size(Parcel in) {
        this.type = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.fps = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.fps);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return String.format(Locale.US, "Size(%dx%d@%d,type:%d)", Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.fps), Integer.valueOf(this.type));
    }

    /* renamed from: clone */
    public Size m128clone() {
        Size size = null;
        try {
            size = (Size) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (size == null) {
            Size size2 = new Size(this.type, this.width, this.height, this.fps, new ArrayList(this.fpsList));
            return size2;
        }
        size.fpsList = new ArrayList(this.fpsList);
        return size;
    }
}
