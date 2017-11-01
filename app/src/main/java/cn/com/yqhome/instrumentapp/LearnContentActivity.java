package cn.com.yqhome.instrumentapp;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.Class.Learn;

public class LearnContentActivity extends AppCompatActivity {
    private static String TAG = "LearnContentActivity";
    int windowWidth;
    private ViewPager viewPager;
    private List<ImageView> dotList;
    private LinearLayout dotLayout;
    int currentItem;
//  时间轴
    private LinearLayout palyContrainer;
    private String mediaDuration;
    private SeekBar timeBar;
    private TextView playerTime;
    private ImageButton play;
    private boolean isplaying = false;
    private boolean kill = false;
    private boolean prepared = false;


    private ProgressDialog pDialog;
    private MediaPlayer mediaPlayer;
    private Learn learn;
    private LearnContentActivity mLearnContentActivity;
//  坐标
    private int pTop;
    private int pLeft;
    private int pHeight;
    private int pWidth;
    private float scaleHeight;
    private float scaleWidth;

//    bpt
    private View playLayer;
    private String bpt;
    private List<JSONObject> pt;
    private List<JSONObject> bt;
    private List<JSONObject> bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
            getSupportActionBar().setTitle("返回");
        }
//      get learn info
        if (getIntent().getSerializableExtra(BaseUtils.INTENT_LEARN) != null){
            learn = (Learn) getIntent().getSerializableExtra(BaseUtils.INTENT_LEARN);
            if (learn.files == null || learn.files.size() == 0){
                Toast toast=Toast.makeText(this, "乐谱有误请重试", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                finish();
            }
//            bpt
            if (learn.bpt != null && !learn.bpt.isEmpty()){
                bpt = learn.bpt;
                String[] bptlist = bpt.split(";");

                pt = new ArrayList<>();
                bt = new ArrayList<>();
                bp = new ArrayList<>();

                for (int i = 0; i < bptlist.length ; i++) {
                    if (bptlist[i].indexOf("pt")>=0 && bptlist[i].indexOf("{")>=0 && bptlist[i].indexOf("}")>=0){
                        // 是pt
                        String jsonString = bptlist[i].substring(bptlist[i].indexOf("=")+1);
                        try {
                            JSONObject tmpBp = new JSONObject(jsonString);
                            pt.add(tmpBp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (bptlist[i].indexOf("bt")>=0 && bptlist[i].indexOf("{")>=0 && bptlist[i].indexOf("}")>=0){
                        // bt
                        String jsonString = bptlist[i].substring(bptlist[i].indexOf("=")+1);
                        try {
                            JSONObject tmpBt = new JSONObject(jsonString);
                            bt.add(tmpBt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (bptlist[i].indexOf("bp")>=0 && bptlist[i].indexOf("{")>=0 && bptlist[i].indexOf("}")>=0){
                        // bp
                        String jsonString = bptlist[i].substring(bptlist[i].indexOf("=")+1);
                        try {
                            JSONObject tmpBP = new JSONObject(jsonString);
                            bp.add(tmpBP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else{
            Toast toast=Toast.makeText(this, "乐谱有误请重试", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            finish();
        }

        setContentView(R.layout.activity_learn_content);

//        init view
        viewPager = (ViewPager) findViewById(R.id.learn_content_viewpager);



        dotLayout = (LinearLayout)findViewById(R.id.learn_content_dot);

        palyContrainer = (LinearLayout)findViewById(R.id.learn_content_playercontainer);
        play = (ImageButton) findViewById(R.id.content_learn_play);
        timeBar = (SeekBar) findViewById(R.id.content_learn_timeBar);
        playerTime = (TextView) findViewById(R.id.content_learn_playTime);

        playLayer = (View) findViewById(R.id.learn_content_layer);
        playLayer.setVisibility(View.GONE);
//
        if (learn.mp3 != null && learn.mp3.get("path")!= null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    isplaying = true;
                    play.setImageResource(R.drawable.pause);
                    startRunnable();
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                http://vprbbc.streamguys.net/vprbbc24.mp3
                    if (isplaying == false ){
                        if (prepared){
                            mediaPlayer.start();
                            isplaying = true;
                            startRunnable();
                            play.setImageResource(R.drawable.pause);
                        }
                    }else{
                        mediaPlayer.pause();
                        isplaying = false;
                        stopRunnable();
                        play.setImageResource(R.drawable.play);
                    }
                }
            });
        }
        else{
            palyContrainer.setVisibility(View.GONE);
        }


//        dialog
        pDialog = new ProgressDialog(LearnContentActivity.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setTitle("加载中...");//设置标题
        pDialog.setIndeterminate(false);//设置进度条是否为不明确


        dotList = new ArrayList<ImageView>();
        //        获得屏幕的宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowWidth = displayMetrics.widthPixels;

        try{
            for (int i = 0; i < learn.files.size(); i++) {
                ImageView dotView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16,16);
                params.leftMargin = 4;
                params.rightMargin = 4;
                dotLayout.addView(dotView,params);
                dotList.add(dotView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i <dotList.size() ; i++) {
            if (i==0){
                dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
            }else{
                dotList.get(i).setBackgroundResource(R.drawable.dot_blur);
            }
        }
//
        viewPager.setFocusable(true);
        viewPager.setAdapter(new GalleryAdapter());
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                int index = position ;
                currentItem = index; //修改当前时间
                for (int i = 0; i < dotList.size() ; i++) {
                    if (i == index){
                        dotList.get(i).setBackgroundResource(R.drawable.dot_focus);
                    }else{
                        dotList.get(i).setBackgroundResource(R.drawable.dot_blur);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int firstPage = 0;
        currentItem = firstPage;
        viewPager.setCurrentItem(firstPage, false);
        mLearnContentActivity = this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        pTop = viewPager.getTop();
        pLeft = viewPager.getLeft();

        pHeight = viewPager.getHeight();
        pWidth = viewPager.getWidth();
        scaleWidth = (float) ((float)pWidth / 650.0);
        scaleHeight = (float)((float)pHeight / 841.0);
    }

    private void playFollowTime(int curTime) throws JSONException {
        double preCurTime = (double) curTime / 1000.0;
        Log.i(TAG,"preCurTime:"+preCurTime);
        if (bp != null){
            playLayer.setVisibility(View.VISIBLE);//显示
            for (int i = 0; i <pt.size() ; i++) {
                if (i>=1){
                    if (preCurTime < pt.get(i).getInt("e") && preCurTime > pt.get(i-1).getInt("e")){
                        if (currentItem != pt.get(i).getInt("p")){
                            // 翻页
                            viewPager.setCurrentItem(pt.get(i).getInt("p")-1,true);
                        }
                    }
                }else{
                    if (preCurTime < pt.get(i).getInt("e")){
                        if (currentItem != 0){
                           // 翻页
                            viewPager.setCurrentItem(0,true);
                        }
                    }
                }
            }

            for (int i = 0; i < bt.size(); i++) {
                JSONObject curElement = bt.get(i);
                JSONObject preElement = null;
                if (i>0){
                    preElement = bt.get(i-1);
                }
                if ((i==0 && curElement.getInt("e")>preCurTime*10)||( preElement != null && preElement.getInt("e") < preCurTime * 10 && i>0)){
                    int bpint = (curElement.getInt("b")-1);
                    JSONObject element = bp.get(bpint);
                    float top = (element.getInt("t")*scaleHeight)+pTop;
                    float left = (element.getInt("l")*scaleWidth)+pLeft;
                    float width = (element.getInt("w")*scaleWidth);
                    float height = (element.getInt("h")*scaleHeight);

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) playLayer.getLayoutParams();
                    layoutParams.leftMargin = (int)left;
                    layoutParams.topMargin = (int) top;
                    layoutParams.height = (int) height;
                    layoutParams.width = (int) width;
                    playLayer.setLayoutParams(layoutParams);
                }else{
                    continue;
                }
            }
        }
    }

    private void playFollowPosition(float x,float y) throws JSONException {
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        y = y - contentTop;
        for (int i = 0; i < bp.size(); i++) {
            JSONObject element = bp.get(i);
            float top = (element.getInt("t")*scaleHeight)+pTop;
            float left = (element.getInt("l")*scaleWidth)+pLeft;
            float width = (element.getInt("w")*scaleWidth);
            float heigth = (element.getInt("h")*scaleHeight);

            if (x >= left && x<=(left+width) && y>=top && y<=(top+heigth)){
                for (int j=0;j<bt.size();j++){
                   JSONObject tmpJson = bt.get(j);
                   if ((i+1)==tmpJson.getInt("b")) {
                       isplaying = true;
                       if (j==0){
                           playFollowTime(0);
                           timeBar.setProgress(0);
                           mediaPlayer.seekTo(0);
                       }else{
                           playFollowTime(bt.get(j-1).getInt("e")*100+100);//多加100毫秒
                           timeBar.setProgress(bt.get(j-1).getInt("e")*100);
                           mediaPlayer.seekTo(bt.get(j-1).getInt("e")*100);
                       }
//                       开始播放音乐
                       mediaPlayer.start();
                       startRunnable();
                       play.setImageResource(R.drawable.pause);
                       break;
                   }
                }
                break;
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mediaPlayer != null){
            mediaPlayer.reset();
//        mediaPlayer = MediaPlayer.create(this,Uri.parse("http://downloads.bbc.co.uk/learningenglish/features/tews/170905_tews_it_beats_me_download.mp3"));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    prepared = true;
                    mediaDuration = timeFormat(mediaPlayer.getDuration());
                    pDialog.cancel();
                    setPlayerTitleText();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(TAG,"onCompletion=================");
                    stopRunnable();
                    mediaPlayer.stop();
                    //初始化timebar and time show
                    timeBar.setProgress(0);
                    playerTime.setText("0.00/"+mediaDuration);
                    play.setImageResource(R.drawable.play);
                }
            });

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.i(TAG,"percent"+percent);

                    timeBar.setSecondaryProgress((int)(percent * mediaPlayer.getDuration()/100));
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            try
            {
                if (learn.mp3 != null && learn.mp3.get("path")!=null){
                    if (learn.mp3.get("path").toString().indexOf(getResources().getString(R.string.host))>0){
                        mediaPlayer.setDataSource(learn.mp3.get("path").toString());
                    }else{
                        mediaPlayer.setDataSource(BaseUtils.ROOTURL + learn.mp3.get("path").toString());
                    }
                }

                mediaPlayer.prepareAsync();
                pDialog.show();
            }
            catch(IOException e)
            {
                pDialog.cancel();
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                pDialog.cancel();
                e.printStackTrace();
            }
            catch (IllegalStateException e)
            {
                pDialog.cancel();
                e.printStackTrace();
            }
        }


    }

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
            isplaying=false;
            stopRunnable();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            Log.i(TAG,"getX:"+ev.getX());
            Log.i(TAG,"getY:"+ev.getY());
            try {
                playFollowPosition(ev.getX(),ev.getY());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setPlayerTitleText() {
        setInitProgress();
        if (mediaDuration != null){
            playerTime.setText(timeFormat(mediaPlayer.getCurrentPosition())+"/"+mediaDuration);
        }else{
            playerTime.setText(timeFormat(mediaPlayer.getCurrentPosition())+"/"+timeFormat(mediaPlayer.getDuration()));
        }
    }

    private void setInitProgress(){
        int total = mediaPlayer.getDuration();
        int curTime = mediaPlayer.getCurrentPosition();
        timeBar.setMax(total);
        timeBar.setProgress(curTime);
        if (isplaying){
            try {
                playFollowTime(curTime); //修改layer
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private String timeFormat(int time){
        String formatString = "";
        int secs = time/1024;

        int hours = (int) Math.floor(secs/(60*60));
        int mins;
        if (hours>0){
            mins = (int) Math.floor((secs - hours * 60 * 60)/60);
        }else{
            mins = (int) Math.floor(secs/60);
        }

        int sec = (secs - hours * 60 * 60 - mins * 60) ;
        if (hours>0 && hours<10){
            formatString = "0"+hours;
        }
        if (hours>=10){
            formatString = ""+hours;
        }

        if (mins>0 && mins<10){
            if (formatString==""){
                formatString = "0"+mins;
            }else{
                formatString = formatString +":0"+mins;
            }
        }else{
            if (formatString==""){
                formatString = ""+mins;
            }else{
                formatString = formatString +":"+mins;
            }

        }

        if (sec>=0 && sec<10){
            if (formatString==""){
                formatString = "0"+sec;
            }else{
                formatString = formatString +":0"+sec;
            }
        }else{
            if (formatString==""){
                formatString = ""+sec;
            }else{
                formatString = formatString +":"+sec;
            }
        }
        return formatString;
    }
    /**
     * Handler for the runnable
     */
    private Handler handler = new Handler();
    /**
     * Runnable to manage the seek bar progression
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isplaying) {
                setPlayerTitleText();
            }
            if (!kill) {
                handler.postDelayed(this, 500);
            }
        }
    };

    /**
     * Start the seek bar manager runnable
     */
    public void startRunnable() {
        kill = false;
        runOnUiThread(runnable);
    }

    /**
     * Stop the seek bar manager runnable
     */
    public void stopRunnable() {
        kill = true;
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

    private class GalleryAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = innerPosition(position);

            ImageView imageview = new ImageView(mLearnContentActivity);
            imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageview);

            if (learn.files != null && learn.files.size()>0){
                if (learn.files.get(index).indexOf(getResources().getString(R.string.host))>0){
                    Glide.with(mLearnContentActivity).load(learn.files.get(index)).into(imageview);
                }else{
                    Glide.with(mLearnContentActivity).load(BaseUtils.ROOTURL + learn.files.get(index)).into(imageview);
                }
            }
            return imageview;
        }
        @Override
        public int getCount() {
            return learn.files.size();
        }
        private int innerPosition(int position) {
            return position;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
