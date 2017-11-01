package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/10.
 */

public class RecycleAdapter extends RecyclerView.Adapter<ItemHolder> {

    private LayoutInflater mLayoutInflater;
    private int mType = 0;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    //获取从Activity中传递过来每个item的数据集合
    private String dataType;
    private List<Forum> mForumDatas;
    private List<News> mNewsDatas;


    public RecycleAdapter(String dataType){
        this.dataType = dataType;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        if (dataType == BaseUtils.TYPE_ITEM_FORUM){
            mHeaderView = mLayoutInflater.inflate(R.layout.forum_list_header,parent);
        }
        //&& viewType == BaseUtils.CELL_HEADER
        if(mHeaderView != null && dataType == BaseUtils.TYPE_ITEM_FORUM) {
            return new ItemHolder(mHeaderView,BaseUtils.CELL_HEADER);
        }
        if(mFooterView != null && viewType == BaseUtils.CELL_FOOTER){
            return new ItemHolder(mFooterView,BaseUtils.CELL_FOOTER);
        }
        return ItemHolder.newInstance(parent,viewType);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        if(getItemViewType(position) == BaseUtils.CELL_NO_PICTURE){
            if(holder instanceof ItemHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
//                ((ItemHolder) holder).tv.setText(mDatas.get(position-1));
//                holder.bind(mForumDatas.get(position-1),BaseUtils.TYPE_ITEM_FORUM,BaseUtils.CELL_NO_PICTURE);
                return;
            }
            return;
        }else if(getItemViewType(position) == BaseUtils.CELL_HEADER){
            return;
        }else{
            return;
        }

    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {

        if (mHeaderView != null){
            if (position == 0){
                //第一个item应该加载Header
                return BaseUtils.CELL_HEADER;
            }else{
                if (dataType==BaseUtils.TYPE_ITEM_FORUM){
                    Forum forum = (Forum) mForumDatas.get(position-1);
                    return BaseUtils.CELL_NO_PICTURE;
                }
                if (dataType == BaseUtils.TYPE_ITEM_NEWS){
                    News news = (News) mNewsDatas.get(position-1);
                    return BaseUtils.CELL_NO_PICTURE;
                }
            }
        }else{
            if (dataType==BaseUtils.TYPE_ITEM_FORUM){
                Forum forum = (Forum) mForumDatas.get(position);
//                if (forum)
                return BaseUtils.CELL_NO_PICTURE;
            }
            if (dataType == BaseUtils.TYPE_ITEM_NEWS){
                News news = (News) mNewsDatas.get(position);
                return BaseUtils.CELL_NO_PICTURE;
            }
        }

        if (mFooterView != null){
            if (position == getItemCount()-1){
                //最后一个,应该加载Footer
                return BaseUtils.CELL_FOOTER;
            }
        }

        return BaseUtils.CELL_NO_PICTURE;
    }

    public void setType(int type){
        mType = type;
    }

    public void setForumValues(List<Forum> datas, String type){
        dataType = type;
        mForumDatas = datas;
    }

    public void setNewsValues(List<News> datas, String type){
        dataType = type;
        mNewsDatas = datas;
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            if (dataType==BaseUtils.TYPE_ITEM_FORUM){
                return mForumDatas.size();
            }
            if (dataType==BaseUtils.TYPE_ITEM_NEWS){
                return mNewsDatas.size();
            }
        }else if(mHeaderView == null && mFooterView != null){
            if (dataType == BaseUtils.TYPE_ITEM_FORUM){
                return mForumDatas.size()+1;
            }
            if (dataType == BaseUtils.TYPE_ITEM_NEWS){
                return mNewsDatas.size()+1;
            }

        }else if (mHeaderView != null && mFooterView == null){
            if (dataType == BaseUtils.TYPE_ITEM_FORUM){
                return mForumDatas.size()+1;
            }
            if (dataType == BaseUtils.TYPE_ITEM_NEWS){
                return mNewsDatas.size()+1;
            }
        }else {
            if (dataType == BaseUtils.TYPE_ITEM_FORUM){
                return mForumDatas.size()+2;
            }
            if (dataType == BaseUtils.TYPE_ITEM_NEWS){
                return mNewsDatas.size()+2;
            }

        }
        return 0;
    }
}
