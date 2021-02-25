package com.primesys.VehicalTracking.Activity.DeviceReport;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Dto.MonitorSOSPressDTO;
import com.primesys.VehicalTracking.MyAdpter.MonitorSOSPressAdapter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MonitorSOSPressActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private RecyclerView reportRecycleview;
    private LinearLayoutManager mLayoutManager;
    private Context mContext=MonitorSOSPressActivity.this;
    private Toolbar toolbar;
    private TextView tv_report_title;
    private View mLayout;
    String gmtTimeStampFromDate;
    private SweetAlertDialog pDialog1;
    private StringRequest stringRequest;
    ArrayList<MonitorSOSPressDTO> deviceInfoList=new ArrayList<>();
    private MonitorSOSPressAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_sospress);
        findViewById();
        gmtTimeStampFromDate = getIntent().getStringExtra("gmtTimeStampFromDate");
        GetSosData();
    }

    private void findViewById() {
        reportRecycleview=(RecyclerView)findViewById(R.id.report_recyclerView);
        mLayoutManager = new LinearLayoutManager(mContext);
        reportRecycleview.setLayoutManager(mLayoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_report_title=findViewById(R.id.tv_report_title);
        mLayout=findViewById(R.id.lay_lin);
    }

    private void GetSosData(){

        pDialog1 = Common.ShowSweetProgress(mContext, " Getting  report wait....");
        pDialog1.setCancelable(true);

        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GetSosData",new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e(PrimesysTrack.TAG,"onResponse---getSOSData------"+response);

                ParseReportResponse(response);
                pDialog1.dismiss();
            }
        },

                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.dismiss();
                        Log.e(PrimesysTrack.TAG,"onResponse---getDeviceOffStatus------"+error);

                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                        }
                    }
                })  {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StartDateTime",gmtTimeStampFromDate+"");
                params.put("ParentId", Common.userid+"");

                Log.e(PrimesysTrack.TAG,"REq---getSOSData------"+params);
                return params;
            }
        };

        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        PrimesysTrack.mRequestQueue.add(stringRequest);
    }

    private void ParseReportResponse(String response) {

        try {

            JSONArray jsonArray = new JSONArray(response);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jo = (JSONObject) jsonArray.get(i);
                MonitorSOSPressDTO dto=new MonitorSOSPressDTO();

                dto.setLat(jo.getString("lat").toString());
                dto.setLang(jo.getString("lang").toString());
                dto.setAddress(Common.getStringAddress(mContext,Double.parseDouble(dto.getLat()), Double.parseDouble(dto.getLang())));
                dto.setGpsDeviceName(jo.getString("gpsDeviceName").toString());
                dto.setTime(jo.getString("time").toString());
                dto.setVoltageLevel(jo.getString("voltage_level").toString());
                dto.setGsmSignalStrength(jo.getString("gsm_signal_strength").toString());
                dto.setSpeed(jo.getString("speed").toString());

                deviceInfoList.add(dto);
            }
            if (deviceInfoList.size()>0){
                mAdapter = new MonitorSOSPressAdapter(mContext, R.layout.card_device_sos_report, deviceInfoList);
                reportRecycleview.setAdapter(mAdapter);
            }else {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Alert");
                pDialog.setContentText("Report not found for the selected date "+Common.getDateCurrentTimeZone(Long.parseLong(gmtTimeStampFromDate)));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.dismiss();
                        finish();
                    }
                });
                pDialog.show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_download, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.download:

                boolean result = checkStroragePermission();
                if (result) {
                    savesxlfile();
                }

                return true;

        }

        return false;
    }

    private void savesxlfile(){
        pDialog1 = Common.ShowSweetProgress(mContext, " Save report wait....");
        pDialog1.setCancelable(true);

        //printlnToUser("writing xlsx file");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("device_sos_report_"));
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Name");
        cell = row.createCell(1);
        cell.setCellValue("Address");
        cell = row.createCell(2);
        cell.setCellValue("Time");
        cell = row.createCell(3);
        cell.setCellValue("GSM Signal Strength");
        cell = row.createCell(4);
        cell.setCellValue("Speed");
        cell = row.createCell(5);
        cell.setCellValue("Battery Level");

        for (int i=0;i<deviceInfoList.size();i++) {
            row = sheet.createRow(i+1);

            try {
                cell = row.createCell(0);
                cell.setCellValue(deviceInfoList.get(i).getGpsDeviceName());
                cell = row.createCell(1);
                cell.setCellValue(deviceInfoList.get(i).getAddress());
                cell = row.createCell(2);
                cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(deviceInfoList.get(i).getTime())));
                cell = row.createCell(3);
                cell.setCellValue(deviceInfoList.get(i).getGsmSignalStrength());
                cell = row.createCell(4);
                cell.setCellValue(deviceInfoList.get(i).getSpeed());
                cell = row.createCell(5);
                cell.setCellValue(deviceInfoList.get(i).getVoltageLevel());


            }catch (Exception e){
                e.printStackTrace();
                Log.e(PrimesysTrack.TAG,"-------------Null pos--"+i);
            }

        }

        String outFileName ="device_sos_report_"+Common.getDateCurrentTimeZone(Long.parseLong(gmtTimeStampFromDate)).replace(":","-")+".xlsx";
        try {
            //printlnToUser("writing file " + outFileName);
            //File cacheDir = getCacheDir();
            File cacheDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File outFile = new File(cacheDir, outFileName);
            Log.e("--------------",cacheDir+"------------"+outFile);
            OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            Common.ShowSweetSucess(mContext,"Report save to download folder.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        pDialog1.cancel();
    }


    public boolean checkStroragePermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_CALENDAR)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write External storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savesxlfile();
                } else {
                    //code for deny
                    Snackbar.make(mLayout, "Write External storage permission request was denied.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
}