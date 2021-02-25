package com.primesys.VehicalTracking.Dto;

import com.google.android.gms.maps.model.LatLng;

public class FeatureAddressDetailsDTO {

	String featureDetail,feature_image,section,blockSection;
Double kiloMeter,distance,latitude,longitude;
int featureCode;

	public Double getNearByDistance() {
		return NearByDistance;
	}

	public void setNearByDistance(Double nearByDistance) {
		NearByDistance = nearByDistance;
	}

	LatLng location;
Double NearByDistance;

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getFeatureDetail() {
		return featureDetail;
	}

	public void setFeatureDetail(String featureDetail) {
		this.featureDetail = featureDetail;
	}

	public String getFeature_image() {
		return feature_image;
	}

	public void setFeature_image(String feature_image) {
		this.feature_image = feature_image;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getBlockSection() {
		return blockSection;
	}

	public void setBlockSection(String blockSection) {
		this.blockSection = blockSection;
	}

	public Double getKiloMeter() {
		return kiloMeter;
	}

	public void setKiloMeter(Double kiloMeter) {
		this.kiloMeter = kiloMeter;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public int getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(int featureCode) {
		this.featureCode = featureCode;
	}
}
