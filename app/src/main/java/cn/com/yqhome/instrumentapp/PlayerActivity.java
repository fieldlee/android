package cn.com.yqhome.instrumentapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.Adapter.CommentAdapter;
import cn.com.yqhome.instrumentapp.Class.Comment;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.View.NoTouchLinearLayout;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class PlayerActivity extends AppCompatActivity {
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private TextView forumTitle;
    private TextView avator;
    private ImageView avatorImage;
    private Button attentionBtn;
    private ListView commentlistview;

    private LinearLayout replyLinearLayout;
    private NoTouchLinearLayout edit_vg_lyt;

    private TextView replyTextView;
    private ImageButton collectBtn;
    private ImageButton supportBtn;
    private ImageButton shareBtn;

    private EditText mCommentEdittext;
    private Button mSendBut;

    private List<Comment> listcomments;

    private Forum forum;
    private News news;
    private String urlVideo;
    private String urlThumb;
    private String titleVideo;

    private PlayerActivity mPlayerActivity;
    private CommentAdapter commentAdapter;
    private String commentPid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

//      get news or forum
        if (getIntent().getSerializableExtra(BaseUtils.INTENT_NEWS_VIDEO)!=null){
            news = (News)getIntent().getSerializableExtra(BaseUtils.INTENT_NEWS_VIDEO);
            if (news.videos != null && news.videos.length>0){
                urlVideo = news.videos[0];
            }
            if (news.images != null && news.images.length>0){
                urlThumb = news.images[0];
            }
            titleVideo = news.title;
        }
        if (getIntent().getSerializableExtra(BaseUtils.INTENT_FORUM_VIDEO)!=null){
            forum = (Forum)getIntent().getSerializableExtra(BaseUtils.INTENT_FORUM_VIDEO);
            if (forum.videos != null && forum.videos.length>0){
                urlVideo = forum.videos[0];
            }
            if (forum.images != null && forum.images.length>0){
                urlThumb = forum.images[0];
            }
            titleVideo = forum.title;
        }
        setContentView(R.layout.activity_player);

        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        if (urlVideo != null){
            jzVideoPlayerStandard.setUp(urlVideo
                    , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,titleVideo);
            if (urlThumb  != null){
                Uri uri = Uri.parse(urlThumb);
                jzVideoPlayerStandard.thumbImageView.setImageURI(uri);
            }
            //        播放视频
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    jzVideoPlayerStandard.startVideo();
                }
            },1000);
        }
        else{
            Toast toast=Toast.makeText(this, "该视频地址有误，请重试", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            finish();
        }

//      forum title
        forumTitle = (TextView)findViewById(R.id.content_palyer_title);
        avator = (TextView)findViewById(R.id.content_palyer_avator);
        avatorImage = (ImageView)findViewById(R.id.content_palyer_avatorImage);
        attentionBtn = (Button)findViewById(R.id.content_player_attend);

        if (news != null){
            forumTitle.setText(news.title);
            avator.setText(news.avator);
            if (news.avatorPath.length()>500){
                String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatorImage.setImageBitmap(decodedByte);
            }else{
                Glide.with(this).load(news.avatorPath).into(avatorImage);
            }
        }

        if (forum != null){
            forumTitle.setText(forum.title);
            avator.setText(forum.avator);
            if (forum.avatorPath.length()>500){
                String pureBase64Encoded = forum.avatorPath.substring(forum.avatorPath.indexOf(",")  + 1);
                byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatorImage.setImageBitmap(decodedByte);
            }else{
                Glide.with(this).load(forum.avatorPath).into(avatorImage);
            }
        }

//      listview
        commentlistview = (ListView)findViewById(R.id.content_comment_listview);
        commentlistview.setDividerHeight(1);
        commentlistview.setDivider(new ColorDrawable(getResources().getColor(R.color.dividerColor)));
//      comment List View
        commentAdapter = new CommentAdapter(this,this);

        commentAdapter.commentlistener = new CommentAdapter.OnCommentClickListener(){
            @Override
            public void onCommentClick(String id) {
                commentPid = id;
                edit_vg_lyt.setVisibility(View.VISIBLE);
                replyLinearLayout.setVisibility(View.GONE);
                onFocusChange(true);
            }
        };

        commentAdapter.supportlistener = new CommentAdapter.OnSupportClickListener(){
            @Override
            public void onSupportClick(String id) {
                RequestParams params = new RequestParams();
                params.add("id",id);
                WebUtils.SupportComment(mPlayerActivity,params, new CallbackListener(){
                    @Override
                    public void commentSupportCallback() {
                        commentAdapter.reloadDatas();
                    }
                });
            }
        };
        if (news != null){
            commentAdapter.setNews(news);
        }
        if (forum != null){
            commentAdapter.setForum(forum);
        }
        commentlistview.setAdapter(commentAdapter);

//        回复评论按钮
        replyTextView = (TextView) findViewById(R.id.content_palyer_text);
        collectBtn = (ImageButton)findViewById(R.id.content_palyer_collection);
        supportBtn = (ImageButton)findViewById(R.id.content_palyer_support);
        shareBtn = (ImageButton)findViewById(R.id.content_palyer_share);

        collectBtn.setImageResource(R.drawable.ic_music_note_white_24dp);
        shareBtn.setImageResource(R.drawable.ic_favorite_white_24dp);

        replyLinearLayout = (LinearLayout)findViewById(R.id.reply_bar);
        edit_vg_lyt = (NoTouchLinearLayout)findViewById(R.id.edit_vg_lyt);
        edit_vg_lyt.setOnResizeListener(new NoTouchLinearLayout.OnResizeListener() {
            @Override
            public void OnResize() {
                //判断控件是否显示
                if (edit_vg_lyt.getVisibility() == View.VISIBLE) {
                    edit_vg_lyt.setVisibility(View.GONE);
                    replyLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mCommentEdittext = (EditText) findViewById(R.id.edit_comment);
        mSendBut = (Button) findViewById(R.id.but_comment_send);
//        下方回复评论的输入框
        replyTextView.setOnClickListener(new ClickListener());
//        评论界面发表按钮
        mSendBut.setOnClickListener(new ClickListener());


        mPlayerActivity = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("info", "landscape"); // 横屏
                }
            }, 500);
        }
        else if (this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("info", "portrait"); // 竖屏
                }
            },500);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }



    /**
     * 点击屏幕其他地方收起输入法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                onFocusChange(false);

            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏或者显示输入框
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            /**
             *这堆数值是算我的下边输入区域的布局的，
             * 规避点击输入区域也会隐藏输入区域
             */

            v.getLocationInWindow(leftTop);
            int left = leftTop[0] - 50;
            int top = leftTop[1] - 50;
            int bottom = top + v.getHeight() + 300;
            int right = left + v.getWidth() + 120;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 事件点击监听器
     */
    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮
                    if(mCommentEdittext.getText().equals("")){
                        Toast toast=Toast.makeText(mPlayerActivity, "请输入评论内容", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    RequestParams params = new RequestParams();
                    if (commentPid == null){
                        if (news != null){
                            params.add("parentId",news.id);
                        }
                        if (forum != null){
                            params.add("parentId",forum.id);
                        }
                    }
                    else{
                        params.add("parentId",commentPid);
                    }
                    params.add("content", String.valueOf(mCommentEdittext.getText()));


                    if (BaseUtils.getUser(PlayerActivity.this).get("id") != null){
                        params.add("author",BaseUtils.getUser(PlayerActivity.this).get("id").toString());
                    }
                    else{
                        params.add("author","18616017950");
                    }


                    WebUtils.WriteComment(mPlayerActivity,params,new CallbackListener(){
                        @Override
                        public void commentWriteCallback() {
                            mCommentEdittext.setText("");
                            commentAdapter.reloadDatas();
                        }
                    });
                    replyLinearLayout.setVisibility(View.VISIBLE);
                    edit_vg_lyt.setVisibility(View.GONE);
                    onFocusChange(false);
                    break;
                case R.id.content_palyer_text:        //底部评论按钮
//                    isReply = false;
                    edit_vg_lyt.setVisibility(View.VISIBLE);
                    replyLinearLayout.setVisibility(View.GONE);
                    onFocusChange(true);
                    break;
                case R.id.content_palyer_support:
                    RequestParams paramsSupport = new RequestParams();
                    if (news != null){
                        paramsSupport.add("id",news.id);
                    }
                    if (forum != null){
                        paramsSupport.add("id",forum.id);
                    }

                    WebUtils.SupportComment(PlayerActivity.this,paramsSupport,new CallbackListener(){
                        @Override
                        public void commentSupportCallback() {
                            Toast toast=Toast.makeText(PlayerActivity.this, "您的赞，已收到", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                    });
                    break;
                case R.id.content_palyer_collection:
//                        "avatorPath": this.news.avatorPath,
//                        "avator": this.news.avator,
//                        "newsId": this.news._id,
//                        "author": this.news.author,
//                        "title": this.news.title,
//                        "username": window.localStorage.getItem("username")
                    RequestParams paramsNews = new RequestParams();
                    if (news != null){

                        paramsNews.put("avatorPath",news.avatorPath);
                        paramsNews.put("avator",news.avator);
                        paramsNews.put("newsId",news.id);
                        paramsNews.put("author",news.author);
                        paramsNews.put("title",news.title);
                        paramsNews.put("username",BaseUtils.getUser(PlayerActivity.this).get("id").toString());

                    }
                    if (forum != null){

                        paramsNews.put("avatorPath",forum.avatorPath);
                        paramsNews.put("avator",forum.avator);
                        paramsNews.put("forumId",forum.id);
                        paramsNews.put("author",forum.author);
                        paramsNews.put("title",forum.title);
                        paramsNews.put("username",BaseUtils.getUser(PlayerActivity.this).get("id").toString());
                    }

                    WebUtils.Collect(PlayerActivity.this,paramsNews,new CallbackListener(){
                        @Override
                        public void collectCallback() {
                            Toast toast=Toast.makeText(PlayerActivity.this, "已为您收藏该帖子", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                    });
                    break;
            }
        }
    }
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        if (isFocus == false){
            commentPid = null;
        }else{
            if (!BaseUtils.isLogin(mPlayerActivity)){
                return;
            }
        }

        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                    replyLinearLayout.setVisibility(View.VISIBLE);
                    edit_vg_lyt.setVisibility(View.GONE);
                }
            }
        }, 100);
    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}
