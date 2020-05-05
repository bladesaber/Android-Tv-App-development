package bladesaber.luckgame2v1.DataStructure;

/**
 * Created by bladesaber on 2018/4/16.
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
