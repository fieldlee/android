package cn.com.yqhome.instrumentapp.Fragments.Live;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.yqhome.instrumentapp.R;


public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>{

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_item_card, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}