package com.primesys.VehicalTracking.Dto;

import java.io.Serializable;

public class LoginDetails implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
String class_id,School_id,UserType,mobileNumber,emailId;

public String getClass_id() {
	return class_id;
}

public void setClass_id(String class_id) {
	this.class_id = class_id;
}

public String getSchool_id() {
	return School_id;
}

public void setSchool_id(String school_id) {
	School_id = school_id;
}

public String getUserType() {
	return UserType;
}

public void setUserType(String userType) {
	UserType = userType;
}

public String getMobileNumber() {
	return mobileNumber;
}

public void setMobileNumber(String mobileNumber) {
	this.mobileNumber = mobileNumber;
}

public String getEmailId() {
	return emailId;
}

public void setEmailId(String emailId) {
	this.emailId = emailId;
}

}
