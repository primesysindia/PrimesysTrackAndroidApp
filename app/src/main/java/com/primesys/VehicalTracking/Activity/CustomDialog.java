package com.primesys.VehicalTracking.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.primesys.VehicalTracking.R;


public class CustomDialog {
	public static void displayDialog(String status,Context localcont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(localcont);
		builder.setTitle("Message");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage("\n"+status);
		builder.setCancelable(true);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
