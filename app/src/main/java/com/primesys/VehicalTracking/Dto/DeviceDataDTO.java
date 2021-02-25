package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class DeviceDataDTO implements Serializable {


	String id;
	String name;
	String path;
	String type;
	String imei_no;
	String remaining_days_to_expire;
	String expiary_date;
	String status;

	public String getShowGoogleAddress() {
		return showGoogleAddress;
	}

	public void setShowGoogleAddress(String showGoogleAddress) {
		this.showGoogleAddress = showGoogleAddress;
	}

	String color;
	String showGoogleAddress="0";

	public String getRemaining_days_to_expire() {
		return remaining_days_to_expire;
	}

	public void setRemaining_days_to_expire(String remaining_days_to_expire) {
		this.remaining_days_to_expire = remaining_days_to_expire;
	}


	public String getExpiary_date() {
		return expiary_date;
	}

	public void setExpiary_date(String expiary_date) {
		this.expiary_date = expiary_date;
	}


	public String getStatus() {
		return status;
	}

	public String getColor() {
		return color;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public void setColor(String color) {
		this.color = color;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImei_no() {
		return imei_no;
	}

	public void setImei_no(String imei_no) {
		this.imei_no = imei_no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private static final long serialVersionUID = 1L;

}
