package com.primesys.VehicalTracking.ActivityMykiddyLike;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.primesys.VehicalTracking.Dto.MainSliderImageDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dev-User on 4/3/2018.
 */

public class AddsViewpagerPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<MainSliderImageDTO> images;
    LayoutInflater layoutInflater;


    public AddsViewpagerPagerAdapter(Context context, ArrayList<MainSliderImageDTO> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.card_viewpager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_adds);
        Picasso.with(context)
                .load(Common.Relative_URL+images.get(position).getSliderImage().trim())
                .into(imageView);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
