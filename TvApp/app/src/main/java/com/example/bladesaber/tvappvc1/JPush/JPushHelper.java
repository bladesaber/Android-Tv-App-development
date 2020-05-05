package com.example.bladesaber.tvappvc1.JPush;

import android.content.Context;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by bladesaber on 2018/4/3.
 */

public class JPushHelper {

    private static int sequence = 1;

    public void handleAction(Context context, int Action, String Text){
        // Alias 只可以有一个
        switch (Action){
            case JPushManager.ALIA_ADD_SET:
                JPushInterface.setAlias(context,sequence,Text);
                Toast.makeText(context,"Alia Add Set Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
            case JPushManager.ALIA_DEL:
                JPushInterface.deleteAlias(context,sequence);
                Toast.makeText(context,"Alia Del Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
        }
    }

    public void handleAction(Context context, int Action, Set<String> Tag){
        switch (Action){
            case JPushManager.TAG_ADD:
                JPushInterface.addTags(context,sequence,Tag);
                Toast.makeText(context,"Tag Add Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
            case JPushManager.TAG_SET:
                JPushInterface.setTags(context,sequence,Tag);
                Toast.makeText(context,"Tag Set Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
            case JPushManager.TAG_DEL:
                JPushInterface.deleteTags(context,sequence,Tag);
                Toast.makeText(context,"Tag Del Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
        }
    }

    public void handleAction(Context context, int Action){
        switch (Action){
            case JPushManager.TAG_CLEAN:
                JPushInterface.cleanTags(context,sequence);
                Toast.makeText(context,"Tag Clean Operation",Toast.LENGTH_LONG).show();
                sequence++;
                break;
            default:
                break;
        }
    }

    public Set<String> getInputTag(Context context,String tag){
        if (tag.isEmpty()){
            Toast.makeText(context,"Empty is false",Toast.LENGTH_LONG).show();
            return null;
        }
        Set<String> tagset = new LinkedHashSet<String>();
        String[] sArray = tag.split(",");
        for (String tag_sig : sArray){
            tagset.add(tag_sig);
        }
        if (tagset.isEmpty()){
            return null;
        }
        return tagset;
    }

    public String getInputAlia(Context context,String Alias){
        if (Alias.isEmpty()){
            Toast.makeText(context,"Empty is false",Toast.LENGTH_LONG).show();
            return null;
        }
        return Alias;
    }

}
