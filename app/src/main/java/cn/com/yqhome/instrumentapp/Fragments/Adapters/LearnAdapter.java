package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Learn;

/**
 * Created by depengli on 2017/9/29.
 */

public class LearnAdapter extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onItemClick(Learn item);
    }
    private OnItemClickListener listener;
    private List<Learn> mLearnList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LearnHolder.newInstance(parent,viewType);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType , LearnAdapter.OnItemClickListener listener) {
        this.listener = listener;
        return onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       Learn learn = mLearnList.get(position);
        ((LearnHolder)holder).bind((Object) learn,BaseUtils.TYPE_ITEM_LEARN, BaseUtils.CELL_LEARN_MP3,listener);
    }

    @Override
    public int getItemCount() {
        if (mLearnList == null){
            return 0;
        }
        return mLearnList.size();
    }

    public void setLearnDatas(List<Learn> mdata){
        mLearnList = mdata;
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
