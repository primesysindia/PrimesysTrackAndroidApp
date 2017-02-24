package com.primesys.VehicalTracking.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.SmsNotificationDTO;
import com.primesys.VehicalTracking.R;

public class ShowLocationOfCar extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public SmsNotificationDTO smsdata=new SmsNotificationDTO();
    private MarkerOptions markerOptions;
    Context context=ShowLocationOfCar.this;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_location_show);
        helper=DBHelper.getInstance(this.getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        smsdata= (SmsNotificationDTO) getIntent().getSerializableExtra("SMSData");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new MarkerInfiWindow());

        SerCarLocation();

    }

    private void SerCarLocation() {

        mMap.clear();
        LatLng latLng = new LatLng(Double.parseDouble(smsdata.getLat()),Double.parseDouble(smsdata.getLang()));
        markerOptions = new MarkerOptions().position(latLng);

     //   markerOptions.snippet(Geomsg+"\n"+"Latitude : "+String.format("%.6f",latLng.latitude)+"\t"+"Longitude : "+String.format("%.6f",latLng.longitude));
     //   markerOptions.title("Speed : "+smsdata.getSpeed()+" km/h"+"\t"+"Date : "+ smsdata.getTime()+"  "+smsdata.getDate()+"\n");
          markerOptions.title(smsdata.getImeiNo());
        // markerOptions.title(mCircle.getRadius()+" meter");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_car));
        mMap.addMarker(markerOptions).showInfoWindow();
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        System.err.println("=====show====car location"+smsdata.getLat()+","+smsdata.getLang());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(smsdata.getLat()),Double.parseDouble( smsdata.getLang())), 15.5f), 4000, null);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent login = new Intent(context, LoginActivity.class);
        // set the new task and clear flags
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
    }



    class MarkerInfiWindow implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        TextView veh_name,alarm_type,alarm_time;
        MarkerInfiWindow(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_marker, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView veh_name = ((TextView)myContentsView.findViewById(R.id.name));
            TextView alarm_type = ((TextView)myContentsView.findViewById(R.id.alarmtype));
            TextView alarm_time = ((TextView)myContentsView.findViewById(R.id.alarmtime));

            veh_name.setText(helper.getdevice_name(marker.getTitle()));
            alarm_type.setText(smsdata.getNotify_Title());
            if (smsdata.getTime()!=null&&smsdata.getTime()!=null)
            alarm_time.setText(smsdata.getDate()+" " +smsdata.getTime());


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }



}
