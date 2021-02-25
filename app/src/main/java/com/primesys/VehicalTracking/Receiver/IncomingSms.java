package com.primesys.VehicalTracking.Receiver;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.primesys.VehicalTracking.Activity.ShowLocationOfCar;
import com.primesys.VehicalTracking.ActivityMykiddyLike.SMSlistAdpter;
import com.primesys.VehicalTracking.ActivityMykiddyLike.Voice_Servilence;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Guest.Adpter.GVhehicalSMSlistAdpter;
import com.primesys.VehicalTracking.MyAdpter.VhehicalSMSlistAdpter;
import com.primesys.VehicalTracking.PrimesysTrack;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.SMSFuction;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IncomingSms extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final String TAG = null;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private NotificationManager mNotificationManager;
    public static Context mContext;
    private RequestQueue reuestQueue;
    private StringRequest stringRequest;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String DeviceSimNo;
    private String key_DeviceSimNo = "DeviceSimNo";
    DBHelper helper;
    Random random = new Random();
    private int notificationId=0;
    public void onReceive(Context context, Intent intent) {

        // Retrandroid.content.SharedPreferencesieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        this.mContext = context.getApplicationContext();

        SmsMessage[] smgs = null;
        String senderNum = "";
        String message = "";

        try {
            sharedPreferences = context.getSharedPreferences("User_data", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            DeviceSimNo = sharedPreferences.getString(key_DeviceSimNo, "");

            if (bundle!=null){

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    // you are default


                    if (bundle != null) {
                        // Retrieve the sms message received
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        smgs = new SmsMessage[pdus.length];

                        for (int i = 0; i < smgs.length; i++) {
                            smgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            senderNum = smgs[i].getOriginatingAddress();
                            message += smgs[i].getMessageBody().toString();
                        }
                    }

                } else {
                    // for below KitKat do like normal


                    if (bundle != null) {
                        // Retrieve the sms message received
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        smgs = new SmsMessage[pdus.length];

                        for (int i = 0; i < smgs.length; i++) {
                            smgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            senderNum = smgs[i].getOriginatingAddress();
                            message += smgs[i].getMessageBody().toString();
                        }
                    }

                }

                Log.e("SmsReceiver------------", "senderNum:" + senderNum + "; message:" + message);
                if (SMSFuction.CuurentDeviceDelect!=null&& SMSFuction.CuurentDeviceDelect.getType().equalsIgnoreCase("Car"))
                {

                    try {
                        if (VhehicalSMSlistAdpter.pDialogmain != null && VhehicalSMSlistAdpter.pDialogmain.isShowing()) {
                            VhehicalSMSlistAdpter.pDialogmain.dismiss();
                            VhehicalSMSlistAdpter.countdowntimer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (senderNum.contains(DeviceSimNo) && message.contains("Speed  Alarm")) {

                        MatchSpeedAlarm(context, message);
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);
                        Log.e("abortBroadcast", "abortBroadcast------------: " + senderNum + "; message: " + message);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("http://maps.google.com/maps?q=N0.000000,E0.000000")) {

                        sendNotification(context, "Current Location", "We can't get your location.Please try again.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("http://maps.google.com/maps")) {


                        MatchLocation(context, message);
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);

                        Log.d(TAG, "In side loaction...: " + message);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("speedok!")) {

                        sendNotification(context, "Speed alert", "Speed Set Successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("Stop electricity ok!")) {

                        sendNotification(context, "Engine Stop ", "Engine Stop successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("supply electricity ok!")) {

                        sendNotification(context, "Engine Start", "Engine Start successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("stop oil ok!")) {

                        sendNotification(context, "Stop Oil", "Stop Oil successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("supply oil ok!")) {

                        sendNotification(context, "Supply Oil", "Supply Oil successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("ACC ON OK")) {

                        sendNotification(context, "ACC  ", "ACC ON successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("ACC OFF OK")) {

                        sendNotification(context, "ACC ", "ACC OFF successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("ACC") && message.contains("!!!")) {


                        ParseACCONMsg(context, message);
                        sendNotification(context, "Speed", "Speed Set successfully.");
                        GVhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("cut power alert")) {
                        //

                        ParseACCOFFMsg(context, message);
                        sendNotification(context, "Speed", "Speed Set successfully.");
                        /*   GVhehicalSMSlistAdpter.setSucessDialog(mContext);*/


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("cancel speed") && message.contains("!")) {

                        sendNotification(context, "Speed alert", "Over speed alert cancel successfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("no stockade ok") && message.contains("!")) {

                        sendNotification(context, "Geo-fence ", "Geo-fence disable sucessfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("stockade ok") && message.contains("!")) {

                        sendNotification(context, "Geo-fence ", "Geo-fence set sucessfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("no stockade ok") && message.contains("!")) {

                        sendNotification(context, "Geo-fence ", "Geo-fence disable sucessfully.");
                        VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("stockade") && message.contains("IMEI")) {

                        ParseGeofenceMsg(context, message);
                        // sendNotification(context,"Geo-fence ","Geo-fence set sucessfully.");
                        // VhehicalSMSlistAdpter.setSucessDialog(mContext);


                    } else if (senderNum.contains(DeviceSimNo) && SMSlistAdpter.pDialogmain != null) {
                        try {
                            if (SMSlistAdpter.pDialogmain.isShowing()) {
                                SMSlistAdpter.pDialogmain.dismiss();
                                SMSlistAdpter.countdowntimer.cancel();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }else if (SMSFuction.CuurentDeviceDelect!=null&& SMSFuction.CuurentDeviceDelect.getType().equalsIgnoreCase("Child"))
                {

                    try {
                        if (SMSlistAdpter.pDialogmain != null && SMSlistAdpter.pDialogmain.isShowing()) {
                            SMSlistAdpter.pDialogmain.dismiss();
                            SMSlistAdpter.countdowntimer.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (senderNum.contains(DeviceSimNo) && message.contains("http://maps.google.com/maps?q=N0.000000,E0.000000")) {

                        sendNotification(context, "Current Location", "We can't get your location.Please try again.");
                        SMSlistAdpter.setSucessDialog(context);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("http://maps.google.com/maps")) {


                        MatchIdCardLocation(context, message);
                        SMSlistAdpter.setSucessDialog(context);

                        Log.d(TAG, "In side loaction...: " + message);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("OK#CALLMODE#")) {

                        SMSlistAdpter.setSucessDialog(context);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("OK#CALLVOL#")) {

                        SMSlistAdpter.setSucessDialog(context);


                    } else if (senderNum.contains(DeviceSimNo) && message.contains("OK FN1:")) {


                        SMSlistAdpter.setSucessDialog(context);

                    } else if (senderNum.contains(DeviceSimNo) && message.contains("OK FN1:")) {

                        SMSlistAdpter.setSucessDialog(context);

                    } else if (senderNum.contains(DeviceSimNo) && (message.equalsIgnoreCase("OK") || senderNum.contains(DeviceSimNo) && message.equalsIgnoreCase("Monitor ok!!"))) {


                        if (Voice_Servilence.pDialogmain != null || Voice_Servilence.pDialogmain.isShowing()) {
                            Voice_Servilence.pDialogmain.dismiss();
                            Voice_Servilence.closeActivity();
                        }

                        if (senderNum.contains(DeviceSimNo))
                            Common.ShowSweetSucess(context.getApplicationContext(), "Your voice Surveillance set sucessfully in monitoring mode please wait for call.");

                        String Devicesimno = Voice_Servilence.devicedata.getDeviceSimNumber();
                        Log.i("SmsReceiver", "SmsReceiver number matcg--------------------");
                        if (Voice_Servilence.devicedata.getVSCallback().equals("0")) {
                            if (Devicesimno != null) {
                                try {
                                    if (Voice_Servilence.pDialogmain != null || Voice_Servilence.pDialogmain.isShowing()) {
                                        Voice_Servilence.pDialogmain.dismiss();
                                        Voice_Servilence.countdowntimer.cancel();

                                        Voice_Servilence.closeActivity();
                                    }

                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    callIntent.setData(Uri.parse("tel:" + Devicesimno + ""));
                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        context.startActivity(callIntent);
                                        return;
                                    }

                                    GetCommandtoTrack();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

								/*if(message.equalsIgnoreCase(Voice_Servilence.devicedata.getActualCommand())||message.equalsIgnoreCase("OK")||message.equalsIgnoreCase("Monitor ok!!")) {

								}*/

                            } else {
                                Common.ShowSweetAlert(context, "Phone No. Not Found");
                            }

                        }
                        //	sendNotification();
                    } else if (senderNum.contains(DeviceSimNo) && message.contains(SMSlistAdpter.CurrentSMSObj.getAnsFromDevice().trim())) {

                        //MatchSpeedAlarm(context,message);
                        SMSlistAdpter.setSucessDialog(context);

                    } else if (senderNum.contains(DeviceSimNo)) {
                        //  GVhehicalSMSlistAdpter.setSucessDialog(mContext);

                    }
                }
            }


        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
            e.printStackTrace();

        }
    }

    public void ParseGeofenceMsg(Context context, String message) {
        SmsNotificationDTO smsdata=new SmsNotificationDTO();

        String txt="stockade IMEI:358511020003418    18.592493,N,73.773507,E";

        String re1="(stockade)";	// Word 1
        String re2="( )";	// White Space 1
        String re3="(IMEI)";	// Word 2
        String re4="(:)";	// Any Single Character 1
        String re5="(\\d+)";	// Integer Number 1
        String re6="(    )";	// White Space 2
        String re7="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 1
        String re8="(,)";	// Any Single Character 2
        String re9="(N)";	// Variable Name 1
        String re10="(,)";	// Any Single Character 3
        String re11="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 2
        String re12="(,)";	// Any Single Character 4
        String re13="(E)";	// Variable Name 2

        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12+re13,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(message);
        if (m.find())
        {
            String word1=m.group(1);
            String ws1=m.group(2);
            String word2=m.group(3);
            String c1=m.group(4);
            String int1=m.group(5);
            String ws2=m.group(6);
            String float1=m.group(7);
            String c2=m.group(8);
            String var1=m.group(9);
            String c3=m.group(10);
            String float2=m.group(11);
            String c4=m.group(12);
            String var2=m.group(13);
            System.out.print("("+word1.toString()+")"+"("+ws1.toString()+")"+"("+word2.toString()+")"+"("+c1.toString()+")"+"("+int1.toString()+")"+"("+ws2.toString()+")"+"("+float1.toString()+")"+"("+c2.toString()+")"+"("+var1.toString()+")"+"("+c3.toString()+")"+"("+float2.toString()+")"+"("+c4.toString()+")"+"("+var2.toString()+")"+"\n");


            smsdata.setNotify_Title("Geo-fence");
            smsdata.setNotify_Type("Geo-fence");
            smsdata.setLatDir(m.group(9));
            smsdata.setLangDir(m.group(13));


            if (m.group(9).equalsIgnoreCase("N")&&m.group(13).equalsIgnoreCase("E")) {
                smsdata.setLat(m.group(7));
                smsdata.setLang(m.group(11));
            }else   if (m.group(9).equalsIgnoreCase("N")&&m.group(13).equalsIgnoreCase("W")) {

                smsdata.setLat(m.group(7));
                smsdata.setLang("-"+m.group(11));
            }
            else if (m.group(9).equalsIgnoreCase("S")&&m.group(13).equalsIgnoreCase("E")) {
                smsdata.setLat("-"+m.group(7));
                smsdata.setLang(m.group(11));

            }else  if (m.group(9).equalsIgnoreCase("S")&&m.group(13).equalsIgnoreCase("W")) {
                smsdata.setLat("-"+m.group(7));
                smsdata.setLang("-"+m.group(11));
            }
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            smsdata.setSpeed(null);
            smsdata.setTime(mydate);
            smsdata.setDate(mydate);
            smsdata.setImeiNo(m.group(5));


            if (Common.ACCSqliteEnable)
                PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);
            sendGeofencingNotification(context,smsdata);


        }
    }



    private void ParseACCOFFMsg(Context context, String message) {


        String txt="cut power alert!IMEI:355488020822947  18.562457,N,73.956082,E";
        SmsNotificationDTO smsdata=new SmsNotificationDTO();

        String re1="(cut)";	// Word 1
        String re2="( )";	// White Space 1
        String re3="(power)";	// Word 2
        String re4="( )";	// White Space 2
        String re5="(alert)";	// Word 3
        String re6="(!)";	// Any Single Character 1
        String re7="(IMEI)";	// Word 4
        String re8="(:)";	// Any Single Character 2
        String re9="(\\d+)";	// Integer Number 1
        String re10="(  )";	// White Space 3
        String re11="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 1
        String re12="(,)";	// Any Single Character 3
        String re13="((?:[a-z][a-z0-9_]*))";	// Variable Name 1
        String re14="(,)";	// Any Single Character 4
        String re15="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 2
        String re16="(,)";	// Any Single Character 5
        String re17="((?:[a-z][a-z0-9_]*))";	// Variable Name 2

        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12+re13+re14+re15+re16+re17,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(message);
        if (m.find())
        {
            String word1=m.group(1);
            String ws1=m.group(2);
            String word2=m.group(3);
            String ws2=m.group(4);
            String word3=m.group(5);
            String c1=m.group(6);
            String word4=m.group(7);
            String c2=m.group(8);
            String int1=m.group(9);
            String ws3=m.group(10);
            String float1=m.group(11);
            String c3=m.group(12);
            String var1=m.group(13);
            String c4=m.group(14);
            String float2=m.group(15);
            String c5=m.group(16);
            String var2=m.group(17);
            System.out.print("("+word1.toString()+")"+"("+ws1.toString()+")"+"("+word2.toString()+")"+"("+ws2.toString()+")"+"("+word3.toString()+")"+"("+c1.toString()+")"+"("+word4.toString()+")"+"("+c2.toString()+")"+"("+int1.toString()+")"+"("+ws3.toString()+")"+"("+float1.toString()+")"+"("+c3.toString()+")"+"("+var1.toString()+")"+"("+c4.toString()+")"+"("+float2.toString()+")"+"("+c5.toString()+")"+"("+var2.toString()+")"+"\n");


            smsdata.setNotify_Title("ACC OFF");
            smsdata.setNotify_Type("ACC");
            smsdata.setLatDir(m.group(13));
            smsdata.setLangDir(m.group(17));


            if (m.group(13).equalsIgnoreCase("N")&&m.group(17).equalsIgnoreCase("E")) {
                smsdata.setLat(m.group(11));
                smsdata.setLang(m.group(15));
            }else   if (m.group(13).equalsIgnoreCase("N")&&m.group(17).equalsIgnoreCase("W")) {

                smsdata.setLat(m.group(11));
                smsdata.setLang("-"+m.group(15));
            }
            else if (m.group(13).equalsIgnoreCase("S")&&m.group(17).equalsIgnoreCase("E")) {
                smsdata.setLat("-"+m.group(11));
                smsdata.setLang(m.group(15));

            }else  if (m.group(13).equalsIgnoreCase("S")&&m.group(17).equalsIgnoreCase("W")) {
                smsdata.setLat("-"+m.group(11));
                smsdata.setLang("-"+m.group(15));
            }
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            smsdata.setSpeed(null);
            smsdata.setTime(mydate);
            smsdata.setDate(mydate);
            smsdata.setImeiNo(m.group(9));

            if (Common.ACCSqliteEnable)
            PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);
            try{
                PostNotificationData_Server(mContext, smsdata);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void ParseACCONMsg(Context context, String message) {
        if (message.contains("N")&&message.contains("E")) {

            System.out.print("Inside *-------------------------------***********************ParseACCONMsgParseACCONMsgParseACCONMsgParseACCONMsg***************----------");;

            String txt = "ACC  !!!IMEI:355488020822828     N20.900905,E77.810837";
            SmsNotificationDTO smsdata = new SmsNotificationDTO();
            String re1 = "(ACC)";    // Word 1
            String re2 = "(  )";    // White Space 1
            String re3 = "(!)";    // Any Single Character 1
            String re4 = "(!)";    // Any Single Character 2
            String re5 = "(!)";    // Any Single Character 3
            String re6 = "(IMEI)";    // Word 2
            String re7 = "(:)";    // Any Single Character 4
            String re8 = "(\\d+)";    // Integer Number 1
            String re9 = "(     )";    // White Space 2
            String re10 = "([a-z])";    // Any Single Word Character (Not Whitespace) 1
            String re11 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";    // Float 1
            String re12 = "(,)";    // Any Single Character 5
            String re13 = "([a-z])";    // Any Single Word Character (Not Whitespace) 2
            String re14 = "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";    // Float 2

            Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6 + re7 + re8 + re9 + re10 + re11 + re12 + re13 + re14, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(message);
            if (m.find()) {
                String word1 = m.group(1);
                String ws1 = m.group(2);
                String c1 = m.group(3);
                String c2 = m.group(4);
                String c3 = m.group(5);
                String word2 = m.group(6);
                String c4 = m.group(7);
                String int1 = m.group(8);
                String ws2 = m.group(9);
                String w1 = m.group(10);
                String float1 = m.group(11);
                String c5 = m.group(12);
                String w2 = m.group(13);
                String float2 = m.group(14);
                System.out.print("(" + word1.toString() + ")" + "(" + ws1.toString() + ")" + "(" + c1.toString() + ")" + "(" + c2.toString() + ")" + "(" + c3.toString() + ")" + "(" + word2.toString() + ")" + "(" + c4.toString() + ")" + "(" + int1.toString() + ")" + "(" + ws2.toString() + ")" + "(" + w1.toString() + ")" + "(" + float1.toString() + ")" + "(" + c5.toString() + ")" + "(" + w2.toString() + ")" + "(" + float2.toString() + ")" + "\n");

                smsdata.setNotify_Title("ACC ON");
                smsdata.setNotify_Type("ACC");
                smsdata.setLatDir(m.group(10));
                smsdata.setLangDir(m.group(13));


                if (m.group(10).equalsIgnoreCase("N") && m.group(13).equalsIgnoreCase("E")) {
                    smsdata.setLat(m.group(11));
                    smsdata.setLang(m.group(14));
                } else if (m.group(10).equalsIgnoreCase("N") && m.group(13).equalsIgnoreCase("W")) {

                    smsdata.setLat(m.group(11));
                    smsdata.setLang("-" + m.group(14));
                } else if (m.group(10).equalsIgnoreCase("S") && m.group(13).equalsIgnoreCase("E")) {
                    smsdata.setLat("-" + m.group(11));
                    smsdata.setLang(m.group(14));

                } else if (m.group(10).equalsIgnoreCase("S") && m.group(13).equalsIgnoreCase("W")) {
                    smsdata.setLat("-" + m.group(11));
                    smsdata.setLang("-" + m.group(14));
                }

                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                smsdata.setSpeed(null);
                smsdata.setTime(mydate);
                smsdata.setDate(mydate);
                smsdata.setImeiNo(m.group(8));

                if (Common.ACCSqliteEnable)
                PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);

                try {
                    PostNotificationData_Server(context,smsdata);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else {
            System.out.print("Inside *------elseeeeeeeeeeee--------------355488020878745-----------***********************ParseACCONMsgParseACCONMsgParseACCONMsgParseACCONMsg***************----------");;

                String txt = "ACC  !!!IMEI:355488020822828";
                SmsNotificationDTO smsdata = new SmsNotificationDTO();
                String re1 = "(ACC)";    // Word 1
                String re2 = "(  )";    // White Space 1
                String re3 = "(!)";    // Any Single Character 1
                String re4 = "(!)";    // Any Single Character 2
                String re5 = "(!)";    // Any Single Character 3
                String re6 = "(IMEI)";    // Word 2
                String re7 = "(:)";    // Any Single Character 4
                String re8 = "(\\d+)";    // Integer Number 1


                Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6 + re7 + re8, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(message);
                if (m.find()) {
                    String word1 = m.group(1);
                    String ws1 = m.group(2);
                    String c1 = m.group(3);
                    String c2 = m.group(4);
                    String c3 = m.group(5);
                    String word2 = m.group(6);
                    String c4 = m.group(7);
                    String int1 = m.group(8);

                    System.out.print("(" + word1.toString() + ")" + "(" + ws1.toString() + ")" + "(" + c1.toString() + ")" + "(" + c2.toString() + ")" + "(" + c3.toString() + ")" + "(" + word2.toString() + ")" + "(" + c4.toString() + ")" + "(" + int1.toString() );

                    smsdata.setNotify_Title("ACC ON");
                    smsdata.setNotify_Type("ACC");

                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    smsdata.setSpeed(null);
                    smsdata.setTime(mydate);
                    smsdata.setDate(mydate);
                    smsdata.setImeiNo(m.group(8));

                    if (Common.ACCSqliteEnable)
                    PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);

                    try {
                        PostNotificationData_Server(mContext, smsdata);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

        }



    }

    public void MatchSpeedAlarm(Context context, String message) {
        String txt="Speed  Alarm!IMEI:355488020878745  18.562317,N,73.834386,E";

        System.out.print("/***********************"+message);
        String re1="( )";	// Variable Name 1

        String re2="(Speed)";	// Variable Name 1
        String re3="(  )";	// White Space 2
        String re4="(Alarm)";	// Word 1
        String re5="(!)";	// Any Single Character 1
        String re6="(IMEI)";	// Word 2
        String re7="(:)";	// Any Single Character 2
        String re8="(\\d+)";	// Integer Number 1
        String re9="(  )";	// White Space 3
        String re10="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 1
        String re11="(,)";	// Any Single Character 3
        String re12="((?:[a-z][a-z0-9_]*))";	// Variable Name 2
        String re13="(,)";	// Any Single Character 4
        String re14="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Float 2
        String re15="(,)";	// Any Single Character 5
        String re16="((?:[a-z][a-z0-9_]*))";	// Variable Name 3
        SmsNotificationDTO smsdata=new SmsNotificationDTO();

        Pattern p = Pattern.compile(re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12+re13+re14+re15+re16,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(message);
        if (m.find())
        {
            //String ws1=m.group(1);
            String var1=m.group(1);
            String ws2=m.group(2);
            String word1=m.group(3);
            String c1=m.group(4);
            String word2=m.group(5);
            String c2=m.group(6);
            String int1=m.group(7);
            String ws3=m.group(8);
            String float1=m.group(9);
            String c3=m.group(10);
            String var2=m.group(11);
            String c4=m.group(12);
            String float2=m.group(13);
            String c5=m.group(14);
            String var3=m.group(15);
            System.out.print(""+"("+var1.toString()+")"+"("+ws2.toString()+")"+"("+word1.toString()+")"+"("+c1.toString()+")"+"("+word2.toString()+")"+"("+c2.toString()+")"+"("+int1.toString()+")"+"("+ws3.toString()+")"+"("+float1.toString()+")"+"("+c3.toString()+")"+"("+var2.toString()+")"+"("+c4.toString()+")"+"("+float2.toString()+")"+"("+c5.toString()+")"+"("+var3.toString()+")"+"\n");
        }
        smsdata.setNotify_Title("Speed  Alarm ");
        smsdata.setNotify_Type("Speed  Alarm");
        smsdata.setLatDir(m.group(11));
        smsdata.setLangDir(m.group(15));


        if (m.group(11).equalsIgnoreCase("N")&&m.group(15).equalsIgnoreCase("E")) {
            smsdata.setLat(m.group(9));
            smsdata.setLang(m.group(13));
        }else   if (m.group(11).equalsIgnoreCase("N")&&m.group(15).equalsIgnoreCase("W")) {

            smsdata.setLat(m.group(9));
            smsdata.setLang("-"+m.group(13));
        }
        else if (m.group(11).equalsIgnoreCase("S")&&m.group(15).equalsIgnoreCase("E")) {
            smsdata.setLat("-"+m.group(9));
            smsdata.setLang(m.group(13));

        }else  if (m.group(11).equalsIgnoreCase("S")&&m.group(15).equalsIgnoreCase("W")) {
            smsdata.setLat("-"+m.group(9));
            smsdata.setLang("-"+m.group(13));
        }

        smsdata.setSpeed(null);
        smsdata.setTime(null);
        smsdata.setDate(null);
        smsdata.setImeiNo(m.group(7));

        if (Common.ACCSqliteEnable)
        PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);

        sendNotification(context,smsdata);

    }


    public void MatchLocation(Context context, String message){

        Log.d(TAG, "In MatchLocation l***************...:" + message);
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
        Matcher m = p.matcher(message);
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

        if (Common.ACCSqliteEnable)
       PrimesysTrack.mDbHelper.insertSMSNotification(smsdata);
        SendLocationNotification(context,smsdata);

    }





    private void SendLocationNotification(Context context, SmsNotificationDTO smsdata) {
        Log.d(TAG, "Preparing to send notification...: " + smsdata);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(),ShowLocationOfCar.class);
        intent.putExtra("SMSData", smsdata);
        System.err.println("=====SendLocationNotification"+smsdata.getLat()+","+smsdata.getLang());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notificationId = random.nextInt(9999 - 1000) + 1000;

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

    private void sendNotification(Context context,SmsNotificationDTO smsdata) {
        if (smsdata.getTime()==null&&smsdata.getTime()==null)
        {
            smsdata.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            smsdata.setTime(new SimpleDateFormat("hh:mm:ss aa").format(new Date()));

            System.out.println("---------------"+smsdata.getDate()+smsdata.getTime());
        }
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(),ShowLocationOfCar.class);
        intent.putExtra("SMSData", smsdata);
        System.err.println("=====SendLocationNotification--" +
                ""+smsdata.getLat()+","+smsdata.getLang());
        @SuppressLint("WrongConstant") PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT|Notification.FLAG_AUTO_CANCEL);

        notificationId = random.nextInt(9999 - 1000) + 1000;

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(smsdata.getNotify_Title())
                .setTicker("Primesys Track")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(smsdata.getNotify_Title()))
                .setContentText(smsdata.getNotify_Title());

                mBuilder.setContentIntent(contentIntent);
                mBuilder.setOngoing(false);
                mBuilder.setAutoCancel(true);
                mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mBuilder.setOnlyAlertOnce(true);
        Common.playDefaultNotificationSound(context);

        mNotificationManager.notify(notificationId, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }


    private void sendNotification(Context context,String title,String msg) {

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
/*

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GeofenceShowSTudent.class), 0);
*/

        notificationId = random.nextInt(9999 - 1000) + 1000;

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(title)
                .setTicker("Primesys Track")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
                mBuilder.setOngoing(false);
                mBuilder.setAutoCancel(true);
                mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mBuilder.setOnlyAlertOnce(true);
        //     mBuilder.setContentIntent(contentIntent);
        Common.playDefaultNotificationSound(context);

        mNotificationManager.notify(notificationId, mBuilder.build());


        Log.d(TAG, "Notification sent successfully.");
    }





    void PostNotificationData_Server(Context context, final SmsNotificationDTO smsdata)
    {
        reuestQueue= Volley.newRequestQueue(context);

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST, Common.TrackURL+"UserServiceAPI/PostNotificationData_Server",new Response.Listener<String>() {
       // stringRequest = new StringRequest(Request.Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/UserServiceAPI/PostNotificationData_Server",new Response.Listener<String>() {
        @Override
            public void onResponse(String response) {
            Log.e("PostNotita_Ser=======",response);
                if(response!=null)
                    parseJSON(response);
               // pDialog.hide();
            }

        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	pDialog.hide();
                        if(error.networkResponse != null && error.networkResponse.data != null){
                            VolleyError er = new VolleyError(new String(error.networkResponse.data));
                            error = er;
                            Log.e("Response ",error.toString());
                            parseJSON(new String(error.networkResponse.data));
                          //  pDialog.hide();

                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try
                {

                    //	params.put("id",id);

                    params.put("NTitle",smsdata.getNotify_Title()+"");
                    params.put("NType",smsdata.getNotify_Type()+"");
                    params.put("LatDir",smsdata.getLatDir()+"");
                    params.put("LanDir",smsdata.getLangDir()+"");
                    params.put("Lat",smsdata.getLat()+"");
                    params.put("lang", smsdata.getLang()+"");
                    params.put("ImieNo", smsdata.getImeiNo()+"");
                    params.put("UserId", Common.userid);

                    System.out.println("PostNotificationData_Server Req --------"+params);
                    //params.put("bdate",birthDate);
                }catch(Exception e)
                {

                }
                Log.e("params ",params+"");
                //	params.put("user", user+"");
                return params;
            }

        };
        stringRequest.setTag(TAG);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                20 * 1000,
                0,
                1
        );
        stringRequest.setRetryPolicy(retryPolicy);
        reuestQueue.add(stringRequest);
    }



    private void sendGeofencingNotification(Context context, SmsNotificationDTO smsdata) {
            if (smsdata.getTime()==null&&smsdata.getTime()==null)
            {
                smsdata.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                smsdata.setTime(new SimpleDateFormat("hh:mm:ss aa").format(new Date()));

                System.out.println("---------------"+smsdata.getDate()+smsdata.getTime());
            }
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(context.getApplicationContext(),ShowLocationOfCar.class);
            intent.putExtra("SMSData", smsdata);
            System.err.println("=====SendLocationNotification--" +
                    ""+smsdata.getLat()+","+smsdata.getLang());
             @SuppressLint("WrongConstant") PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT|Notification.FLAG_AUTO_CANCEL);

            notificationId = random.nextInt(9999 - 1000) + 1000;

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                    context).setSmallIcon(R.mipmap.ic_icon)
                    .setContentTitle(smsdata.getNotify_Title())
                    .setTicker("Primesys Track")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(smsdata.getNotify_Title()))
                    .setContentText(smsdata.getNotify_Title()+", Vehicle  goes outside geo-fence");

            mBuilder.setContentIntent(contentIntent);
            mBuilder.setOngoing(false);
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mBuilder.setOnlyAlertOnce(true);
            Common.playDefaultNotificationSound(context);

            mNotificationManager.notify(notificationId, mBuilder.build());

    }




    private void GetCommandtoTrack() {


        reuestQueue=Volley.newRequestQueue(mContext); //getting Request object from it

        //JSon object request for reading the json data
        stringRequest = new StringRequest(Request.Method.POST,Common.TrackURL+"UserServiceAPI/GetCommandtoTrack",new Response.Listener<String>() {
            //stringRequest = new StringRequest(Method.POST,"http://192.168.1.102:8022/TrackingAppDB/TrackingAPP/SQLQuiz/Postscore",new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("Response of GetCommandtoTrack----"+response);

                parseJSON(response);


            }

        },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error!=null)
                            Log.d("Error", error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("StudentID", ShowMapFragment.StudentId+"");
                params.put("ComandType","switch_to_track");

                System.out.println("REq----get mob------"+params);
                return params;
            }

        };


        stringRequest.setTag(TAG);         stringRequest.setRetryPolicy(Common.vollyRetryPolicy);
        // Adding request to request queue
        reuestQueue.add(stringRequest);
    }

    protected void parseJSON(String result) {
        // TODO Auto-generated method stub

        //nce of Get sim no----{"DeviceSimNumber":"9145734716",
        //"":"0","":"1","":
        //	"monitor","ActualCommand":"MONITOR#","error":"false"}

        try {
            JSONObject jo=new JSONObject(result);
            if (jo.getString("error").equals("false")) {
                String actualcmd=jo.getString("ActualCommand");


                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Voice_Servilence.devicedata.getDeviceSimNumber(), null,actualcmd, null, null);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }




    public void MatchIdCardLocation(Context context, String message){

        Log.d(TAG, "In MatchLocation l***************...:" + message);
        String txt="http://maps.google.com/maps?q=N18.590489,E73.771106  Speed:0.0 km/h   Time:13:25:22  Date:16/10/17  IMEI:355488020878745";
        SmsNotificationDTO smsdata=new SmsNotificationDTO();

        //http://maps.google.com/maps?q=N18.590658,E73.771300
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


        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(extractUrls(message).get(0));
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

            System.out.print("("+word1.toString()+")"+"("+c1.toString()+")"+"("+c2.toString()+")"+"("+unixpath1.toString()+")"+"("+c3.toString()+")"+"("+w1.toString()+")"+"("+c4.toString()+")"+"("+w2.toString()+")"+"("+float1.toString()+")"+"("+c5.toString()+")"+"("+w3.toString()+")"+"("+float2.toString()+")");
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
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smsdata.setDate(sdf.format(c.getTime()));
        helper = DBHelper.getInstance(context);
        if (Common.ACCSqliteEnable)
            helper.insertSMSNotification(smsdata);
        SendLocationNotification(context,smsdata);

    }



    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


}