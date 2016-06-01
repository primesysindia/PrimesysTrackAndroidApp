package com.primesys.VehicalTracking.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class CircularNetworkImageView extends NetworkImageView {
	Context mContext;

	public CircularNetworkImageView(Context context) {
		super(context);
		mContext = context;
	}

	public CircularNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	public CircularNetworkImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		if(bm==null) return;
		setImageDrawable(new BitmapDrawable(mContext.getResources(),
				getCircularBitmap(bm)));
	}

	
	public Bitmap getCircularBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int width = bitmap.getWidth();
		//int height= bitmap.getHeight();
	    if(bitmap.getWidth()>bitmap.getHeight())
			width = bitmap.getHeight();
		final int color = 0xffFFFFFF;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, width);
		final RectF rectF = new RectF(rect);
		final float roundPx = width / 2;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		//canvas.drawRoundRect(rectF, 15, 15, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 300;
		int targetHeight = 300;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
				targetHeight, Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
						Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, 
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()), 
						new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;
	}


}
