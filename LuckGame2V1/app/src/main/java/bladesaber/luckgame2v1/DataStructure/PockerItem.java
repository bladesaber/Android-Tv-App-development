package bladesaber.luckgame2v1.DataStructure;

/**
 * Created by bladesaber on 2018/4/20.
 */

public class PockerItem {

    private int id;
    private String name;
    private double probability;
    private String type;

    public String getPresent(){
        return name;
    }

    public int getId(){
        return id;
    }

    public String getType(){
        return type;
    }

}
