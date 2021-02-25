package com.primesys.VehicalTracking.Dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pt002 on 2/2/18.
 */

public class DriverEmpTaskSheduledDTO implements Parcelable {
    public String getReportStartTime() {
        return ReportStartTime;
    }

    public void setReportStartTime(String reportStartTime) {
        ReportStartTime = reportStartTime;
    }

    String start_weekdate,EmpUserID,VehicleID,ReportStartTime,Address,Lat,Lon,StartTime,EndTime,task_id,days,months,monthsChar,years,car_name,day_name;
String route;

    public String getStart_weekdate() {
        return start_weekdate;
    }

    public void setStart_weekdate(String start_weekdate) {
        this.start_weekdate = start_weekdate;
    }

    public DriverEmpTaskSheduledDTO(Parcel in) {
        EmpUserID = in.readString();
        VehicleID = in.readString();
        Address = in.readString();
        Lat = in.readString();
        Lon = in.readString();
        StartTime = in.readString();
        EndTime = in.readString();
        task_id = in.readString();
        days = in.readString();
        months = in.readString();
        monthsChar = in.readString();
        years = in.readString();
        car_name = in.readString();
        day_name = in.readString();
        route = in.readString();
    }

    public static final Creator<DriverEmpTaskSheduledDTO> CREATOR = new Creator<DriverEmpTaskSheduledDTO>() {
        @Override
        public DriverEmpTaskSheduledDTO createFromParcel(Parcel in) {
            return new DriverEmpTaskSheduledDTO(in);
        }

        @Override
        public DriverEmpTaskSheduledDTO[] newArray(int size) {
            return new DriverEmpTaskSheduledDTO[size];
        }
    };

    public DriverEmpTaskSheduledDTO() {

    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    @Override
    public String toString() {
        return "DriverEmpTaskSheduledDTO{" +

                "EmpUserID='" + EmpUserID + '\'' +
                ", VehicleID='" + VehicleID + '\'' +
                ", Address='" + Address + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Lon='" + Lon + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", task_id='" + task_id + '\'' +
                ", days='" + days + '\'' +
                ", months='" + months + '\'' +
                ", monthsChar='" + monthsChar + '\'' +
                ", years='" + years + '\'' +
                ", car_name='" + car_name + '\'' +
                '}';
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getEmpUserID() {
        return EmpUserID;
    }

    public void setEmpUserID(String empUserID) {
        EmpUserID = empUserID;
    }

    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String vehicleID) {
        VehicleID = vehicleID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getMonthsChar() {
        return monthsChar;
    }

    public void setMonthsChar(String monthsChar) {
        this.monthsChar = monthsChar;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Address);
    }

}
