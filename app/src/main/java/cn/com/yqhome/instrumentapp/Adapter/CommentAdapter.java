package cn.com.yqhome.instrumentapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.yqhome.instrumentapp.Class.Comment;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/19.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<JSONObject> list = new ArrayList<>();
    private static final int CELL_DIVER = 0;
    private static final int CELL_COMMENT = 1;
    private static final int CELL_SUBCOMMENT = 2;
    private Forum forum;
    private News news;
    private Activity mActivity;
    private CommentAdapter mCommentAdapter;

    public CommentAdapter(Context context, Activity activity){
        mContext = context;
        mActivity = activity;
        mCommentAdapter = this;

    }

    public void setForum(Forum forum) {
        this.forum = forum;
        WebUtils.Comment(mActivity,new RequestParams(),this.forum.id, new CallbackListener(){
            @Override
            public void commentCallback(List<JSONObject> commentslist) {
                list = commentslist;

                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setNews(News news) {
        this.news = news;
        WebUtils.Comment(mActivity,new RequestParams(),this.news.id, new CallbackListener(){
            @Override
            public void commentCallback(List<JSONObject> commentslist) {
                list = commentslist;

                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void reloadDatas(){
        if (news != null){
            WebUtils.Comment(mActivity,new RequestParams(),this.news.id,new CallbackListener(){
                @Override
                public void commentCallback(List<JSONObject> commentslist) {
                    list = commentslist;

                    mCommentAdapter.notifyDataSetChanged();
                }
            });
        }
        if (forum != null){
            WebUtils.Comment(mActivity,new RequestParams(),this.forum.id,new CallbackListener(){
                @Override
                public void commentCallback(List<JSONObject> commentslist) {
                    list = commentslist;

                    mCommentAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return CELL_COMMENT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
//        ViewHolderDiver holderDiver = null;
        ViewHolderForum holderForum = null;
        ViewHolderSubForum holderSubForum = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext);
//            // 按当前所需的样式，确定new的布局
            switch (type) {
                case CELL_SUBCOMMENT:
                    convertView = inflater.inflate(R.layout.comment_cell_html,
                            parent, false);
                    holderSubForum = new ViewHolderSubForum();

                    holderSubForum.avatorView = (TextView)convertView.findViewById(R.id.comment_cell_avator);
                    holderSubForum.avatorImage = (ImageView)convertView.findViewById(R.id.comment_cell_avatorimage);
                    holderSubForum.timeView = (TextView)convertView.findViewById(R.id.comment_cell_time);
                    holderSubForum.contentView = (TextView)convertView.findViewById(R.id.comment_cell_html);

                    convertView.setTag(holderSubForum);
                    break;
                case CELL_COMMENT:
                    convertView = inflater.inflate(R.layout.comment_cell_html,
                            parent, false);
                    holderForum = new ViewHolderForum();

                    holderForum.avatorView = (TextView)convertView.findViewById(R.id.comment_cell_avator);
                    holderForum.avatorImage = (ImageView)convertView.findViewById(R.id.comment_cell_avatorimage);
                    holderForum.timeView = (TextView)convertView.findViewById(R.id.comment_cell_time);
                    holderForum.contentView = (TextView)convertView.findViewById(R.id.comment_cell_html);

                    convertView.setTag(holderForum);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case CELL_COMMENT:
                    holderForum = (ViewHolderForum) convertView.getTag();
                    break;
                case CELL_SUBCOMMENT:
                    holderSubForum =  (ViewHolderSubForum) convertView.getTag();
                    break;
            }
        }
        // 设置资源
        switch (type) {
            case CELL_SUBCOMMENT:
                holderSubForum.contentView.setText("testsetsetset");
                holderSubForum.timeView.setText("9分钟前");
                holderSubForum.avatorView.setText("fieldlee");
                break;
            case CELL_COMMENT:
                JSONObject tmpObj = list.get(position);
                    try {
                        holderForum.contentView.setText(Html.fromHtml(tmpObj.getString("content")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        holderForum.timeView.setText(tmpObj.getString("fromTime"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        holderForum.avatorView.setText(tmpObj.getString("avator"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (tmpObj.getString("avatorPath").length()>500){
                            String pureBase64Encoded = tmpObj.getString("avatorPath").substring(tmpObj.getString("avatorPath").indexOf(",")  + 1);
                            byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            holderForum.avatorImage.setImageBitmap(decodedByte);
                        }
                        else{
                            Glide.with(mContext).load(tmpObj.getString("avatorPath")).into(holderForum.avatorImage);
                        }
                        break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
            case CELL_DIVER:
                break;
        }

        return convertView;
    }

    private class ViewHolderDiver{

    }

    private class ViewHolderForum{
        TextView timeView;
        TextView avatorView;
        TextView contentView;
        ImageView avatorImage;
//        comment_cell_avatorimage
//        comment_cell_avator
//        comment_cell_time
//        comment_cell_html
    }
    private class ViewHolderSubForum{
        TextView timeView;
        TextView avatorView;
        TextView contentView;
        ImageView avatorImage;
    }
}
