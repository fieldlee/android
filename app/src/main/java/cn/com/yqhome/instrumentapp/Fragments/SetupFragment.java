package cn.com.yqhome.instrumentapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/8.
 */

public class SetupFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{
    private String TAG = "SetupFragment";
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appbarLayout;
    private SetupInfoFragment setupInfoFragment = new SetupInfoFragment();
    private TabsAdapter tabsAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(false);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_fragment,container,false);
        initView(v);
        return  v;
    }

    public void initView(View v){
        tabLayout = (TabLayout) v.findViewById(R.id.materialup_tabs);
        viewPager  = (ViewPager) v.findViewById(R.id.materialup_viewpager);
        appbarLayout = (AppBarLayout) v.findViewById(R.id.materialup_appbar);
        mProfileImage = (ImageView) v.findViewById(R.id.materialup_profile_image);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        tabsAdapter = new TabsAdapter(((MainActivity)getActivity()).getSupportFragmentManager(),setupInfoFragment);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewPager.setAdapter(tabsAdapter);

        Log.i(TAG,TAG+"onStart+"+viewPager.getCurrentItem());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,TAG+"onResume+"+viewPager.getCurrentItem());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,TAG+"onPause+"+viewPager.getCurrentItem());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,TAG+"onStop+"+viewPager.getCurrentItem());
        viewPager.setAdapter(null);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;
        private SetupInfoFragment setupInfoFragment;
        TabsAdapter(FragmentManager fm,SetupInfoFragment setupInfoFragment) {
            super(fm);
            this.setupInfoFragment = setupInfoFragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            if (i==0){
                return this.setupInfoFragment;
            }
            return FakePageFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0){
                return "信息设置";
            }else if (position==1){
                return "关注";
            }else{
                return "关注";
            }

        }
    }
}
