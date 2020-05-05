package bladesaber.luckgamev1.SupportCompement.LoopListView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import bladesaber.luckgamev1.R;

/**
 * Created by bladesaber on 2018/4/12.
 */

public class MyListViewAdapter extends BaseAdapter {

    private List<ListViewItem> DataList;
    private Context mContext;
    private ViewHolder holder;

    public MyListViewAdapter(Context context, List<ListViewItem> list) {
        mContext = context;
        DataList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return DataList.get(position % DataList.size());
    }

    @Override
    public long getItemId(int position) {
        return position % DataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);

            holder=new ViewHolder();
            holder.item_Name = (TextView) convertView.findViewById(R.id.item_Name);
            holder.item_present = (TextView) convertView.findViewById(R.id.item_Present);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        String Text = "恭喜 "+DataList.get( (position % DataList.size()) ).getName();
        System.out.println("MyListViewAdapter.getView.Name: "+Text);
        holder.item_Name.setText(Text);
        holder.item_present.setText(DataList.get( (position % DataList.size()) ).getPresent());

        return convertView;
    }

    class ViewHolder {
        public TextView item_Name;
        public TextView item_present;
    }
}
