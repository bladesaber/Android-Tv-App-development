package com.wanzhuan.livegame.ActivitySet.RollLottery;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wanzhuan.livegame.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bladesaber on 2018/4/16.
 */

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

    private List<ViewItem> mData;

    private Context context;

    public MyRecycleAdapter(Context context,List<ViewItem> data) {
        this.mData = data;
        this.context = context;
    }

    public void updateData(List<ViewItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public List<ViewItem> getmData(){
        return mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View oneView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(oneView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int newPos=position % mData.size();

        //holder.imageView.setImageResource(R.drawable.default_icon);

        Picasso.with(context)
                .load(mData.get(newPos).getImage_url())
                .config(Bitmap.Config.RGB_565)
                .fit()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        //return mData == null ? 0 : mData.size();
        return Integer.MAX_VALUE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.recycle_player_icon);
        }
    }

}
