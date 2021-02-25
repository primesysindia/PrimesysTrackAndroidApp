package com.primesys.VehicalTracking.ActivityMykiddyLike.MykiddyFragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.VTSFragments.ShowMapFragment;
import com.primesys.VehicalTracking.VTSFragments.history_API_Fragment;

import java.util.ArrayList;
import java.util.List;


public class HomeTrackFragment extends Fragment {
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_map,
            R.drawable.ic_history,
    };
    private Typeface typeFace;

    private View rootView;
    private Context context;

    public HomeTrackFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) 
    {

        rootView = inflater.inflate(R.layout.fragment_home_track, container, false);
        context = container.getContext();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpagertrack);

        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeFace);
                    ((TextView) tabViewChild).setTextSize(15);
                }
            }
        }


        // Inflate the layout for this fragment
        return rootView;
    }




    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new ShowMapFragment(), "Track");
        adapter.addFrag(new history_API_Fragment(), "History");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        /* @Override
         public Fragment getItem(int position) {
             Log.e("INside ---","Fragment---------------------------------"+position);

             return mFragmentList.get(position);

         }
 */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



        @Override
        public Fragment getItem(int position) {
            Fragment fragment =null;
            switch (position) {
                case 0:
                    fragment = Fragment.instantiate(context, ShowMapFragment.class.getName());

                   /* if (Common.FeatureAddressEnable){
                        fragment = Fragment.instantiate(context, ShowMapFragmentFeatureAddressEnable.class.getName());

                    }else {
                        fragment = Fragment.instantiate(context, ShowMapFragment.class.getName());

                    }*/
                    break;
                case 1:
                    fragment = Fragment.instantiate(context, history_API_Fragment.class.getName());
/*
                    if (Common.FeatureAddressEnable){
                        fragment = Fragment.instantiate(context, history_API_FragmentFearureaddress.class.getName());

                    }else {
                        fragment = Fragment.instantiate(context, history_API_Fragment.class.getName());

                    }*/
                    break;

            }
            return fragment;
        }




        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
