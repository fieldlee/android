package cn.com.yqhome.instrumentapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.Map;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.BuildConfig;
import cn.com.yqhome.instrumentapp.ClipImageActivity;
import cn.com.yqhome.instrumentapp.FileUtil;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;
import cn.com.yqhome.instrumentapp.MainActivity;
import cn.com.yqhome.instrumentapp.R;
import cn.com.yqhome.instrumentapp.WebUtils;

/**
 * Created by depengli on 2017/9/8.
 */

public class SetupFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{
    private String TAG = "SetupFragment";
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;

    private TextView  avatorView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppBarLayout appbarLayout;
    private SetupInfoFragment setupInfoFragment = new SetupInfoFragment();
    private CollectionFragment collectionFragment = new CollectionFragment();
    private TabsAdapter tabsAdapter;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像
    private ImageView mProfileImage;
    //调用照相机返回图片文件
    private File tempFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarShow(false);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_fragment,container,false);
        initView(v);
        return  v;
    }

    public void initView(View v){
        tabLayout = (TabLayout) v.findViewById(R.id.materialup_tabs);
        viewPager  = (ViewPager) v.findViewById(R.id.materialup_viewpager);
        appbarLayout = (AppBarLayout) v.findViewById(R.id.materialup_appbar);
        mProfileImage = (ImageView) v.findViewById(R.id.setup_info_profile_image);
        avatorView = (TextView) v.findViewById(R.id.setup_info_avator);

        if (BaseUtils.getUser(getActivity()) != null){
            if (BaseUtils.getUser(getActivity()).get("name") != null && BaseUtils.getUser(getActivity()).get("name").toString() != ""){
                avatorView.setText(BaseUtils.getUser(getActivity()).get("name").toString());
            }else{
                if (BaseUtils.getUser(getActivity()).get("id") != null && BaseUtils.getUser(getActivity()).get("id").toString() != ""){
                    avatorView.setText(BaseUtils.getUser(getActivity()).get("id").toString());
                }else{
                    avatorView.setText("未登录");
                }
            }
        }
        else{
            avatorView.setText("未登录");
        }

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseUtils.isLogin(getActivity())){
                    uploadHeadImage();
                }
            }
        });
//        取头像信息
        if (BaseUtils.getUser(getActivity()) != null){
            Map userObj = BaseUtils.getUser(getActivity());
            if (userObj.get("avatorImage") != null && userObj.get("avatorImage").toString() != ""){
                if (userObj.get("avatorImage").toString().length()>500){
                    String pureBase64Encoded = BaseUtils.getUser(getActivity()).get("avatorImage").toString().substring(BaseUtils.getUser(getActivity()).get("avatorImage").toString().indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    mProfileImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(getActivity()).
                            load(BaseUtils.getUser(getActivity()).get("avatorImage").toString()).
                            diskCacheStrategy(DiskCacheStrategy.RESULT).
                            thumbnail(0.5f).
                            placeholder(R.drawable.material_flat).
                            priority(Priority.LOW).
                            error(R.drawable.material_flat).
                            fallback(R.drawable.material_flat).
                            into(mProfileImage);
//                    Glide.with(getActivity()).load(BaseUtils.getUser(getActivity()).get("avatorImage").toString()).into(mProfileImage);
                }
            }
        }

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        tabsAdapter = new TabsAdapter(((MainActivity)getActivity()).getSupportFragmentManager(),setupInfoFragment,collectionFragment);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewPager.setAdapter(tabsAdapter);

        Log.i(TAG,TAG+"onStart+"+viewPager.getCurrentItem());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,TAG+"onResume+"+viewPager.getCurrentItem());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,TAG+"onPause+"+viewPager.getCurrentItem());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,TAG+"onStop+"+viewPager.getCurrentItem());
        viewPager.setAdapter(null);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明

        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == getActivity().RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == getActivity().RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = FileUtil.getRealFilePathFromUri(getActivity(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    mProfileImage.setImageBitmap(bitMap);

//                    update avatorimage
                    BaseUtils.saveUserImage(getActivity(),"data:image/jpeg;base64,"+BaseUtils.bitmapToBase64(bitMap));
//                    update avator image backend
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("data","data:image/jpeg;base64,"+BaseUtils.bitmapToBase64(bitMap));
                    WebUtils.updateAvatorImage(getActivity(),BaseUtils.getUser(getActivity()).get("id").toString(),requestParams,new CallbackListener(){
                        @Override
                        public void updateAvatorImageCallback() {

                        }
                    });
                }
                break;
        }
    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipImageActivity.class);
        intent.putExtra("type", 2);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }



    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;
        private SetupInfoFragment setupInfoFragment;
        private CollectionFragment collectionFragment;
        TabsAdapter(FragmentManager fm,SetupInfoFragment setupInfoFragment,CollectionFragment collectionFragment) {
            super(fm);
            this.setupInfoFragment = setupInfoFragment;
            this.collectionFragment = collectionFragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            if (i==0){
                return this.setupInfoFragment;
            }
            return this.collectionFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0){
                return "信息设置";
            }else if (position==1){
                return "关注";
            }else{
                return "关注";
            }

        }
    }
}
