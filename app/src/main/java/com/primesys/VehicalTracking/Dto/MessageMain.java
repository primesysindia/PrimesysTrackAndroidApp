package com.primesys.VehicalTracking.Dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class MessageMain {
	private String  event, from, to, mesageText="",group_id,student_id;
	private String name,app_id,key,type="s";
	private long date_time,ref_id;
    private int status=0;
    
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public long getRef_id() {
		return ref_id;
	}
	public void setRef_id(long ref_id) {
		this.ref_id = ref_id;
	}
	public MessageMain() {
		group_id="0000";
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public String getApp_id() {
		return app_id;
	}


	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getDate_time() {
		return date_time;
	}

	public void setDate_time(long date_time) {
		this.date_time = date_time;//convertDateToTime()
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
			this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
			this.to = to;
	}
	public String getMesageText() {
		return mesageText;
	}

	public void setMesageText(String mesageText) {
		this.mesageText = mesageText;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	//convert date and time to time only
	String convertDateToTime(String dates)
	{
		String time ="";
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss"); 
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss aa");
			Date date = dateFormat.parse(dates);
			time=dateFormat2.format(date);
		}
		catch(Exception ex)
		{
			Log.e("Exception in time",ex.getMessage()+" "+ex.getCause());
		}
		return time;
	}
	//for Parent To Priciple +Tecacher

	@Override
	public String toString() {
		JSONObject jmsg = new JSONObject();
		try
		{
			jmsg.put("event", event);
			jmsg.put("app_id",app_id);
			jmsg.put("from",from);
			jmsg.put("msg",mesageText);
			jmsg.put("to", to);
			jmsg.put("ref_id",ref_id);
			jmsg.put("type", type);
		}catch(Exception e){}
		return jmsg.toString();
	}
	

}
