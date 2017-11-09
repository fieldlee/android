package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.webRtc.PeerConnectionParameters;
import cn.com.yqhome.instrumentapp.webRtc.WebRtcClient;

/**
 * Created by depengli on 2017/10/15.
 */

public class LiveActivity extends Activity implements WebRtcClient.RtcListener{

    private final static String TAG = "LiveActivity";
    private final static int VIDEO_CALL_SENT = 666;
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
    private GLSurfaceView vsv;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;
    private WebRtcClient client;
    private String mSocketAddress;
    private String callerId ;

    private Live live;

    private Button backBtn;
    private Button supportBtn;
    private ListView listView;
    private List<JSONObject> listChat = new ArrayList<>();
    private LiveArrayAdapter liveArrayAdapter;
    private boolean hadPresent = false;
    private String currentCallId ;
    private  Runnable updateThread = new Runnable()
    {
        @Override
        public void run()
        {
            liveArrayAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_live);
//      获得live
        live = (Live) getIntent().getSerializableExtra(BaseUtils.INTENT_Live);


//        资源get
        backBtn = (Button)findViewById(R.id.live_btn_back);
        backBtn.getBackground().setAlpha(50);
        backBtn.setTextColor(R.color.white);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        supportBtn = (Button)findViewById(R.id.live_btn_support);
        supportBtn.getBackground().setAlpha(50);
        supportBtn.setTextColor(R.color.white);
        supportBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (currentCallId != null){
                    JSONObject msgObj = new JSONObject();
                    if (BaseUtils.getUser(LiveActivity.this)!=null && BaseUtils.getUser(LiveActivity.this).get("id") != null){
                        Map<String,Object> tmpmap = BaseUtils.getUser(LiveActivity.this);
                        try {
                            if (tmpmap.get("name").toString() == ""){
                                msgObj.put("avator","匿名");
                            }else{
                                msgObj.put("avator",tmpmap.get("name").toString());
                            }

                            msgObj.put("text","掌声");
                            msgObj.put("avatorImage",tmpmap.get("avatorImage").toString());
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.camera20);
                            msgObj.put("image","data:image/jpeg;base64,"+BaseUtils.bitmapToBase64(bmp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        try {
                            msgObj.put("avator","匿名");
                            msgObj.put("text","掌声");
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.camera20);
                            msgObj.put("image","data:image/jpeg;base64,"+BaseUtils.bitmapToBase64(bmp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        client.sendMsgMessage(currentCallId,"阿毛",client.roomid,msgObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        liveArrayAdapter = new LiveArrayAdapter(LiveActivity.this,listChat);
        listView = (ListView)findViewById(R.id.live_listview_chat);
        listView.getBackground().setAlpha(50);
        listView.setAdapter(liveArrayAdapter);


        mSocketAddress = "http://" + getResources().getString(R.string.host);
        mSocketAddress += (":" + getResources().getString(R.string.port) + "/");

        vsv = (GLSurfaceView) findViewById(R.id.glview_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

        // local and remote render
        remoteRender = VideoRendererGui.create(
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);
    }

    private void init() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        PeerConnectionParameters params = new PeerConnectionParameters(
                true, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);

        client = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext());
        client.roomid = live.id;
        client.isPresent = false;

    }

    @Override
    public void onPause() {
        super.onPause();
        vsv.onPause();
        if(client != null) {
            client.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        vsv.onResume();
        if(client != null) {
            client.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(client != null) {
            client.onDestroy();
        }
    }

    @Override
    public void onReceiveMessage(JSONObject msgObj) {
        Log.i("LiveActivity",msgObj.toString());
        //判断是否是需要公布的message
        if (msgObj.has("text") || msgObj.has("image")){
            listChat.add(msgObj);
            liveArrayAdapter.setmMessages(listChat);
            LiveActivity.this.runOnUiThread(updateThread); //更新adapter
        }

    }

    @Override
    public void onCallReady(String callId) {
        Log.i(TAG,"callId:"+callId);
        currentCallId = callId;
        try {
            client.joinRoom(client.roomid,callId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (callerId != null) {
//            try {
//                answer(callerId);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            没有主播client id
//            call(callId);
//        }
    }

    @Override
    public void onPresendId(String presendId) {
        Log.i(TAG,"presendId:"+presendId);
        callerId = presendId;
        if (callerId != null && !callerId.isEmpty()) {
            if (!hadPresent){
                hadPresent = true;
                try {
                    answer(callerId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            没有主播client id
//            call(callId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "主播还在化妆，请稍后...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void answer(String callerId) throws JSONException {

        JSONObject initObj = new JSONObject();
        if (BaseUtils.getUser(LiveActivity.this) != null){
            Map<String,Object> tmpmap = BaseUtils.getUser(LiveActivity.this);
            initObj.put("avatorImage",tmpmap.get("avatorImage").toString());
            initObj.put("avator",tmpmap.get("name").toString());
            initObj.put("text","进入直播间");
        }else{
            initObj.put("avator","匿名用户");
            initObj.put("text","进入直播间");
        }
        listChat.add(initObj);
        liveArrayAdapter.setmMessages(listChat);
        LiveActivity.this.runOnUiThread(updateThread); //更新adapter

        client.sendMessage(callerId,"阿毛", "init", initObj);
        if (client.isPresent){
            client.start(live.avator);
        }else{
            client.recieve(live.avator);
        }

    }

    @Override
    public void onStatusChanged(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), newStatus, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocalStream(MediaStream localStream) {
        Log.i(TAG,"local");
        if (client.isPresent){
            localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
            VideoRendererGui.update(localRender,
                    LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                    LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                    scalingType);
        }

    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        Log.i(TAG,"REMOTE:");
        if (!client.isPresent){
            remoteStream.videoTracks.get(0).addRenderer(new VideoRenderer(remoteRender));
            VideoRendererGui.update(remoteRender,
                    REMOTE_X, REMOTE_Y,
                    REMOTE_WIDTH, REMOTE_HEIGHT, scalingType);
//            VideoRendererGui.update(localRender,
//                    LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
//                    LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
//                    scalingType);
        }

    }

    @Override
    public void onRemoveRemoteStream(int endPoint) {
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType);
    }


    private class LiveArrayAdapter extends ArrayAdapter<JSONObject> {

        private List<JSONObject> mMessages;

        public void setmMessages(List<JSONObject> mMessages) {
            this.mMessages = mMessages;
        }

        public LiveArrayAdapter(Context context, List<JSONObject> objects) {
            super(context, 0, objects);
            mMessages = objects;
        }

        @Nullable
        @Override
        public JSONObject getItem(int position) {
            return mMessages.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            JSONObject tmpObj = this.mMessages.get(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_live_record, null);
            ImageView avatorImage = (ImageView) convertView.findViewById(R.id.cell_live_avatorimage);
            TextView avatorView = (TextView) convertView.findViewById(R.id.cell_live_avator);
            ImageView imagView = (ImageView) convertView.findViewById(R.id.cell_live_image);
            TextView textView = (TextView) convertView.findViewById(R.id.cell_live_text);
//            avatorImage
//            avator
//            text
//            image
            try {
                if (tmpObj.has("avatorImage")){
                    if (tmpObj.getString("avatorImage").length()>500){
                        String pureBase64Encoded = tmpObj.getString("avatorImage").substring(tmpObj.getString("avatorImage").indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        avatorImage.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(getContext()).
                                load(tmpObj.getString("avatorImage")).
                                diskCacheStrategy(DiskCacheStrategy.RESULT).
                                thumbnail(0.5f).
                                placeholder(R.drawable.user_avatar).
                                priority(Priority.LOW).
                                error(R.drawable.user_avatar).
                                fallback(R.drawable.user_avatar).
                                into(avatorImage);
//                            Glide.with(mContext).load(tmpObj.getString("avatorPath")).into(holderForum.avatorImage);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                avatorView.setText(tmpObj.getString("avator"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                textView.setText(tmpObj.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (tmpObj.has("image")){
                    imagView.setVisibility(View.VISIBLE);
                    if (tmpObj.getString("image").length()>500){
                        String pureBase64Encoded = tmpObj.getString("image").substring(tmpObj.getString("image").indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imagView.setImageBitmap(decodedByte);
                    }
                    else{
                        Glide.with(getContext()).
                                load(tmpObj.getString("image")).
                                diskCacheStrategy(DiskCacheStrategy.RESULT).
                                thumbnail(0.5f).
                                placeholder(R.drawable.user_avatar).
                                priority(Priority.LOW).
                                error(R.drawable.user_avatar).
                                fallback(R.drawable.user_avatar).
                                into(imagView);
//                            Glide.with(mContext).load(tmpObj.getString("avatorPath")).into(holderForum.avatorImage);
                    }
                }else{
                    imagView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
