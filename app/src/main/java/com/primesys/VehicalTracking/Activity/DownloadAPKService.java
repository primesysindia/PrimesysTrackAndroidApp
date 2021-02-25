package com.primesys.VehicalTracking.Activity;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAPKService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    int Download_Update=0;
    public static String Download_Path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

    public DownloadAPKService() {
        super("DownloadAPKService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
    	
    	
  /* 	AlertDialog alertDialog = new AlertDialog.Builder(this)
        .setTitle("Title")
        .setMessage("Are you sure?")
        .create();
		
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
    	*/
    	
    //    ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        System.out.println("---------DownloadAPKService");

        Home.mBuilder.setProgress(100, Download_Update, false);
       int id=100;
       Home.mNotifyManager.notify(id, Home.mBuilder.build());

        try {
            URL url = new URL(Common.UpdateAppURL);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(Download_Path+"/PrimesysTrack.apk");

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
             //   resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                Download_Update=(int) (total * 100 / fileLength);
                Home.mBuilder.setProgress(100,Download_Update , false);
           	Home.mNotifyManager.notify(id, Home.mBuilder.build());
              //  receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            Home.mBuilder.setContentTitle("Update PrimesysTrack")
			.setContentText("Download  Completed")
			.setSmallIcon(R.mipmap.ic_icon)
            .setProgress(100, 100, false);
            Home.mNotifyManager.notify(id, Home.mBuilder.build());
    		Home.mNotifyManager.cancel(id);
    		InstallApkFromstorage();
    		//InstallAPK();
        } catch (IOException e) {
            e.printStackTrace();
            
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
      
     //   receiver.send(UPDATE_PROGRESS, resultData);
    }
    
    
	private void InstallApkFromstorage() {
		// TODO Auto-generated method stub
	
		 String vsName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/download/";
		 File file = new File(Download_Path, "/PrimesysTrack.apk");
		 System.out.println(":"+file);
		 Intent install=new Intent(Intent.ACTION_VIEW);
		 install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		 install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 startActivity(install);
		 
	}
	public  void InstallAPK(){
		System.err.println("-------------InstallAPK----------");
		 File file = new File(Download_Path, "MyKiddyTracker_Live.apk");
	    if(file.exists()){
	        try {   

	            String command;
	            command = "adb install -r " + file;
	    		System.err.println("-------------InstallAPK---"+Environment.getExternalStorageDirectory() +"-------"+file.toString());

	         //   Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "sh -c cd "+ command });
	         //   Process proc = Runtime.getRuntime().exec("sh -c cd "+ command);
	    		//Process proc =Runtime.getRuntime().exec("adb install "+ file.toString()+"\n"); 
	    		
	    		            command = "adb install -r " + Download_Path+"MyKiddyTracker_Live.apk";
	    		            
	    		            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
	    		          //  Process p = Runtime.getRuntime().exec("cd " + Environment.getExternalStorageDirectory() + "/.yasmin");

	    		    		System.err.println("-------------InstallAPK---command-------"+proc.toString());

	    		            proc.waitFor();
	    		       
	        } catch (Exception e) {
	        e.printStackTrace();
	        }
	     }else {
			Common.ShowSweetAlert(this.getApplicationContext(), "file does not exist");
		}
	  }
	/**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public boolean isRooted() {
        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception exception) {
            // ignore
            exception.printStackTrace();
        }
        String[] commands = {
        		"/bin/chmod 777 /data/data/com.primesys.mitra/myBin",

                "adb install -r " + Download_Path+"MyKiddyTracker_Live.apk"
        };
        for (String command : commands) {
            try {
                Runtime.getRuntime().exec(command);
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Log.d("message-startUp: ", "RootUtil");
        return false;
    }
	/*private void InstallAPK() {
		 File file = new File(Download_Path, "/MyKiddyTracker_Live.apk");
	    if(file.exists()){
	        try {   
	    		System.err.println("-------------InstallAPK----------"+file.toString());

	            final String command = "pm install -r " + file;
	         //   Process p = Runtime.getRuntime().exec("su");
	            Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
	            proc.waitFor();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	     }
	}
    */
    
   /* public class DownloadReceiver extends ResultReceiver{
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                }
            }
        }
    }
	*/
}