package com.primesys.VehicalTracking.MyAdpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.primesys.VehicalTracking.Dto.DeviceDataDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircleTransform;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class ShowMapAdapter  extends ArrayAdapter<DeviceDataDTO> {

	TextView txtchild,txtcar,txtpet;
	Context context;
	ImageLoader imageLoader;
	int layoutResourceId;
	ArrayList<DeviceDataDTO> deviceList=new ArrayList<>();
	ArrayList<DeviceDataDTO>deviceListCopy;

	public static Boolean trackInfo=false;
	LinearLayout laychild,laypet,layCar;
	Bitmap bitmap;
	static RequestQueue RecordSyncQueue;
	public static DeviceDataDTO gp;
	LinearLayout lay_main;
	View veh_status;
	public ShowMapAdapter(Context context, int layoutResourceId,
			ArrayList<DeviceDataDTO> data, ImageLoader imageLoader2) {
		super(context, layoutResourceId,data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.deviceList = data;
		this.deviceListCopy = new ArrayList<>();
		this.deviceListCopy.addAll(deviceList);
		this.imageLoader=imageLoader2;
		trackInfo=false;
	//	GShowMapFragment.StudentId=Integer.parseInt(data.get(0).getId());

	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		if (convertView== null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);

		} 

		try {

			gp = deviceList.get(position);
			final LinearLayout lay = (LinearLayout) convertView.findViewById(R.id.lay_main);
			final CircularNetworkImageView imgchild = (CircularNetworkImageView) convertView.findViewById(R.id.img_child);
			txtchild = (TextView) convertView.findViewById(R.id.txt_child);
			lay_main= (LinearLayout) convertView.findViewById(R.id.lay_main);
			txtchild.setText(gp.getName());
			imgchild.setTag(position);
			txtchild.setTag(position);
			if (gp.getColor()!=null)
			{
				lay_main.setBackgroundColor(Color.parseColor(gp.getColor()));
			//	convertView.setBackgroundColor(Color.parseColor(gp.getColor()));
			}

			//to show Network Images  here child
			try {

				Picasso.with(context).
						load(Common.Relative_URL + gp.getPath().replaceAll(" ", "%20")).transform(new CircleTransform())
						.placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
						.error(R.drawable.student)
						.into(imgchild)
				;

			} catch (Exception ex) {
				ex.printStackTrace();

			}
		/*	convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					GShowMapFragment.flag = 0;
					DeviceDataDTO user = deviceList.get((Integer) imgchild.getTag());
					GShowMapFragment.StudentId = Integer.parseInt(user.getId());
					GShowMapFragment.Photo = user.getPath().replaceAll(" ", "%20");

					trackInfo = false;


					try {

						URL url = new URL(Common.Relative_URL + user.getPath().replaceAll(" ", "%20"));
						HttpURLConnection connection = null;
						connection = (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						Bitmap myBitmap = BitmapFactory.decodeStream(input);
						ShowMap.bmp1 = Common.getRoundedShape(myBitmap);


						LoginActivity.mClient.sendMessage(StopTracKEvent());
						LoginActivity.mClient.sendMessage(makeJSON(user.getId()));


					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});


		}catch(Exception ex){
			Log.e("Exception-Adatpte", ""+ex);
		}

		if (position==0){
			convertView.setSelected(true);
		}*/
		}catch (Exception e){
			e.printStackTrace();
		}

		return convertView;
	}

	protected String makeJSON(String Id) {
		String trackSTring = "{}";
		try {
			JSONObject jo = new JSONObject();
			jo.put("event", "start_track");
			if (Common.roleid.equals("5")) {
				jo.put("student_id", "demo_pet");
				Common.currTrack = "demo_pet";


			} else {
				jo.put("student_id", Integer.parseInt(Id));
				Common.currTrack = Id;
			}
			trackSTring = jo.toString();
		} catch (Exception e) {

		}

		Log.e("Send req For track chiled---", trackSTring + " " + Id);
		return trackSTring;
	}


	//Stop track
	String StopTracKEvent() {
		String trackSTring = "{}";
		try {
			JSONObject jo = new JSONObject();
			jo.put("event", "stop_track");
			trackSTring = jo.toString();
		} catch (Exception e) {

		}
		return trackSTring;
		// TODO Auto-generated method stub

	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		deviceList.clear();
		if (charText.length() == 0) {
			deviceList.addAll(deviceListCopy);
		} else {
			for (DeviceDataDTO wp : deviceListCopy) {
				if (wp.getName().toLowerCase(Locale.getDefault())
						.contains(charText)) {
					deviceList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}


}
