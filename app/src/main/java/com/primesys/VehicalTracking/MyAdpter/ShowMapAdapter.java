package com.primesys.VehicalTracking.MyAdpter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircleTransform;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

public class ShowMapAdapter  extends ArrayAdapter<GmapDetais> {

	TextView txtchild,txtcar,txtpet;
	Context context;
	ImageLoader imageLoader;
	int layoutResourceId;
	ArrayList<GmapDetais> datal;
	public static Boolean trackInfo=false;
	LinearLayout laychild,laypet,layCar;
	Bitmap bitmap;
	static RequestQueue RecordSyncQueue;
	public static GmapDetais gp;
	public ShowMapAdapter(Context context, int layoutResourceId,
			ArrayList<GmapDetais> data, ImageLoader imageLoader2) {
		super(context, layoutResourceId,data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.datal = data;
		this.imageLoader=imageLoader2;
		trackInfo=false;
	//	ShowMapFragment.StudentId=Integer.parseInt(data.get(0).getId());

	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		if (convertView== null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);

		} 

		try {

			gp = datal.get(position);
			final LinearLayout lay = (LinearLayout) convertView.findViewById(R.id.lay_main);
			final CircularNetworkImageView imgchild = (CircularNetworkImageView) convertView.findViewById(R.id.img_child);
			txtchild = (TextView) convertView.findViewById(R.id.txt_child);
			txtchild.setText(gp.getName());
			imgchild.setTag(position);
			txtchild.setTag(position);


			//to show Network Images  here child
			try {

				Picasso.with(context).
						load(Common.Relative_URL + gp.getPath().replaceAll(" ", "%20")).transform(new CircleTransform())
						.placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
						.error(R.drawable.student)
						.into(imgchild)
				;

			} catch (Exception ex) {

			}
		/*	convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ShowMapFragment.flag = 0;
					GmapDetais user = datal.get((Integer) imgchild.getTag());
					ShowMapFragment.StudentId = Integer.parseInt(user.getId());
					ShowMapFragment.Photo = user.getPath().replaceAll(" ", "%20");

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


}
