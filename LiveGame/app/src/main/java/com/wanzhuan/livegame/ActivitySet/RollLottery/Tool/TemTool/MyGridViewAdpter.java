package com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.TemTool;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wanzhuan.livegame.ActivitySet.RollLottery.Tool.WinnerItem;
import com.wanzhuan.livegame.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * GridView加载数据的适配器
 * @author Administrator
 *
 */
public class MyGridViewAdpter extends BaseAdapter{

    private Context context;
    private List<WinnerItem> lists;//数据源
    private int mIndex; // 页数下标，标示第几页，从0开始
    private int mPargerSize;// 每页显示的最大的数量



    public MyGridViewAdpter(Context context, List<WinnerItem> lists,
                            int mIndex, int mPargerSize) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size() > (mIndex + 1) * mPargerSize ?
                mPargerSize : (lists.size() - mIndex*mPargerSize);
    }

    @Override
    public WinnerItem getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0 + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0 + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_home_gridview, null);

            holder.tv_name = (TextView)convertView.findViewById(R.id.home_item_tv);
            holder.iv_nul = (CircleImageView) convertView.findViewById(R.id.home_item_img);
            holder.present = (TextView) convertView.findViewById(R.id.home_item_present);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        final int pos = position + mIndex * mPargerSize;

        holder.tv_name.setText(lists.get(pos).getName());
        holder.present.setText(lists.get(pos).present);
        Picasso.with(context)
                .load(lists.get(pos).getIcon_url()).
                into(holder.iv_nul);

        return convertView;
    }
    static class ViewHolder{
        private TextView tv_name;
        private CircleImageView iv_nul;
        private TextView present;
    }
}
