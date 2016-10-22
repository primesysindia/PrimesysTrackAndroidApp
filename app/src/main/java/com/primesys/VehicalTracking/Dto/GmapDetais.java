package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class GmapDetais implements Serializable {
	/**
	 * 
	 */
	String id,name,path,type,imei_no;
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
