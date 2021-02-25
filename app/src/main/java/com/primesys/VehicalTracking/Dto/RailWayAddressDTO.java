package com.primesys.VehicalTracking.Dto;

import java.util.ArrayList;

public class RailWayAddressDTO {
	String fileName,railWay,division,stationFrom,stationTo,chainage,trolley,line,mode,addressId;
	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	ArrayList<FeatureAddressDetailsDTO> featureAddressDetail;

	public String getFileName() {
		return fileName;
	}

	public ArrayList<FeatureAddressDetailsDTO> getFeatureAddressDetail() {
		return featureAddressDetail;
	}

	public void setFeatureAddressDetail(
			ArrayList<FeatureAddressDetailsDTO> featureAddressDetail) {
		this.featureAddressDetail = featureAddressDetail;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRailWay() {
		return railWay;
	}

	public void setRailWay(String railWay) {
		this.railWay = railWay;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getStationFrom() {
		return stationFrom;
	}

	public void setStationFrom(String stationFrom) {
		this.stationFrom = stationFrom;
	}

	public String getStationTo() {
		return stationTo;
	}

	public void setStationTo(String stationTo) {
		this.stationTo = stationTo;
	}

	public String getChainage() {
		return chainage;
	}

	public void setChainage(String chainage) {
		this.chainage = chainage;
	}

	public String getTrolley() {
		return trolley;
	}

	public void setTrolley(String trolley) {
		this.trolley = trolley;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	} 
	

}
