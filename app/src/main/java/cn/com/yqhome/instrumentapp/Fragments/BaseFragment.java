package cn.com.yqhome.instrumentapp.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.MainActivity;

/**
 * Created by depengli on 2017/9/9.
 */

public class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;

    protected void  startLoading(String title,String message){
        if (progressDialog != null){
            progressDialog.dismiss();
        }else{
            progressDialog = ProgressDialog.show(getActivity(),title,message,true);
        }
    }

    protected void  endLoading(){
        new Thread(){
            @Override
            public void run() {
                try{
                    sleep(300);
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.run();
    }

    protected void pushFragment(BaseFragment fragment){
        ((MainActivity)getActivity()).pushFragment(fragment);
    }
    protected void popFragment() {
        ((MainActivity) getActivity()).popFragment(this);
    }
}

