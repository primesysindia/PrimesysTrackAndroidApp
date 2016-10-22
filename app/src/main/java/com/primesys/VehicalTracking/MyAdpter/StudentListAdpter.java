package com.primesys.VehicalTracking.MyAdpter;


import java.util.ArrayList;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircleTransform;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

public class StudentListAdpter  extends ArrayAdapter<GmapDetais>{

	TextView txtchild,txtcar,txtpet;
	Context context;
	int layoutResourceId;
	ArrayList<GmapDetais> datal;
	public static Boolean trackInfo=false;
	LinearLayout laychild,laypet,layCar;
	Bitmap bitmap;
	static RequestQueue RecordSyncQueue;
	public static GmapDetais user1;
	public StudentListAdpter(Context context, int layoutResourceId,
			ArrayList<GmapDetais> data) {
		super(context, layoutResourceId,data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.datal = data;
		trackInfo=false;
		//GeofencingHome.GeoStudentId=Integer.parseInt(data.get(0).getId());

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {

		if (convertView== null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);

		} 

		try{

			GmapDetais gp=datal.get(position);
			final LinearLayout lay=(LinearLayout)convertView.findViewById(R.id.lay_main);
			final CircularNetworkImageView imgchild=(CircularNetworkImageView)convertView.findViewById(R.id.img_child);
			txtchild=(TextView)convertView.findViewById(R.id.txt_child);
			txtchild.setText(gp.getName());
			imgchild.setTag(position);
			txtchild.setTag(position);

		
			
			
			//to show Network Images  here child
		
				 Picasso.with(context).
				 load(Common.Relative_URL+gp.getPath().replaceAll(" ","%20")).transform(new CircleTransform())
				 .placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
                 .error(R.drawable.student)     
				 .into(imgchild)
				 ;

			
		}catch(Exception ex){
			Log.e("Exception-Adatpte", ""+ex);
		}



		return convertView;
	}

	private Bitmap getRoundedShape(Bitmap bitmap) {
		int targetWidth = 40;
		int targetHeight = 40;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
				targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), 
						((float) targetHeight)) / 2),
						Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = bitmap;
		canvas.drawBitmap(sourceBitmap, 
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()), 
						new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;

	}

	
}
