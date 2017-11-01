package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;

/**
 * Created by depengli on 2017/10/29.
 */

public class NewsAdapter extends RecyclerView.Adapter {

    public interface OnItemClickListener {
        void onItemClick(News item);
    }
    private OnItemClickListener listener;
    private List<News> mNewsList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return NewsHolder.newInstance(parent, viewType);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType ,OnItemClickListener listener) {
        this.listener = listener;
        return onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News tmpNews = mNewsList.get(position);
        if ((tmpNews.images == null ||tmpNews.images.length == 0) && (tmpNews.videos == null ||tmpNews.videos.length ==0)){

            ((NewsHolder)holder).bind((Object) tmpNews, BaseUtils.TYPE_ITEM_NEWS,BaseUtils.CELL_NO_PICTURE,listener);
        }
        if (tmpNews.videos != null && tmpNews.videos.length==1){
            int a = position % 2 ;
            if (a == 0){
                ((NewsHolder)holder).bind((Object) tmpNews,BaseUtils.TYPE_ITEM_NEWS,BaseUtils.CELL_VIDEO_PICTURE,listener);
            }
            else{
                ((NewsHolder)holder).bind((Object) tmpNews,BaseUtils.TYPE_ITEM_NEWS,BaseUtils.CELL_VIDEO_BIG_PICTURE,listener);
            }
        }

        if ((tmpNews.videos == null || tmpNews.videos.length == 0)  && (tmpNews.images != null && tmpNews.images.length > 0 && tmpNews.images.length < 3)){
            ((NewsHolder)holder).bind((Object) tmpNews,BaseUtils.TYPE_ITEM_NEWS,BaseUtils.CELL_ONE_PICTURE,listener);
        }

        if ( (tmpNews.videos == null || tmpNews.videos.length == 0)  && (tmpNews.images != null && tmpNews.images.length >= 3)){
            ((NewsHolder)holder).bind((Object) tmpNews,BaseUtils.TYPE_ITEM_NEWS,BaseUtils.CELL_THREE_PICTURE,listener);
        }
        return;
    }

    @Override
    public int getItemViewType(int position) {
        News tmpNews = mNewsList.get(position);
        if ((tmpNews.images == null || tmpNews.images.length == 0) && (tmpNews.videos == null || tmpNews.videos.length ==0)){
            return  BaseUtils.CELL_NO_PICTURE;
        }
        if (tmpNews.videos != null && tmpNews.videos.length==1){
            int a = position % 2 ;
            if (a == 0){
                return  BaseUtils.CELL_VIDEO_PICTURE;
            }
            else{
                return BaseUtils.CELL_VIDEO_BIG_PICTURE;
            }
        }

        if ((tmpNews.videos == null || tmpNews.videos.length == 0)  && tmpNews.images != null && tmpNews.images.length > 0 && tmpNews.images.length < 3){
            return  BaseUtils.CELL_ONE_PICTURE;
        }

        if ((tmpNews.videos == null || tmpNews.videos.length == 0)  && tmpNews.images != null && tmpNews.images.length >= 3){
            return  BaseUtils.CELL_THREE_PICTURE;
        }
        return BaseUtils.CELL_NO_PICTURE;
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setNewsDatas(List<News> mdata){
        mNewsList = mdata;
    }

    public void setListener(NewsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

}
