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
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.com.yqhome.instrumentapp.BaseUtils;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.R;

/**
 * Created by depengli on 2017/9/29.
 */

public class LearnHolder extends RecyclerView.ViewHolder {
    private TextView nTitle;
    private TextView nSubTitle;
    private ImageView avatorImage;

    private View containerView;
    public LearnHolder(View itemView,int type) {
        super(itemView);
        containerView = itemView;
//        if (type == BaseUtils.CELL_LEARN_MP3){
            nTitle = (TextView)itemView.findViewById(R.id.cell_learn_title);
            nSubTitle = (TextView)itemView.findViewById(R.id.cell_learn_subtitle);
            avatorImage = (ImageView)itemView.findViewById(R.id.cell_learn_avator);
//        }
    }

    public static LearnHolder newInstance(ViewGroup container, int viewType) {
        View root;
        root = LayoutInflater.from(container.getContext()).inflate(R.layout.cell_learn_mp3, container, false);
        return new LearnHolder(root,viewType);
    }

    public void bind(Object obj, String type, int viewType,final LearnAdapter.OnItemClickListener listener) {

        if (type == BaseUtils.TYPE_ITEM_LEARN && obj != null){
            final Learn learn = (Learn) obj;
                nTitle.setText(learn.title);
                nSubTitle.setText(learn.avator);
                if (learn.authorPath != null){
                    if (learn.authorPath.length()>500){
                        String pureBase64Encoded = learn.authorPath.substring(learn.authorPath.indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        avatorImage.setImageBitmap(decodedByte);
                    }else{
                        Glide.with(this.itemView.getContext()).
                                load(learn.authorPath).
                                diskCacheStrategy(DiskCacheStrategy.RESULT).
                                thumbnail(0.5f).
                                placeholder(R.drawable.user_avatar).
                                priority(Priority.LOW).
                                error(R.drawable.user_avatar).
                                fallback(R.drawable.user_avatar).
                                into(avatorImage);

                    }
                }else{
                    Glide.with(this.itemView.getContext()).
                            load(learn.authorPath).
                            diskCacheStrategy(DiskCacheStrategy.RESULT).
                            thumbnail(0.5f).
                            placeholder(R.drawable.user_avatar).
                            priority(Priority.LOW).
                            error(R.drawable.user_avatar).
                            fallback(R.drawable.user_avatar).
                            into(avatorImage);
                }

                containerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(learn);
                    }
                });
//            }
        }

    }

}
