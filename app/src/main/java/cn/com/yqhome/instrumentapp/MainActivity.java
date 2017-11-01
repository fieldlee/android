package cn.com.yqhome.instrumentapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.nfc.Tag;
import android.support.design.widget.BottomNavigationView;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

import java.util.ArrayList;

import cn.com.yqhome.instrumentapp.Fragments.BaseFragment;
import cn.com.yqhome.instrumentapp.Fragments.Forum.WriteForumActivity;
import cn.com.yqhome.instrumentapp.Fragments.ForumFragment;
import cn.com.yqhome.instrumentapp.Fragments.HomeFragment;
import cn.com.yqhome.instrumentapp.Fragments.LearnFragment;
import cn.com.yqhome.instrumentapp.Fragments.Live.LiveContentFragment;
import cn.com.yqhome.instrumentapp.Fragments.Live.SimpleAdapter;
import cn.com.yqhome.instrumentapp.Fragments.LiveFragment;
import cn.com.yqhome.instrumentapp.Fragments.SetupFragment;
import cn.com.yqhome.instrumentapp.Fragments.SetupInfoFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
//    public static final TAGKey = "MainActivity";
    private static final String TAGKey = "MainActivity";
    private static final boolean DEBUG = true;
    private Menu menu;
    BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    TextBadgeItem numberBadgeItem;

    HomeFragment homeFragment;
    ForumFragment forumFragment;
    LearnFragment learnFragment;
    LiveFragment liveFragment;
    SetupFragment setupFragment;

    Class[] clazzFragments;
    String[] nameFragments;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    private ArrayList<BaseFragment> fragmentStack = new ArrayList<BaseFragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigationbar);

        bottomNavigationBar.setTabSelectedListener(this);

//      初始化fragment
//        homeFragment =  new HomeFragment();
//        forumFragment = new ForumFragment();
//        learnFragment = new LearnFragment();
//        liveFragment =  new LiveFragment();
//        setupFragment = new SetupFragment();

        numberBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(false);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "首页").setActiveColorResource(R.color.orange).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "论坛").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "乐谱").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "直播V").setActiveColorResource(R.color.brown))
                .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "我的").setActiveColorResource(R.color.grey))
                .setFirstSelectedPosition(lastSelectedPosition)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise();
        fm = getFragmentManager();
        Class[] clazzs = {HomeFragment.class, ForumFragment.class, LearnFragment.class, LiveFragment.class, SetupFragment.class};
        clazzFragments = clazzs;
        nameFragments = new String[]{"乐器之家", "论坛","乐谱","直播鉴赏","我的信息"};
        changeFragment(0); //初始化显示home fragment
    }

    public void setActionBarShow(boolean show){
        if (show){
            getSupportActionBar().show();
        }else{
            getSupportActionBar().hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.home_menu == item.getItemId()){
//            Intent intent = new Intent(this,PlayerActivity.class);
//            startActivity(intent);

//            Intent intent1 = new Intent(this,LearnContentActivity.class);
//            startActivity(intent1);
            Intent intentLive = new Intent(this,LoginActivity.class);
            startActivity(intentLive);

            Log.i(TAGKey,"onOptionsItemSelected:home_menu");
        }
        if(R.id.home_search == item.getItemId()){
            Intent intent = new Intent(this,RemoteActivity.class);
            startActivity(intent);
            Log.i(TAGKey,"onOptionsItemSelected:home_search");
        }
        if (R.id.write_formu == item.getItemId()){
            Intent intent = new Intent(this, WriteForumActivity.class);
            startActivity(
                    intent
            );
            Log.i(TAGKey,"onOptionsItemSelected:write_formu");
        }

        if (item.getItemId() == R.id.action_list_to_grid) {
            if (!((Animatable) item.getIcon()).isRunning()) {
                GridLayoutManager gridLayoutManager = this.liveFragment.getLiveContentFragment().getGridLayoutManager();
                SimpleAdapter simpleAdapter = this.liveFragment.getLiveContentFragment().getSimpleAdapter();
                if (gridLayoutManager.getSpanCount() == 2) {
                    item.setIcon(AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_list_to_grid));
                    gridLayoutManager.setSpanCount(1);
                } else {
                    item.setIcon(AnimatedVectorDrawableCompat.create(MainActivity.this, R.drawable.avd_grid_to_list));
                    gridLayoutManager.setSpanCount(2);
                }
                ((Animatable) item.getIcon()).start();
                simpleAdapter.notifyItemRangeChanged(0, simpleAdapter.getItemCount());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //   BottomNavigationBar
    @Override
    public void onTabSelected(int position) {
        if (DEBUG){
            Log.i(TAGKey,"onTabSelected position:"+position);
        }
        changeFragment(position);
    }

    @Override
    public void onTabUnselected(int position) {
        if (DEBUG){
            Log.i(TAGKey,"onTabUnselected position:"+position);
        }
    }

    @Override
    public void onTabReselected(int position) {
        if (DEBUG){
            Log.i(TAGKey,"onTabReselected position:"+position);
        }
    }

    public void  changeFragment(int index){
        BaseFragment fragment = null;
        try {
            fragment = (BaseFragment) clazzFragments[index].newInstance();

            setTitle(nameFragments[index]);
            if (index==0){
                this.homeFragment = (HomeFragment)fragment;
                if (this != null && this.menu != null){
                    this.menu.clear();
                    getMenuInflater().inflate(R.menu.home_menu,this.menu);
                }
            }
            if (index == 1){
                this.forumFragment = (ForumFragment)fragment;
                if (this != null && this.menu != null){
                    this.menu.clear();
                    getMenuInflater().inflate(R.menu.forum_menu,this.menu);
                }

            }
            if (index==2){
                this.learnFragment = (LearnFragment)fragment;
                if (this != null && this.menu != null){
                    this.menu.clear();
//                    getMenuInflater().inflate(R.menu.home_menu,this.menu);
                }
            }

            if (index == 3){
                this.liveFragment = (LiveFragment)fragment;
                if (this != null && this.menu != null){
                    this.menu.clear();
                    getMenuInflater().inflate(R.menu.live_menu,this.menu);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        replaceFragment(fragment);
    }

    public void pushFragment(BaseFragment newFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.home_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        fragmentStack.add(newFragment);
    }

    public void popFragment(BaseFragment fragment) {
        if (fragmentStack.size() > 1 && fragmentStack.get(fragmentStack.size() - 1) == fragment) {
            fragmentStack.remove(fragmentStack.size() - 1);

            fm.popBackStack();
        }
    }

    private void replaceFragment(BaseFragment newFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.replace(R.id.home_fragment_container, newFragment);
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }

        fragmentStack.clear();
        fragmentStack.add(newFragment);
    }


    /**
     * 返回物理键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showexitdilog();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置安卓物理键点击弹出对话框
     */
    private void showexitdilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你确定要退出吗");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
