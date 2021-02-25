package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class TripInfoDto  implements Serializable {
	
	String report_id;
    String device;
    String totalkm;
    String srclat;
    String devicename;
    String src_adress;
    String dest_address;
    String srclon;
    String srcspeed;
    String srctimestamp;

    public String getSrc_adress() {
        return src_adress;
    }

    public void setSrc_adress(String src_adress) {
        this.src_adress = src_adress;
    }

    public String getDest_address() {
        return dest_address;
    }

    public void setDest_address(String dest_address) {
        this.dest_address = dest_address;
    }

    String destlat;
    String destlon;
    String destspeed;
    String desttimestamp;
    String maxspeed;
    String avgspeed;
	


	public String getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(String maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getAvgspeed() {
		return avgspeed;
	}

	public void setAvgspeed(String avgspeed) {
		this.avgspeed = avgspeed;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getDestlat() {
		return destlat;
	}

	public void setDestlat(String destlat) {
		this.destlat = destlat;
	}

	public String getDestlon() {
		return destlon;
	}

	public void setDestlon(String destlon) {
		this.destlon = destlon;
	}

	public String getDestspeed() {
		return destspeed;
	}

	public void setDestspeed(String destspeed) {
		this.destspeed = destspeed;
	}

	public String getDesttimestamp() {
		return desttimestamp;
	}

	public void setDesttimestamp(String desttimestamp) {
		this.desttimestamp = desttimestamp;
	}

	public String getSrctimestamp() {
		return srctimestamp;
	}

	public void setSrctimestamp(String srctimestamp) {
		this.srctimestamp = srctimestamp;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getTotalkm() {
		return totalkm;
	}

	public void setTotalkm(String totalkm) {
		this.totalkm = totalkm;
	}

	public String getSrclat() {
		return srclat;
	}

	public void setSrclat(String srclat) {
		this.srclat = srclat;
	}

	public String getSrclon() {
		return srclon;
	}

	public void setSrclon(String srclon) {
		this.srclon = srclon;
	}

	public String getSrcspeed() {
		return srcspeed;
	}

	public void setSrcspeed(String srcspeed) {
		this.srcspeed = srcspeed;
	}
	
}
