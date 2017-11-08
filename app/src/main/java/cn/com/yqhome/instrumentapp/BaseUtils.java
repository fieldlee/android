package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.R;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by depengli on 2017/9/12.
 */

public class BaseUtils {
    public static final int TYPE_LIST = 0;
    public static final int TYPE_GRID = 1;
    //"钢琴","萨克斯","小提琴","吉他","二胡","琵琶","其他"
    public static final  String TYPE_INSTRUMENT[] = {"钢琴","萨克斯","小提琴","吉他","二胡","琵琶"};
    public static final  String TYPE_LEARNINSTRUMENT[] =  {"钢琴","萨克斯","小提琴","吉他","大提琴","架子鼓"};


    public static final String REQ_NEWS = "REQ_NEWS";
    public static final String REQ_FORUM = "REQ_FORUM";
    public static final String REQ_LEARN = "REQ_LEARN";


    public static final String INTENT_FORUM_VIDEO = "INTENTFORUMVIDEO";
    public static final String INTENT_FORUM_CONTEXT = "INTENTFORUMCONTEXT";
    public static final String INTENT_NEWS_VIDEO = "INTENTNEWSVIDEO";
    public static final String INTENT_NEWS_CONTEXT = "INTENTNEWSCONTEXT";
    public static final String INTENT_Live = "INTENTLIVE";
    public static final String INTENT_LEARN = "INTENTLEARN";

    public static final String INTENT_LOADURL = "WebPageUrl";

    public static final String TYPE_ITEM_FORUM = "Forum";
    public static final String TYPE_ITEM_NEWS = "News";
    public static final String TYPE_ITEM_LEARN = "Learn";
    public static final String TYPE_ITEM_LIVE = "Live";

    public static final int CELL_HEADER = 0;  //说明是带有Header的
    public static final int CELL_FOOTER = 1;  //说明是带有Footer的
    public static final int CELL_NO_PICTURE = 2;
    public static final int CELL_ONE_PICTURE = 3;
    public static final int CELL_THREE_PICTURE = 4;
    public static final int CELL_VIDEO_PICTURE = 5;
    public static final int CELL_VIDEO_BIG_PICTURE = 6;

    public static final int CELL_LEARN_MP3 = 7;

    public static final int CELL_LIVE = 8;
//http://106.14.209.183:4200/api/web/forum/sub/recent

    public static final String ROOTURL = "http://192.168.1.100:3000/";

    public static final String LoginURL = "/api/auth/login";
    public static final String RegisterURL = "/api/auth/register";
    public static final String ForgetURL = "/api/auth/forget";
    public static final String ADSURL = "/api/web/ads";
    public static final String NEWSURL = "/api/web/news/mobile/";
    public static final String FORMUURL = "/api/web/forum/mobile/";
    public static final String LIVEURL = "/api/web/show";
    public static final String LEARNURL = "/api/web/score/mobile/";
    public static final String COMMENTURL = "/api/web/comment/";
    public static final String COLLECTURL = "/api/web/collect";
    public static final String GETCOLLECTURL = "/api/web/collect/";
    public static final String COMMENTSupportURL = "/api/web/comment/support";

    public static final String NEWSByIdURL = "/api/web/news/byid/";
    public static final String FORUMByIdURL = "/api/web/forum/sub/byid/";
    public static final String LearnCommentByIDURL = "/api/web/scorecomment/";
    public static final String LearnWriteCommentURL = "/api/web/scorecomment";
    public static final String LearnSupportCommentURL = "/api/web/scorecomment/support";


    public static String md5(String string) {
        byte[] hash = new byte[0];
        try{
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss Z");
        Date convertedDate = new Date();
        try{
            convertedDate = dateFormat.parse(dateString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static int getResByType(int type){
        switch (type){
            case CELL_NO_PICTURE:
                return R.layout.cell_no_picture;

            case CELL_ONE_PICTURE:
                return R.layout.cell_one_picture;

            case CELL_THREE_PICTURE:
                return R.layout.cell_three_pictures;

            case CELL_VIDEO_PICTURE:
                return R.layout.cell_video;

            case CELL_VIDEO_BIG_PICTURE:
                return R.layout.cell_video_big;

            default:
                return 0;

        }
    }

    public static List<String> getImgStr(String htmlStr){

        List<String> pics = new ArrayList<>();

        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
//        String regEx_img = "<img src=\"([^\"]+)";
        Log.i("ContentAdapter1",htmlStr);
        Matcher matcher = Pattern.compile(imgRegex).matcher(htmlStr.replace("\\",""));
        while (matcher.find()) {
            Log.i("ContentAdapter1",matcher.group(1));
            pics.add(matcher.group(1));
        }
        return pics;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void saveUserImage(Activity activity,String avatorImage){
        SharedPreferences.Editor sharedata = activity.getSharedPreferences("user",0).edit();
        sharedata.putString("avatorImage",avatorImage);
        sharedata.commit();
    }

    public static void saveUser(Activity activity,String avator,String id,boolean rememberMe,String password,String token){
        SharedPreferences.Editor sharedata = activity.getSharedPreferences("user",Context.MODE_PRIVATE).edit();
        sharedata.putString("name",avator);
        sharedata.putString("id",id);
        sharedata.putBoolean("remember",rememberMe);
        sharedata.putString("password",password);
        sharedata.putString("token",token);
        sharedata.commit();
    }

    public static Map<String,Object> getUser(Activity activity){
        Map<String,Object> objUser = new HashMap<String,Object>();
        SharedPreferences sharedData = activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        objUser.put("name",sharedData.getString("name","")) ;
        objUser.put("id",sharedData.getString("id","")) ;
        objUser.put("image",sharedData.getString("image","")) ;
        objUser.put("remember",sharedData.getBoolean("remember",false));
        objUser.put("password",sharedData.getString("password","")) ;
        objUser.put("token",sharedData.getString("token","")) ;
        return objUser;
    }

    public static void saveReqTime(Activity activity,String key,Long time){
        SharedPreferences.Editor sharedata = activity.getSharedPreferences("requestTime",0).edit();
        sharedata.putLong(key,time);
        sharedata.commit();
    }

    public static long getReqTime(Activity activity,String key){
        SharedPreferences sharedData = activity.getSharedPreferences("requestTime",0);
        return sharedData.getLong(key,0);
    }

    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        }else {
            return false;
        }
    }

    public static final class ComparatorValues implements Comparator<ContentValues>{

        @Override
        public int compare(ContentValues object1, ContentValues object2) {
            long m1=object1.getAsLong("issueTime");
            long m2=object2.getAsLong("issueTime");
            int result=0;
            if(m1>m2)
            {
                result=1;
            }
            if(m1<m2)
            {
                result=-1;
            }
            return result;
        }

    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
                toolbar.setBackgroundColor(activity.getResources().getColor(colorResId));

                //底部导航栏
//                window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLogin(Activity activity){
        Map<String,Object> loginUser = getUser(activity);
        if (loginUser.get("id") != null && loginUser.get("id") != ""){
            return true;
        }else{
            Intent intentLive = new Intent(activity,LoginActivity.class);
            activity.startActivity(intentLive);
            return  false;

        }
    }
}
