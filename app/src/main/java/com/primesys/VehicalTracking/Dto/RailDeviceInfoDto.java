package com.primesys.VehicalTracking.Dto;

import java.util.Comparator;

public class RailDeviceInfoDto {

	
	String Name ,DeviceID;
	int DeviceType,StudentId,deviceOnStatus;
	String lat,lang,railLat,railLang,time;
	String lat_direction,lan_direction,address,speed;
	FeatureAddressDetailsDTO railFeatureDto;


	








	public FeatureAddressDetailsDTO getRailFeatureDto() {
		return railFeatureDto;
	}



	public void setRailFeatureDto(FeatureAddressDetailsDTO railFeatureDto) {
		this.railFeatureDto = railFeatureDto;
	}



	public int getDeviceOnStatus() {
		return deviceOnStatus;
	}



	public void setDeviceOnStatus(int deviceOnStatus) {
		this.deviceOnStatus = deviceOnStatus;
	}

	
	

	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getSpeed() {
		return speed;
	}



	public void setSpeed(String speed) {
		this.speed = speed;
	}



	public String getLat() {
		return lat;
	}

	
	
	public String getLat_direction() {
		return lat_direction;
	}



	public void setLat_direction(String lat_direction) {
		this.lat_direction = lat_direction;
	}



	public String getLan_direction() {
		return lan_direction;
	}



	public void setLan_direction(String lan_direction) {
		this.lan_direction = lan_direction;
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

	public String getRailLat() {
		return railLat;
	}

	public void setRailLat(String railLat) {
		this.railLat = railLat;
	}

	public String getRailLang() {
		return railLang;
	}

	public void setRailLang(String railLang) {
		this.railLang = railLang;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getStudentId() {
		return StudentId;
	}

	public void setStudentId(int studentId) {
		StudentId = studentId;
	}

	public int getDeviceType() {
		return DeviceType;
	}

	public void setDeviceType(int deviceType) {
		DeviceType = deviceType;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}

	/*Comparator for sorting the list by Store Name*/
	public static Comparator<RailDeviceInfoDto> StatusComparator = new Comparator<RailDeviceInfoDto>() {

		public int compare(RailDeviceInfoDto s1, RailDeviceInfoDto s2) {
			String StoreName1 = s1.getDeviceOnStatus()+"";
			String StoreName2 = s2.getDeviceOnStatus()+"";

			//ascending order
			return StoreName1.compareTo(StoreName2);

			//descending order
			//return StoreName2.compareTo(StoreName1);
		}};
}
