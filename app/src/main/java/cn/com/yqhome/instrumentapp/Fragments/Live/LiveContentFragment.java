package cn.com.yqhome.instrumentapp.Fragments.Live;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ForumAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.LiveAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.RecycleAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.LiveActivity;
import cn.com.yqhome.instrumentapp.PlayerActivity;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/11.
 */

public class LiveContentFragment extends Fragment {
    private static final String TAG = "LiveContentFragment";
    ShimmerRecyclerView recyclerView;
    LiveAdapter mAdapter;

    SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler();

    public GridLayoutManager gridLayoutManager;
    public SimpleAdapter simpleAdapter;

    private List<Live> listLives;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.livecontext_fragment,container,false);
        initView(view);
        return  view;
    }
    private void initView(View view){

        DatabaseHelper dHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dHelper.getReadableDatabase();
        listLives = Live.getAllLivesFromDB(db);
//        listLives = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.livecontext_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


            }
        });

        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.livecontext_recycleGrid);
//        mAdapter = new RecycleAdapter("news");
//        mAdapter.setType(1);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mAdapter = new LiveAdapter();
        mAdapter.setListener(new LiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Live item) {
                Bundle bundle = new Bundle();

                bundle.putSerializable(BaseUtils.INTENT_Live,item);
                Intent intent = new Intent(getActivity(), LiveActivity.class);
//                intent.putExtra(BaseUtils.INTENT_FORUM_VIDEO,item.id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAdapter.setLiveDatas(listLives);
        recyclerView.setAdapter(mAdapter);

        recyclerView.getItemAnimator().setChangeDuration(700);
//        simpleAdapter = new SimpleAdapter();
//        recyclerView.setAdapter(simpleAdapter);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (listLives.size() == 0){
            recyclerView.showShimmerAdapter();
//            recyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.hideShimmerAdapter();
//                }
//            },2000);

        }else{


        }

    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setRefreshing(false);
    }

    public GridLayoutManager getGridLayoutManager() {
        return gridLayoutManager;
    }

    public SimpleAdapter getSimpleAdapter() {
        return simpleAdapter;
    }

}
