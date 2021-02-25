package com.primesys.VehicalTracking.Activity;

import android.util.Log;

import com.primesys.VehicalTracking.Dto.LoginDetails;
import com.primesys.VehicalTracking.Dto.MessageMain;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
	private String serverMessage;
	private static OnMessageReceived mMessageListener = null;
	private boolean mRun = false;
	private PrintWriter out;
	private BufferedReader in;
	private BufferedOutputStream  baos ;
	boolean valid = true;
	MessageMain msg = new MessageMain();
	static String SERVERIP = Common.SERVERIP;
	static Date date = new Date();
	private Socket socket;
	static String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss")
	.format(date);
	
	
	public Client(OnMessageReceived listener) {
		mMessageListener = listener;
	}

	public Client() {
	}

	public void sendMessage(MessageMain message) {
		if (out != null && !out.checkError()) {
			try {
				mMessageListener.messageSend(message);
				out.println(message);
				out.flush();
			} catch (Exception e) {
				Log.e("write the message error",
						e.getMessage() + " " + e.getCause());
			}
		}

	}

	public void sendMessage(String message) {
		if (out != null && !out.checkError()) {
			try {
				mMessageListener.messageSend(message);
				out.println(message);
				out.flush();
			} catch (Exception e) {
				Log.e("write the message error",
						e.getMessage() + " " + e.getCause());
			}
		}
	}
	//send the file data from socket to other socket
	public void sendFile(byte[] data)
	{
		try
		{
	      baos.write(data,0,data.length-1);
	     // baos.flush();
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}
		finally{
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	     
	}
	
	public void stopClient() {
		mRun = false;
	}

	public void run() {
		mRun = true;
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			Log.e("serverAddr", serverAddr.toString());
			Log.e("TCP Client", "C: Connecting...");
			socket = new Socket(serverAddr, Common.PORT);
		//	socket.setSoTimeout(30*1000);
			if (valid) {
				LoginDetails L_Details = LoginActivity.Ldetails;
				PrintWriter obj = new PrintWriter(socket.getOutputStream());
				JSONObject jo = new JSONObject();
				JSONObject joData = new JSONObject();
				joData.put("class", "" + L_Details.getClass_id()); // send class id
				joData.put("school", "" + L_Details.getSchool_id());// send school Id
				joData.put("user_type", "" + L_Details.getUserType());// send
				joData.put("mobile_number", "" + L_Details.getMobileNumber());
				joData.put("email_id", "" + L_Details.getEmailId());
				joData.put("device_id", LoginActivity.deviceid);
				jo.put("event", "join");
				jo.put("name", Common.userid);
				jo.put("app_id", Common.app_id);
				System.out.println("Client.run()"+ Common.timstamp);
				jo.put("timestamp", Common.timstamp);
				jo.put("data", joData); // optional parameter
				obj.write(jo.toString());
				Log.e("3333333333","--------"+jo);
				obj.flush();// flush the socket
				valid = false;
			}
			try {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				baos = new BufferedOutputStream(socket.getOutputStream());
				Log.e("TCP Client", "C: Sent.");
				Log.e("TCP Client", "C: Done.");

				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));

				while (mRun&&socket.isConnected()) {
					serverMessage = in.readLine();
					if (serverMessage != null && mMessageListener != null) {
						mMessageListener.messageReceived(serverMessage);
					}
					serverMessage = null;
				}
			} catch (Exception e) {
				Log.e("TCP", "S: Error", e);
				e.printStackTrace();
			} finally {
				socket.close();
				stopClient();
			}
		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);
			e.printStackTrace();
		}
	}

	public interface OnMessageReceived {
		public void messageReceived(String message);
		public void messageSend(MessageMain message);
		public void messageSend(String message);
	}
    
	  
}