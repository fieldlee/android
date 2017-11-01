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
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/10/23.
 */

public class LiveHolder extends RecyclerView.ViewHolder {

    private TextView nSing;
    private ImageView nAvatorImage;
    private ImageView nliveImage;
    private TextView nAvator;

    private View containerView;
    public LiveHolder(View itemView) {
        super(itemView);
        containerView = itemView;
        nSing = (TextView) itemView.findViewById(R.id.live_cell_sign);
        nAvator = (TextView) itemView.findViewById(R.id.live_cell_avator);
        nAvatorImage = (ImageView) itemView.findViewById(R.id.live_cell_avatorImage);
        nliveImage = (ImageView) itemView.findViewById(R.id.live_cell_image);
    }

    public static LiveHolder newInstance(ViewGroup container) {
        View root = LayoutInflater.from(container.getContext()).inflate(R.layout.temp_item_card, container, false);
        return new LiveHolder(root);
    }


    public void bind(Object obj,final LiveAdapter.OnItemClickListener listener) {
        final Live live = (Live)obj;
        nAvator.setText(live.avator);
        nSing.setText(live.sign);
        if (live.avatorPath.length()>500){

            String pureBase64Encoded = live.avatorPath.substring(live.avatorPath.indexOf(",")  + 1);
            byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            nAvatorImage.setImageBitmap(decodedByte);
        }else{

            Glide.with(containerView.getContext()).load(live.avatorPath).into(nAvatorImage);
        }
        if (live.image.indexOf(R.string.host)>=0){
            Glide.with(containerView.getContext()).load(live.image).into(nliveImage);
        }else{
            String host = "http://" + containerView.getContext().getResources().getString(R.string.host) +":"+containerView.getContext().getResources().getString(R.string.port);
            String imageUrl = host + live.image;
            Glide.with(containerView.getContext()).load(imageUrl).into(nliveImage);
        }
        containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(live);
            }
        });
    }
}
