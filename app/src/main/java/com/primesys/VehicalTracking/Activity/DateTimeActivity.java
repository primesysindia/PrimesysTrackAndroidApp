package com.primesys.VehicalTracking.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.datetimepicker.SlideDateTimeListener;
import com.example.datetimepicker.SlideDateTimePicker;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class DateTimeActivity extends FragmentActivity
{
	ArrayList<Location> loc=new ArrayList<Location>();
	int StudentId;
	Context dateContext=DateTimeActivity.this;
	public static Boolean selStatus=false;
	private SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
	private SlideDateTimeListener listener = new SlideDateTimeListener() {

		@Override
		public void onDateTimeSet(Date date)
		{

			LoginActivity.mClient.sendMessage(makeJSONHistory(""+ Common.getGMTTimeStampFromDate(mFormatter.format(date))));
			setResult(RESULT_OK, getIntent().putExtra("dateTime", mFormatter.format(date)));
			selStatus=true;
			finish();
		}

		// Optional cancel listener
		@Override
		public void onDateTimeCancel()
		{
			finish();
		}
	};
	// JSON request to get the history
	String makeJSONHistory(String timestamp)
	{

		PrimesysTrack.mDbHelper.truncateTables("db_history");
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","get_tracking_history");
			if(Common.roleid.equals("5"))
			{					
				if (getIntent().getStringExtra("Type").equalsIgnoreCase("Child")) 
				jo.put("student_id","demo_student");
				else if(getIntent().getStringExtra("Type").equalsIgnoreCase("Car"))
				jo.put("student_id","demo_car");
				else if(getIntent().getStringExtra("Type").equalsIgnoreCase("Pet"))
					jo.put("student_id","demo_pet");
			}
			else
				jo.put("student_id",StudentId);
			jo.put("timestamp",Integer.parseInt(timestamp));

			trackSTring=jo.toString();
		}
		catch(Exception e)
		{
			Toast.makeText(dateContext, "->"+e, Toast.LENGTH_LONG).show();
		}
		return trackSTring;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_time);
		StudentId=getIntent().getIntExtra("StudentId",0);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dateRepresentation = cal.getTime();
		new SlideDateTimePicker.Builder(getSupportFragmentManager())
		.setListener(listener)
		.setInitialDate(new Date())
		.setMinDate(dateRepresentation)
		.setMaxDate(new Date())
		//.setIs24HourTime(true)
		//.setTheme(SlideDateTimePicker.HOLO_DARK)
		.setIndicatorColor(Color.parseColor("#CC6698"))
		.build()
		.show();

	}
}
