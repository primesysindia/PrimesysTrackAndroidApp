package com.primesys.VehicalTracking.Utility;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.primesys.VehicalTracking.R;


@SuppressLint("NewApi")
public class MyCustomProgressDialog extends ProgressDialog {
  private Animation animation;
private ImageView la;
private ObjectAnimator anim;
  static Context context;

  public static ProgressDialog ctor(Context context) {
    MyCustomProgressDialog dialog = new MyCustomProgressDialog(context);
    dialog.setIndeterminate(true);
    dialog.setCancelable(true);
    MyCustomProgressDialog.context=context;
    return dialog;
  }

  public MyCustomProgressDialog(Context context) {
    super(context);
  }

  public MyCustomProgressDialog(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_custom_progress_dialog);

    try {
		 la = (ImageView) findViewById(R.id.animationimage);
		 animation = AnimationUtils.loadAnimation(context, R.anim.custom_progress_dialog_animation);
		// la.startAnimation(animation);
		 
		 
		   anim = ObjectAnimator.ofFloat(la, "rotation", 0, 360);
		    anim.setDuration(1000);
		    anim.setRepeatCount(animation.INFINITE);
		    anim.setRepeatMode(ObjectAnimator.RESTART);
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    
   
  }

  @Override
  public void show() {
    super.show();
   // animation.reset();
    anim.start();
  }

  @Override
  public void dismiss() {
    super.dismiss();
  
    anim.end();
    anim.cancel();

    
  }
  
  
  public void startAnimation(View view) {
	    anim.start();
	  }

	  public void endAnimation(View view) {
	    anim.end();
	  }

	  public void cancelAnimation(View view) {
	    anim.cancel();
	  }

	  public void pauseAnimation(View view) {
	    anim.pause();
	  }

	  public void resumeAnimation(View view) {
	    anim.resume();
	  }
}
