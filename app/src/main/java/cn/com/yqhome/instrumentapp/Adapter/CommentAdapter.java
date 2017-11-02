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

    public interface OnCommentClickListener {
        void onCommentClick(String id);
    }
    public interface OnSupportClickListener {
        void onSupportClick(String id);
    }

    public OnCommentClickListener commentlistener;
    public OnSupportClickListener supportlistener;

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
                for (int i = 0; i < commentslist.size(); i++) {
                    JSONObject comJson = commentslist.get(i);
                    try {
                        comJson.put("isSub",false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add(comJson); //添加
                    try {
                        if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                            for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                subComJson.put("isSub",true); //Sub 添加
                                list.add(subComJson); //添加
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setNews(News news) {
        this.news = news;
        WebUtils.Comment(mActivity,new RequestParams(),this.news.id, new CallbackListener(){
            @Override
            public void commentCallback(List<JSONObject> commentslist) {
                for (int i = 0; i < commentslist.size(); i++) {
                    JSONObject comJson = commentslist.get(i);
                    try {
                        comJson.put("isSub",false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.add(comJson); //添加
                    try {
                        if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                            for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                subComJson.put("isSub",true); //Sub 添加
                                list.add(subComJson); //添加
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mCommentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void reloadDatas(){
        list = new ArrayList<JSONObject>();
        if (news != null){
            WebUtils.Comment(mActivity,new RequestParams(),this.news.id,new CallbackListener(){
                @Override
                public void commentCallback(List<JSONObject> commentslist) {
                    for (int i = 0; i < commentslist.size(); i++) {
                        JSONObject comJson = commentslist.get(i);
                        try {
                            comJson.put("isSub",false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        list.add(comJson); //添加
                        try {
                            if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                                for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                    JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                    subComJson.put("isSub",true); //Sub 添加
                                    list.add(subComJson); //添加
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mCommentAdapter.notifyDataSetChanged();
                }
            });
        }
        if (forum != null){
            WebUtils.Comment(mActivity,new RequestParams(),this.forum.id,new CallbackListener(){
                @Override
                public void commentCallback(List<JSONObject> commentslist) {
                    for (int i = 0; i < commentslist.size(); i++) {
                        JSONObject comJson = commentslist.get(i);
                        try {
                            comJson.put("isSub",false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        list.add(comJson); //添加
                        try {
                            if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                                for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                    JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                    subComJson.put("isSub",true); //Sub 添加
                                    list.add(subComJson); //添加
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
        if (list.get(position) != null){
            JSONObject tmpObj = list.get(position);
            try {
                if (tmpObj.getBoolean("isSub") == false){
                    return CELL_COMMENT;
                }else{
                    return CELL_SUBCOMMENT;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return CELL_COMMENT;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        ViewHolderForum holderForum = null;
        ViewHolderSubForum holderSubForum = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext);
//            // 按当前所需的样式，确定new的布局
            switch (type) {
                case CELL_SUBCOMMENT:
                    convertView = inflater.inflate(R.layout.comment_re_cell_html,
                            parent, false);

                    holderSubForum = new ViewHolderSubForum();
                    holderSubForum.avatorView = (TextView)convertView.findViewById(R.id.recomment_cell_avator);
                    holderSubForum.avatorImage = (ImageView)convertView.findViewById(R.id.recomment_cell_avatorimage);
                    holderSubForum.timeView = (TextView)convertView.findViewById(R.id.recomment_cell_time);
                    holderSubForum.contentView = (TextView)convertView.findViewById(R.id.recomment_cell_html);
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
                    holderForum.supportBtn = (Button)convertView.findViewById(R.id.comment_cell_support_btn);
                    holderForum.commentBtn = (Button)convertView.findViewById(R.id.comment_cell_recomment_btn);
                    convertView.setTag(holderForum);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case CELL_COMMENT:
                    holderForum     = (ViewHolderForum) convertView.getTag();
                    break;
                case CELL_SUBCOMMENT:
                    holderSubForum  = (ViewHolderSubForum) convertView.getTag();
                    break;
            }
        }
        // 设置资源
        switch (type) {
            case CELL_SUBCOMMENT:
                JSONObject tmpreObj = list.get(position);
                try {
                    holderSubForum.contentView.setText(Html.fromHtml(tmpreObj.getString("content")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderSubForum.timeView.setText(tmpreObj.getString("fromTime"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderSubForum.avatorView.setText(tmpreObj.getString("avator"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (tmpreObj.getString("avatorPath").length()>500){
                        String pureBase64Encoded = tmpreObj.getString("avatorPath").substring(tmpreObj.getString("avatorPath").indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holderSubForum.avatorImage.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(mContext).load(tmpreObj.getString("avatorPath")).into(holderSubForum.avatorImage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case CELL_COMMENT:
                final JSONObject tmpObj = list.get(position);
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (commentlistener != null){
                        holderForum.commentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    commentlistener.onCommentClick(tmpObj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if (supportlistener != null){
                        holderForum.supportBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    supportlistener.onSupportClick(tmpObj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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
        Button supportBtn;
        Button commentBtn;
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
