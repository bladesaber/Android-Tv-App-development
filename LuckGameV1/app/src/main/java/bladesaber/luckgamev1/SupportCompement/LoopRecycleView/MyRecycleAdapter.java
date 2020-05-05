package bladesaber.luckgamev1.SupportCompement.LoopRecycleView;

import android.nfc.NfcEvent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bladesaber.luckgamev1.DataStructure.UserItem;
import bladesaber.luckgamev1.R;
import bladesaber.luckgamev1.SupportCompement.LoopListView.ListViewItem;

/**
 * Created by bladesaber on 2018/4/13.
 */

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

    private List<ListViewItem> mData;

    public MyRecycleAdapter(List<ListViewItem> data) {
        this.mData = data;
    }

    public void updateData(List<ListViewItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public List<ListViewItem> getmData(){
        return mData;
    }

    public void UpdateList(){
        if(mData == null) {
            mData = new ArrayList<>();
        }

        ListViewItem listViewItem = new ListViewItem();
        listViewItem.setName(mData.get(0).getName());
        listViewItem.setPresent(mData.get(0).getPresent());

        deleteItem();

        mData.add(getItemCount(),listViewItem);

        notifyItemInserted(getItemCount());
    }

    private void deleteItem() {
        if(mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(0);
        notifyItemRemoved(0);
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
        // 绑定数据
        String Text = mData.get(position).getName();
        holder.Name.setText(Text);
        holder.Present.setText(mData.get(position).getPresent());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Present;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.item_Name);
            Present = (TextView) itemView.findViewById(R.id.item_Present);
        }
    }

}
