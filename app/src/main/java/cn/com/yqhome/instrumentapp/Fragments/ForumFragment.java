package cn.com.yqhome.instrumentapp.Fragments;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.javier.filterview.FilterView;
import com.javier.filterview.OnFilterViewResultListener;
import com.javier.filterview.single.OnSingleOptionListener;
import com.javier.filterview.single.SingleOption;
import com.javier.filterview.single.SingleSection;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.DropDown.DropDownMenu;
import cn.com.yqhome.instrumentapp.DropDown.MenuListAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Forum.ForumContentFragment;

import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/8.
 */

public class ForumFragment extends BaseFragment {
    public static final String TAG = "ForumFragment";

    private ViewPager forumViewPager;
    private TabLayout mTabLayout;
    //钢琴 萨克斯 小提琴 二胡 吉他 琵琶 其他
    private String mTitles[] = {"钢琴","萨克斯","小提琴","吉他","二胡","琵琶","其他"};
    private int mImages[] = {
            R.drawable.tab_home,
            R.drawable.tab_report,
            R.drawable.tab_message,
            R.drawable.tab_mine
    };
    private ForumFragmentAdapter forumAdapter;
    private List<Fragment> forumFragments;


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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(true);
        View v = inflater.inflate(R.layout.forum_fragments,container,false);
        initView(v);
        return  v;
    }

    public void initView(View view){
        forumFragments = new ArrayList<Fragment>();
        for (int i = 0; i < mTitles.length ; i++) {
            ForumContentFragment forumContentFragment = new ForumContentFragment();
            forumContentFragment.setType(mTitles[i]);
            forumFragments.add(forumContentFragment);
        }
        forumViewPager = (ViewPager) view.findViewById(R.id.forum_fragment_viewpager);
        forumViewPager.setFocusable(true);
        forumAdapter = new ForumFragmentAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        forumViewPager.setAdapter(forumAdapter);
//        mTabLayout
        mTabLayout = (TabLayout) view.findViewById(R.id.forum_fragment_tabs);
        mTabLayout.setupWithViewPager(forumViewPager);
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

    private class ForumFragmentAdapter extends FragmentStatePagerAdapter {
        public ForumFragmentAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            ForumContentFragment contextFragment = (ForumContentFragment)forumFragments.get(position);
            return contextFragment;
        }

        @Override
        public int getCount() {
            return forumFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
