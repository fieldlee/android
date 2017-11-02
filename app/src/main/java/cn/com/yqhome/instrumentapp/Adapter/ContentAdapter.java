package cn.com.yqhome.instrumentapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Comment;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.Fragments.Adapters.ForumAdapter;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/13.
 */

public class ContentAdapter extends BaseAdapter {
    private static final String TAGKey = "ContentAdapter";
    private static final int CELL_HEADER = 0;
    private static final int CELL_TEXT = 1;
    private static final int CELL_IMAGE = 2;
    private static final int CELL_DIVER = 3;
    private static final int CELL_COMMENT = 4;
    private static final int CELL_RECOMMENT = 5;
    private String[] ls;
    private List<HashMap<String,String>> list;
    private List<JSONObject> comments = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private String contentString;
    private News news;
    private Forum forum;
    private ContentAdapter mContentAdapter;

    public interface OnCommentClickListener {
        void onCommentClick(String id);
    }
    public interface OnSupportClickListener {
        void onSupportClick(String id);
    }

    public OnCommentClickListener commentlistener;
    public OnSupportClickListener supportlistener;

    public ContentAdapter(Context context,Activity activity,
                          String contentHtml
                            ) {
        contentString = contentHtml;
        ls = contentString.split("</p>");
        list = new ArrayList<>();
        for (int i = 0; i < ls.length; i++) {
            ls[i] = ls[i]+"</p>";
            HashMap<String,String> tmpMap = new HashMap<>();
            if (ls[i].indexOf("img")>0){
                List images = BaseUtils.getImgStr(ls[i]);
                tmpMap.put("type","image");
                tmpMap.put("value",images.get(0).toString());

            }else{
                tmpMap.put("type","text");
                tmpMap.put("value",ls[i]);
            }
            list.add(tmpMap);
        }
        mContext = context;
        mActivity = activity;
        mContentAdapter = this;
    }

