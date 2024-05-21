package com.xiaopeng.vui.commons;
/* loaded from: classes2.dex */
public enum VuiElementType {
    BUTTON("Button"),
    LISTVIEW("ListView"),
    CHECKBOX("CheckBox"),
    RADIOBUTTON("RadioButton"),
    RADIOGROUP("RadioGroup"),
    GROUP("Group"),
    EDITTEXT("EditText"),
    PROGRESSBAR("ProgressBar"),
    SEEKBAR("SeekBar"),
    TABHOST("TabHost"),
    SEARCHVIEW("SearchView"),
    RATINGBAR("RatingBar"),
    SWITCH("Switch"),
    RECYCLEVIEW("RecycleView"),
    TEXTVIEW("TextView"),
    SCROLLVIEW("ScrollView"),
    IMAGEBUTTON("ImageButton"),
    IMAGEVIEW("ImageView"),
    CUSTOM("Custom"),
    XSLIDER("XSlider"),
    XTABLAYOUT("XTabLayout"),
    STATEFULBUTTON("StatefulButton"),
    STATE("State"),
    XGROUPHEADER("XGroupHeader"),
    VIEWPAGER("ViewPager"),
    TIMEPICKER("TimePicker"),
    VIRTUALLIST("VirtualList"),
    VIRTUALLISTITEM("VirtualListItem"),
    UNKNOWN("Unknown");
    
    private String type;

    VuiElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}