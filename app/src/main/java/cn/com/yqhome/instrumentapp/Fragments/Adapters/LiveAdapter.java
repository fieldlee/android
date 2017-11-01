package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Live;

/**
 * Created by depengli on 2017/10/23.
 */

public class LiveAdapter extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(Live item);
    }
    private OnItemClickListener listener;

    private List<Live> mLiveList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LiveHolder.newInstance(parent);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ,OnItemClickListener listener) {
        this.listener = listener;
        return onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Live live = mLiveList.get(position);
        LiveHolder liveHolder = (LiveHolder) holder;
        liveHolder.bind((Object)live,listener);
    }

    @Override
    public int getItemViewType(int position) {
        return BaseUtils.CELL_LIVE;
    }

    @Override
    public int getItemCount() {
        return mLiveList.size();
    }

    public void setLiveDatas(List<Live> mdata){
        mLiveList = mdata;
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
