package com.primesys.VehicalTracking.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.FeatureAddressDetailsDTO;
import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.MainSliderImageDTO;
import com.primesys.VehicalTracking.Dto.RailWayAddressDTO;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Dto.UserModuleDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.historyFragment;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "tracking.db";
	private static final int DATABASE_VERSION = 17;

	private static final String TABLE_PARENT="db_parent";
	private static final String TABLE_HISTORY="db_history";
	private static final String TABLE_USER="db_user";
	private static final String TABLE_PARENTINFO="db_parentinfo";
	public static final String TABLE_DEVICE_LIST = "db_device_list";
    public static final String TABLE_SMSFUNC_LIST = "db_smsfunc_list";
	public static final String TABLE_MYKIDDYSMSFUNC_LIST = "db_mykiddysmsfunc_list";

	public static final String TABLE_SMSNOTIFICATION_LIST = "db_smsnotofication_list";
    public static final String TABLE_ACCREPORT_LIST = "db_accreport_list";
	public static final String TABLE_GEOFENCE_LIST = "db_geofence_list";
	public static final String TABLE_SOSNO = "db_sos_no";
	public static final String TABLE_MODULE = "db_module";
	public static final String TABLE_SLIDER = "db_slider";
	public static final String TABLE_FEATUREADDRESS= "db_feature_address";

	private static final String create_table_parent="create table "+TABLE_PARENT+" (id INTEGER PRIMARY KEY AUTOINCREMENT,message TEXT,sentdate DATETIME ,from_msg VARCHAR(50),to_msg VARCHAR(50),group_id VARCHAR(50),type varchar(1),ref_id long,student_id varchar(20),status INTEGER,send_status INTEGER)";
	private static final String create_table_history="create table "+TABLE_HISTORY+" (id INTEGER PRIMARY KEY AUTOINCREMENT,lat DOUBLE,lan DOUBLE,timestamp INTEGER,speed INTEGER)";
	private static final String create_table_user="Create table "+TABLE_USER+"(userid String PRIMARY KEY  not null,Photo blob)";
	private static final String create_table_parentinfo="Create table "+TABLE_PARENTINFO+"(id INTEGER PRIMARY KEY ,parent_name varchar(20),parent_id INTEGER,photo varchar(200))";
	private static final String create_table_devicelist="CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,StudentID Varchar(30),Name Varchar(30),photo Varchar(200),type Varchar(15),IMEI_no Varchar(20),ExpiryDate Varchar(40),Remaining_Day Varchar(11),ShowGoogleAddress Varchar(2))";
	private static final String create_table_smsfunclist="CREATE TABLE IF NOT EXISTS " + TABLE_SMSFUNC_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,CommnadType Varchar(100),ActualCommand Varchar(200),Title Varchar(200),AnsFromDevice Varchar(1500))";

	private static final String create_table_mykidddysmsfunclist="CREATE TABLE IF NOT EXISTS " + TABLE_MYKIDDYSMSFUNC_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,CommnadType Varchar(100),ActualCommand Varchar(200),Title Varchar(200),AnsFromDevice Varchar(1500))";

	private static final String create_table_smsnotifylist="CREATE TABLE IF NOT EXISTS " + TABLE_SMSNOTIFICATION_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,Notify_Title Varchar(100),Notify_Type Varchar(50),LatDir Varchar(5),LangDir Varchar(5),Lat Varchar(20),Lang Varchar(25),Speed Varchar(15),Time Varchar(15)," +
			"Date Varchar(15),ImeiNo Varchar(20),UpdateTime Varchar(50))";
	private static final String create_table_accreportlist="CREATE TABLE IF NOT EXISTS " + TABLE_ACCREPORT_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,CommnadType Varchar(100),ActualCommand Varchar(200),Title Varchar(200),AnsFromDevice Varchar(1500))";
	private static final String create_table_geofencelist="CREATE TABLE IF NOT EXISTS " + TABLE_GEOFENCE_LIST +" (Id INTEGER,StudentID Varchar(30),GeoId INTEGER PRIMARY KEY,GeoName Varchar(30),Geofenceno Varchar(10),Fencelat Varchar(15)"
			+ ",Fencelang Varchar(15),FenceType Varchar(20),FenceStatus varchar(20),Currlat Varchar(15),Currlang Varchar(15),Speed Varchar(15),Timestamp Varchar(15),Distance Varchar(11),Discription TEXT)";
	private static final String create_table_sosno="CREATE TABLE IF NOT EXISTS " + TABLE_SOSNO +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,SosId Varchar(30),SosName Varchar(30),SosNumber Varchar(20))";
	private static final String create_table_module="CREATE TABLE IF NOT EXISTS " + TABLE_MODULE +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,ModuleId Varchar(10),Module Varchar(30),ModuleTitle Varchar(50)" +
			",ModuleDesc Varchar(100),ModuleActivity Varchar(50),ImageUrl TEXT)";

	private static final String create_table_slider="CREATE TABLE IF NOT EXISTS " + TABLE_SLIDER +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,SliderId Varchar(10),ShopId Varchar(30),SliderImage TEXT,CreatedAt Varchar(50))";
	private static final String create_table_feature_address="CREATE TABLE IF NOT EXISTS " + TABLE_FEATUREADDRESS +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"filename Varchar(500),railway Varchar(200),division Varchar(200)," +
			"stationFrom Varchar(200),stationTo Varchar(200),chainage Varchar(200)" +
			",trolley Varchar(200),line Varchar(10),section Varchar(200),blockSction Varchar(200),mode Varchar(50),kiloMeter Double" +
			",distance Double,latitude Double,longitude Double,featureCode INTEGER" +
			",featureDetail Varchar(500),featureImage Varchar(1000))";



	private static DBHelper helper;
	SQLiteDatabase database;
	DBHelper context=DBHelper.this;
	public static DBHelper getInstance(Context context) {
		if (helper == null) {
			helper  = new DBHelper(context,DATABASE_NAME,DATABASE_VERSION);
		}
		return helper;
	}
	public DBHelper(Context context, String name,int version) {
		super(context, name,null, version);
	}
	//create  tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		try
		{
			db.execSQL(create_table_parent);
			db.execSQL(create_table_history);
			db.execSQL(create_table_user);

			db.execSQL(create_table_parentinfo);

			db.execSQL(create_table_devicelist);
			db.execSQL(create_table_smsfunclist);
			db.execSQL(create_table_smsfunclist);
			db.execSQL(create_table_smsnotifylist);
			db.execSQL(create_table_accreportlist);
			db.execSQL(create_table_sosno);
			db.execSQL(create_table_module);
			db.execSQL(create_table_slider);
			db.execSQL(create_table_accreportlist);
			db.execSQL(create_table_smsnotifylist);
			db.execSQL(create_table_smsfunclist);
			db.execSQL(create_table_mykidddysmsfunclist);
			db.execSQL(create_table_feature_address);
			db.execSQL(create_table_geofencelist);



		}catch(Exception e)
		{
			Log.e("Exception  table",e.getMessage()+" "+e.getCause());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_PARENTINFO);

		db.execSQL("DROP TABLE IF EXISTS "+TABLE_DEVICE_LIST);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SMSFUNC_LIST);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_MYKIDDYSMSFUNC_LIST);

		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SMSNOTIFICATION_LIST);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCREPORT_LIST);

		db.execSQL("DROP TABLE IF EXISTS "+TABLE_GEOFENCE_LIST);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SOSNO);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_MODULE);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SLIDER);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_FEATUREADDRESS);

		// create new tables
		onCreate(db);
	}


	//delete all the records from the table
	public void truncateTables(String tablename)
	{
		try
		{
			System.err.println("Inside trucate");
			database=this.getWritableDatabase();
			String query ="DELETE FROM "+tablename;
			database.execSQL(query);
		}catch(Exception e)
		{
			Log.e("Exception in truncate",e.getMessage());
		}
		finally{
			database.close();
		}
	}


	//delete all the records from the table
	public void truncateSmsDataTables()
	{
		try
		{
			System.err.println("Inside trucate");
			database=this.getWritableDatabase();
			String query ="DELETE FROM db_smsnotofication_list WHERE Id IN (SELECT Id  FROM db_smsnotofication_list ORDER BY Id  ASC LIMIT "+Common.ACCSMSDeleteNo+" )";
			database.execSQL(query);
		}catch(Exception e)
		{
			Log.e("Exception in truncate",e.getMessage());
		}
		finally{
			database.close();
		}
	}
	//get last timestamp value from the DB
	public void getLastTimeStamp()
	{
		String tablename="";
		String timestamp="00000";

		if(Common.roleid.equals("3")||Common.roleid.equals("5"))
			tablename=TABLE_PARENT;
		database=this.getReadableDatabase();
		String sql="select  sentdate from "+tablename+" ORDER BY sentdate DESC LIMIT 1 ";
		try
		{
			Cursor cursor=database.rawQuery(sql, null);
			if(cursor.moveToFirst())
			{
				timestamp=cursor.getString(0);
			}
		}catch(Exception e)
		{

		}finally
		{
			database.close();
		}
		Common.timstamp= timestamp;
	}
	//convert date into unix timestamp
	long convertToLong(String date)
	{
		long time=0;
		try
		{
			DateFormat dfm = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			time = dfm.parse(date).getTime();
		}catch(Exception e)
		{
			Log.e("date conversation error",e.getMessage());
			e.printStackTrace();
			time=Long.parseLong(date);
		}
		return time;
	}
	// insert the data into the history table
	public String insertLocation(ArrayList<LocationData> list)
	{
		String result="success";
		database=this.getWritableDatabase();
		try
		{
			String sql="insert or replace into "+TABLE_HISTORY+"(lat,lan,timestamp,speed) values(?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql);
			database.beginTransaction();
			for(LocationData l : list)
			{
				statement.bindDouble(1, l.getLat());
				statement.bindDouble(2, l.getLan());
				statement.bindLong(3, l.getTimestamp());
				statement.bindLong(4, l.getSpeed());
				statement.execute();
			}

			database.setTransactionSuccessful();
			database.endTransaction();
			Common.Location_getting=true;
		}catch(Exception e)
		{
			result="failure";
			System.err.print(e);
			e.printStackTrace();
		}
		finally
		{
			if (Common.Location_getting) {
				//ShowMap.GoToHistoty();

				historyFragment.addDefaultLocations();
				database.close();
			}
		}
		return result;
	}
	//select all the history detail from the table
	public ArrayList<LocationData> showDetails()
	{
		ArrayList<LocationData> list=new ArrayList<LocationData>();
		try
		{
			String query="Select * from "+TABLE_HISTORY;
			database=this.getReadableDatabase();
			Cursor cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{

				LocationData l=new LocationData();

				l.setLat(cursor.getDouble(1));
				l.setLan(cursor.getDouble(2));
				l.setTimestamp(cursor.getLong(3));
				l.setSpeed(cursor.getInt(4));
				list.add(l);

			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{

			database.close();
		}
		return list;
	}

	//insert the Photo and the userid
	public long insertPhoto(Bitmap bmp,String userid)
	{
		long c=0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
		byte[] buffer=out.toByteArray();
		database=this.getWritableDatabase();
		database.beginTransaction();
		ContentValues values;
		try
		{
			values = new ContentValues();
			values.put("photo", buffer);
			values.put("userid",userid);
			c= database.replace(TABLE_USER, null, values);
			database.setTransactionSuccessful();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			database.endTransaction();
			database.close();
		}
		return c;
	}
	// get the bitmap object based on the id
	public Bitmap getBitMap(String id)
	{
		Bitmap bmp=null;
		database=this.getWritableDatabase();
		database.beginTransaction();
		try
		{
			String sql="select Photo from "+TABLE_USER+" where userid ="+id;
			Cursor cursor=database.rawQuery(sql, null);
			if(cursor.getCount()>0){
				if (cursor.moveToNext()) {
					byte[] blob = cursor.getBlob(cursor.getColumnIndex("Photo"));
					// Convert the byte array to Bitmap
					bmp=BitmapFactory.decodeByteArray(blob, 0, blob.length);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.endTransaction();
			database.close();
		}
		return bmp;
	}
	//get the count of unread messages
	public int getCount(String userid,boolean isGroup)
	{
		String sql;
		int cnt=0;
		if(!isGroup)
			sql="select message from "+TABLE_PARENT+" where status =0 and from_msg="+userid+" and to_msg="+Common.userid;
		else
			sql="select message from "+TABLE_PARENT+" where status =0 and to_msg="+userid+" and from_msg !="+Common.userid;

		database=this.getReadableDatabase();
		Cursor cur=database.rawQuery(sql, null);
		cnt=cur.getCount();
		database.close();
		return cnt;
	}



	public void Insert_Device_list(ArrayList<DeviceDataDTO> arr) {

		// TODO Auto-generated method stub

		truncateTables(TABLE_DEVICE_LIST);
		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String sql1="Insert or Replace into "+TABLE_DEVICE_LIST+"(StudentID,Name,photo,type,IMEI_no,ExpiryDate,Remaining_Day,ShowGoogleAddress) values(?,?,?,?,?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(DeviceDataDTO l : arr)
			{
				statement.bindString(1,l.getId());
				statement.bindString(2, l.getName());
				statement.bindString(3, l.getPath());
				statement.bindString(4, l.getType());
				statement.bindString(5, l.getImei_no());
				statement.bindString(6, l.getExpiary_date());
				statement.bindString(7, l.getRemaining_days_to_expire());
				statement.bindString(8, l.getShowGoogleAddress());


				statement.execute();
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.close();
		}


	}



	public ArrayList<DeviceDataDTO> Show_Device_list() {

		ArrayList<DeviceDataDTO> list=new ArrayList<DeviceDataDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
			String query="Select * from "+TABLE_DEVICE_LIST;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				DeviceDataDTO dmDetails=new DeviceDataDTO();

				dmDetails.setId(cursor.getString(1));
				dmDetails.setName(cursor.getString(2));

				dmDetails.setPath(cursor.getString(3).replaceAll("~", "").trim());
				dmDetails.setType(cursor.getString(4));
				dmDetails.setImei_no(cursor.getString(5));
				dmDetails.setExpiary_date(cursor.getString(6));
				dmDetails.setRemaining_days_to_expire(cursor.getString(7));
				dmDetails.setShowGoogleAddress(cursor.getString(8));


				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.close();
		}
		return list;


	}


	public void insertSMSNotification(SmsNotificationDTO smsdata) {

		Log.e("insertSMSNonti ---",smsdata.toString());

		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String query="select count(*) from "+TABLE_SMSNOTIFICATION_LIST;
			Cursor cusr=database.rawQuery(query,null);
			if(cusr.getCount()>=Common.ACCSmsDeleteCheckCount)
				truncateSmsDataTables();

			String sql1="Insert or Replace into "+TABLE_SMSNOTIFICATION_LIST+"(Notify_Title,Notify_Type,LatDir,LangDir,Lat,Lang,Speed,Time,Date,ImeiNo,UpdateTime)" +
					" values(?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";

			statement.bindString(1,smsdata.getNotify_Title()+"");
			statement.bindString(2,smsdata.getNotify_Type()+"");
			statement.bindString(3,smsdata.getLatDir()+"");
			statement.bindString(4,smsdata.getLangDir()+"");

			statement.bindString(5,smsdata.getLat()+"");
			statement.bindString(6,smsdata.getLang()+"");
			statement.bindString(7,smsdata.getSpeed()+"");
			statement.bindString(8,smsdata.getTime()+"");
			statement.bindString(9,smsdata.getDate()+"");
			statement.bindString(10,smsdata.getImeiNo()+"");
			statement.bindString(11,System.currentTimeMillis()+"");

			statement.execute();

			database.setTransactionSuccessful();
			database.endTransaction();

		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.close();
		}

	}

	// get the Student name object based on the id
	public String get(String imei)
	{
		String name=null;
		database=this.getWritableDatabase();
		database.beginTransaction();
		try
		{
			String sql="select Name from "+TABLE_DEVICE_LIST+" where IMEI_no ="+imei;
			Cursor cursor=database.rawQuery(sql, null);
			if(cursor.getCount()>0){
				if (cursor.moveToNext()) {

					name=cursor.getString(cursor.getColumnIndex("Name"));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.endTransaction();
			database.close();
		}
		return name;
	}



	// get the Student name object based on the id
	public String getdevice_name_from_StudentId(String StudentId)
	{
		String name=null;
		database=this.getWritableDatabase();
		database.beginTransaction();
		Log.e("Helper ", "getdevice_name_from_StudentId----------" + StudentId.toString());

		try
		{
			String sql="select Name from "+TABLE_DEVICE_LIST+" where StudentID ="+StudentId;

			Cursor cursor=database.rawQuery(sql, null);
			if(cursor.getCount()>0){
				if (cursor.moveToNext()) {

					name=cursor.getString(cursor.getColumnIndex("Name"));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.endTransaction();
			database.close();
		}
		return name;
	}



	public void insertAccReport(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata) {

		Log.e("insertACCreport---",smslistdata.toString());

		truncateTables(TABLE_ACCREPORT_LIST);
		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String sql1="Insert or Replace into "+TABLE_ACCREPORT_LIST+"(CommnadType,ActualCommand,Title,AnsFromDevice) values(?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(VehicalTrackingSMSCmdDTO l : smslistdata)
			{
				statement.bindString(1,l.getCommnadType());
				statement.bindString(2,l.getActualCommand());
				statement.bindString(3,l.getTitle());
				statement.bindString(4,l.getAnsFromDevice());

				statement.execute();
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.close();
		}

	}






	public ArrayList<VehicalTrackingSMSCmdDTO> Show_AccReport() {

		ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
			database.beginTransaction();
			String query="Select * from "+TABLE_ACCREPORT_LIST;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				VehicalTrackingSMSCmdDTO dmDetails=new VehicalTrackingSMSCmdDTO();

				dmDetails.setCommnadType(cursor.getString(1));
				dmDetails.setActualCommand(cursor.getString(2));

				dmDetails.setTitle(cursor.getString(3).replaceAll("~", "").trim());
				dmDetails.setAnsFromDevice(cursor.getString(4));

				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}

		return list;


	}



	public ArrayList<SmsNotificationDTO> Show_SMSACCNofication(String deviceImieNo) {

		ArrayList<SmsNotificationDTO> list=new ArrayList<SmsNotificationDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
			database.beginTransaction();
			String query="Select * from "+TABLE_SMSNOTIFICATION_LIST+" where ImeiNo ="+deviceImieNo;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{

				SmsNotificationDTO dmDetails=new SmsNotificationDTO();

				dmDetails.setTime(cursor.getString(8));
				dmDetails.setImeiNo(cursor.getString(10));

				dmDetails.setDate(cursor.getString(9).replaceAll("~", "").trim());
				dmDetails.setNotify_Type(cursor.getString(4));
				dmDetails.setLang(cursor.getString(6));
				dmDetails.setLatDir(cursor.getString(3));
				dmDetails.setLangDir(cursor.getString(4));
				dmDetails.setLat(cursor.getString(5));
				dmDetails.setNotify_Title(cursor.getString(1).replaceAll("~", "").trim());
				dmDetails.setNotify_Type(cursor.getString(2));


				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}

		return list;


	}





	public void insertSMSFunction(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata) {

		Log.e("inseron sms---",smslistdata.toString());

		truncateTables(TABLE_SMSFUNC_LIST);
		String result="success";
		try
		{
			database=this.getWritableDatabase();

			String sql1="Insert or Replace into "+TABLE_SMSFUNC_LIST+"(CommnadType,ActualCommand,Title,AnsFromDevice) values(?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(VehicalTrackingSMSCmdDTO l : smslistdata)
			{
				statement.bindString(1,l.getCommnadType());
				statement.bindString(2,l.getActualCommand());
				statement.bindString(3,l.getTitle());
				statement.bindString(4,l.getAnsFromDevice());

				statement.execute();
			}

		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();

		}

	}






	public ArrayList<VehicalTrackingSMSCmdDTO> Show_SMSFunction() {

		ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();

			database.beginTransaction();
			String query="Select * from "+TABLE_SMSFUNC_LIST;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				VehicalTrackingSMSCmdDTO dmDetails=new VehicalTrackingSMSCmdDTO();

				dmDetails.setCommnadType(cursor.getString(1));
				dmDetails.setActualCommand(cursor.getString(2));

				dmDetails.setTitle(cursor.getString(3).replaceAll("~", "").trim());
				dmDetails.setAnsFromDevice(cursor.getString(4));

				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}

		return list;


	}






	public void insertMykiddySMSFunction(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata) {

		Log.e(PrimesysTrack.TAG,smslistdata.toString());

		truncateTables(TABLE_MYKIDDYSMSFUNC_LIST);
		String result="success";
		try
		{
			database=this.getWritableDatabase();

			String sql1="Insert or Replace into "+TABLE_MYKIDDYSMSFUNC_LIST+"(CommnadType,ActualCommand,Title,AnsFromDevice) values(?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(VehicalTrackingSMSCmdDTO l : smslistdata)
			{
				statement.bindString(1,l.getCommnadType());
				statement.bindString(2,l.getActualCommand());
				statement.bindString(3,l.getTitle());
				statement.bindString(4,l.getAnsFromDevice());

				statement.execute();
			}

		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();

		}

	}






	public ArrayList<VehicalTrackingSMSCmdDTO> Show_MykiddySMSFunction() {

		ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();

			database.beginTransaction();
			String query="Select * from "+TABLE_MYKIDDYSMSFUNC_LIST;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				VehicalTrackingSMSCmdDTO dmDetails=new VehicalTrackingSMSCmdDTO();

				dmDetails.setCommnadType(cursor.getString(1));
				dmDetails.setActualCommand(cursor.getString(2));

				dmDetails.setTitle(cursor.getString(3).replaceAll("~", "").trim());
				dmDetails.setAnsFromDevice(cursor.getString(4));

				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}

		return list;


	}
	// get the Student name object based on the id
	public String getdevice_name(String imei)
	{
		String name=null;
		database=this.getWritableDatabase();
		database.beginTransaction();
		try
		{
			String sql="select Name from "+TABLE_DEVICE_LIST+" where IMEI_no ="+imei;
			Cursor cursor=database.rawQuery(sql, null);
			if(cursor.getCount()>0){
				if (cursor.moveToNext()) {

					name=cursor.getString(cursor.getColumnIndex("Name"));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.endTransaction();
			database.close();
		}
		return name;
	}

	public String insertGeofence(GeofenceDTO m) {

		String result="success";

		database=this.getWritableDatabase();
		try
		{

			System.out.println("Inside insertGeofence-----------------------------------------");

			String sql="Insert or Replace into  "+TABLE_GEOFENCE_LIST+"(StudentID,GeoId,GeoName,Geofenceno,Fencelat,Fencelang,FenceType"
					+ ",FenceStatus,Currlat,Currlang,Speed,Timestamp,Distance,Discription) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement statement = database.compileStatement(sql);
			database.beginTransaction();
			statement.bindString(1,m.getStudentId()+"");
			statement.bindLong(2, Integer.parseInt(m.getGeoID()));
			statement.bindString(3, m.getGeoName());
			statement.bindString(4, "1");
			statement.bindString(5, m.getLat());
			statement.bindString(6, m.getLang());
			statement.bindString(7, m.getType());
			statement.bindString(8,m.getGeofencestatus());
			statement.bindString(9, m.getCurrlat());
			statement.bindString(10,m.getCurrlang());
			statement.bindString(11,m.getSpeed()+"");
			statement.bindString(12,m.getTimestamp()+"");
			statement.bindString(13,m.getDistance()+"");
			statement.bindString(14,m.getDiscripation()+"");

			statement.execute();


		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{	database.setTransactionSuccessful();

			database.endTransaction();
			database.close();
		}
		return result;

	}

	public ArrayList<GeofenceDTO> show_geofencelist(String studntid) {
		ArrayList<GeofenceDTO> list=new ArrayList<GeofenceDTO>();
		Cursor cursor = null;

		System.out.println("---------DBHELper show_geofencelist-- "+studntid);
		try
		{	database=this.getReadableDatabase();
			database.beginTransaction();
			String query="Select * from "+TABLE_GEOFENCE_LIST+" Where StudentID = "+studntid ;
			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				GeofenceDTO geofenceObj=new GeofenceDTO();
				geofenceObj.setStudentId(cursor.getString(1));

				geofenceObj.setCurrlat(cursor.getString(9));
				geofenceObj.setCurrlang(cursor.getString(10));
				geofenceObj.setSpeed(cursor.getInt(11));
				geofenceObj.setTimestamp(cursor.getInt(12));
				geofenceObj.setGeofencestatus(cursor.getString(8));

				geofenceObj.setGeoID(cursor.getString(2));
				geofenceObj.setFenceno(cursor.getString(4));


				geofenceObj.setGeoName(cursor.getString(3));
				geofenceObj.setType(cursor.getString(7));
				geofenceObj.setLat(cursor.getString(5));
				geofenceObj.setLang(cursor.getString(6));
				geofenceObj.setDistance(cursor.getString(13));
				geofenceObj.setDiscripation(cursor.getString(14));

				list.add(geofenceObj);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			//cursor.close();

			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();		}
		return list;
	}


	public void DeleteGeofenceHistory(String geoID) {
		String result="";
		database=this.getWritableDatabase();
		try
		{
			String query="select * from "+TABLE_GEOFENCE_LIST +" Where GeoId = "+geoID ;
			Cursor cusr=database.rawQuery(query,null);
			if(cusr.getCount()>0)
			{

				database.beginTransaction();

				String sql ="DELETE FROM "+TABLE_GEOFENCE_LIST +" Where GeoId = "+geoID ;
				database.execSQL(sql);



			}
		}catch(Exception e)
		{
			result = "failure";
		}
		finally
		{

			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();		}
	}



	public void Insert_Module_list(ArrayList<UserModuleDTO> arr) {

		// TODO Auto-generated method stub
		if(Common.connectionStatus ){
			truncateTables(TABLE_MODULE);
		}
		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String sql1="Insert or Replace into "+TABLE_MODULE+"(ModuleId,Module,ModuleTitle,ModuleDesc,ModuleActivity," +
					"ImageUrl) values(?,?,?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(UserModuleDTO l : arr)
			{
				statement.bindString(1,l.getModuleId());
				statement.bindString(2, l.getModule());
				statement.bindString(3, l.getModuleTitle());
				statement.bindString(4, l.getModuleDesc());
				statement.bindString(5, l.getModuleActivity());
				statement.bindString(6, l.getImageUrl());
				statement.execute();
			}

		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}


	}



	public ArrayList<UserModuleDTO> Show_Module_list() {

		ArrayList<UserModuleDTO> list= new ArrayList<>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
			database.beginTransaction();
			String query="Select * from "+TABLE_MODULE;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				UserModuleDTO dmDetails=new UserModuleDTO();


				dmDetails.setModuleId(cursor.getString(1));
				dmDetails.setModule(cursor.getString(2));
				dmDetails.setModuleTitle(cursor.getString(3));
				dmDetails.setModuleDesc(cursor.getString(4));
				dmDetails.setModuleActivity(cursor.getString(5));
				dmDetails.setImageUrl(cursor.getString(6));
				list.add(dmDetails);
			}
			cursor.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();		}
		return list;


	}

	public void Insert_Slider_list(ArrayList<MainSliderImageDTO> arr) {

		// TODO Auto-generated method stub
		if(Common.connectionStatus ){
			truncateTables(TABLE_SLIDER);
		}
		String result="success";
		database=this.getWritableDatabase();
		try
		{


			String sql1="Insert or Replace into "+TABLE_SLIDER+"(SliderId,ShopId,SliderImage,CreatedAt) values(?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";
			for(MainSliderImageDTO l : arr)
			{
				statement.bindString(1,l.getSliderId());
				statement.bindString(2, l.getShopId());
				statement.bindString(3, l.getSliderImage());
				statement.bindString(4, l.getCreatedAt());

				statement.execute();
			}

		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();
		}


	}



	public ArrayList<MainSliderImageDTO> Show_Slider_list() {

		ArrayList<MainSliderImageDTO> list= new ArrayList<>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
			database.beginTransaction();
			String query="Select * from "+TABLE_SLIDER;

			cursor=database.rawQuery(query, null);
			while(cursor.moveToNext())
			{
				MainSliderImageDTO dmDetails=new MainSliderImageDTO();


				dmDetails.setSliderId(cursor.getString(1));
				dmDetails.setShopId(cursor.getString(2));
				dmDetails.setSliderImage(cursor.getString(3));
				dmDetails.setCreatedAt(cursor.getString(4));

				list.add(dmDetails);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.setTransactionSuccessful();
			database.endTransaction();
			database.close();		}
		return list;


	}

	public void insertFeatureAddress(ArrayList<RailWayAddressDTO> data) {

		// TODO Auto-generated method stub

		Log.e(PrimesysTrack.TAG,"-------------insertFeatureAddress----------"+data.size());
		truncateTables(TABLE_FEATUREADDRESS);
		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String sql1="Insert or Replace into "+TABLE_FEATUREADDRESS+"(filename,railway,division,stationFrom ,stationTo ," +
					"chainage,trolley,line,mode,section,blockSction,kiloMeter,distance" +
					",latitude,longitude,featureCode,featureDetail,featureImage )" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();

			for (int i=0;i<data.size();i++){
				RailWayAddressDTO railData = data.get(i);
				for(FeatureAddressDetailsDTO l : railData.getFeatureAddressDetail())
				{
					statement.bindString(1,railData.getFileName());
					statement.bindString(2, railData.getRailWay());
					statement.bindString(3, railData.getDivision());
					statement.bindString(4, railData.getStationFrom());
					statement.bindString(5, railData.getStationTo());
					statement.bindString(6, railData.getChainage());
					statement.bindString(7, railData.getTrolley());
					statement.bindString(8,railData.getLine());
					statement.bindString(9, railData.getMode());
					statement.bindString(10, l.getSection());
					statement.bindString(11, l.getBlockSection());
					statement.bindDouble(12, l.getKiloMeter());
					statement.bindDouble(13, l.getDistance());
					statement.bindDouble(14, l.getLatitude());
					statement.bindDouble(15, l.getLongitude());
					statement.bindLong(16, l.getFeatureCode());
					statement.bindString(17, l.getFeatureDetail());
					statement.bindString(18,l.getFeature_image());
					statement.execute();
				}
			}

			database.setTransactionSuccessful();
			database.endTransaction();
		}catch(Exception e)
		{
			result="failure";
			e.printStackTrace();
		}
		finally
		{
			database.close();
		}

		Log.e(PrimesysTrack.TAG,"-------------insertFeatureAddress-----result-----"+result);

	}


	public ArrayList<FeatureAddressDetailsDTO> getFeatureAddress(LatLng center) {

		ArrayList<FeatureAddressDetailsDTO> FeatureAddressDetailslist=new ArrayList<FeatureAddressDetailsDTO>();
		Cursor cursor = null;
		try
		{
			database=this.getReadableDatabase();
			//String query="Select * from "+TABLE_FEATUREADDRESS;

			String queary1="Select * from "+TABLE_FEATUREADDRESS+" where" +
					" latitude != 0  and latitude between " +( center.latitude - 0.070)
					+" and "+(center.latitude + 0.070)  +" and longitude between" +
					" "+( center.longitude  - 0.070)+" and "+(center.longitude + 0.070)
					+" order by abs("+center.longitude+ " - latitude) " +
					"+ abs("+center.longitude+" - longitude)"+"LIMIT 500";


			cursor=database.rawQuery(queary1, null);
			while(cursor.moveToNext())
			{
				FeatureAddressDetailsDTO addressDto=new FeatureAddressDetailsDTO();

					addressDto.setDistance(cursor.getDouble(13));
					addressDto.setFeature_image(cursor.getString(18).replaceAll("~", "").trim());
					addressDto.setFeatureCode(cursor.getInt(16));
					addressDto.setFeatureDetail(cursor.getString(17));
					addressDto.setKiloMeter(cursor.getDouble(12));
					addressDto.setLatitude(cursor.getDouble(14));
					addressDto.setLongitude(cursor.getDouble(15));
					addressDto.setSection(cursor.getString(9));
					addressDto.setBlockSection(cursor.getString(10));

					addressDto.setLocation(new LatLng(addressDto.getLatitude(),addressDto.getLongitude()));

					FeatureAddressDetailslist.add(addressDto);
					//   addMarkerToMap(addressDto,j,jsonObject.getString("stationFrom"),jsonObject.getString("stationTo"));

				}


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			database.close();
		}
		return FeatureAddressDetailslist;


	}
}
