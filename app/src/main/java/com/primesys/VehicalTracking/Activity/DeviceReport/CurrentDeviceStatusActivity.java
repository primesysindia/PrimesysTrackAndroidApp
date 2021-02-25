package com.primesys.VehicalTracking.Activity.DeviceReport;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Dto.FeatureAddressDetailsDTO;
import com.primesys.VehicalTracking.Dto.RailDeviceInfoDto;
import com.primesys.VehicalTracking.MyAdpter.CurrentDeviceReportAdpter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CurrentDeviceStatusActivity extends AppCompatActivity {
    private  final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =100 ;
    ProgressDialog progressDialog2 ;

    private StringRequest stringRequest;
    private SweetAlertDialog pDialog1;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private Context mContext=CurrentDeviceStatusActivity.this;
    String gmtTimeStampFromDate,pastTime;
    ArrayList<RailDeviceInfoDto> deviceInfoList=new ArrayList<>();
    private RecyclerView reportRecycleview;
    private LinearLayoutManager mLayoutManager;
    private CurrentDeviceReportAdpter mAdapter;
    private Toolbar toolbar;
    private View mLayout;
    TextView tv_off_devices,tv_on_devices;
    int device_on_count=0;
    private ProgressBar download_progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_device_status);
        findViewById();
        gmtTimeStampFromDate=getIntent().getStringExtra("gmtTimeStampFromDate");
        pastTime=getIntent().getStringExtra("pastTime");
        getDeviceCurrentReport();

    }

    private void findViewById() {
        reportRecycleview=(RecyclerView)findViewById(R.id.report_recyclerView);
        mLayoutManager = new LinearLayoutManager(mContext);
        reportRecycleview.setLayoutManager(mLayoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLayout=findViewById(R.id.lay_lin);
        tv_on_devices=findViewById(R.id.tv_on_devices);
        tv_off_devices=findViewById(R.id.tv_off_devices);
        download_progressBar=findViewById(R.id.download_progressBar);

    }


    private void getDeviceCurrentReport() {

        pDialog1 = Common.ShowSweetProgress(mContext, " Getting  report wait....");
        pDialog1.setCancelable(true);
        device_on_count=0;

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/GenerateGPSHolder10MinData",new Response.Listener<String>() {
       // stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.102:8080/TrackingAppDB/TrackingAPP/"+"UserServiceAPI/GenerateGPSHolder10MinData",new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(PrimesysTrack.TAG,"onResponse---getDeviceCurrentReport------"+response);
                ParseReportReponce(response);
                pDialog1.hide();
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog1.hide();
                        Log.e(PrimesysTrack.TAG,"onResponse---error------"+error);

                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            System.out.println(error.toString());
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StartDateTime",gmtTimeStampFromDate+"");
                params.put("pastMin", pastTime+"");
                params.put("ParentId", Common.userid+"");

                Log.e(PrimesysTrack.TAG,"REq---getDeviceCurrentReport------"+params);
                return params;
            }

        };


        stringRequest.setTag("VTS");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        PrimesysTrack.mRequestQueue.add(stringRequest);

    }

    private void ParseReportReponce(String response) {
        try {

            JSONArray jsonArray=new JSONArray(response);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jo = (JSONObject) jsonArray.get(i);
                RailDeviceInfoDto dto=new RailDeviceInfoDto();
                dto.setName(jo.getString("Name").toString());
                dto.setStudentId(jo.getInt("StudentId"));
                dto.setDeviceID(jo.getString("DeviceID").toString());
                dto.setLang(jo.getString("lang").toString());
                dto.setLat(jo.getString("lat").toString());

                dto.setDeviceOnStatus(jo.getInt("deviceOnStatus"));
                if (dto.getDeviceOnStatus()==1)
                    device_on_count++;

               /* if (dto.getDeviceOnStatus()==0)
                dto.setAddress(Common.getStringAddress(mContext,Double.parseDouble(dto.getLat()), Double.parseDouble(dto.getLang())));*/
                dto.setSpeed(jo.getString("speed").toString());

                dto.setTime(jo.getString("time").toString());

                if (jo.has("railFeatureDto")){
                    JSONObject rs=jo.getJSONObject("railFeatureDto");
                    FeatureAddressDetailsDTO addressDto=new FeatureAddressDetailsDTO();
                    addressDto.setDistance(rs.getDouble("distance"));
                    addressDto.setFeatureCode(rs.getInt("featureCode"));
                   // addressDto.setFeature_image(rs.getString("feature_image"));
                    addressDto.setFeatureDetail(rs.getString("featureDetail"));
                    addressDto.setKiloMeter(rs.getDouble("kiloMeter"));
                    addressDto.setLatitude(rs.getDouble("latitude"));
                    addressDto.setLongitude(rs.getDouble("longitude"));
                    addressDto.setSection(rs.getString("section"));
                    addressDto.setNearByDistance(rs.getDouble("NearByDistance"));
                    dto.setRailFeatureDto(addressDto);
                }
                deviceInfoList.add(dto);
            }

          //  download_progressBar.setVisibility(View.VISIBLE);

            Collections.sort(deviceInfoList,RailDeviceInfoDto.StatusComparator);
            if (deviceInfoList.size()>0){
                mAdapter = new CurrentDeviceReportAdpter(mContext, R.layout.card_device_current_report, deviceInfoList);
                reportRecycleview.setAdapter(mAdapter);
                tv_off_devices.setVisibility(View.VISIBLE);
                tv_on_devices.setVisibility(View.VISIBLE);

                tv_off_devices.setText("Device Off No:"+(deviceInfoList.size()-device_on_count));
                tv_on_devices.setText("Device On No:"+device_on_count);

            }else {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("Alert");
                pDialog.setContentText("Report not found for the selected date "+Common.getDateCurrentTimeZone(Long.parseLong(gmtTimeStampFromDate)));
                pDialog.setCancelable(true);
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        pDialog.hide();
                        finish();
                    }
                }).show();

        }
        }catch (Exception e){
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
                  //  download_progressBar.setVisibility(View.VISIBLE);

                    savesxlfile();
                }



                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private synchronized void savesxlfile(){
        //printlnToUser("writing xlsx file");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("current_device_status_"));
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Name");
        cell = row.createCell(1);
        cell.setCellValue("Device Status");
        cell = row.createCell(2);
        cell.setCellValue("Last location time");
        cell = row.createCell(3);
        cell.setCellValue("Device Address");
        cell = row.createCell(4);
        cell.setCellValue("Device Latitude");
        cell = row.createCell(5);
        cell.setCellValue("Device Longitude");

        for (int i=0;i<deviceInfoList.size();i++) {
            row = sheet.createRow(i+1);

            try {
                cell = row.createCell(0);
                cell.setCellValue(deviceInfoList.get(i).getName());
                cell = row.createCell(4);
                cell.setCellValue(deviceInfoList.get(i).getLat());
                cell = row.createCell(5);
                cell.setCellValue(deviceInfoList.get(i).getLang());
                if (deviceInfoList.get(i).getDeviceOnStatus()==0){
                    cell = row.createCell(1);
                    cell.setCellValue("Off");
                    cell = row.createCell(2);
                    cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(deviceInfoList.get(i).getTime())));
                    cell = row.createCell(3);
                    cell.setCellValue(Common.getStringAddress(mContext,Double.parseDouble(deviceInfoList.get(i).getLat()), Double.parseDouble(deviceInfoList.get(i).getLang())));

                }
                else {
                    cell = row.createCell(1);
                    cell.setCellValue("On");
                    cell = row.createCell(2);
                    cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(deviceInfoList.get(i).getTime())));

                    if (deviceInfoList.get(i).getRailFeatureDto()!=null){
                        StringBuilder sb = new StringBuilder("");
                        sb.append("Section :"+deviceInfoList.get(i).getRailFeatureDto().getSection());
                        sb.append("\n"+(String.format("Location: %.2f",deviceInfoList.get(i).getRailFeatureDto().getKiloMeter() + ((deviceInfoList.get(i).getRailFeatureDto().getDistance()))* 0.001)) + " Km ");
                        sb.append("\n "+String.format("And far  %.2f", deviceInfoList.get(i).getRailFeatureDto().getNearByDistance())+ " meter from ");
                        sb.append("\n"+deviceInfoList.get(i).getRailFeatureDto().getFeatureDetail());
                        cell = row.createCell(3);
                        cell.setCellValue(sb.toString());
                    }else {
                        cell = row.createCell(3);
                        cell.setCellValue("Device is not on railway track.");
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e(PrimesysTrack.TAG,"-------------Null pos--"+i);
            }

        }


        String outFileName ="current_device_status_"+Common.getDateCurrentTimeZone(Long.parseLong(gmtTimeStampFromDate)).replace(":","-")+".xlsx";
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
          //download_progressBar.setVisibility(View.GONE);

            Common.ShowSweetSucess(mContext,"Report save to download folder.");
            //printlnToUser("sharing file...");
            // share(outFileName, getApplicationContext());

        } catch (Exception e) {
            //printlnToUser(e.toString());
            e.printStackTrace();
        }finally {

        }


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
