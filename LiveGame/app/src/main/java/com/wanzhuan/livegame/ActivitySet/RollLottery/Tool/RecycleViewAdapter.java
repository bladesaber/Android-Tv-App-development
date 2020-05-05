package com.wanzhuan.livegame.ActivitySet.RollLottery.Tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wanzhuan.livegame.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by bladesaber on 2018/7/6.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<WinnerItem> mData;

    private Context context;

    public RecycleViewAdapter(Context context,List<WinnerItem> data) {
        this.context = context;
        this.mData = data;
    }

    public void updateData(List<WinnerItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void add_item(WinnerItem item){

        if (mData.size()>0) {
            mData.remove(0);
            notifyItemRemoved(0);
        }

        mData.add(0,item);
        notifyItemInserted(0);

    }

    public List<WinnerItem> getmData(){
        return mData;
    }

    public void updateList(){
        if(mData == null) {
            mData = new ArrayList<>();
        }

        WinnerItem WinnerItem = new WinnerItem();
        WinnerItem.name = mData.get(getItemCount()).name;
        WinnerItem.present = mData.get(getItemCount()).present;

        deleteItem();

        mData.add(0,WinnerItem);

        notifyItemInserted(0);
    }

    private void deleteItem() {
        if(mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(getItemCount());
        notifyItemRemoved(getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View oneView = LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(oneView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String Text = mData.get(position).name;
        holder.Name.setText(Text);
        holder.Present.setText(mData.get(position).present);

        Picasso.with(context)
                .load(mData.get(position).icon_url)
                .config(Bitmap.Config.RGB_565)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Present;
        CircleImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.roll_lottery_item_Name);
            Present = (TextView) itemView.findViewById(R.id.roll_lottery_item_Present);
            imageView = (CircleImageView) itemView.findViewById(R.id.roll_lottery_winner_circle_icon);
        }
    }

}
