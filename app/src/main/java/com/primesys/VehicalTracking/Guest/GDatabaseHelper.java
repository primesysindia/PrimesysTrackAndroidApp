package com.primesys.VehicalTracking.Guest;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.Dto.LocationData;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by root on 26/12/16.
 */

public class GDatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "GDatabaseHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "GuestData.db";
    private static final int DATABASE_VERSION = 1;
    private String pathToSaveDBFile;

    private static GDatabaseHelper helper;
    private SQLiteDatabase database;

    public static final String TABLE_DEVICE_LIST = "Gdevice_list";
    private static final String TABLE_SMSFUNC_LIST = "Gsmsfunc_list";
    private static final String TABLE_ACCREPORT_LIST = "Gaccreport_list";
    private static final String TABLE_HISTORY="Ghistory";
    private static final String TABLE_CURRENTLOC="GcurrentLocation";



    private static final String TABLE_PARENT="db_parent";
    private static final String TABLE_USER="db_user";
    private static final String TABLE_PARENTINFO="db_parentinfo";




    public GDatabaseHelper(Context context, String filePath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

        pathToSaveDBFile = new StringBuffer(context.getFilesDir().getAbsolutePath().toString()).append("/").append(DATABASE_NAME).toString();
        Log.e("pathToSDatabaseHelper--","---------"+pathToSaveDBFile);
    }


    public static GDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper  = new GDatabaseHelper(context,DATABASE_NAME);
        }
        return helper;
    }


    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist) {
            Log.e(TAG, "Database exists.");
            int currentDBVersion = getVersionId();
            Log.e(TAG, "Database exists.currentDBVersion====="+currentDBVersion+"  DATABASE_VERSION==="+DATABASE_VERSION);

            if (DATABASE_VERSION > currentDBVersion) {
                Log.e(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                        e.printStackTrace();
                            }
            }
        } else {
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());            e.printStackTrace();



            }
        }
    }
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();


        } catch(SQLiteException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        Log.e(TAG, "-----------checkDataBase  "+checkDB);

        return checkDB;
    }
    private void copyDataBase() throws IOException {

        Log.e("pathToSaveDBFilatae==",""+pathToSaveDBFile);

        OutputStream os = new FileOutputStream(pathToSaveDBFile);
        InputStream is = myContext.getAssets().open("Databases/"+DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        Log.e(TAG, "-----------copyDataBase    ==="+is);

        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
        Log.e(TAG, "-----------copyDataBase");


    }
    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    private int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v =  cursor.getInt(0);
        db.close();
        return v;
    }



    public ArrayList<String> getAllAddress() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "select  distinct  Address from voting";


        Log.e(TAG, query);


        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<String>();
        while(cursor.moveToNext()) {

            list.add(cursor.getString(cursor.getColumnIndex("Address")));
        }
        db.close();
        return list;
    }
//////////////////////////////******************************////////////////////////////////////



    //select all the history detail from the table
    public ArrayList<LocationData> showDetails()
    {
        ArrayList<LocationData> list=new ArrayList<LocationData>();SQLiteDatabase db=null;

        try
        {
            String query="Select * from "+TABLE_HISTORY;
             db= SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor=db.rawQuery(query, null);
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

            db.close();
        }
        return list;
    }








    public ArrayList<DeviceDataDTO> Show_Device_list() {


        ArrayList<DeviceDataDTO> list=new ArrayList<DeviceDataDTO>();
        Cursor cursor = null;
        SQLiteDatabase db=null;

        try
        {
            Log.e("-pathToSav-ShowDevice--","----------"+pathToSaveDBFile);
             db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

            String query="Select * from "+TABLE_DEVICE_LIST;

            cursor=db.rawQuery(query, null);
            while(cursor.moveToNext())
            {
                DeviceDataDTO dmDetails=new DeviceDataDTO();

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
            db.close();
        }
        return list;


    }


    public ArrayList<VehicalTrackingSMSCmdDTO> Show_SMSFunction() {

        ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
        Cursor cursor = null;
        try
        {
            database = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);


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


    // get the Student name object based on the id
    public String get(String imei)
    {
        String name=null;
        database = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
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
    public String getdevice_name(String imei)
    {
        String name=null;
        database = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
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
        database = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
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






    public ArrayList<VehicalTrackingSMSCmdDTO> Show_AccReport() {

        ArrayList<VehicalTrackingSMSCmdDTO> list=new ArrayList<VehicalTrackingSMSCmdDTO>();
        Cursor cursor = null;
        try
        {
            database = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
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
            database.close();
        }

        return list;


    }

    //Get Current loc list
    public ArrayList<LocationData> get_CurrentLocationlist(int studentId) {
        ArrayList<LocationData> list=new ArrayList<LocationData>();SQLiteDatabase db=null;

        try
        {
            String query="Select * from "+TABLE_CURRENTLOC;
            db= SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor=db.rawQuery(query, null);
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

            db.close();
        }
        return list;
    }
}