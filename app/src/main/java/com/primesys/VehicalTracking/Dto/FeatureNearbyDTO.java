package com.primesys.VehicalTracking.Dto;

public class FeatureNearbyDTO {
	String gpsDeviceName,date,signalLoaction,featureDetail,startLocation,endLocation,timespent,section;
	String startLat,startLang,endLat,endLang,signalLat,signalLang;
	public String getSection() {
		return section;
	}

	public String getStartLat() {
		return startLat;
	}

	public String getSignalLat() {
		return signalLat;
	}

	public void setSignalLat(String signalLat) {
		this.signalLat = signalLat;
	}

	public String getSignalLang() {
		return signalLang;
	}

	public void setSignalLang(String signalLang) {
		this.signalLang = signalLang;
	}

	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}

	public String getStartLang() {
		return startLang;
	}

	public void setStartLang(String startLang) {
		this.startLang = startLang;
	}

	public String getEndLat() {
		return endLat;
	}

	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}

	public String getEndLang() {
		return endLang;
	}

	public void setEndLang(String endLang) {
		this.endLang = endLang;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getGpsDeviceName() {
		return gpsDeviceName;
	}

	public void setGpsDeviceName(String gpsDeviceName) {
		this.gpsDeviceName = gpsDeviceName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSignalLoaction() {
		return signalLoaction;
	}

	public void setSignalLoaction(String signalLoaction) {
		this.signalLoaction = signalLoaction;
	}

	public String getFeatureDetail() {
		return featureDetail;
	}

	public void setFeatureDetail(String featureDetail) {
		this.featureDetail = featureDetail;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public String getTimespent() {
		return timespent;
	}

	public void setTimespent(String timespent) {
		this.timespent = timespent;
	}



}
