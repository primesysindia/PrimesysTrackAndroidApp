package com.primesys.VehicalTracking.Dto;

public class MonitorSOSPressDTO {

    String gpsDeviceName, lat, lang, time, locationTime, speed, gsmSignalStrength, deviceId, voltageLevel, address;

    public String getGpsDeviceName() {
        return gpsDeviceName;
    }

    public void setGpsDeviceName(String gpsDeviceName) {
        this.gpsDeviceName = gpsDeviceName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setLocationTime(String locationTime){
        this.locationTime = locationTime;
    }

    public String getLocationTime() {
        return locationTime;
    }

    public void setSpeed(String speed){
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setGsmSignalStrength(String gsmSignalStrength){
        this.gsmSignalStrength = gsmSignalStrength;
    }

    public String getGsmSignalStrength() {
        return gsmSignalStrength;
    }

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setVoltageLevel(String voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    public String getVoltageLevel() {
        return voltageLevel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
