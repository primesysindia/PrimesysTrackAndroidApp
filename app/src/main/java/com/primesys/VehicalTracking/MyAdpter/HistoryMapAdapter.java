package com.primesys.VehicalTracking.MyAdpter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import com.primesys.VehicalTracking.Db.DBHelper;
import com.primesys.VehicalTracking.Dto.GmapDetais;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.CircleTransform;
import com.primesys.VehicalTracking.Utility.CircularNetworkImageView;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryMapAdapter  extends ArrayAdapter<GmapDetais>{

	TextView txtchild,txtcar,txtpet;
	Context context;
	ImageLoader imageLoader;
	int layoutResourceId;
	ArrayList<GmapDetais> datal;
	String timestamp;
	LinearLayout laychild,laypet,layCar;
	Bitmap bitmap;
	static RequestQueue RecordsSyncQueue;
	public static GmapDetais user1;
	public HistoryMapAdapter(Context context, int layoutResourceId,
			ArrayList<GmapDetais> data, ImageLoader imageLoader2) {
		super(context, layoutResourceId,data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.datal = data;
		this.imageLoader=imageLoader2;


	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		if (convertView== null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutResourceId, parent, false);

		} 

		try {
			//final SimpleDateFormat mFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
			final String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date());
			GmapDetais gp = datal.get(position);
			final LinearLayout lay = (LinearLayout) convertView.findViewById(R.id.lay_main);
			final CircularNetworkImageView imgchild = (CircularNetworkImageView) convertView.findViewById(R.id.img_child);
			txtchild = (TextView) convertView.findViewById(R.id.txt_child);
			txtchild.setText(gp.getName());
			imgchild.setTag(position);
			txtchild.setTag(position);


			try {
				/*(imgchild).setImageUrl(Common.relativeurl+gp.getPath().replaceAll(" ","%20"), imageLoader);
				((CircularNetworkImageView) imgchild)
				.setDefaultImageResId(0);
				((CircularNetworkImageView) imgchild)
				.setErrorImageResId(0);*/
				Picasso.with(context).
						load(Common.Relative_URL + gp.getPath().replaceAll(" ", "%20"))
						.transform(new CircleTransform())
						.placeholder(R.drawable.student) //this is optional the image to display while the url image is downloading
						.error(R.drawable.student)
						.into(imgchild);

			} catch (Exception e) {

			}

/*

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//	ShowMapFragment gmap=new ShowMapFragment();
						//	gmap.flag=0;
						DateTimeActivity.selStatus = true;
						user1 = datal.get((Integer) txtchild.getTag());
						historyFragment.StudentId = Integer.parseInt(user1.getId());
						Log.e("History studid---------", historyFragment.StudentId + "");

						ShowMapFragment.Updatestatus = true;
						BitmapDrawable bitmap_draw = (BitmapDrawable) imgchild.getDrawable();
						Bitmap bmp = bitmap_draw.getBitmap();
						ShowMapFragment.bmp1 = bmp;
						LoginActivity.mClient.sendMessage(makeJSONHistoryChild(formattedDate,historyFragment.StudentId+""));

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
*/


		}catch (Exception e){
			e.printStackTrace();
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


	//History Child
	String makeJSONHistoryChild(String date,String Id)
	{
		DBHelper helper=DBHelper.getInstance(context);
		helper.truncateTables("db_history");
		String trackSTring="{}";
		try{
			JSONObject jo=new JSONObject();
			jo.put("event","get_tracking_history");
			if(Common.roleid.equals("5"))
				jo.put("student_id","demo_student");
			else
				jo.put("student_id",Integer.parseInt(Id));
			jo.put("timestamp",Common.convertToLong(date));
			trackSTring=jo.toString();
		}
		catch(Exception e)
		{

		}
		return trackSTring;
	}

}
