package com.wanzhuan.poster;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanzhuan.poster.AppManager.AppManager;
import com.wanzhuan.poster.DataStructure.ViewModel;
import com.wanzhuan.poster.Global.GlobalSignManager;
import com.wanzhuan.poster.HttpClient.GetThread;
import com.wanzhuan.poster.HttpClient.PostThread;
import com.wanzhuan.poster.Library.easyLibrary.ContainerPosterManager;
import com.wanzhuan.poster.Library.easyLibrary.DataMaker;
import com.wanzhuan.poster.Library.easyLibrary.PosterManager;
import com.wanzhuan.poster.PushManager.PushListener;
import com.wanzhuan.poster.PushManager.PushManager;
import com.wanzhuan.poster.TestManager.TestClass;
import com.wanzhuan.poster.Tool.ScheduleManager;
import com.wanzhuan.poster.Tool.ScheduleModel;
import com.wanzhuan.poster.ViewContainer.ViewContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;

public class MainActivity extends Activity {

    private LottieAnimationView loading_View = null;

    private RelativeLayout container;
    private ImageView background = null;

    private int reStartTime = 10000000;

    private Type viewModel_type = new TypeToken<List<ViewModel>>() {}.getType();
    private Type schedule_type = new TypeToken<List<ScheduleModel.ScheduleItem>>() {}.getType();
    private Type int_type = new TypeToken<Integer>() {}.getType();
    private Type string_type = new TypeToken<String>() {}.getType();
    private Gson gson = new Gson();

    private Map<Integer,ViewContainer> all_containers = new HashMap<>();
    private List<ViewContainer> containers = new ArrayList<>();

    private int current_position = 0;
    private int pre_position = 0;
    private ViewContainer current_container = null;

    private PushManager pushManager;

    private boolean lottie_end = true;
    private boolean loading_end = false;
    private boolean default_load = false;

    private final int GET_MODEL = 200;
    private final int CHECK_START_SETTING = 201;
    private final int START = 202;
    private final int NEXT_CONTAINER = 203;
    private final int GET_REGISTER_STATUS = 204;
    private Handler Looper = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case GET_MODEL:
                    System.out.println("MainActivity.Model is: " + (String) msg.obj);
                    reslove_Model_Message((String)msg.obj);
                    break;
                case CHECK_START_SETTING:

                    // 目前不做使用
                    //Start();

                    First_Start();

                    break;
                case START:
                    // 目前不做使用
                    //Start();
                    break;
                case NEXT_CONTAINER:

                    Start_V2();

