package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;

/**
 * Created by depengli on 2017/9/15.
 */

public class ForumAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onItemClick(Forum item);
    }
    private OnItemClickListener listener;
    private List<Forum> mForumList;
    private View mHeader;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==BaseUtils.CELL_HEADER){
            return new ItemHolder(mHeader,BaseUtils.CELL_HEADER);
        }
        return ItemHolder.newInstance(parent, viewType);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ,OnItemClickListener listener) {
        this.listener = listener;
        return onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Forum tmpForum;
        if (mHeader == null){
            tmpForum = mForumList.get(position);
        }else{
            if (position==0){
                return;
            }
            tmpForum = mForumList.get(position-1);
        }
        if (tmpForum.images.length == 0 && tmpForum.videos.length ==0){

            ((ItemHolder)holder).bind((Object) tmpForum,BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_NO_PICTURE,listener);
        }
        if (tmpForum.videos.length==1){
            int a = position % 2 ;
            if (a == 0){
                ((ItemHolder)holder).bind((Object) tmpForum,BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_VIDEO_PICTURE,listener);
            }
            else{
                ((ItemHolder)holder).bind((Object) tmpForum,BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_VIDEO_BIG_PICTURE,listener);
            }
        }

        if (tmpForum.videos.length == 0  && tmpForum.images.length > 0 && tmpForum.images.length < 3){
            ((ItemHolder)holder).bind((Object) tmpForum,BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_ONE_PICTURE,listener);
        }

        if (tmpForum.videos.length == 0  && tmpForum.images.length >= 3){
            ((ItemHolder)holder).bind((Object) tmpForum,BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_THREE_PICTURE,listener);
        }
        return;
    }

    @Override
    public int getItemViewType(int position) {
        Forum tmpForum;
        if (mHeader == null){
            tmpForum = mForumList.get(position);
        }else{
//            获得cell header
            if (position == 0){
                return BaseUtils.CELL_HEADER;
            }

            tmpForum = mForumList.get(position-1);
        }

        if (tmpForum.images.length == 0 && tmpForum.videos.length ==0){
           return  BaseUtils.CELL_NO_PICTURE;
        }
        if (tmpForum.videos.length==1){
            int a = position % 2 ;
            if (a == 0){
//                return BaseUtils.CELL_VIDEO_BIG_PICTURE;
                return  BaseUtils.CELL_VIDEO_PICTURE;
            }
            else{
                return BaseUtils.CELL_VIDEO_BIG_PICTURE;
            }
        }

        if (tmpForum.videos.length == 0  && tmpForum.images.length > 0 && tmpForum.images.length < 3){
            return  BaseUtils.CELL_ONE_PICTURE;
        }

        if (tmpForum.videos.length == 0  && tmpForum.images.length >= 3){
            return  BaseUtils.CELL_THREE_PICTURE;
        }
        return BaseUtils.CELL_NO_PICTURE;
    }

    @Override
    public int getItemCount() {
        if (mHeader==null){
            return mForumList.size();
        }
        return mForumList.size() + 1;
    }

    public void setForumDatas(List<Forum> mdata){
        mForumList = mdata;
    }

    public void setListener(ForumAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public View getHeaderView(){
        return mHeader;
    }

    public void setHeaderView(View headerView) {
        mHeader = headerView;
        notifyItemInserted(0);
    }
}
