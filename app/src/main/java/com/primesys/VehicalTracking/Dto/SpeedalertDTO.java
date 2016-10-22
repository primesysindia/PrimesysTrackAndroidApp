package com.primesys.VehicalTracking.Dto;

public class SpeedalertDTO {
	 String StudentId,AlertName,Studentname,alertID,Speed, Timestamp;
	 String alertemail="0",alertapp="0",Enable="1",Sms="0";

	public String getAlertemail() {
		return alertemail;
	}

	public void setAlertemail(String alertemail) {
		this.alertemail = alertemail;
	}

	public String getAlertapp() {
		return alertapp;
	}

	public void setAlertapp(String alertapp) {
		this.alertapp = alertapp;
	}

	public String getEnable() {
		return Enable;
	}

	public void setEnable(String enable) {
		Enable = enable;
	}

	public String getSms() {
		return Sms;
	}

	public void setSms(String sms) {
		Sms = sms;
	}

	public String getStudentId() {
		return StudentId;
	}

	public void setStudentId(String studentId) {
		StudentId = studentId;
	}

	public String getAlertName() {
		return AlertName;
	}

	public void setAlertName(String alertName) {
		AlertName = alertName;
	}

	public String getStudentname() {
		return Studentname;
	}

	public void setStudentname(String studentname) {
		Studentname = studentname;
	}

	public String getAlertID() {
		return alertID;
	}

	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}

	public String getSpeed() {
		return Speed;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
}
