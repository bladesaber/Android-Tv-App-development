package bladesaber.luckgamev1.DataStructure;

import java.util.List;

/**
 * Created by bladesaber on 2018/4/12.
 */

public class UserItem {

    private int id;
    private String name;
    private String code;
    private String type;
    private String status;
    private SigUser user;

    public String getUser(){
        return user.getNick_name();
    }

    public String getPrize(){
        return name;
    }

}
