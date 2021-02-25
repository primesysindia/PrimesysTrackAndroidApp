package com.primesys.VehicalTracking.Dto;

/**
 * Created by pt002 on 15/12/17.
 */

public class EmpDayUpdateStatusDTO {
        String emp_id,user_id,day,month,year,time,comment,
                att_day_status,att_day_status_name,isgrant;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDay() {
        return day;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getAtt_day_status_name() {
        return att_day_status_name;
    }

    public String getIsgrant() {
        return isgrant;
    }

    public void setIsgrant(String isgrant) {
        this.isgrant = isgrant;
    }

    public void setAtt_day_status_name(String att_day_status_name) {
        this.att_day_status_name = att_day_status_name;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAtt_day_status() {
        return att_day_status;
    }

    public void setAtt_day_status(String att_day_status) {
        this.att_day_status = att_day_status;
    }
}
