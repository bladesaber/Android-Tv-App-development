package bladesaber.luckgamev1.SupportCompement.LoopRecycleView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import java.util.TimerTask;

import bladesaber.luckgamev1.R;

/**
 * Created by bladesaber on 2018/4/13.
 */

public class TimeTaskScroll2 extends TimerTask {

    private MyRecycleAdapter recycleAdapter;

    public TimeTaskScroll2(Context context, MyRecycleAdapter myRecycleAdapter) {
        this.recycleAdapter = myRecycleAdapter;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //  控制速度
            // listview.smoothScrollToPosition(position);//平滑到某个item
            // listview.setSelection(position);//滑动到某个item
            // listview.scrollListBy(600);//向下滑动600px。向上是-600
            // listview.smoothScrollBy(600, 2000);//向下平滑1000px，在2s内。向上是-600
            recycleAdapter.UpdateList();
        }
    };


    @Override
    public void run() {
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }

}
