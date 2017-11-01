package cn.com.yqhome.instrumentapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.DropDown.DropDownMenu;
import cn.com.yqhome.instrumentapp.DropDown.MenuListAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Learn.LearnContentFragment;

import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/8.
 */

public class LearnFragment extends BaseFragment {
    public static final String TAG = "ForumFragment";

    private ViewPager learnViewPager;
    private TabLayout mTabLayout;
    private String mTitles[] = {"钢琴","萨克斯","小提琴","吉他","大提琴","架子鼓","其他"};

    private LearnFragmentAdapter learnAdapter;
    private List<android.support.v4.app.Fragment> learnFragments;


    private String headers[] = {"城市", "年龄", "性别"};
    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[] = {"不限", "男", "女"};
    private DropDownMenu mDropDownMenu;

    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private MenuListAdapter mMenuAdapter1;
    private MenuListAdapter mMenuAdapter2;
    private MenuListAdapter mMenuAdapter3;

    private List<View> popupViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(true);
        View v = inflater.inflate(R.layout.learn_fragment,container,false);
        initView(v);
        return  v;
    }

    public void initView(View view){
        learnFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.length ; i++) {
            LearnContentFragment learnContentFragment = new LearnContentFragment();
            learnContentFragment.setType(mTitles[i]);
            learnFragments.add(learnContentFragment);
        }
        learnViewPager = (ViewPager) view.findViewById(R.id.learn_fragment_viewpager);
        learnViewPager.setFocusable(true);
        learnAdapter = new LearnFragmentAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        learnViewPager.setAdapter(learnAdapter);
        learnViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabLayout.getTabCount() ; i++) {
                    TabLayout.Tab tab = mTabLayout.getTabAt(position);
                    TextView tv= (TextView)tab.getCustomView().findViewById(R.id.tab_title);
                    if(tv != null){
                        if (position == i){
                            tv.setTextColor(R.color.orange);
                        }
                       else{
                            tv.setTextColor(R.color.blue);
                        }
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mTabLayout
        mTabLayout = (TabLayout) view.findViewById(R.id.learn_fragment_tabs);
        mTabLayout.setupWithViewPager(learnViewPager);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(learnAdapter.getTabView(i));
        }



//        下拉菜单设置

        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.forum_fragment_dropDownMenu);

        //这里是每个下拉菜单之后的布局,目前只是简单的listview作为展示
        listView1 = new ListView(getActivity());
        listView2 = new ListView(getActivity());
        listView3 = new ListView(getActivity());

        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);
        listView3.setDividerHeight(0);

        mMenuAdapter1 = new MenuListAdapter(getActivity(), Arrays.asList(citys));
        mMenuAdapter2 = new MenuListAdapter(getActivity(), Arrays.asList(ages));
        mMenuAdapter3 = new MenuListAdapter(getActivity(), Arrays.asList(sexs));

        listView1.setAdapter(mMenuAdapter1);
        listView2.setAdapter(mMenuAdapter2);
        listView3.setAdapter(mMenuAdapter3);

        popupViews.add(listView1);
        popupViews.add(listView2);
        popupViews.add(listView3);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(citys[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(ages[position]);
                mDropDownMenu.closeMenu();
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(sexs[position]);
                mDropDownMenu.closeMenu();
            }
        });
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);

    }

    private class LearnFragmentAdapter extends FragmentStatePagerAdapter {
        public LearnFragmentAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            LearnContentFragment contextFragment = (LearnContentFragment)learnFragments.get(position);
            return contextFragment;
        }

        @Override
        public int getCount() {
            return learnFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        public View getTabView(int position){
            View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.tab_custom_layout, null);
            TextView tv= (TextView) view.findViewById(R.id.tab_title);
            tv.setText(mTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.tab_imageview);
            img.setImageResource(R.drawable.ball);
            return view;
        }
    }
}
