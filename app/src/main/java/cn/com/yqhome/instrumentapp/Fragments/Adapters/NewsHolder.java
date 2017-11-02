package cn.com.yqhome.instrumentapp.Fragments.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.News;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/10/29.
 */

public class NewsHolder extends RecyclerView.ViewHolder {


    private TextView nTitle;
    private ImageView nImageView;
    private TextView nNumPictures;
    private TextView nSubTitle;
    private ImageView avatorImage;


    private ImageView nImageView1;
    private ImageView nImageView2;
    private ImageView nImageView3;



    private TextView nVideoTime;
    private ImageView navatorImage;
    private ImageView nVideoBtnImage;


    private View containerView;
    public NewsHolder(View itemView,int type) {
        super(itemView);
        containerView = itemView;
        if (type == BaseUtils.CELL_HEADER){
            return;
        }
        if (type == BaseUtils.CELL_FOOTER){
            return;
        }

        if (type == BaseUtils.CELL_NO_PICTURE){
            nTitle = (TextView) itemView.findViewById(R.id.cell_no_picture_title);
            nSubTitle = (TextView) itemView.findViewById(R.id.cell_no_picture_subtitle);
            avatorImage = (ImageView) itemView.findViewById(R.id.cell_no_picture_avator);
        }
        if (type == BaseUtils.CELL_ONE_PICTURE){
            nTitle = (TextView) itemView.findViewById(R.id.cell_one_title);
            nSubTitle = (TextView) itemView.findViewById(R.id.cell_one_subtitle);
            nImageView = (ImageView)itemView.findViewById(R.id.cell_one_imageView);
            nNumPictures = (TextView) itemView.findViewById(R.id.cell_one_numImages);
            avatorImage = (ImageView) itemView.findViewById(R.id.cell_no_picture_avator);
        }

        if (type == BaseUtils.CELL_THREE_PICTURE){
            nTitle = (TextView) itemView.findViewById(R.id.cell_three_title);
            nSubTitle = (TextView) itemView.findViewById(R.id.cell_three_subtitle);
            nImageView1 = (ImageView)itemView.findViewById(R.id.cell_three_one_imageView);
            nImageView2 = (ImageView)itemView.findViewById(R.id.cell_three_two_imageView);
            nImageView3 = (ImageView)itemView.findViewById(R.id.cell_three_three_imageView);
//        nNumPictures = (TextView) itemView.findViewById(R.id.cell_one_numImages);
            avatorImage = (ImageView) itemView.findViewById(R.id.cell_no_picture_avator);
        }

        if (type == BaseUtils.CELL_VIDEO_PICTURE){
            nTitle = (TextView) itemView.findViewById(R.id.cell_video_title);
            nSubTitle = (TextView) itemView.findViewById(R.id.cell_video_subtitle);
            nImageView = (ImageView)itemView.findViewById(R.id.cell_video_imageView);
            nVideoTime = (TextView) itemView.findViewById(R.id.cell_video_time);
            navatorImage = (ImageView) itemView.findViewById(R.id.cell_video_avator);
            nVideoBtnImage =  (ImageView) itemView.findViewById(R.id.cell_video_btn);
        }

        if (type == BaseUtils.CELL_VIDEO_BIG_PICTURE){
            nTitle = (TextView) itemView.findViewById(R.id.cell_video_big_title);
            nSubTitle = (TextView) itemView.findViewById(R.id.cell_video_big_subtitle);
            nImageView = (ImageView)itemView.findViewById(R.id.cell_video_big_imageView);
            nVideoTime = (TextView) itemView.findViewById(R.id.cell_video_big_time);
            navatorImage = (ImageView) itemView.findViewById(R.id.cell_video_big_avator);
            nVideoBtnImage =  (ImageView) itemView.findViewById(R.id.cell_video_big_btn);
        }
    }
    public static NewsHolder newInstance(ViewGroup container, int viewType) {
        View root;
        if (viewType == BaseUtils.CELL_NO_PICTURE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_no_picture, container, false);
            return new NewsHolder(root,viewType);
        }
        if (viewType == BaseUtils.CELL_ONE_PICTURE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_one_picture, container, false);
            return new NewsHolder(root,viewType);
        }
        if (viewType == BaseUtils.CELL_THREE_PICTURE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_three_pictures, container, false);
            return new NewsHolder(root,viewType);
        }
        if (viewType == BaseUtils.CELL_VIDEO_PICTURE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_video, container, false);
            return new NewsHolder(root,viewType);
        }
        if (viewType == BaseUtils.CELL_VIDEO_BIG_PICTURE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_video_big, container, false);
            return new NewsHolder(root,viewType);
        }

        if (viewType == BaseUtils.CELL_LIVE){
            root = LayoutInflater.from(container.getContext()).inflate(R.layout.temp_item_card, container, false);
            return new NewsHolder(root,viewType);
        }

        root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_no_picture, container, false);
        return new NewsHolder(root,viewType);
    }

    public void bind(Object obj, String type, int viewType,final NewsAdapter.OnItemClickListener listener) {

        if (type == BaseUtils.TYPE_ITEM_NEWS && obj != null){
            final News news = (News) obj;
            if (viewType == BaseUtils.CELL_NO_PICTURE){
                nTitle.setText(news.title);
                nSubTitle.setText(news.avator);
                if (news.avatorPath.length()>500){
                    String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(this.itemView.getContext()).load(news.avatorPath).into(avatorImage);
                }
                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(news);
                    }
                });
            }
            if (viewType ==BaseUtils.CELL_ONE_PICTURE){
                nTitle.setText(news.title);
                nSubTitle.setText(news.avator);
                Glide.with(containerView.getContext()).load(news.images[0]).into(nImageView);
                nNumPictures.setText(news.images.length+"图");

                if (news.avatorPath.length()>500){

                    String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(this.itemView.getContext()).load(news.avatorPath).into(avatorImage);
                }
                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(news);
                    }
                });
            }
            if (viewType ==BaseUtils.CELL_THREE_PICTURE){
                nTitle.setText(news.title);
                nSubTitle.setText(news.avator);
                Glide.with(this.itemView.getContext()).load(news.images[0]).into(nImageView1);
                Glide.with(this.itemView.getContext()).load(news.images[1]).into(nImageView2);
                Glide.with(this.itemView.getContext()).load(news.images[2]).into(nImageView3);


                nNumPictures = (TextView) itemView.findViewById(R.id.cell_one_numImages);
                if (news.avatorPath.length()>500){

                    String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(this.itemView.getContext()).load(news.avatorPath).into(avatorImage);
                }
                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(news);
                    }
                });
            }
            if (viewType ==BaseUtils.CELL_VIDEO_PICTURE){
                nTitle.setText(news.title);
                nSubTitle.setText(news.avator);

//                nVideoTime.setText(forum.duration);
                nVideoTime.setText("5:30");
                Glide.with(this.itemView.getContext()).load(news.images[0]).into(nImageView);
//                navatorImage = (ImageView) itemView.findViewById(R.id.cell_video_avator);
//                nVideoBtnImage
                if (news.avatorPath.length()>500){
                    String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    navatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(this.itemView.getContext()).load(news.avatorPath).into(navatorImage);
                }

                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick( news);
                    }
                });
            }
            if (viewType ==BaseUtils.CELL_VIDEO_BIG_PICTURE){
                nTitle.setText(news.title);
                nSubTitle.setText(news.avator);
                Glide.with(this.itemView.getContext()).load(news.images[0]).into(nImageView);
                nVideoTime.setText("5:30");
//                nVideoTime.setText(forum.duration);
                if (news.avatorPath.length()>500){

                    String pureBase64Encoded = news.avatorPath.substring(news.avatorPath.indexOf(",")  + 1);
                    byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    navatorImage.setImageBitmap(decodedByte);
                }else{
                    Glide.with(this.itemView.getContext()).load(news.avatorPath).into(navatorImage);
                }
                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(news);
                    }
                });
            }
        }
    }

}
