package bladesaber.luckgamev1.SupportCompement.LoopListView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import java.util.TimerTask;

/**
 * Created by bladesaber on 2018/4/12.
 */

public class TimeTaskScroll extends TimerTask {
    private ListView listView;

    public TimeTaskScroll(Context context, ListView listView) {
        this.listView = listView;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            //  控制速度
            // listview.smoothScrollToPosition(position);//平滑到某个item
            // listview.setSelection(position);//滑动到某个item
            // listview.scrollListBy(600);//向下滑动600px。向上是-600
            // listview.smoothScrollBy(600, 2000);//向下平滑1000px，在2s内。向上是-600
            listView.smoothScrollBy(5, 200);
        }
    };


    @Override
    public void run() {
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }

}
