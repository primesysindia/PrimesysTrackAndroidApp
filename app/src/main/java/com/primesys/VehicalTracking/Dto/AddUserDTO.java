package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class AddUserDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String userName;String userId,userCity;
	

	public String getUserCity() {
		return userCity;
	}


	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


}
