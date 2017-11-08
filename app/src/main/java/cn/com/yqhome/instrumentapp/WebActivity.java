package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by depengli on 2017/11/6.
 */

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private String loadUrl;
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
        loadUrl = getIntent().getStringExtra(BaseUtils.INTENT_LOADURL);

        setContentView(R.layout.activity_web);
        webView = (WebView)findViewById(R.id.web_activity_page);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if (loadUrl != null){
            Log.i("WebActivity",this.loadUrl.toString());
            if (loadUrl.indexOf("http")>=0){
                webView.loadUrl(this.loadUrl);
            }
            else{
                webView.loadUrl("http://".concat(this.loadUrl) );
            }
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.i("WebActivity2",url);
            }
        });
    }
}
