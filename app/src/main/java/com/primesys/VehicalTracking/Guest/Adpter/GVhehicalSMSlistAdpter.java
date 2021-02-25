package com.primesys.VehicalTracking.Guest.Adpter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Activity.ShowLocationOfCar;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.Guest.Fragments.GSMSFuction;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GVhehicalSMSlistAdpter extends RecyclerView.Adapter<GVhehicalSMSlistAdpter.ViewHolder>{

    private SharedPreferences.Editor editor;
    public List<VehicalTrackingSMSCmdDTO> smslist=new ArrayList<VehicalTrackingSMSCmdDTO>();
    private int rowLayout;
    private static Context mContext;
    public   int position=0;

    public SharedPreferences sharedPreferences;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    String DeviceSimNo="";
    private String TAG="ADPetr";
    public static VehicalTrackingSMSCmdDTO CurrentSMSObj=null;
    private String speed="";
    private int TAG_CODE_PERMISSION_SMS=400;


    private String key_DeviceSimNo="DeviceSimNo";
    public static final int MY_PERMISSIONS_REQUEST_SMS = 123;
    public static Boolean pinexist=false;
    public static String Enginepin="";
    public Dialog createpindialog;
    public static SweetAlertDialog pDialogmain;
    public static  CountDownTimer countdowntimer;


    public GVhehicalSMSlistAdpter(ArrayList<VehicalTrackingSMSCmdDTO> smslistdata, int row_smslist, Context context) {
        super();
        this.smslist = smslistdata;
        this.rowLayout = row_smslist;
        this.mContext = context;
        sharedPreferences=mContext.getSharedPreferences("User_data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(rowLayout, viewGroup, false);

        System.out.println("  1  "+new ViewHolder(v)+"   2  "+viewGroup+"   3  "+rowLayout);


        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VehicalTrackingSMSCmdDTO item = smslist.get(position);
        //  holder.itemDesc.setText(item.get());
        holder.smstitle.setText(item.getTitle());
        Typeface typeFace=Typeface.createFromAsset(mContext.getAssets(),"Times New Roman.ttf");
        holder.smstitle.setTypeface(typeFace);

        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return smslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView smstitle;
        
        public ViewHolder(View itemView)  {
            super(itemView);
            smstitle = (TextView)itemView.findViewById(R.id.titlesms);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CurrentSMSObj = smslist.get(getAdapterPosition());
                    if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Over Speed"))
                    Show_speedalert_dilog(CurrentSMSObj);
                    else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("stop electric"))
                    {
                            ShowPassword_dialog("Are you sure to stop vehicle?");

                    }

                  else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
                    {
                            ShowPassword_dialog("Are you sure to start vehicle?");

                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Change Pin"))
                    {

                        Common.ShowSweetAlert(mContext,mContext.getResources().getString(R.string.str_guest_error_msg));
                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Forget Pin"))
                    {
                        Common.ShowSweetAlert(mContext,mContext.getResources().getString(R.string.str_guest_error_msg));

                    } else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Forget Pin"))
                    {
                        Common.ShowSweetAlert(mContext,mContext.getResources().getString(R.string.str_guest_error_msg));

                    }
                    else
                    ShowConfirmDialog(CurrentSMSObj);
                }

            });


        }


    private void ShowConfirmDialog(final VehicalTrackingSMSCmdDTO currentSMSObj) {
         new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirmation?")
                .setContentText("Do you really want to do "+currentSMSObj.getTitle() +" ?.")
                .setConfirmText("Confirm!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) ==
                                    PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) ==
                                            PackageManager.PERMISSION_GRANTED) {


                            }
                            else {
                                ActivityCompat.requestPermissions((Activity) GSMSFuction.context, new String[] {
                                                Manifest.permission.SEND_SMS,
                                                Manifest.permission.READ_SMS },
                                        TAG_CODE_PERMISSION_SMS);
                            }

                            /* if (!checkPermission())
                                Common.ShowSweetAlert(mContext,"You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                            else Sendsms(currentSMSObj.getActualCommand());*/

                        }else  Sendsms(currentSMSObj.getActualCommand());


                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    private void Show_speedalert_dilog(final VehicalTrackingSMSCmdDTO currentSMSObj) {

        final EditText speedlimit;
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.speed_limt);
        dialog.setTitle("Set Speed Limit");

        speedlimit=(EditText)dialog.findViewById(R.id.speedlimit);
        VehicalTrackingSMSCmdDTO Currentobj=new VehicalTrackingSMSCmdDTO();

        Button Submitdetil = (Button) dialog.findViewById(R.id.alertsubmit);
        Button cancel = (Button) dialog.findViewById(R.id.alertcancel);

        Submitdetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 speed =String.format("%03d", Integer.parseInt(speedlimit.getText().toString() ));
                DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!checkPermission())
                        Common.ShowSweetAlert(mContext,"You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                    else
                        Sendsms(CurrentSMSObj.getActualCommand()+speed+"#");

                }else{

                    Sendsms(CurrentSMSObj.getActualCommand()+speed+"#");

                }
                    dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void Sendsms( String msg) {

        setProgressDialog();

        try {
            DeviceSimNo=sharedPreferences.getString(key_DeviceSimNo,"");
            countdowntimer= new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    GVhehicalSMSlistAdpter.pDialogmain.dismiss();
                    setSucessDialog(mContext);
                    if (CurrentSMSObj.getTitle().contains("Current Location"))
                        sendNotifyCarLocation();

                }

            }.start();
         //   Common.ShowSweetSucess(mContext,"Send Sms Sucessfully");
        }catch (Exception ex) {
            ex.printStackTrace();
        }



    }


        protected void setErrormsg(int errormsg) {
            // TODO Auto-generated method stub
            GVhehicalSMSlistAdpter.pDialogmain.dismiss();
            Common.ShowSweetAlert(mContext,CurrentSMSObj.getTitle()+"  is failed,"+mContext.getResources().getString(errormsg));
        }


        public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }




    protected void ShowPassword_dialog(final String msg) {
        // custom dialog

        final EditText txt_pin;
        final Dialog dialog = new Dialog( mContext);
        dialog.setContentView(R.layout.dialog_submitpassword);
        dialog.setTitle("Verify Engine Pin");
        txt_pin = (EditText) dialog.findViewById(R.id.txt_pin);
        txt_pin.setHint("For Guest User enter 1234 .");

        Button Submitpin = (Button) dialog.findViewById(R.id.pinsubmit);


        // if button is clicked, close the custom dialog
        Submitpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_pin.getText().length()!=0){

                    if (txt_pin.getText().toString().equals("1234")){

                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(msg.toString())
                                .setCancelText("No,cancel !")
                                .setConfirmText("Yes!")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sendsms(CurrentSMSObj.getActualCommand());


                                        sDialog.cancel();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();


                        dialog.dismiss();
                    }
                    else
                        Common.ShowSweetAlert(mContext,mContext.getResources().getString(R.string.passwordnotmatch));

                }else{
                    txt_pin.setError("Please enter valid four digit engine pin .");
                }

            }
        });

        dialog.show();
    }

    }

    private void sendNotifyCarLocation()
    {

            String txt="http://maps.google.com/maps?q=N18.590489,E73.771106  Speed:0.0 km/h   Time:13:25:22  Date:16/10/17  IMEI:355488020878745";
            SmsNotificationDTO smsdata=new SmsNotificationDTO();


            String re1="(http)";	// Word 1
            String re2="(:)";	// Any Single Character 1
            String re3="(\\/)";	// Any Single Character 2
            String re4="((?:\\/[\\w\\.\\-]+)+)";	// Unix Path 1
            String re5="(\\?)";	// Any Single Character 3
            String re6="(q)";	// Any Single Word Character (Not Whitespace) 1
            String re7="(=)";	// Any Single Character 4
            String re8="([a-z])";	// Any Single Word Character (Not Whitespace) 2
            String re9="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 1
            String re10="(,)";	// Any Single Character 5
            String re11="([a-z])";	// Any Single Word Character (Not Whitespace) 3
            String re12="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 2
            String re13="(  )";	// White Space 1
            String re14="(Speed)";	// Word 2
            String re15="(:)";	// Any Single Character 6
            String re16="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 3
            String re17="( )";	// White Space 2
            String re18="(km)";	// Word 3
            String re19="((?:\\/[\\w\\.\\-]+)+)";	// Unix Path 2
            String re20="(   )";	// White Space 3
            String re21="(Time)";	// Word 4
            String re22="(:)";	// Any Single Character 7
            String re23="((?:(?:[0-1][0-9])|(?:[2][0-3])|(?:[0-9])):(?:[0-5][0-9])(?::[0-5][0-9])?(?:\\s?(?:am|AM|pm|PM))?)";	// HourMinuteSec 1
            String re24="(  )";	// White Space 4
            String re25="(Date)";	// Word 5
            String re26="(:)";	// Any Single Character 8
            String re27="((?:(?:[0-2]?\\d{1})|(?:[3][01]{1}))[-:\\/.](?:[0]?[1-9]|[1][012])[-:\\/.](?:(?:\\d{1}\\d{1})))(?![\\d])";	// DDMMYY 1
            String re28="(  )";	// White Space 5
            String re29="(IMEI)";	// Word 6
            String re30="(:)";	// Any Single Character 9
            String re31="(\\d+)";	// Integer Number 1

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12+re13+re14+re15+re16+re17+re18+re19+re20+re21+re22+re23+re24+re25+re26+re27+re28+re29+re30+re31,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(txt);
            if (m.find())
            {
                String word1=m.group(1);
                String c1=m.group(2);
                String c2=m.group(3);
                String unixpath1=m.group(4);
                String c3=m.group(5);
                String w1=m.group(6);
                String c4=m.group(7);
                String w2=m.group(8);
                String float1=m.group(9);
                String c5=m.group(10);
                String w3=m.group(11);
                String float2=m.group(12);
                String ws1=m.group(13);
                String word2=m.group(14);
                String c6=m.group(15);
                String float3=m.group(16);
                String ws2=m.group(17);
                String word3=m.group(18);
                String unixpath2=m.group(19);
                String ws3=m.group(20);
                String word4=m.group(21);
                String c7=m.group(22);
                String time1=m.group(23);
                String ws4=m.group(24);
                String word5=m.group(25);
                String c8=m.group(26);
                String ddmmyy1=m.group(27);
                String ws5=m.group(28);
                String word6=m.group(29);
                String c9=m.group(30);
                String d1=m.group(31);
                System.out.print("("+word1.toString()+")"+"("+c1.toString()+")"+"("+c2.toString()+")"+"("+unixpath1.toString()+")"+"("+c3.toString()+")"+"("+w1.toString()+")"+"("+c4.toString()+")"+"("+w2.toString()+")"+"("+float1.toString()+")"+"("+c5.toString()+")"+"("+w3.toString()+")"+"("+float2.toString()+")"+"("+ws1.toString()+")"+"("+word2.toString()+")"+"("+c6.toString()+")"+"("+float3.toString()+")"+"("+ws2.toString()+")"+"("+word3.toString()+")"+"("+unixpath2.toString()+")"+"("+ws3.toString()+")"+"("+word4.toString()+")"+"("+c7.toString()+")"+"("+time1.toString()+")"+"("+ws4.toString()+")"+"("+word5.toString()+")"+"("+c8.toString()+")"+"("+ddmmyy1.toString()+")"+"("+ws5.toString()+")"+"("+word6.toString()+")"+"("+c9.toString()+")"+"("+d1.toString()+")"+"\n");
            }


            smsdata.setNotify_Title("Current Location");
            smsdata.setNotify_Type("Location");
            smsdata.setLatDir(m.group(8));
            smsdata.setLangDir(m.group(11));




            if (m.group(8).equalsIgnoreCase("N")&&m.group(11).equalsIgnoreCase("E")) {
                smsdata.setLat(m.group(9));
                smsdata.setLang(m.group(12));
            }else   if (m.group(8).equalsIgnoreCase("N")&&m.group(11).equalsIgnoreCase("W")) {

                smsdata.setLat(m.group(9));
                smsdata.setLang("-"+m.group(12));
            }
            else if (m.group(8).equalsIgnoreCase("S")&&m.group(11).equalsIgnoreCase("E")) {
                smsdata.setLat("-"+m.group(9));
                smsdata.setLang(m.group(12));

            }else  if (m.group(8).equalsIgnoreCase("S")&&m.group(11).equalsIgnoreCase("W")) {
                smsdata.setLat("-"+m.group(9));
                smsdata.setLang("-"+m.group(12));
            }



            smsdata.setSpeed(m.group(16));
            smsdata.setTime(m.group(23));
            smsdata.setDate(m.group(27));
            smsdata.setImeiNo(m.group(31));

            SendLocationNotification(mContext,smsdata);


    }

    private void setProgressDialog() {

        pDialogmain = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        pDialogmain.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogmain.setTitleText("Wait For Responce");

        if (CurrentSMSObj.getCommnadType().equals("stop electric"))
            pDialogmain.setContentText((mContext.getResources().getString(R.string.stop_engine_ensure)));
        else if (CurrentSMSObj.getCommnadType().equalsIgnoreCase("Supply electric"))
            pDialogmain.setContentText((mContext.getResources().getString(R.string.start_engine_ensure)));
        else
        pDialogmain.setContentText((mContext.getResources().getString(R.string.common_error_sms)));
        pDialogmain.setCancelable(false);
        pDialogmain.show();
    }

    public static void setSucessDialog(Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Success");
        pDialog.setContentText(CurrentSMSObj.getTitle()+" is execute successfully.");
        pDialog.setCancelable(true);
        pDialog.show();
    }




    private void SendLocationNotification(Context context, SmsNotificationDTO smsdata) {
        Log.d(TAG, "Preparing to send notification...: " + smsdata);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(),ShowLocationOfCar.class);
        intent.putExtra("SMSData", smsdata);
        System.err.println("=====SendLocationNotification"+smsdata.getLat()+","+smsdata.getLang());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        int notificationId = 1000;

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                context.getApplicationContext()).setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(smsdata.getNotify_Title())
                .setTicker("Primesys Track")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(smsdata.getNotify_Title()));


        Common.playDefaultNotificationSound(context);
        //	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS) ;
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE) ;
        mBuilder.setContentText("Check your vehicle Location");

        mBuilder.setOngoing(false);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }

}
