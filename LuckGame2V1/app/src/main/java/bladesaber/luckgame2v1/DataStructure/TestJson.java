package bladesaber.luckgame2v1.DataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bladesaber on 2018/4/12.
 */

public class TestJson {

    private List<dataItem> data;

    private class dataItem{
        private String prize;

        public String getPrize(){
            return prize;
        }
    }

    public List<String> getData(){
        List<String> dataList = new ArrayList<String>();
        for (int i=0;i<data.size();i++){
            dataList.add(data.get(i).getPrize());
        }
        return dataList;
    }

}
