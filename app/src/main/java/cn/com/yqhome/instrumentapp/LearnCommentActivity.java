package cn.com.yqhome.instrumentapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;

import cn.com.yqhome.instrumentapp.Adapter.ContentAdapter;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.View.NoTouchLinearLayout;

/**
 * Created by depengli on 2017/11/7.
 */

public class LearnCommentActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView contentListView;
    private LinearLayout replyLinearLayout;
    private NoTouchLinearLayout edit_vg_lyt;
    private TextView replyTextView;
    private ImageButton collectBtn;
    private ImageButton supportBtn;
    private ImageButton shareBtn;
    private EditText mCommentEdittext;
    private Button mSendBut;
    private String commentPid;
    private Learn learn;
    private static final String TAGKey = "LearnCommentActivity";

    private ContentAdapter contentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
            getSupportActionBar().setTitle("返回");
        }

        if (getIntent().getSerializableExtra(BaseUtils.INTENT_LEARN)!=null){
            learn = (Learn)getIntent().getSerializableExtra(BaseUtils.INTENT_LEARN);
        }

        setContentView(R.layout.activity_context);

        contentListView = (ListView)findViewById(R.id.content_listView);
//        commentListView = (ListView)findViewById(R.id.comment_listView);
        contentAdapter = new ContentAdapter(this,getParent());
        contentAdapter.setLearn(learn);
        contentAdapter.commentlistener = new ContentAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClick(String id) {
                commentPid = id;
                edit_vg_lyt.setVisibility(View.VISIBLE);
                replyLinearLayout.setVisibility(View.GONE);
                onFocusChange(true);
            }
        };

        contentAdapter.supportlistener = new ContentAdapter.OnSupportClickListener() {
            @Override
            public void onSupportClick(String id) {
                RequestParams params = new RequestParams();
                params.add("id",id);
                WebUtils.SupportLearn(LearnCommentActivity.this,params,new CallbackListener(){
                    @Override
                    public void commentSupportCallback() {
                        contentAdapter.reloadDatas();
                    }
                });
            }
        };

        contentListView.setAdapter(contentAdapter);
        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

//        回复评论按钮
        replyTextView = (TextView) findViewById(R.id.content_palyer_text);
        collectBtn = (ImageButton)findViewById(R.id.content_palyer_collection);
        supportBtn = (ImageButton)findViewById(R.id.content_palyer_support);
        shareBtn = (ImageButton)findViewById(R.id.content_palyer_share);

        collectBtn.setImageResource(R.drawable.ic_music_note_white_24dp);
        shareBtn.setImageResource(R.drawable.ic_favorite_white_24dp);

        replyLinearLayout = (LinearLayout)findViewById(R.id.content_reply_bar);
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
//        收藏
        collectBtn.setOnClickListener(new ClickListener());
//        支持
        supportBtn.setOnClickListener(new ClickListener());
//        分享
        shareBtn.setOnClickListener(new ClickListener());


        if (learn != null){
            // 打开就展开输入框
            edit_vg_lyt.setVisibility(View.VISIBLE);
            replyLinearLayout.setVisibility(View.GONE);
            onFocusChange(true);
        }
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

    @Override
    public void onClick(View v) {

    }

    /**
     * 事件点击监听器
     */
    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮

                    if (!BaseUtils.isLogin(LearnCommentActivity.this)){
                        return;
                    }

                    if (mCommentEdittext.getText().equals("")){
                        Toast toast=Toast.makeText(getParent(), "请输入评论内容", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                    RequestParams params = new RequestParams();

                    if (commentPid == null){
                        if (learn != null){
                            params.add("parentId",learn.id);
                        }
                    }else{
                        params.add("parentId",commentPid);
                    }

                    params.add("content", String.valueOf(mCommentEdittext.getText()));
                    if (BaseUtils.getUser(LearnCommentActivity.this).get("id") != null){
                        params.add("author",BaseUtils.getUser(LearnCommentActivity.this).get("id").toString());
                    }

                    WebUtils.WriteLearnComment(LearnCommentActivity.this,params,new CallbackListener(){
                        @Override
                        public void commentWriteCallback() {
                            mCommentEdittext.setText("");
                            contentAdapter.reloadDatas();
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
                    if (learn != null){
                        paramsSupport.add("id",learn.id);
                    }
                    WebUtils.SupportLearn(LearnCommentActivity.this,paramsSupport,new CallbackListener(){
                        @Override
                        public void commentSupportCallback() {
                            Toast toast=Toast.makeText(LearnCommentActivity.this, "您的赞，已收到", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                    });
                    break;
                case R.id.content_palyer_collection:
                    RequestParams paramsNews = new RequestParams();
                    if (learn != null){
//                        paramsNews.put("avatorPath",learn.avatorPath);
//                        paramsNews.parentId =   requestJson["parentId"];
//                        paramsNews.content =   requestJson["content"];
//                        paramsNews.author =   requestJson["author"];

                        paramsNews.put("parentId",learn.id);
                        paramsNews.put("author",BaseUtils.getUser(LearnCommentActivity.this).get("id").toString());

                    }

                    WebUtils.Collect(LearnCommentActivity.this,paramsNews,new CallbackListener(){
                        @Override
                        public void collectCallback() {
                            Toast toast=Toast.makeText(LearnCommentActivity.this, "已为您收藏该帖子", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;
                        }
                    });

                    break;
                case R.id.content_palyer_share:

                    ShareEntity testBean = new ShareEntity("我是标题", "我是内容，描述内容。");
                    testBean.setUrl("https://www.baidu.com"); //分享链接
                    testBean.setImgUrl("https://www.baidu.com/img/bd_logo1.png");

                    ShareUtil.showShareDialog(LearnCommentActivity.this, testBean, ShareConstant.REQUEST_CODE);
                    break;
            }
        }
    }
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;

        if (isFocus==false){
            commentPid = null; //收缩后清空pid
        }
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
//                    isEidtComment = true;
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
//                    isEidtComment = false;
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                    replyLinearLayout.setVisibility(View.VISIBLE);
                    edit_vg_lyt.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
