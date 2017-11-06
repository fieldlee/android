package cn.com.yqhome.instrumentapp.Fragments.Forum;

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

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.ContextActivity;
import cn.com.yqhome.instrumentapp.DatabaseHelper;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ForumAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ItemDivider;
import cn.com.yqhome.instrumentapp.PlayerActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/10.
 */
public class ForumContentFragment extends Fragment {
    private static final String TAG = "ForumContentFragment";
    ShimmerRecyclerView recyclerView;
    ForumAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler();
    private List<Forum> listForums;
    private String type;

    public void setType(String type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.forumcontext_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View v){

//        List<Forum> listForums = BaseUtils.getAllForums(getContext());
        DatabaseHelper dHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dHelper.getReadableDatabase();
        listForums = Forum.getAllForumsByType(db,type);

        recyclerView = (ShimmerRecyclerView) v.findViewById(R.id.forumcontext_recycleView);
        mAdapter = new ForumAdapter();
//        setHeaderView();//设置HeaderView
        mAdapter.setListener(new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Forum item) {
                if (item.videos == null || item.videos.length == 0){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BaseUtils.INTENT_FORUM_CONTEXT,item);
                    Intent intent = new Intent(getActivity(), ContextActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BaseUtils.INTENT_FORUM_VIDEO,item);
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        mAdapter.setForumDatas(listForums);

        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDivider().setDividerWith(1).setDividerColor(R.color.colorDivider));
        if (listForums.size()==0){
            recyclerView.showShimmerAdapter();
        }

//        swipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.forumcontext_refresh);
        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams params = new RequestParams();
                WebUtils.Forum(getActivity(),params,type,new CallbackListener(){
                    @Override
                    public void forumsCallback(List<Forum> forumList) {
                        for (int i = 0; i < forumList.size(); i++) {
                            listForums.add(forumList.get(i));
                        }
                        mAdapter.setForumDatas(listForums);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        //        请求网络
        RequestParams params = new RequestParams();
        WebUtils.Forum(getActivity(),params,type,new CallbackListener(){
            @Override
            public void forumsCallback(List<Forum> forumList) {
                for (int i = 0; i < forumList.size(); i++) {
                    listForums.add(forumList.get(i));
                }
                mAdapter.setForumDatas(listForums);
                recyclerView.hideShimmerAdapter();
            }
        });
    }

    private void setHeaderView(){
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.forum_list_header, recyclerView, false);
        mAdapter.setHeaderView(header);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void loadCards(){
//        mAdapter.setForumValues(listForums,BaseUtils.TYPE_ITEM_FORUM);
        recyclerView.hideShimmerAdapter();
    }
}
