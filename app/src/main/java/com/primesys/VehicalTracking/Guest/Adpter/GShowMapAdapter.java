package com.primesys.VehicalTracking.Guest.Adpter;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

public class GShowMapAdapter extends ArrayAdapter<DeviceDataDTO> {

	TextView txtchild,txtcar,txtpet;
	Context context;
	ImageLoader imageLoader;
	int layoutResourceId;
	ArrayList<DeviceDataDTO> datal;
	public static Boolean trackInfo=false;
	LinearLayout laychild,laypet,layCar;
	Bitmap bitmap;
	static RequestQueue RecordSyncQueue;
	public static DeviceDataDTO gp;
	LinearLayout lay_main;
	View veh_status;
	public GShowMapAdapter(Context context, int layoutResourceId,
						   ArrayList<DeviceDataDTO> data, ImageLoader imageLoader2) {
		super(context, layoutResourceId,data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.datal = data;
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

			gp = datal.get(position);
			final LinearLayout lay = (LinearLayout) convertView.findViewById(R.id.lay_main);
			final CircularNetworkImageView imgchild = (CircularNetworkImageView) convertView.findViewById(R.id.img_child);
			txtchild = (TextView) convertView.findViewById(R.id.txt_child);
			lay_main= (LinearLayout) convertView.findViewById(R.id.lay_main);
			txtchild.setText(gp.getName());
			imgchild.setTag(position);
			txtchild.setTag(position);
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

		}catch (Exception e){
			e.printStackTrace();
		}

		return convertView;
	}




}
