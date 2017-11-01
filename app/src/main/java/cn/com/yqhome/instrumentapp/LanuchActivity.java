package cn.com.yqhome.instrumentapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class LanuchActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView textView;
    int start = 4;
    private boolean hadIntent = false;
    Handler LaunchHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    if (start > 0){
                        textView.setText("自动跳过   "+ start--);
                        this.sendEmptyMessageDelayed(2, 1000);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanuch);

        imageView = (ImageView) findViewById(R.id.lauch_im);
        textView = (TextView) findViewById(R.id.lauch_tx);
        textView.setOnClickListener(this);


        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.5f);
        animation.setDuration(5000);
        LaunchHandler.sendEmptyMessage(2);

        File dir = getFilesDir();
        final File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.mipmap.start);
        }
        imageView.startAnimation(animation);
/**
 * 给imageview加上动画监听，在动画结束之后执行跳转代码
 */
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
                return;
//                if (BaseUtils.isNetworkConnected(LanuchActivity.this)){
//                    BaseUtils.get(Constant.START, new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(new String(responseBody));
//                                String url = jsonObject.getString("img");
//                                BaseUtils.getImage(url, new BinaryHttpResponseHandler() {
//                                    @Override
//                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                        saveImage(imgFile, bytes);
//                                        startActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                        startActivity();
//                                    }
//                                });
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                        }
//                    });
//                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        startActivity();
    }
    private void startActivity() {
        if (hadIntent==false){
            hadIntent = true;
            Intent intent = new Intent(LanuchActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        }
    }

    public void saveImage(File file, byte[] bytes) {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