                    break;
                case GET_REGISTER_STATUS:
                    System.out.println("MainActivity.Check is: " + (String)msg.obj);
                    Check_RegisterStatus((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CrashReport.initCrashReport(getApplicationContext(), "bb13855280", false);

        //background = (ImageView) findViewById(R.id.background_view);
        container = (RelativeLayout) findViewById(R.id.Container);

        //Loading();
        //get_Model();

        // 目前不做使用
        //PosterManager.getInstance().setParent(container);

        // 布局测试接口
        //LayoutTest();

        // 正式开始接口
        pushManager = new PushManager(MainActivity.this);
        pushManager.set_loadingvIew(container);
        pushManager.setListener(new PushListener() {
            @Override
            public void NextStep() {
                get_Check();
            }

            @Override
            public void ResolvePushMessage(String msg) {
                switch (msg){
                    case "POSTER_UPDATE":
                        reset();
                        break;
                    default:
                        break;
                }
            }
        });
        pushManager.initXG(MainActivity.this);

        //get_Check();

        // 测试开始接口
        //get_Model_for_test();

    }

    private void Loading(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loading_View = new LottieAnimationView(getBaseContext());
        loading_View.setLayoutParams(params);
        loading_View.setAnimation(R.raw.lottielogo1);
        loading_View.loop(false);
        loading_View.pauseAnimation();
        loading_View.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                loading_View.pauseAnimation();
                loading_View.cancelAnimation();
                container.removeView(loading_View);
                lottie_end = true;
                //background.setVisibility(View.VISIBLE);

                // 目前不做使用
                //Looper.sendEmptyMessageDelayed(START,1000);

                Looper.sendEmptyMessageDelayed(CHECK_START_SETTING,1000);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        container.addView(loading_View);
        loading_View.playAnimation();
    }

    /*
    private void Start(){
        if (loading_end && lottie_end) {
            if (GlobalSignManager.Erro_Layout_Check) {
                if (reStartTime >= 1000) {
                    PosterManager.getInstance().start(reStartTime);
                } else {
                    Toast.makeText(MainActivity.this, "海报总时间不可小于1秒，请重设", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(MainActivity.this, "模板布局不适配", Toast.LENGTH_LONG).show();
            }
        }else {
            Looper.sendEmptyMessageDelayed(CHECK_START_SETTING,1000);
        }
    }
    */

    private void get_Check(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_code", AppManager.AppManager_GetMac(getBaseContext()));
            jsonObject.put("app_code", GlobalSignManager.App_Code);
            jsonObject.put("token", GlobalSignManager.getInstance().getDevice_token());
            jsonObject.put("version_code",AppManager.AppManager_GetVersionCode(getBaseContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PostThread postThread = new PostThread(getBaseContext(),GlobalSignManager.Reginster_Message_Url,jsonObject){
            public void Handle_Response(String Result){
                Message message = new Message();
                message.what = GET_REGISTER_STATUS;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        postThread.start();
    }

    private void Check_RegisterStatus(String json){
        JsonElement jsonElement = new JsonParser().parse(json).getAsJsonObject().get("data").getAsJsonObject().get("xxx_id");
        GlobalSignManager.xxx_id = gson.fromJson(jsonElement,int_type);
        if (GlobalSignManager.xxx_id > 0){
            get_Model();
        }
    }

    private void get_Model(){
        loading_end = false;

        String url = GlobalSignManager.Medel_Url + "?mid=" + GlobalSignManager.xxx_id +"&status=RELEASED"+"&device_code="+AppManager.getMacAddress(getBaseContext());
        GetThread getThread = new GetThread(getBaseContext(),url){
            @Override
            public void Handle_Response(String Result) {
                Message message = new Message();
                message.what = GET_MODEL;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

    private void get_Model_for_test(){
        GetThread getThread = new GetThread(getBaseContext(),"http://192.168.31.39/laravelTest/public/api/poster"){
            @Override
            public void Handle_Response(String Result) {
                Message message = new Message();
                message.what = GET_MODEL;
                message.obj = Result;
                Looper.sendMessage(message);
            }
        };
        getThread.start();
    }

    private void First_Start_with_Schdule(){
        if (loading_end && lottie_end) {
            if (default_load){
                background.setVisibility(View.VISIBLE);
            }else {
                if (containers!=null && containers.size()>0) {
                    if (GlobalSignManager.Erro_Layout_Check) {
                        current_position = 0;
                        if (current_container != null) {
                            container.removeView(current_container);
                        }
                        container.addView(containers.get(current_position));
                        current_container = containers.get(current_position);
                        ContainerPosterManager.getInstance(getBaseContext()).start(current_container);
                        Looper.sendEmptyMessageDelayed(NEXT_CONTAINER, current_container.getTime());
                    } else {
                        Toast.makeText(MainActivity.this, "模板不兼容", Toast.LENGTH_LONG).show();
                    }
                }else {
                    background.setVisibility(View.VISIBLE);
                }
            }
        }else {
            Looper.sendEmptyMessageDelayed(CHECK_START_SETTING,1000);
        }
    }

    private void First_Start(){
        if (loading_end && lottie_end) {
            if (default_load){
                background.setVisibility(View.VISIBLE);
            }else {
                if (GlobalSignManager.Erro_Layout_Check) {
                    current_position = 0;
                    if (current_container != null) {
                        container.removeView(current_container);
                    }
                    container.addView(containers.get(current_position));
                    current_container = containers.get(current_position);
                    ContainerPosterManager.getInstance(getBaseContext()).start(current_container);
                    Looper.sendEmptyMessageDelayed(NEXT_CONTAINER, current_container.getTime());
                } else {
                    Toast.makeText(MainActivity.this, "模板不兼容", Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Looper.sendEmptyMessageDelayed(CHECK_START_SETTING,1000);
        }
    }

    public void Start_V2(){

        pre_position = current_position;
        current_position++;
        if (current_position>containers.size()-1){
            current_position = 0;
        }

        if (current_position != pre_position) {

            if (current_container!=null){
                container.removeView(current_container);
                current_container.stop();
            }

            container.addView(containers.get(current_position));
            current_container = containers.get(current_position);
        }

        ContainerPosterManager.getInstance(getBaseContext()).start(current_container);
        Looper.sendEmptyMessageDelayed(NEXT_CONTAINER,current_container.getTime());
    }

    private void reslove_Model_Message(String json){

        JsonObject json2 = new JsonParser().parse(json).getAsJsonObject();
        JsonArray jsonObjectList = json2.get("data").getAsJsonArray();

        ScheduleManager.getInstance().getScheduleModelList().clear();
        //  等待后台接口配合
        List<ScheduleModel.ScheduleItem> scheduleItems = gson.fromJson(json2.get("groups"),schedule_type);
        ScheduleManager.getInstance().setScheduleModelList(scheduleItems);

        if (jsonObjectList!=null && jsonObjectList.size()>0) {
            for (JsonElement jsonElement : jsonObjectList) {

                JsonObject jsonObject = jsonElement.getAsJsonObject();

                int id = gson.fromJson(jsonObject.get("id"),int_type);
                ViewContainer container = new ViewContainer(getBaseContext(),id);

                Integer duration = gson.fromJson(jsonObject.get("total_duration"),int_type);
                if (duration!=null && duration>1000) {
                    container.setTime(duration.intValue());
                }else {
                    Toast.makeText(MainActivity.this, "此模板轮播时间过短", Toast.LENGTH_LONG).show();
                }

                String url = gson.fromJson(jsonObject.get("bg_image_url"),string_type);
                container.setBackground(url);

                List<ViewModel> viewModels = gson.fromJson(jsonObject.get("items"), viewModel_type);

                if (viewModels != null && viewModels.size() > 0) {

                    // 这边要做不少的修改
                    //containers.add(container);
                    // 这里做时间计划的修改
                    all_containers.put(id,container);

                    for (ViewModel viewModel : viewModels) {
                        ContainerPosterManager.getInstance(getBaseContext()).add(getBaseContext(), container.getTasks(),container.getViewList(), viewModel);
                    }

                    ScheduleManager.getInstance().setListener(new ScheduleManager.Schedule_Listener() {
                        @Override
                        public void Handle(List<Integer> ids) {
                            Looper.removeCallbacksAndMessages(null);
                            if (ids!=null) {
                                containers.clear();
                                for (int id : ids) {
                                    containers.add(all_containers.get(id));
                                }
                                First_Start_with_Schdule();
                            }else {
                                containers.clear();
                                Iterator<Map.Entry<Integer, ViewContainer>> it = all_containers.entrySet().iterator();

                                while(it.hasNext()) {
                                    Map.Entry<Integer, ViewContainer> entry = it.next();
                                    containers.add(entry.getValue());
                                }
                                First_Start_with_Schdule();
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "模板内没任何内容在使用", Toast.LENGTH_LONG).show();
                }
            }

            // For Test Using
            //First_Start();
            System.out.println("MainActivity.ScheduleManager.start");
            ScheduleManager.getInstance().start();

        }else {
            default_load();
            Toast.makeText(MainActivity.this,"未选用任何模板",Toast.LENGTH_LONG).show();
        }

        loading_end = true;

        /*
        List<ViewModel> viewModels = gson.fromJson(json1.get("items"),viewModel_type);
        if (viewModels!=null && viewModels.size()>0) {
            for (ViewModel viewModel : viewModels) {
                PosterManager.getInstance().add(getBaseContext(), viewModel);
            }
            loading_end = true;
            // 目前不做使用
            //Start();
        }else {
            Toast.makeText(MainActivity.this,"请选择模板再使用",Toast.LENGTH_LONG).show();
        }
        */
    }

    private void default_load(){
        if (background==null){
            default_load = true;
            background = new ImageView(getBaseContext());
            background.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            background.setLayoutParams(params);
            background.setImageResource(R.drawable.default_background);
            container.addView(background);
        }
    }

    private void reset(){
        Looper.removeCallbacksAndMessages(null);
        all_containers.clear();
        current_container.stop();
        containers.clear();
        get_Model();
    }

    //----------------------------------------------------------------------------------------------
    private void LayoutTest(){
        container.setBackgroundResource(R.drawable.bacgroud_yinpiin);

        container.addView(DataMaker.getInstance().createImageView(getBaseContext(),606,305,642,50,"https://qn.3128play.com/poster/5/xiari.png"));

        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),50,250,587,477,"冰火两重天","#7ACCA4",27.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),30,30,597,725,"¥","#F75151",20.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),60,60,579,771,"20","#F75151",25.0f));

        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),50,250,1105,476,"波霸奶茶","#7ACCA4",27.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),30,30,1113,725,"¥","#F75151",20.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),60,60,1097,771,"16","#F75151",25.0f));

        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),50,250,1519,476,"满杯柠檬","#7ACCA4",27.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),30,30,1528,727,"¥","#F75151",20.0f));
        container.addView(DataMaker.getInstance().createTextView(getBaseContext(),60,60,1512,773,"18","#F75151",25.0f));

        container.addView(DataMaker.getInstance().createImageView(getBaseContext(),200,560,390,438,"https://qn.3128play.com/poster/5/binghuo.png"));
        container.addView(DataMaker.getInstance().createImageView(getBaseContext(),245,513,847,477,"https://qn.3128play.com/poster/5/boba.png"));
        container.addView(DataMaker.getInstance().createImageView(getBaseContext(),193,557,1330,460,"https://qn.3128play.com/poster/5/manbei.png"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pushManager!=null){
            pushManager.onDestroy(MainActivity.this);
        }

        Looper.removeCallbacksAndMessages(null);

        ScheduleManager.getInstance().stop();

        // 目前不做使用
        //PosterManager.getInstance().onDestroy();
    }
}
