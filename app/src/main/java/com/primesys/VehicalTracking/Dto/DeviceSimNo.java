package com.primesys.VehicalTracking.Dto;

public class DeviceSimNo {

	String   DeviceSimNumber
    ,VSEnabled,StudentID
    ,VSCallback,CommnadType,ActualCommand,error,Message;

	public String getStudentID() {
		return StudentID;
	}

	public void setStudentID(String studentID) {
		StudentID = studentID;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getDeviceSimNumber() {
		return DeviceSimNumber;
	}

	public void setDeviceSimNumber(String deviceSimNumber) {
		DeviceSimNumber = deviceSimNumber;
	}

	public String getVSEnabled() {
		return VSEnabled;
	}

	public void setVSEnabled(String vSEnabled) {
		VSEnabled = vSEnabled;
	}

	public String getVSCallback() {
		return VSCallback;
	}

	public void setVSCallback(String vSCallback) {
		VSCallback = vSCallback;
	}

	public String getCommnadType() {
		return CommnadType;
	}

	public void setCommnadType(String commnadType) {
		CommnadType = commnadType;
	}

	public String getActualCommand() {
		return ActualCommand;
	}

	public void setActualCommand(String actualCommand) {
		ActualCommand = actualCommand;
	}

	
}
