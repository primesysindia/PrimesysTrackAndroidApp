package com.primesys.VehicalTracking.Receiver;



import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.primesys.VehicalTracking.Activity.Forget_password;
import com.primesys.VehicalTracking.Activity.ShowLocationOfCar;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.Dto.VehicalTrackingSMSCmdDTO;
import com.primesys.VehicalTracking.MyAdpter.VhehicalSMSlistAdpter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IncomingSms extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final String TAG = null;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private NotificationManager mNotificationManager;
    public static Context context1;
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
        this.context1 = context.getApplicationContext();
        helper = DBHelper.getInstance(context1);

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
                if(VhehicalSMSlistAdpter.pDialogmain.isShowing()) {
                    VhehicalSMSlistAdpter.pDialogmain.dismiss();
                    VhehicalSMSlistAdpter.countdowntimer.cancel();
                }
                if (senderNum.contains(DeviceSimNo)&&message.contains("Speed  Alarm")) {
                    abortBroadcast();
                    MatchSpeedAlarm(context,message);
                    VhehicalSMSlistAdpter.setSucessDialog(context1);
                    Log.e("abortBroadcast", "abortBroadcast------------: " + senderNum + "; message: " + message);

                }else if (senderNum.contains(DeviceSimNo)&&message.contains("http://maps.google.com/maps?q=N0.000000,E0.000000") ) {
                       abortBroadcast();
                    sendNotification(context,"Current Location","We can't get your location.Please try again.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);

                }
                else if (senderNum.contains(DeviceSimNo)&&message.contains("http://maps.google.com/maps")) {
                    abortBroadcast();

                    MatchLocation(context,message);
                    VhehicalSMSlistAdpter.setSucessDialog(context1);

                    Log.d(TAG, "In side loaction...: " + message);

                }
                else if (senderNum.contains(DeviceSimNo)&&message.contains("speedok!")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Speed Set Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);

                }
                else if (senderNum.contains(DeviceSimNo)&&message.contains("Stop Electicity ok!")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Engine Stop Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }
                else if (senderNum.contains(DeviceSimNo)&&message.contains("supply electcity ok!")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Engine Start Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }else if (senderNum.contains(DeviceSimNo)&&message.contains("stop oil ok!")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Stop Oil Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);




                }else if (senderNum.contains(DeviceSimNo)&&message.contains("supply oil ok!")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Supply Oil Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC ON OK")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","ACC ON Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC OFF OK")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","ACC OFF Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC! ")) {
                    abortBroadcast();
                    sendNotification(context,"Speed","Speed Set Sucessfully.");
                    VhehicalSMSlistAdpter.setSucessDialog(context1);


                }else if(senderNum.contains(DeviceSimNo)) {
                  //  VhehicalSMSlistAdpter.setSucessDialog(context1);

                }
            }



            /*if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    if (senderNum.contains(DeviceSimNo)&&message.contains("Speed  Alarm")) {
                        abortBroadcast();
                        MatchSpeedAlarm(context,message);
                        Log.e("abortBroadcast", "abortBroadcast------------: " + senderNum + "; message: " + message);

                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("http://maps.google.com/maps")) {
                        abortBroadcast();

                        MatchLocation(context,message);


                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("speedok!")) {
                        abortBroadcast();
                    }
                    else if (senderNum.contains(DeviceSimNo)&&message.contains("Stop Electicity ok!")) {
                        abortBroadcast();
                    }
                    else if (senderNum.contains(DeviceSimNo)&&message.contains("supply electcity ok!")) {
                        abortBroadcast();
                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("stop oil ok!")) {
                        abortBroadcast();
                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("supply oil ok!")) {
                        abortBroadcast();
                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC ON OK")) {
                        abortBroadcast();
                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC OFF OK")) {
                        abortBroadcast();
                    }else if (senderNum.contains(DeviceSimNo)&&message.contains("ACC!")) {
                        abortBroadcast();
                    }else if(senderNum.contains(DeviceSimNo)) {

                    }




                  *//*  if (senderNum.contains(Voice_Servilence.devicedata.getDeviceSimNumber())&& (message.equalsIgnoreCase("OK")||message.equalsIgnoreCase("Monitor ok!!"))) {

                    	Toast.makeText(context.getApplicationContext(), "Your voice Surveillance set sucessfully in monitoring mode please wait for call.", Toast.LENGTH_LONG).show();;


						//Common.showlongToast("Your voice Surveillance set sucessfully in monitoring mode please wait for call.",context.getApplicationContext());
						String Devicesimno=Voice_Servilence.devicedata.getDeviceSimNumber();
						Log.i("SmsReceiver", "SmsReceiver number matcg--------------------");
						if (Voice_Servilence.devicedata.getVSCallback().equals("0")) {
							if (Devicesimno!=null) {
								try{
									if(Voice_Servilence.pDialogmain.isShowing())
				            			Voice_Servilence.pDialogmain.dismiss();
				            			Voice_Servilence.closeActivity();

									Intent callIntent = new Intent(Intent.ACTION_CALL);
									callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

									callIntent.setData(Uri.parse("tel:"+Devicesimno+""));
									context.startActivity(callIntent);
									GetCommandtoTrack();

								}catch(Exception e)
								{
									e.printStackTrace();
								}

								*//**//*if(message.equalsIgnoreCase(Voice_Servilence.devicedata.getActualCommand())||message.equalsIgnoreCase("OK")||message.equalsIgnoreCase("Monitor ok!!")) {
									abortBroadcast();
								}*//**//*

							}else{
								Common.showToast("Phone No. Not Found", context);
							}

						}
					//	sendNotification();
						Voice_Servilence.countdowntimer.cancel();
					}*//*

                }
            }*/

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
            e.printStackTrace();

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

        helper = DBHelper.getInstance(context1);

        helper.insertSMSNotification(smsdata);

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

        helper = DBHelper.getInstance(context1);

       helper.insertSMSNotification(smsdata);
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
        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT|Notification.FLAG_AUTO_CANCEL);

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






	
}