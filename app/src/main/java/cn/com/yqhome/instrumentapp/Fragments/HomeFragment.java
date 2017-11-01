package cn.com.yqhome.instrumentapp.Fragments;


import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Ads;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Home.HomeContextFragment;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/8.
 */

public class HomeFragment extends BaseFragment {
    public static final String TAG = "HomeFragment";
    int windowWidth;
    ViewPager viewPager;
    ViewPager homecontextViewPager;
    GalleryAdapter mPagerAdapter;
    LinearLayout dotLayout;
    private List<ImageView> imageViewsList = new ArrayList<ImageView>();
    private List<ImageView> dotList = new ArrayList<ImageView>();
    private List<Ads> adslist = new ArrayList<Ads>();
    private List<Fragment> listFragments;
    private TabLayout mTabLayout;

    private String mTitles[] = {"资讯"};//,"报表1","消息1","我的1"
    int[] drawimages = new int[]{R.drawable.images,R.drawable.images,R.drawable.images,R.drawable.images};
    private int mImages[] = {
            R.drawable.tab_home,
            R.drawable.tab_report,
            R.drawable.tab_message,
            R.drawable.tab_mine
    };
    private ScheduledExecutorService executor;
    int currentItem;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(true);

        View v = inflater.inflate(R.layout.home_fragment,container,false);
        initPage(v); // 初始界面
        initData(v); //加载数据
        return  v;
    }

    private void initData(final View v){
        RequestParams params = new RequestParams();
        WebUtils.Ads(this.getActivity(),params, new CallbackListener(){
            @Override
            public void adsCallback(List<Ads> adslist) {
                initAdsView(v,adslist);
            }
        });

    }

    private void initAdsView(View v,List<Ads> adslist){
        this.adslist = adslist;
//        获得屏幕的宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowWidth = displayMetrics.widthPixels;

//        int[] drawimages = new int[]{R.drawable.images,R.drawable.images,R.drawable.images,R.drawable.images};
        try{
            for (int i = 0; i < adslist.size(); i++) {
                ImageView dotView = new ImageView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16,16);
                params.leftMargin = 4;
                params.rightMargin = 4;
                dotLayout.addView(dotView,params);
                dotList.add(dotView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i <dotList.size() ; i++) {
            if (i==0){
                dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
            }else{
                dotList.get(i).setBackgroundResource(R.drawable.dot_blur);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
//        viewPager.setAdapter(new GalleryAdapter());
        int firstPage = Integer.MAX_VALUE / 2 / adslist.size() * adslist.size();
        currentItem = 0;
        viewPager.setCurrentItem(currentItem, false);
    }

    private void initPage(View v){
//      初始化数据库
        DatabaseHelper dHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dHelper.getWritableDatabase();
        adslist = Ads.getAllAdsFromDB(db);

        viewPager = (ViewPager) v.findViewById(R.id.home_fragment_gallery_container);
        dotLayout = (LinearLayout) v.findViewById(R.id.home_fragment_gallery_dot);
        viewPager.setFocusable(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowWidth = displayMetrics.widthPixels;
        mPagerAdapter = new GalleryAdapter();
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0, false);

        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (adslist.size()>0) {
                    int index = position % adslist.size();
                    for (int i = 0; i < dotList.size(); i++) {
                        if (i == index) {
                            dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
                        } else {
                            dotList.get(i).setBackgroundResource(R.drawable.dot_blur);
                        }
                    }
                    startAutoScroll();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        listFragments = new ArrayList<Fragment>();
        for (int i = 0; i < 1 ; i++) {
            listFragments.add(new HomeContextFragment());
        }

        homecontextViewPager = (ViewPager) v.findViewById(R.id.home_fragment_viewpager);
        homecontextViewPager.setFocusable(true);
        homecontextViewPager.setAdapter(new HomeContextFragmentAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager()));
        homecontextViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mTabHost.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ////      Tabhost
        mTabLayout = (TabLayout) v.findViewById(R.id.home_fragment_tabs);
        mTabLayout.setupWithViewPager(homecontextViewPager);

    }
    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll(); // activity激活时候自动播放
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll(); // activity暂停时候停止自动播放
    }
    private void startAutoScroll() {
        stopAutoScroll();
        executor = Executors.newSingleThreadScheduledExecutor();
        Runnable command = new Runnable() {
            @Override
            public void run() {
                selectNextItem();
            }
            private void selectNextItem() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(++currentItem);
                        if (currentItem>=adslist.size()-1){
                            currentItem = 0;
                        }
                    }
                });
            }
        };
        int delay = 5;
        int period = 5;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        executor.scheduleAtFixedRate(command, delay, period, timeUnit);
    }

    private void stopAutoScroll() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        image.setImageResource(mImages[index]);
        title.setText(mTitles[index]);

        return view;
    }

    private class GalleryAdapter extends PagerAdapter{
        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = adslist.size();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if ( mChildCount > 0) {
                mChildCount --;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager)container).removeView(imageViewsList.get(position));
            int index = innerPosition(position);
            container.removeView((ImageView)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = innerPosition(position);

            ImageView imageview = new ImageView(getActivity());
            imageview.setLayoutParams(new ViewGroup.LayoutParams(windowWidth,(int)(windowWidth/1.78)));
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            imageview.setImageDrawable(getResources().getDrawable(drawimages[0]) );
            container.addView(imageview);
            if (adslist.size()>0){
                if (adslist.get(index).path.indexOf(getResources().getString(R.string.host))>0){
                    Glide.with(getActivity()).load(adslist.get(index).path).into(imageview);
                }else{
                    Glide.with(getActivity()).load(BaseUtils.ROOTURL + adslist.get(index).path).into(imageview);
                }
            }
            return imageview;
        }

        @Override
        public int getCount() {
//            return Integer.MAX_VALUE;
//            return imageViewsList.size();
            return adslist.size();
        }
        private int innerPosition(int position) {
//            if (adslist.size()>0){
//                return position % adslist.size();
//            }
//            return  0;
            return  position;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class HomeContextFragmentAdapter extends FragmentStatePagerAdapter{
        public HomeContextFragmentAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            HomeContextFragment contextFragment = (HomeContextFragment)listFragments.get(position);
            return contextFragment;
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles [position];
        }
    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try{
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }
}
