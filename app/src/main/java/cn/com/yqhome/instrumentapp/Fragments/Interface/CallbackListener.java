package cn.com.yqhome.instrumentapp.Fragments.Interface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.com.yqhome.instrumentapp.Class.Ads;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.Class.News;

/**
 * Created by depengli on 2017/9/17.
 */

public class CallbackListener {

    public void loginCallback(JSONObject userObject){

    }

    public void forumsCallback(List<Forum> forumList){

    }

    public void livesCallback(List<Live> liveList){

    }

    public void registerCallbase(JSONObject userObject){

    }

    public void forgetCallback(JSONObject callObject){

    }

    public void adsCallback(List<Ads> adslist){

    }

    public void newsCallback(List<News> newslist){

    }

    public void learnCallback(List<Learn> learnslist){

    }

    public void commentCallback(List<JSONObject> commentslist){

    }

    public void commentWriteCallback(){

    }

    public void commentSupportCallback(){

    }

    public void collectCallback(){

    }
}
