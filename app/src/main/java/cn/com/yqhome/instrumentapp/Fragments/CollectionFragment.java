package cn.com.yqhome.instrumentapp.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/11/6.
 */

public class CollectionFragment extends Fragment {
    private ListView listView;
    private ColArrayAdapter arrayAdapter;
    private List<JSONObject> tmpList;
    private String[] titles = new String[]{"我的资料2","是否接受通知2","我的余额2"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_fragment_info,container,false);
        initView(v);
        return v;
    }

    public void initView(View view){
        listView = (ListView)view.findViewById(R.id.setup_info_listview);
        tmpList = new ArrayList<>();
        JSONObject obj = new JSONObject();
//JSONObject        try {
//            obj.put("title","test");
//            obj.put("collectTime","5分钟前");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        tmpList.add(obj);


        arrayAdapter = new ColArrayAdapter(getContext(),tmpList);
        listView.setAdapter(arrayAdapter);
//        item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        WebUtils.getCollections(getActivity(), (String) BaseUtils.getUser(getActivity()).get("id"),new CallbackListener(){
            @Override
            public void getCollectionsCallback(List<JSONObject> collectionslist) {
                arrayAdapter.setmCollections(collectionslist);
            }
        });
    }

    private class ColArrayAdapter extends ArrayAdapter<JSONObject> {

        private List<JSONObject> mCollections;

        public void setmCollections(List<JSONObject> mCollections) {
            this.mCollections = mCollections;
        }

        public ColArrayAdapter(Context context, List<JSONObject> objects) {
            super(context, 0, objects);
            mCollections = objects;
        }

        @Nullable
        @Override
        public JSONObject getItem(int position) {
            return mCollections.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            JSONObject tmpObj = this.mCollections.get(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.collection_cell_item, null);
            TextView titleView = (TextView) convertView.findViewById(R.id.collection_cell_title);
            ImageView avatorImage = (ImageView) convertView.findViewById(R.id.collection_cell_avatorImage);
            TextView timeView = (TextView) convertView.findViewById(R.id.collection_cell_coltime);

            try {
                titleView.setText(tmpObj.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                timeView.setText("收藏时间："+tmpObj.getString("collectTime"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (tmpObj.getString("avatorPath").length()>500){

                    String pureBase64Encoded = tmpObj.getString("avatorPath").substring(tmpObj.getString("avatorPath").indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(getContext()).load(tmpObj.getString("avatorPath")).into(avatorImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }

}
