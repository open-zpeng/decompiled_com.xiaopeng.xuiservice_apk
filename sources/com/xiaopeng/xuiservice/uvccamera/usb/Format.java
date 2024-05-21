package com.xiaopeng.xuiservice.uvccamera.usb;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes5.dex */
public class Format implements Parcelable, Cloneable {
    public static final Parcelable.Creator<Format> CREATOR = new Parcelable.Creator<Format>() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.Format.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Format createFromParcel(Parcel in) {
            return new Format(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Format[] newArray(int size) {
            return new Format[size];
        }
    };
    public List<Descriptor> frameDescriptors;
    public int index;
    public int type;

    public Format(int index, int type, List<Descriptor> frameDescriptors) {
        this.index = index;
        this.type = type;
        this.frameDescriptors = frameDescriptors;
    }

    protected Format(Parcel in) {
        this.index = in.readInt();
        this.type = in.readInt();
        this.frameDescriptors = in.createTypedArrayList(Descriptor.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeInt(this.type);
        dest.writeTypedList(this.frameDescriptors);
    }

    /* renamed from: clone */
    public Format m125clone() {
        Format format = null;
        try {
            format = (Format) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (format == null) {
            format = new Format(this.index, this.type, new ArrayList());
        }
        List<Descriptor> descriptorList = new ArrayList<>();
        List<Descriptor> list = this.frameDescriptors;
        if (list != null) {
            for (Descriptor descriptor : list) {
                descriptorList.add(descriptor.m126clone());
            }
        }
        format.frameDescriptors = descriptorList;
        return format;
    }

    /* loaded from: classes5.dex */
    public static class Descriptor implements Parcelable, Cloneable {
        public static final Parcelable.Creator<Descriptor> CREATOR = new Parcelable.Creator<Descriptor>() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.Format.Descriptor.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Descriptor createFromParcel(Parcel in) {
                return new Descriptor(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Descriptor[] newArray(int size) {
                return new Descriptor[size];
            }
        };
        public int fps;
        public int frameInterval;
        public int height;
        public int index;
        public List<Interval> intervals;
        public int type;
        public int width;

        public Descriptor(int _index, int _type, int _width, int _height, int _fps, int _frameInterval, List<Interval> _intervals) {
            this.index = _index;
            this.type = _type;
            this.width = _width;
            this.height = _height;
            this.fps = _fps;
            this.frameInterval = _frameInterval;
            this.intervals = _intervals;
        }

        protected Descriptor(Parcel in) {
            this.index = in.readInt();
            this.type = in.readInt();
            this.width = in.readInt();
            this.height = in.readInt();
            this.fps = in.readInt();
            this.frameInterval = in.readInt();
            this.intervals = in.createTypedArrayList(Interval.CREATOR);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.index);
            dest.writeInt(this.type);
            dest.writeInt(this.width);
            dest.writeInt(this.height);
            dest.writeInt(this.fps);
            dest.writeInt(this.frameInterval);
            dest.writeTypedList(this.intervals);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String toString() {
            return String.format(Locale.US, "Size(%dx%d@%d,type:%d,index:%d)", Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.fps), Integer.valueOf(this.type), Integer.valueOf(this.index));
        }

        @NonNull
        /* renamed from: clone */
        public Descriptor m126clone() {
            Descriptor descriptor = null;
            try {
                descriptor = (Descriptor) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (descriptor == null) {
                descriptor = new Descriptor(this.index, this.type, this.width, this.height, this.fps, this.frameInterval, new ArrayList());
            }
            List<Interval> intervalList = new ArrayList<>();
            List<Interval> list = this.intervals;
            if (list != null) {
                for (Interval interval : list) {
                    intervalList.add(interval.m127clone());
                }
            }
            descriptor.intervals = intervalList;
            return descriptor;
        }
    }

    /* loaded from: classes5.dex */
    public static class Interval implements Parcelable, Cloneable {
        public static final Parcelable.Creator<Interval> CREATOR = new Parcelable.Creator<Interval>() { // from class: com.xiaopeng.xuiservice.uvccamera.usb.Format.Interval.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Interval createFromParcel(Parcel in) {
                return new Interval(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Interval[] newArray(int size) {
                return new Interval[size];
            }
        };
        public int fps;
        public int index;
        public int value;

        public Interval(int index, int value, int fps) {
            this.index = index;
            this.value = value;
            this.fps = fps;
        }

        protected Interval(Parcel in) {
            this.index = in.readInt();
            this.value = in.readInt();
            this.fps = in.readInt();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.index);
            dest.writeInt(this.value);
            dest.writeInt(this.fps);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        /* renamed from: clone */
        public Interval m127clone() {
            Interval interval = null;
            try {
                interval = (Interval) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (interval == null) {
                Interval interval2 = new Interval(this.index, this.value, this.fps);
                return interval2;
            }
            return interval;
        }
    }
}
