package cn.com.yqhome.instrumentapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.yqhome.instrumentapp.InfoActivity;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/10/2.
 */

public class SetupInfoFragment extends Fragment {
    private ListView listView;
    private String[] titles = new String[]{"我的资料","是否接受通知","我的余额"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_fragment_info,container,false);
        initView(v);
        return v;
    }

    public void initView(View view){
        listView = (ListView)view.findViewById(R.id.setup_info_listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.setup_cell_item,R.id.setup_info_itemname,titles);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent infoIntent = new Intent(getActivity(), InfoActivity.class);
                    startActivity(infoIntent);
                }
            }
        });
    }

}
