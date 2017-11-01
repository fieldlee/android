package cn.com.yqhome.instrumentapp.Fragments;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.LiveAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.Fragments.Live.LiveContentFragment;
import cn.com.yqhome.instrumentapp.Fragments.Live.SimpleAdapter;
import cn.com.yqhome.instrumentapp.LiveActivity;
import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/8.
 */

public class LiveFragment extends BaseFragment {
    FrameLayout fLayout;
    public LiveContentFragment liveContentFragment;
    public String TAG = "LiveFragment";
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)


    ShimmerRecyclerView recyclerView;
    LiveAdapter mAdapter;

    SwipeRefreshLayout swipeRefreshLayout;
    public GridLayoutManager gridLayoutManager;
    private List<Live> listLives;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(true);
        View view = inflater.inflate(R.layout.livecontext_fragment,container,false);
        initView(view);
        return  view;

//        View v = inflater.inflate(R.layout.live_fragment,container,false);

//        fLayout = (FrameLayout)v.findViewById(R.id.live_fragment_container);
//        liveContentFragment = new LiveContentFragment();

//        android.support.v4.app.FragmentTransaction transaction =
//                getChildFragmentManager().beginTransaction();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


//        transaction.add(R.id.live_fragment_container,liveContentFragment).commit();


//        ((AppCompatActivity)getActivity())
//                .getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.live_fragment_container,liveContentFragment).commit();

//        return  v;
    }

    private void initView(View view){
        DatabaseHelper dHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dHelper.getReadableDatabase();
        listLives = Live.getAllLivesFromDB(db);
//        listLives = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.livecontext_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                WebUtils.Live(getActivity(),params, new CallbackListener(){
                    @Override
                    public void livesCallback(List<Live> liveList) {
                        mAdapter.setLiveDatas(liveList);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.livecontext_recycleGrid);
        mAdapter = new LiveAdapter();
        mAdapter.setListener(new LiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Live item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BaseUtils.INTENT_Live,item);
                Intent intent = new Intent(getActivity(), LiveActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAdapter.setLiveDatas(listLives);
        recyclerView.setAdapter(mAdapter);
        recyclerView.getItemAnimator().setChangeDuration(700);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.showShimmerAdapter();

        RequestParams params = new RequestParams();
        WebUtils.Live(getActivity(),params, new CallbackListener(){
            @Override
            public void livesCallback(List<Live> liveList) {
                mAdapter.setLiveDatas(liveList);
                recyclerView.hideShimmerAdapter();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setRefreshing(false);
    }

    public LiveContentFragment getLiveContentFragment() {
        return liveContentFragment;
    }

}
