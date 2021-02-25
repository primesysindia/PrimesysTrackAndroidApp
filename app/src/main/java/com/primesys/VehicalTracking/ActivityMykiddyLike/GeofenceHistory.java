package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.primesys.VehicalTracking.Dto.GeofenceDTO;
import com.primesys.VehicalTracking.R;

import java.util.ArrayList;

public class GeofenceHistory extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    RecyclerView geoFencelist;
    ArrayList<GeofenceDTO>geofenceDataList=new ArrayList<>();
    private GeofencingHistoryAdpter fenceHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_history);
        findViewById();
        geofenceDataList= (ArrayList<GeofenceDTO>) getIntent().getSerializableExtra("geofencelist");

        fenceHistoryAdapter = new GeofencingHistoryAdpter(mContext,
                R.layout.geofencinghistory_list_row, geofenceDataList);
        geoFencelist.setAdapter(fenceHistoryAdapter);

    }


    private void findViewById() {
        // TODO Auto-generated method stub
        mContext = GeofenceHistory.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        geoFencelist= (RecyclerView) findViewById(R.id.fencelist);

        geoFencelist.setLayoutManager(new LinearLayoutManager(mContext));
        geoFencelist.setItemAnimator(new DefaultItemAnimator());
       // geoFencelist.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));

        geoFencelist.setHasFixedSize(true);
    }

}
