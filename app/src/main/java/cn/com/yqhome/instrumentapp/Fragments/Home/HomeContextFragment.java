package cn.com.yqhome.instrumentapp.Fragments.Home;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.ContextActivity;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ItemDivider;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.NewsAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.RecycleAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.PlayerActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/10.
 */

public class HomeContextFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "HomeContextFragment";
    public  int indexFragment;
    private ShimmerRecyclerView recyclerView;
    private NewsAdapter mAdapter;
    private Handler handler=new Handler();
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private List<News> newsArrayList = new ArrayList<News>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.homecontext_fragment,container,false);
        initView(view);
        return view;
    }

    public void initView(View v){
        mAdapter = new NewsAdapter();
        recyclerView = (ShimmerRecyclerView) v.findViewById(R.id.homecontext_recycleView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider().setDividerWith(1).setDividerColor(R.color.colorDivider));
        // 请求
        DatabaseHelper dHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dHelper.getWritableDatabase();
        newsArrayList =  News.getAllNewsFromDB(db);
        mAdapter.setNewsDatas(newsArrayList);
        mAdapter.setListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News item) {
                if (item.videos == null || item.videos.length == 0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BaseUtils.INTENT_NEWS_CONTEXT,item);
                    Intent intent = new Intent(getActivity(), ContextActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BaseUtils.INTENT_NEWS_VIDEO,item);
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        if (newsArrayList.size()==0){
            recyclerView.showShimmerAdapter();
        }
        //请求数据
        RequestParams params = new RequestParams();
        WebUtils.News(getActivity(),params,new CallbackListener(){
            @Override
            public void newsCallback(List<News> newslist) {
                for (int i = 0; i <newslist.size() ; i++) {
                    newsArrayList.add(newslist.get(i));
                }
                mAdapter.setNewsDatas(newsArrayList);
                recyclerView.hideShimmerAdapter();
            }
        });

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.homecontext_refreshlayout);
        refreshLayout.setDistanceToTriggerSync(100);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                WebUtils.News(getActivity(),params,new CallbackListener(){
                    @Override
                    public void newsCallback(List<News> newslist) {
                        for (int i = 0; i <newslist.size() ; i++) {
                            newsArrayList.add(newslist.get(i));
                        }
                        mAdapter.setNewsDatas(newsArrayList);
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshLayout.setRefreshing(false);
    }

    public void setIndexFragment(int indexFragment) {
        this.indexFragment = indexFragment;

    }
}
