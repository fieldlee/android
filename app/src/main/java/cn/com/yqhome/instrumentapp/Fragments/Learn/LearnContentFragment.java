package cn.com.yqhome.instrumentapp.Fragments.Learn;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ItemDivider;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.LearnAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.RecycleAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.LearnContentActivity;
import cn.com.yqhome.instrumentapp.PlayerActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/11.
 */

public class LearnContentFragment extends Fragment {
    private ShimmerRecyclerView recyclerView;
    private LearnAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler();
    private List<Learn> learnList = new ArrayList<Learn>();
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.learncontext_fragment,container,false);
        initView(view);
        return view;
    }
    public void initView(View view){

        DatabaseHelper dHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dHelper.getReadableDatabase();
        learnList = Learn.getAllLearnsByType(db,type);

        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.learncontext_recycleView);
        mAdapter = new LearnAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new LearnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Learn item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(BaseUtils.INTENT_LEARN,item);
                Intent intent = new Intent(getActivity(), LearnContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider().setDividerWith(1).setDividerColor(R.color.colorDivider));
        if (learnList.size()==0){
            recyclerView.showShimmerAdapter();
        }
//        swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.learncontext_fragment_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                WebUtils.Learn(getActivity(),params,type,new CallbackListener(){
                    @Override
                    public void learnCallback(List<Learn> learnslist) {
                        for (int i = 0; i < learnslist.size(); i++) {
                            learnList.add(learnslist.get(i));
                        }
                        mAdapter.setLearnDatas(learnList);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        RequestParams params = new RequestParams();
        WebUtils.Learn(getActivity(),params,type,new CallbackListener(){
            @Override
            public void learnCallback(List<Learn> learnslist) {
                for (int i = 0; i < learnslist.size(); i++) {
                    learnList.add(learnslist.get(i));
                }
                mAdapter.setLearnDatas(learnList);
                recyclerView.hideShimmerAdapter();
            }
        });

    }
}