    public void setNews(News news) {
        this.news = news;
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
                    comments.add(comJson); //添加
                    try {
                        if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                            for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                subComJson.put("isSub",true); //Sub 添加
                                comments.add(subComJson); //添加
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mContentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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
                    comments.add(comJson); //添加
                    try {
                        if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                            for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                subComJson.put("isSub",true); //Sub 添加
                                comments.add(subComJson); //添加
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mContentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void reloadDatas(){
        comments = new ArrayList<JSONObject>();
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
                        comments.add(comJson); //添加
                        try {
                            if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                                for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                    JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                    subComJson.put("isSub",true); //Sub 添加
                                    comments.add(subComJson); //添加
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mContentAdapter.notifyDataSetChanged();
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
                        comments.add(comJson); //添加
                        try {
                            if (comJson.getJSONArray("subComments") != null && comJson.getJSONArray("subComments").length()>0){
                                for (int j = 0; j < comJson.getJSONArray("subComments").length(); j++) {
                                    JSONObject subComJson = comJson.getJSONArray("subComments").getJSONObject(j);
                                    subComJson.put("isSub",true); //Sub 添加
                                    comments.add(subComJson); //添加
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mContentAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        //适配header
        return list.size() + 2 + comments.size();
    }

    @Override
    public Object getItem(int position) {
        //适配header
        if (position>=list.size()+1 && position <= list.size()+comments.size()){
            return comments.get(position-(list.size()+1));
        }

        if (position>=1 && position<=list.size()){
            return list.get(position-1);
        }

        return new Object();
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        if (position==0){
            return CELL_HEADER;
        }else{
            if (position>=1 && position<=list.size()){
                if (list.get(position-1).get("type")=="image"){
                    return CELL_IMAGE;
                }else{
                    return CELL_TEXT;
                }
            }else if(position>list.size()+1 && position <= list.size()+comments.size()+1){

                if (comments.get(position-(list.size()+2)) != null){
                    JSONObject tmpJsonObj = comments.get(position-(list.size()+2));
                    try {
                        if (tmpJsonObj.getBoolean("isSub")==false){
                            return CELL_COMMENT;
                        }else{
                            return CELL_RECOMMENT;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return CELL_COMMENT;
            }
            else{
                return CELL_DIVER;
            }
        }
    }
    @Override
    public int getViewTypeCount() {
        return 6;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        ViewHolderText holderText = null;
        ViewHolderImage holderImage = null;
        ViewHolderHeader holderHeader = null;
        ViewHolderDiver holderDiver = null;
        ViewHolderComment holderComment = null;
        ViewHolderReComment holderReComment = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            inflater = LayoutInflater.from(mContext);
//            // 按当前所需的样式，确定new的布局
            switch (type) {
                case CELL_RECOMMENT:
                    convertView = inflater.inflate(R.layout.comment_re_cell_html,
                            parent, false);
                    holderReComment = new ViewHolderReComment();
                    holderReComment.avator = (TextView)convertView.findViewById(R.id.recomment_cell_avator);
                    holderReComment.avatorImage = (ImageView)convertView.findViewById(R.id.recomment_cell_avatorimage);
                    holderReComment.commentTime = (TextView)convertView.findViewById(R.id.recomment_cell_time);
                    holderReComment.comment = (TextView)convertView.findViewById(R.id.recomment_cell_html);
                    convertView.setTag(holderReComment);
                    break;
                case CELL_COMMENT:
                    convertView = inflater.inflate(R.layout.comment_cell_html,
                            parent, false);
                    holderComment = new ViewHolderComment();

                    holderComment.avator = (TextView)convertView.findViewById(R.id.comment_cell_avator);
                    holderComment.avatorImage = (RoundedImageView)convertView.findViewById(R.id.comment_cell_avatorimage);
                    holderComment.commentTime = (TextView)convertView.findViewById(R.id.comment_cell_time);
                    holderComment.comment = (TextView)convertView.findViewById(R.id.comment_cell_html);
                    holderComment.commentBtn = (Button)convertView.findViewById(R.id.comment_cell_recomment_btn);
                    holderComment.supportBtn = (Button)convertView.findViewById(R.id.comment_cell_support_btn);
                    convertView.setTag(holderComment);
                    break;
                case CELL_DIVER:
                    convertView = inflater.inflate(R.layout.content_cell_diver,
                            parent, false);
                    holderDiver = new ViewHolderDiver();
                    convertView.setTag(holderDiver);
                    break;
                case CELL_HEADER:
                    convertView = inflater.inflate(R.layout.content_cell_header,
                            parent, false);
                    holderHeader = new ViewHolderHeader();
//                    holderHeader.textView = (TextView) convertView.findViewById(R.id.content_cell_html);
                    holderHeader.titleView = (TextView) convertView.findViewById(R.id.forumcontext_header_title);
                    holderHeader.avatorView = (TextView) convertView.findViewById(R.id.forumcontext_header_avator);
                    holderHeader.avatorImageView = (ImageView)convertView.findViewById(R.id.forumcontext_header_avatorImage);
                    holderHeader.attentionBtn = (Button)convertView.findViewById(R.id.forumcontext_header_attend);
                    convertView.setTag(holderHeader);
                    break;
                case CELL_TEXT:

                    convertView = inflater.inflate(R.layout.content_cell_html,
                            parent, false);
                    holderText = new ViewHolderText();
                    holderText.textView = (TextView) convertView.findViewById(R.id.content_cell_html);
                    convertView.setTag(holderText);
                    break;
                case CELL_IMAGE:
                    convertView = inflater.inflate(R.layout.content_cell_image,
                            parent, false);
                    holderImage = new ViewHolderImage();
                    holderImage.imageview = (ImageView) convertView
                            .findViewById(R.id.content_cell_imageview);
                    convertView.setTag(holderImage);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case CELL_RECOMMENT:
                    holderReComment = (ViewHolderReComment) convertView.getTag();
                    break;
                case CELL_COMMENT:
                    holderComment = (ViewHolderComment) convertView.getTag();
                    break;
                case CELL_DIVER:
                    holderDiver = (ViewHolderDiver) convertView.getTag();
                    break;
                case CELL_HEADER:
                    ViewHolderHeader tmpHeader =  (ViewHolderHeader) convertView.getTag();
                    holderHeader = tmpHeader;
                    break;
                case CELL_TEXT:
                    holderText = (ViewHolderText) convertView.getTag();
                    break;
                case CELL_IMAGE:
                    holderImage = (ViewHolderImage) convertView.getTag();
                    break;
            }
        }
        // 设置资源
        switch (type) {
            case CELL_RECOMMENT:
                JSONObject recomObj = comments.get(position-list.size()-2);
                try {
                    holderReComment.avator.setText(recomObj.getString("avator"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderReComment.commentTime.setText(recomObj.getString("fromTime"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderReComment.comment.setText(Html.fromHtml(recomObj.getString("content")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (recomObj.getString("avatorPath").length()>500){
                        String pureBase64Encoded = recomObj.getString("avatorPath").substring(recomObj.getString("avatorPath").indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holderReComment.avatorImage.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(mContext).load(recomObj.getString("avatorPath")).into(holderReComment.avatorImage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case CELL_COMMENT:
                final JSONObject comObj = comments.get(position-list.size()-2);
                try {
                    holderComment.avator.setText(comObj.getString("avator"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderComment.commentTime.setText(comObj.getString("fromTime"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    holderComment.comment.setText(Html.fromHtml(comObj.getString("content")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (comObj.getString("avatorPath").length()>500){
                        String pureBase64Encoded = comObj.getString("avatorPath").substring(comObj.getString("avatorPath").indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holderComment.avatorImage.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(mContext).load(comObj.getString("avatorPath")).into(holderComment.avatorImage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (commentlistener != null){
                        holderComment.commentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                commentlistener.onCommentClick(comObj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                }

                if (supportlistener != null){
                    holderComment.supportBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                supportlistener.onSupportClick(comObj.getString("_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
            case CELL_DIVER:

                break;
            case CELL_HEADER:
                if (news != null){
                    holderHeader.titleView.setText(news.title);
                    holderHeader.avatorView.setText(news.avator);
                    if (news.avatorPath.length()>500){
                        String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holderHeader.avatorImageView.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(mContext).load(news.avatorPath).into(holderHeader.avatorImageView);
                    }
                }
                if (forum != null){
                    holderHeader.titleView.setText(forum.title);
                    holderHeader.avatorView.setText(forum.avator);
                    if (forum.avatorPath.length()>500){
                        String pureBase64Encoded = forum.avatorPath.substring(forum.avatorPath.indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holderHeader.avatorImageView.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(mContext).load(forum.avatorPath).into(holderHeader.avatorImageView);
                    }
                }
                break;
            case CELL_TEXT:
                holderText.textView.setText(Html.fromHtml(list.get(position-1).get("value")));
                break;
            case CELL_IMAGE:
//                holderImage.imageview.setText(Html.fromHtml(ls[position]));
                Glide.with(mContext).load(list.get(position-1).get("value")).into(holderImage.imageview);
                break;

        }

        return convertView;
    }

    private class ViewHolderReComment{
        ImageView avatorImage;
        TextView avator;
        TextView comment;
        TextView commentTime;
    }

    private class ViewHolderComment{
        ImageView avatorImage;
        TextView avator;
        TextView comment;
        TextView commentTime;
        Button commentBtn;
        Button supportBtn;
    }

    private class ViewHolderDiver{

    }

    private class ViewHolderHeader{
        TextView titleView;
        TextView avatorView;
        ImageView avatorImageView;
        Button attentionBtn;
    }

    private class ViewHolderText{
        TextView textView;
    }

    private class ViewHolderImage{
        ImageView imageview;
    }
}
