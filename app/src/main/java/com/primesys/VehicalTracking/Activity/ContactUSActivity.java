package com.primesys.VehicalTracking.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;

public class ContactUSActivity extends AppCompatActivity {

    private int TAG_CODE_PERMISSION_SMS=400;
    ImageView img_contact_call, img_contact_mail, img_contact_message;
    private Context mContext = ContactUSActivity.this;
    private int TAG_CODE_PERMISSION_call=150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        findViewById();
        img_contact_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + getResources().getString(R.string.company_phone_num)));
                    startActivity(intent);

                    return;
                }else {


                    ActivityCompat.requestPermissions((Activity) mContext, new String[] {
                                    Manifest.permission.CALL_PHONE},
                            TAG_CODE_PERMISSION_call);
                }
            }
        });


        img_contact_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_SMS) ==
                                    PackageManager.PERMISSION_GRANTED) {


                    }
                    else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[] {
                                        Manifest.permission.SEND_SMS,
                                        Manifest.permission.READ_SMS },
                                TAG_CODE_PERMISSION_SMS);
                    }

                    if (!checkPermission())
                        Common.ShowSweetAlert(mContext,"You denied permission of sending SMS. If you really want to use Sms Function allow that one. ");

                    else Sendsms();

                }else  Sendsms();

            }

        });

        img_contact_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.company_email)});
                startActivity(intent);

            }
        });

    }

    private void Sendsms() {

        try {

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", getResources().getString(R.string.company_phone_num));
            startActivity(smsIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }



    private void findViewById() {
        img_contact_call=findViewById(R.id.img_contact_call);
        img_contact_mail=findViewById(R.id.img_contact_mail);
        img_contact_message=findViewById(R.id.img_contact_message);

    }
}
