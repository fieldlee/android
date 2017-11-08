package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.Class.Ads;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by depengli on 2017/10/25.
 */

public class WebUtils {
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void Login(String username, String password, final CallbackListener callbackListener){
        RequestParams params = new RequestParams();
        params.add("username",username);
        params.add("password",BaseUtils.md5(password));
        client.post(BaseUtils.ROOTURL + BaseUtils.LoginURL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONObject userObject = response.getJSONObject("data");
                        String token = response.getString("token");
                        // add token
                        userObject.put("token",token);
                        userObject.put("success",true);
                        callbackListener.loginCallback(userObject);
                    }else{
                        String message = response.getString("message");
                        JSONObject userObject = new JSONObject();
                        userObject.put("message",message);
                        userObject.put("success",false);
                        callbackListener.loginCallback(userObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                JSONObject userObject = new JSONObject();
                try {
                    userObject.put("message","登录失败，请重试");
                    userObject.put("success",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callbackListener.loginCallback(userObject);
            }
        });
    }

    public static void Register(RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL+BaseUtils.RegisterURL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONObject userObject = response.getJSONObject("data");
                        String token = response.getString("token");
                        // add token
                        userObject.put("token",token);
                        userObject.put("success",true);
                        callbackListener.registerCallbase(userObject);

                    }else{
                        String message = response.getString("message");
                        JSONObject userObject = new JSONObject();
                        userObject.put("message",message);
                        userObject.put("success",false);
                        callbackListener.registerCallbase(userObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObject userObject = new JSONObject();
                try {
                    userObject.put("message","登录失败，请重试");
                    userObject.put("success",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callbackListener.registerCallbase(userObject);
            }
        });
    }

    public static void Forget(RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL+BaseUtils.ForgetURL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        String message = response.getString("message");
                        JSONObject userObject = new JSONObject();
                        userObject.put("message",message);
                        userObject.put("success",true);
                        callbackListener.forgetCallback(userObject);

                    }else{
                        String message = response.getString("message");
                        JSONObject userObject = new JSONObject();
                        userObject.put("message",message);
                        userObject.put("success",false);
                        callbackListener.forgetCallback(userObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObject userObject = new JSONObject();
                try {
                    userObject.put("message","修改密码失败，请重试");
                    userObject.put("success",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callbackListener.forgetCallback(userObject);
            }
        });
    }

    public static void Ads(final Context context, RequestParams params, final  CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL+BaseUtils.ADSURL,params,new JsonHttpResponseHandler(){
            DatabaseHelper dHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dHelper.getWritableDatabase();
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {


                    boolean status = response.getBoolean("success");
                    if (status){
                        List<Ads> adslist = new ArrayList<Ads>();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i <results.length() ; i++) {
                            JSONObject jsonobj = results.getJSONObject(i);
                            Ads ads = new Ads(jsonobj);
                            Ads.insertContentValue(db,ads);
                            adslist.add(ads);
                        }
                        callbackListener.adsCallback(adslist);

                    }else{
//                        List<Ads> adslist = Ads.getAllAdsFromDB(db);
//                        callbackListener.adsCallback(adslist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                List<Ads> adslist = Ads.getAllAdsFromDB(db);
//                callbackListener.adsCallback(adslist);
            }
        });
    }

