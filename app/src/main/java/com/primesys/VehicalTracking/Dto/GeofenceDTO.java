package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class GeofenceDTO implements Serializable {
	
 String StudentId,lat,lang,distance,Type,GeoName,Discripation,Studentname,GeoID,currlat,currlang,fenceno,Geofencestatus;
 int Speed;

	public String getLan_direction() {
		return lan_direction;
	}

	public void setLan_direction(String lan_direction) {
		this.lan_direction = lan_direction;
	}

	public String getLat_direction() {
		return lat_direction;
	}

	public void setLat_direction(String lat_direction) {
		this.lat_direction = lat_direction;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	long Timestamp;
	String lan_direction,lat_direction,address;
 public String getGeofencestatus() {
	return Geofencestatus;
}

public void setGeofencestatus(String geofencestatus) {
	Geofencestatus = geofencestatus;
}

public String getFenceno() {
	return fenceno;
}

public void setFenceno(String fenceno) {
	this.fenceno = fenceno;
}

public String getType() {
	return Type;
}

public void setType(String type) {
	Type = type;
}


 public int getSpeed() {
	return Speed;
}

public void setSpeed(int speed) {
	Speed = speed;
}

public long getTimestamp() {
	return Timestamp;
}

public void setTimestamp(long timestamp) {
	Timestamp = timestamp;
}

public String getCurrlat() {
	return currlat;
}

public void setCurrlat(String currlat) {
	this.currlat = currlat;
}

public String getCurrlang() {
	return currlang;
}

public void setCurrlang(String currlang) {
	this.currlang = currlang;
}

public String getGeoID() {
	return GeoID;
}

public void setGeoID(String geoID) {
	GeoID = geoID;
}

public String getStudentname() {
	return Studentname;
}

public void setStudentname(String studentname) {
	Studentname = studentname;
}

String statusin="0",statusout="0",alertemail="0",alertapp="0",Enable="1",Sms="0";

public String getSms() {
	return Sms;
}

public void setSms(String sms) {
	Sms = sms;
}

public String getGeoName() {
	return GeoName;
}

public void setGeoName(String geoName) {
	GeoName = geoName;
}

public String getDiscripation() {
	return Discripation;
}

public void setDiscripation(String discripation) {
	Discripation = discripation;
}

public String getEnable() {
	return Enable;
}

public void setEnable(String enable) {
	Enable = enable;
}

public String getStudentId() {
	return StudentId;
}

public void setStudentId(String Student) {
	StudentId = Student;
}

public String getStatusin() {
	return statusin;
}

public void setStatusin(String statusin) {
	this.statusin = statusin;
}

public String getStatusout() {
	return statusout;
}

public void setStatusout(String statusout) {
	this.statusout = statusout;
}

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

public String getDistance() {
	return distance;
}

public void setDistance(String distance) {
	this.distance = distance;
}


}
