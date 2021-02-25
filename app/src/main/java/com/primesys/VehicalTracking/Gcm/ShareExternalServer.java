package com.primesys.VehicalTracking.Gcm;

import android.content.Context;
import android.util.Log;

import com.primesys.VehicalTracking.Activity.LoginActivity;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;


public class ShareExternalServer {

	public String shareRegIdWithAppServer(final Context context,
			final String regId) {

		String result = "";
		
		result=shareRegIdWithAppErlangServer(context,regId);
		
		return result;

	/*ap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("regId", regId);

		try {
			URL serverUrl = null;
			try {
				serverUrl = new URL(Common.APP_SERVER_URL);
			} catch (MalformedURLException e) {
				Log.e("AppUtil", "URL Connection Error: "
						+ Common.APP_SERVER_URL, e);
				result = "Invalid URL: " + Common.APP_SERVER_URL;
			}

			StringBuilder postBody = new StringBuilder();
			Iterator<Entry<String, String>> iterator = paramsMap.entrySet()
					.iterator();

			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				postBody.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			
			Log.e("body Request---", body);
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverUrl.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setUseCaches(false);
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				if (status == 200) {
					result = "RegId shared with Application Server. RegId: "
							+ regId;
				} else {
					result = "Post Failure." + " Status: " + status;
				}
			} finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
			}

		} catch (IOException e) {
			result = "Post Failure. Error in sharing with App Server.";
			Log.e("AppUtil", "Error in sharing with App Server: " + e);
		}
		return result;*/
	}
	
	
	
	public String shareRegIdWithAppErlangServer(final Context context,
			final String regId) {

		String result = "";

		try {
			try {
				LoginActivity.mClient.sendMessage(makeGcmRegJSON(regId));
				result="shareRegIdWithAppErlangServer------------Post REgisetr Key Successfully to App Server.";
			} catch (Exception e) {
				e.printStackTrace();
			}

		

		} catch (Exception e) {
			result = "Post Failure. Error in sharing with App Server.";
			Log.e("AppUtil", "Error in sharing with App Server: " + e);
		}
		return result;
	}



	private String makeGcmRegJSON(String regId) {

		String gcmSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event",Common.EV_GEOFENCE_REG_KEY);
			jo.put("parent_id", Common.userid);
			jo.put("reg_key",regId);
			gcmSTring=jo.toString();
			System.err.print("MakeGcmRegJSON Event Fire---"+gcmSTring);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}
		return gcmSTring;
	
	}
}