    public static void News(final Activity activity,RequestParams params,final  CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL+BaseUtils.NEWSURL+BaseUtils.getReqTime(activity,BaseUtils.REQ_NEWS),params,new JsonHttpResponseHandler(){
            DatabaseHelper dHelper = new DatabaseHelper(activity.getBaseContext());
            SQLiteDatabase db = dHelper.getWritableDatabase();
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        List<News> newslist = new ArrayList<News>();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i <results.length() ; i++) {
                            JSONObject jsonobj = results.getJSONObject(i);
                            News news = new News(jsonobj);
                            News.insertContentValue(db,news);
                            newslist.add(news);
                        }
                        //save time
                        BaseUtils.saveReqTime(activity,BaseUtils.REQ_NEWS,System.currentTimeMillis());
                        callbackListener.newsCallback(newslist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                List<News> newslist = News.getAllNewsFromDB(db);
//                callbackListener.newsCallback(newslist);
            }
        });
    }

    public static void Forum(final Activity activity, RequestParams params, final String type, final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL+BaseUtils.FORMUURL+type+"/"+BaseUtils.getReqTime(activity,BaseUtils.REQ_FORUM+type),params,new JsonHttpResponseHandler(){
            DatabaseHelper dHelper = new DatabaseHelper(activity.getBaseContext());
            SQLiteDatabase db = dHelper.getWritableDatabase();
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        List<Forum> forumList = new ArrayList<Forum>();
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i <results.length() ; i++) {
                            JSONObject jsonobj = results.getJSONObject(i);
                            Forum forum = new Forum(jsonobj);
                            Forum.insertContentValue(db,forum);
                            forumList.add(forum);
                        }
                        //save time
                        BaseUtils.saveReqTime(activity,BaseUtils.REQ_FORUM+type,System.currentTimeMillis());
                        callbackListener.forumsCallback(forumList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void Live(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.LIVEURL),params,new JsonHttpResponseHandler(){
            DatabaseHelper dHelper = new DatabaseHelper(activity.getBaseContext());
            SQLiteDatabase db = dHelper.getWritableDatabase();
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONArray objs = response.getJSONArray("results");
                        List<Live> tmpList = new ArrayList<Live>();
                        for (int i = 0; i <objs.length() ; i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            Live live = new Live(obj);
                            tmpList.add(live);
                            long insertLong = Live.insertContentValue(db,live);
                        }
                        callbackListener.livesCallback(tmpList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                List<News> newslist = News.getAllNewsFromDB(db);
//                callbackListener.newsCallback(newslist);
            }
        });
    }

    public static void Learn(final Activity activity, RequestParams params, final String type, final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.LEARNURL)+type+"/"+BaseUtils.getReqTime(activity,BaseUtils.REQ_LEARN+type),params,new JsonHttpResponseHandler(){
            DatabaseHelper dHelper = new DatabaseHelper(activity.getBaseContext());
            SQLiteDatabase db = dHelper.getWritableDatabase();
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONArray objs = response.getJSONArray("results");
                        List<Learn> tmpList = new ArrayList<Learn>();
                        for (int i = 0; i <objs.length() ; i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            Learn learn = new Learn(obj);
                            tmpList.add(learn);
                            Learn.insertContentValue(db,learn);
                        }
                        callbackListener.learnCallback(tmpList);
                        BaseUtils.saveReqTime(activity,BaseUtils.REQ_LEARN+type,System.currentTimeMillis());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void Comment(final Activity activity,RequestParams params,String pid,final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.COMMENTURL).concat(pid),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONArray objs = response.getJSONArray("results");
                        List<JSONObject> tmpCommentsList = new ArrayList<JSONObject>();
                        for (int i = 0; i <objs.length() ; i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            tmpCommentsList.add(obj);
                        }
                        callbackListener.commentCallback(tmpCommentsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void WriteComment(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL.concat(BaseUtils.COMMENTURL),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        callbackListener.commentWriteCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void WriteLearnComment(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL.concat(BaseUtils.LearnWriteCommentURL),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        callbackListener.commentWriteCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void SupportLearn(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL.concat(BaseUtils.LearnSupportCommentURL),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        callbackListener.commentSupportCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void SupportComment(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL.concat(BaseUtils.COMMENTSupportURL),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        callbackListener.commentSupportCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void Collect(final Activity activity,RequestParams params,final CallbackListener callbackListener){
        client.post(BaseUtils.ROOTURL.concat(BaseUtils.COLLECTURL),params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        callbackListener.collectCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void getCollections(Activity activity, String username, final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.GETCOLLECTURL).concat(username), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONArray objs = response.getJSONArray("results");
                        List<JSONObject> tmpCommentsList = new ArrayList<JSONObject>();
                        for (int i = 0; i <objs.length() ; i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            tmpCommentsList.add(obj);
                        }
                        callbackListener.getCollectionsCallback(tmpCommentsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }


    public static void getNewsByID(String id, final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.NEWSByIdURL).concat(id),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONObject newsJson = response.getJSONObject("data");
                        News news = new  News(newsJson);
                        callbackListener.getNewCallback(news);
                    }else{
                        callbackListener.getNewNoCallback(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void getForumByID(String id,final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.FORUMByIdURL).concat(id),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONObject forumJson = response.getJSONObject("data");
                        Forum forum = new Forum(forumJson);
                        callbackListener.getForumCallback(forum);
                    }else{
                        callbackListener.getNewNoCallback(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public static void getLearnComment(Activity activity,String id,final CallbackListener callbackListener){
        client.get(BaseUtils.ROOTURL.concat(BaseUtils.LearnCommentByIDURL).concat(id),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    boolean status = response.getBoolean("success");
                    if (status){
                        JSONArray objs = response.getJSONArray("results");
                        List<JSONObject> tmpCommentsList = new ArrayList<JSONObject>();
                        for (int i = 0; i <objs.length() ; i++) {
                            JSONObject obj = objs.getJSONObject(i);
                            tmpCommentsList.add(obj);
                        }
                        callbackListener.getScoreCommentsCallback(tmpCommentsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

}


