package com.xiaopeng.xuiservice.xapp.miniprog.bean;
/* loaded from: classes5.dex */
public class DeviceData {
    String electricStatus;
    Location navigationCompany;
    Location navigationDestination;
    Location navigationHome;

    public DeviceData(String desLatitude, String desLongitude) {
        this.electricStatus = null;
        this.navigationDestination = new Location(desLatitude, desLongitude);
    }

    public DeviceData(String electricStatus, String desLatitude, String desLongitude) {
        this.electricStatus = null;
        this.electricStatus = electricStatus;
        this.navigationDestination = new Location(desLatitude, desLongitude);
    }

    public DeviceData(String electricStatus, String desLatitude, String desLongitude, String comLatitude, String comLongitude, String homeLatitude, String homeLongitude) {
        this.electricStatus = null;
        this.electricStatus = electricStatus;
        this.navigationCompany = new Location(comLatitude, comLongitude);
        this.navigationDestination = new Location(desLatitude, desLongitude);
        this.navigationHome = new Location(homeLatitude, homeLongitude);
    }
}
