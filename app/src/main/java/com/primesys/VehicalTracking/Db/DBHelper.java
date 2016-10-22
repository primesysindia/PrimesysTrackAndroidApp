package com.primesys.VehicalTracking.Db;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.historyFragment;


public class DBHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "tracking.db";
	private static final int DATABASE_VERSION = 3;

	private static final String TABLE_PARENT="db_parent";
	private static final String TABLE_HISTORY="db_history";
	private static final String TABLE_USER="db_user";
	private static final String TABLE_PARENTINFO="db_parentinfo";
	public static final String TABLE_DEVICE_LIST = "db_device_list";
	private static final String TABLE_SMSFUNC_LIST = "db_smsfunc_list";
	private static final String TABLE_SMSNOTIFICATION_LIST = "db_smsnotofication_list";

	
	private static final String create_table_parent="create table "+TABLE_PARENT+" (id INTEGER PRIMARY KEY AUTOINCREMENT,message TEXT,sentdate DATETIME ,from_msg VARCHAR(50),to_msg VARCHAR(50),group_id VARCHAR(50),type varchar(1),ref_id long,student_id varchar(20),status INTEGER,send_status INTEGER)";
	private static final String create_table_history="create table "+TABLE_HISTORY+" (id INTEGER PRIMARY KEY AUTOINCREMENT,lat DOUBLE,lan DOUBLE,timestamp INTEGER,speed INTEGER)";
	private static final String create_table_user="Create table "+TABLE_USER+"(userid String PRIMARY KEY  not null,Photo blob)";
	private static final String create_table_parentinfo="Create table "+TABLE_PARENTINFO+"(id INTEGER PRIMARY KEY ,parent_name varchar(20),parent_id INTEGER,photo varchar(200))";
	private static final String create_table_devicelist="CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,StudentID Varchar(30),Name Varchar(30),photo Varchar(200),type Varchar(15),IMEI_no Varchar(20))";
	private static final String create_table_smsfunclist="CREATE TABLE IF NOT EXISTS " + TABLE_SMSFUNC_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,CommnadType Varchar(100),ActualCommand Varchar(200),Title Varchar(200),AnsFromDevice Varchar(1500))";
	private static final String create_table_smsnotifylist="CREATE TABLE IF NOT EXISTS " + TABLE_SMSNOTIFICATION_LIST +" ( Id INTEGER PRIMARY KEY AUTOINCREMENT,Notify_Title Varchar(100),Notify_Type Varchar(50),LatDir Varchar(5),LangDir Varchar(5),Lat Varchar(20),Lang Varchar(25),Speed Varchar(15),Time Varchar(15)," +
			"Date Varchar(15),ImeiNo Varchar(20),UpdateTime Varchar(50))";

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


		}catch(Exception e)
		{
			Log.e("Exception in create table",e.getMessage()+" "+e.getCause());
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

		db.execSQL("DROP TABLE IF EXISTS "+TABLE_SMSNOTIFICATION_LIST);

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

		
		
		public void Insert_Device_list(ArrayList<GmapDetais> arr) {

			// TODO Auto-generated method stub

				//truncateTables(TABLE_DEVICE_LIST);
			String result="success";
			database=this.getWritableDatabase();
			try
			{
			
				String sql1="Insert or Replace into "+TABLE_DEVICE_LIST+"(StudentID,Name,photo,type,IMEI_no) values(?,?,?,?,?)";
				SQLiteStatement statement=database.compileStatement(sql1);
				database.beginTransaction();
				String action="complete";
				for(GmapDetais l : arr)
				{ 
					statement.bindString(1,l.getId());
					statement.bindString(2, l.getName());
					statement.bindString(3, l.getPath());
					statement.bindString(4, l.getType());
					statement.bindString(5, l.getImei_no());

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
		
		
		
		public ArrayList<GmapDetais> Show_Device_list() {

			ArrayList<GmapDetais> list=new ArrayList<GmapDetais>();
			Cursor cursor = null;
			try
			{	database=this.getReadableDatabase();
				String query="Select * from "+TABLE_DEVICE_LIST;
				
				 cursor=database.rawQuery(query, null);
				while(cursor.moveToNext())
				{
					GmapDetais dmDetails=new GmapDetais();
		
					dmDetails.setId(cursor.getString(1));
					dmDetails.setName(cursor.getString(2));

					dmDetails.setPath(cursor.getString(3).replaceAll("~", "").trim());
					dmDetails.setType(cursor.getString(4));
					dmDetails.setImei_no(cursor.getString(5));

					list.add(dmDetails);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally{
				cursor.close();
				database.close();
			}
			return list;
		
			
		}

	public void insertSMSFunction(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata) {

		Log.e("insertSMSFunction sms---",smslistdata.toString());

		truncateTables(TABLE_SMSFUNC_LIST);
		String result="success";
		database=this.getWritableDatabase();
		try
		{

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






	public ArrayList<VehicalTrackingSMSCmdDTO> Show_SMSFunction() {

		ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
		Cursor cursor = null;
		try
		{	database=this.getReadableDatabase();
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
			database.close();
		}

		return list;


	}



	public void insertSMSNotification(SmsNotificationDTO smsdata) {

		Log.e("insertSMSNotification ---",smsdata.toString());

		//truncateTables(TABLE_SMSNOTIFICATION_LIST);
		String result="success";
		database=this.getWritableDatabase();
		try
		{

			String sql1="Insert or Replace into "+TABLE_SMSNOTIFICATION_LIST+"(Notify_Title,Notify_Type,LatDir,LangDir,Lat,Lang,Speed,Time,Date,ImeiNo,UpdateTime)" +
					" values(?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement statement=database.compileStatement(sql1);
			database.beginTransaction();
			String action="complete";

				statement.bindString(1,smsdata.getNotify_Title());
				statement.bindString(2,smsdata.getNotify_Type());
				statement.bindString(3,smsdata.getLatDir());
				statement.bindString(4,smsdata.getLangDir());

				statement.bindString(5,smsdata.getLat());
				statement.bindString(6,smsdata.getLang());
				statement.bindString(7,smsdata.getSpeed()+"");
				statement.bindString(8,smsdata.getTime()+"");
				statement.bindString(9,smsdata.getDate()+"");
				statement.bindString(10,smsdata.getImeiNo());
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
}
