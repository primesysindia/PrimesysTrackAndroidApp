package com.primesys.VehicalTracking.Dto;

public class AssociatedParentDTO {
	public String getTotalChild() {
		return TotalChild;
	}

	public void setTotalChild(String totalChild) {
		TotalChild = totalChild;
	}

	String UserId,Name,Address,MobileNo,EmailId,TotalChild;

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getMobileNo() {
		return MobileNo;
	}

	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {
		EmailId = emailId;
	}

}
