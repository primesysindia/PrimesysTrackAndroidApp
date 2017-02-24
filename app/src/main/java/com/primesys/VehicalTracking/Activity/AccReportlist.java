package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.MyAdpter.VhehicalAcclistAdpter;
import com.primesys.VehicalTracking.MyAdpter.VhehicalReportlistAdpter;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Receiver.IncomingSms;
import com.primesys.VehicalTracking.Utility.Common;
import com.primesys.VehicalTracking.VTSFragments.ACCReport;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccReportlist extends AppCompatActivity {

    RecyclerView acclistl;
    LinearLayoutManager laymanager;
    Context context;
    private DBHelper helper= DBHelper.getInstance(AccReportlist.this);

    ArrayList<SmsNotificationDTO> Accreport=new ArrayList<>();
    private VhehicalAcclistAdpter mAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_reportlist);
        context= AccReportlist.this;
        findViewById();

        Accreport=helper.Show_SMSACCNofication(ACCReport.DeviceImieNo);

        mAdpter = new VhehicalAcclistAdpter(Accreport, R.layout.row_accpreport, context);
        acclistl.setAdapter(mAdpter);
        if (Accreport.size()==0){
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Error")
            .setContentText("No report found.")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            onBackPressed();
                            sDialog.dismissWithAnimation();
                        }
                    });

            pDialog.setCancelable(true);
            pDialog.show();        }


    }

    private void findViewById() {
    acclistl= (RecyclerView) findViewById(R.id.acclist);
        acclistl.setLayoutManager(new LinearLayoutManager(context));
        acclistl.setItemAnimator(new DefaultItemAnimator());
        acclistl.setHasFixedSize(true);


    }
}
