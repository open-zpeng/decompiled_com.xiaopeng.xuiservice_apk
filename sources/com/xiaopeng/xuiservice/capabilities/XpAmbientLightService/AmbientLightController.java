package com.xiaopeng.xuiservice.capabilities.XpAmbientLightService;

import android.app.ActivityThread;
import android.car.hardware.atl.AtlConfiguration;
import android.car.hardware.atl.CarAtlManager;
import android.content.Context;
import android.provider.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.xuimanager.utils.LogUtil;
import com.xiaopeng.xuiservice.carcontrol.CarClientManager;
import com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.UByte;
/* loaded from: classes5.dex */
public class AmbientLightController extends HalServiceBaseCarListener implements AmbientLightHal {
    private static final boolean DBG = true;
    public static final String GROUP_LIGHT = "groupLight";
    public static final String SINGLE_LIGHT = "singleLight";
    private static final String TAG = "AmbientLightController";
    private int brightness;
    private int firstColor;
    private GroupInfo groupInfo;
    private HashMap<Integer, Integer> groupLightData;
    private boolean isDoubleColorEnable;
    private boolean isSingleLightControl;
    private AtlConfiguration mAtlConfiguration;
    private int secondColor;

    public AmbientLightController(Context context) {
        super(context);
        this.isSingleLightControl = false;
        this.isDoubleColorEnable = false;
        this.brightness = Settings.System.getInt(ActivityThread.currentActivityThread().getApplication().getContentResolver(), "AmbientLightBright", 100);
        this.groupLightData = new HashMap<>();
        this.groupInfo = new GroupInfo();
        parseGroupDataFromJsons();
        setGroupLightData();
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setTwoLightData(byte colorProtocol, byte[] lightPosition, boolean isSingleLight, byte[] color, byte[] bright, byte[] time) {
        LogUtil.d(TAG, " Current two light status: " + this.isSingleLightControl + " colorProtocol " + ((int) colorProtocol) + " lightPosition " + ((int) lightPosition[0]) + " " + ((int) lightPosition[1]) + " isSingleLight " + isSingleLight + " color " + ((int) color[0]) + " " + ((int) color[1]) + " bright " + ((int) bright[0]) + " " + ((int) bright[1]) + " time " + ((int) time[0]) + " " + ((int) time[1]));
        if (isSingleLight) {
            this.isSingleLightControl = true;
        } else {
            this.isSingleLightControl = false;
        }
        byte[] firstLightData = new byte[8];
        byte[] secondLightData = new byte[8];
        byte[] open = {(byte) getAtlOpen(), (byte) getAtlOpen()};
        byte[] fade = {1, 1};
        firstLightData[2] = (byte) (0 & 1);
        firstLightData[2] = (byte) (firstLightData[2] | ((byte) ((open[0] & 1) << 1)));
        firstLightData[2] = (byte) (firstLightData[2] | ((byte) ((fade[0] & 1) << 2)));
        firstLightData[3] = (byte) ((time[0] & UByte.MAX_VALUE) / 200);
        byte b = bright[0];
        int i = this.brightness;
        firstLightData[4] = (byte) (((b * i) / 20) & 127);
        firstLightData[5] = (byte) (color[0] & 31);
        if (lightPosition[0] != lightPosition[1]) {
            secondLightData[2] = (byte) (0 & 1);
            secondLightData[2] = (byte) (secondLightData[2] | ((byte) ((open[1] & 1) << 1)));
            secondLightData[2] = (byte) (secondLightData[2] | ((byte) ((fade[1] & 1) << 2)));
            secondLightData[3] = (byte) ((time[1] & UByte.MAX_VALUE) / 20);
            secondLightData[4] = (byte) (((bright[1] * i) / 100) & 127);
            secondLightData[5] = (byte) (color[1] & 31);
            secondLightData[0] = (byte) (((lightPosition[0] | lightPosition[1]) & 522240) >> 11);
            secondLightData[1] = (byte) (((lightPosition[1] | lightPosition[0]) & 1536) >> 9);
            setAtlLin2Data(secondLightData, SINGLE_LIGHT);
            secondLightData[0] = (byte) (((lightPosition[0] | lightPosition[1]) & 510) >> 1);
            secondLightData[1] = (byte) ((lightPosition[1] | lightPosition[0]) & 1);
            setAtlLin3Data(secondLightData, SINGLE_LIGHT);
        }
        firstLightData[0] = (byte) (((lightPosition[0] | lightPosition[1]) & 522240) >> 11);
        firstLightData[1] = (byte) (((lightPosition[1] | lightPosition[0]) & 1536) >> 9);
        setAtlLin2Data(firstLightData, SINGLE_LIGHT);
        firstLightData[0] = (byte) (((lightPosition[0] | lightPosition[1]) & 510) >> 1);
        firstLightData[1] = (byte) ((lightPosition[0] | lightPosition[1]) & 1);
        setAtlLin3Data(firstLightData, SINGLE_LIGHT);
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setGroutLightData(byte groupNum, byte colorProtocol, int lightlist, boolean hold, byte color, byte bright, byte time) {
        byte[] groupData = new byte[8];
        byte open = (byte) getAtlOpen();
        byte fade = time == 0 ? (byte) 0 : (byte) 1;
        int lightPosition = lightlist;
        if (this.groupLightData.containsKey(Integer.valueOf(groupNum))) {
            lightPosition = this.groupLightData.get(Integer.valueOf(groupNum)).intValue();
        }
        byte colorCode = (byte) (color - 1);
        LogUtil.d(TAG, "setGroutLightData groupNum " + Integer.toBinaryString(groupNum) + " color " + Integer.toBinaryString(colorCode & UByte.MAX_VALUE) + " bright " + Integer.toBinaryString(bright) + " fade time " + Integer.toBinaryString(time));
        groupData[2] = (byte) (colorProtocol & 1);
        groupData[2] = (byte) (groupData[2] | (((byte) (open & 1)) << 1));
        groupData[2] = (byte) (groupData[2] | (((byte) (fade & 1)) << 2));
        groupData[3] = (byte) (time & UByte.MAX_VALUE);
        groupData[4] = (byte) (((this.brightness * bright) / 100) & 127);
        if (colorProtocol == 0) {
            groupData[5] = (byte) (colorCode & 31);
        }
        int lin1 = this.groupInfo.getLIN1Format() & lightPosition;
        if (lin1 == 0) {
            LogUtil.d(TAG, "LIN 1 is 0");
        } else {
            groupData[0] = (byte) (lin1 & 255);
            groupData[1] = (byte) (lin1 >>> 8);
            setAtlLin2Data(groupData, GROUP_LIGHT);
            LogUtil.d(TAG, "setGroutLightData LIN1 byte0: " + String.format("%8s", Integer.toBinaryString(groupData[0] & UByte.MAX_VALUE)).replace(' ', '0') + " byte1: " + String.format("%8s", Integer.toBinaryString(groupData[1] & UByte.MAX_VALUE)).replace(' ', '0'));
        }
        int lin2 = lightPosition >>> this.groupInfo.getLIN1LightAmount();
        if (lin2 == 0) {
            LogUtil.d(TAG, "LIN2 data is 0");
            return;
        }
        groupData[0] = (byte) (lin2 & 255);
        groupData[1] = (byte) (lin2 >>> 8);
        setAtlLin3Data(groupData, GROUP_LIGHT);
        LogUtil.d(TAG, "setGroutLightData LIN2 byte0: " + String.format("%8s", Integer.toBinaryString(groupData[0] & UByte.MAX_VALUE)).replace(' ', '0') + " byte1: " + String.format("%8s", Integer.toBinaryString(groupData[1] & UByte.MAX_VALUE)).replace(' ', '0'));
    }

    public static byte[] getByteArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAllLightData(byte[] color, byte[] bright, byte[] fade) {
    }

    private void setAtlLin2Data(byte[] linTwoData, String lightType) {
        if (lightType.equals(SINGLE_LIGHT) || !this.isSingleLightControl) {
            try {
                getCarAtlManager().setAtlLin2Data(linTwoData);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void setAtlLin3Data(byte[] linThreeData, String lightType) {
        if (lightType.equals(SINGLE_LIGHT) || !this.isSingleLightControl) {
            try {
                getCarAtlManager().setAtlLin3Data(linThreeData);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setThemeFirstColor(int color) {
        LogUtil.d(TAG, "setThemeFirstColor " + color);
        try {
            this.firstColor = color;
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setThemeSecondColor(int color) {
        LogUtil.d(TAG, "setThemeSecondColor " + color);
        try {
            this.secondColor = color;
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setDoubleThemeColor(int status) {
        LogUtil.d(TAG, "setDoubleThemeColor " + status);
        try {
            this.isDoubleColorEnable = status != 0;
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setBrightnessLevel(int brightness) {
        LogUtil.d(TAG, "setBrightnessLevel " + brightness);
        try {
            this.brightness = brightness;
        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAtlOpen(int status) {
        LogUtil.d(TAG, "setAtlOpen " + status);
        try {
            getCarAtlManager().setAtlOpen(status);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public int getAtlOpen() {
        try {
            return getCarAtlManager().getAtlOpen();
        } catch (Exception e) {
            handleException(e);
            return 0;
        }
    }

    @Override // com.xiaopeng.xuiservice.capabilities.XpAmbientLightService.AmbientLightHal
    public void setAtlConfiguration(AtlConfiguration config) {
        try {
            getCarAtlManager().setAtlConfiguration(config);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private CarAtlManager getCarAtlManager() {
        return CarClientManager.getInstance().getCarManager("xp_atl");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.HalServiceBaseCarListener
    public void dump(PrintWriter writer) {
        writer.println("*AmbientLight HAL*");
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onInit() {
    }

    @Override // com.xiaopeng.xuiservice.carcontrol.BaseCarListener
    protected void onRelease() {
    }

    public List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e(TAG, "Empty file list");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            LogUtil.d(TAG, "File path: " + files[i].getAbsolutePath());
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    private void parseGroupDataFromJsons() {
        LogUtil.i(TAG, "parseGroupDataFromJsons()");
        List<String> jsonFilesName = getFilesAllName("/system/etc/xuiservice/atl");
        if (jsonFilesName == null) {
            return;
        }
        for (int i = 0; i < jsonFilesName.size(); i++) {
            try {
                if (jsonFilesName.get(i).equals("/system/etc/xuiservice/atl/group_configuration.json")) {
                    this.groupInfo = (GroupInfo) new Gson().fromJson((Reader) new FileReader(jsonFilesName.get(i)), (Class<Object>) GroupInfo.class);
                }
            } catch (JsonIOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e2) {
                e2.printStackTrace();
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        }
    }

    private void setGroupLightData() {
        LogUtil.i(TAG, "setGroupLightData()");
        this.groupLightData.clear();
        GroupInfo groupInfo = this.groupInfo;
        if (groupInfo == null || groupInfo.getGroupData() == null) {
            LogUtil.e(TAG, "Empty groupInfo ");
            return;
        }
        for (GroupInfo.Group groupList : this.groupInfo.getGroupData()) {
            this.groupLightData.put(Integer.valueOf(groupList.getGroupNumber()), Integer.valueOf(groupList.getLightPosition()));
        }
    }

    /* loaded from: classes5.dex */
    public static class GroupInfo {
        String LIN1Format = "0x0";
        int LIN1LightAmount = 0;
        List<Group> groupData;

        public int getLIN1Format() {
            return Integer.valueOf(this.LIN1Format, 16).intValue();
        }

        public GroupInfo setLIN1Format(String LIN1Format) {
            this.LIN1Format = LIN1Format;
            return this;
        }

        public int getLIN1LightAmount() {
            return this.LIN1LightAmount;
        }

        public GroupInfo setLIN1LightAmount(int LIN1LightAmount) {
            this.LIN1LightAmount = LIN1LightAmount;
            return this;
        }

        public GroupInfo setGroupData(List<Group> groupData) {
            this.groupData = groupData;
            return this;
        }

        public List<Group> getGroupData() {
            return this.groupData;
        }

        /* loaded from: classes5.dex */
        public static class Group {
            int groupNumber = 0;
            String lightPosition = "0x0";

            public int getGroupNumber() {
                return this.groupNumber;
            }

            public Group setGroupNumber(int groupNumber) {
                this.groupNumber = groupNumber;
                return this;
            }

            public int getLightPosition() {
                return Integer.valueOf(this.lightPosition, 16).intValue();
            }

            public Group setLightPosition(String lightPosition) {
                this.lightPosition = lightPosition;
                return this;
            }
        }
    }
}
