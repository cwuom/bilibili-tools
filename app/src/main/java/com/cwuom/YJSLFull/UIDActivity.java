package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopNotification;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.SmartGlideImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.dionsegijn.steppertouch.OnStepCallback;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 这里是UIDActivity! 我来弥补注释的短板啦
 * 顾名思义这是一个通过UID信息解析的act
 * 创建日期=>2022/7/10
 * @author cwuom
 * */

public class UIDActivity extends AppCompatActivity {
    private String uid;
    private String name;
    private String imgUrl;
    private TextView mTvName;
    private ImageView mIvBg;
    private boolean iLoading = true;

    private Button mBtnView;
    private Button mBtnAllView;
    private Button mBtnPiao;
    private Button mBtnAuto;
    private Button mBtnExit;

//    private Boolean iCookie = true;


    private String sex;
    private String UIDl;
    private String birthday;
    private String liveRoomID;
    private String due_date;
    private String sViewArchive;
    private String sViewArticle;
    private String sLikes;
    private String page;

    private int sNbp = 0;
    private int sView = 0;

    private CustomizedProgressBar mProgessPiao;
    private TextView mTvProgressBar;
    private TextView mTvPiao;

    private StepperTouch stepperTouch;

    private int sPage = 1;
    private float Fx = 0F;


    private BasePopupView xpLoading;
    private BasePopupView xpLoading2;
    private Boolean iShow = false;

    private Button mBtnCheck;
    private String csrf;

    private Handler handler = new UIDActivity.MyHandler(UIDActivity.this);

    private List<Videos> videoList = new ArrayList<Videos>();
    private List<Favorite> favoriteList = new ArrayList<Favorite>();
    private List<Thread> threads = new ArrayList<Thread>();

    private int pLike = 1;
    private boolean iPlike = false;
    private boolean iLike = false;
    private boolean iLShow = true;

    private int vc;

    private PopTip popTipE;
    private boolean iPopTipE = false;

    String[] l = new String[30];
    Videos[] l2 = new Videos[30];

    private String cookie;

    private int iParse = 0;
    private int video_num_coin = 0;
    private String imUID;

    private int coins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uid);

        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
        SharedPreferences.Editor edt = share.edit();
        cookie = share.getString("cookie","");


        stepperTouch = findViewById(R.id.stepperTouch);
        stepperTouch.setMinValue(1);
        stepperTouch.setCount(1);
        stepperTouch.setSideTapEnabled(true);

        mTvName = findViewById(R.id.tv_name);
        mIvBg = findViewById(R.id.iv_bg);
        mBtnCheck = findViewById(R.id.btn_check);
        mBtnView = findViewById(R.id.btn_view);
        mBtnAllView = findViewById(R.id.btn_view_all);
        mBtnPiao = findViewById(R.id.btn_view_piao);

        mProgessPiao = findViewById(R.id.progress);
        mTvProgressBar = findViewById(R.id.tv_data_integrity);
        mTvPiao = findViewById(R.id.tv_piao);
        mBtnAuto = findViewById(R.id.btn_automation);

        DialogX.init(this);

        DialogX.onlyOnePopTip = true;
//        DialogX.globalStyle = new KongzueStyle();

//        //设置为IOS主题
//        DialogX.globalStyle = new IOSStyle();
//
//        //设置为Kongzue主题
//        DialogX.globalStyle = new KongzueStyle();
//
//        //设置为MIUI主题
//        DialogX.globalStyle = new MIUIStyle();
//
//        //设置为MaterialYou主题
//        DialogX.globalStyle = new MaterialYouStyle();

        // 想想还是太麻烦了，废除
//        mTvSex = findViewById(R.id.tv_sex);
//        mTvUIDl = findViewById(R.id.tv_midL);
//        mBirthday = findViewById(R.id.tv_birthday);
//        mTvLiveRoom = findViewById(R.id.tv_live_room);
//        mTvVIP = findViewById(R.id.tv_vip);

        // https://api.bilibili.com/x/relation/stat?vmid=473400804 black
        // https://api.bilibili.com/x/space/upstat?mid=473400804 view likes
        // https://api.bilibili.com/x/space/acc/info?mid=473400804 ....
        // https://api.bilibili.com/x/space/arc/search?mid=473400804&pn=1&ps=25&index=1&jsonp=jsonp

        // https://api.bilibili.com/x/web-interface/archive/like
        // https://api.bilibili.com/x/web-interface/coin/add
        // https://api.bilibili.com/x/article/favorites/add
        // https://api.bilibili.com/x/article/favorites/del


        // 获取传递的信息
        Intent i = getIntent();
        uid = i.getStringExtra("UID");
        Log.e("uid", uid);

        // UID:473400804

        if (Objects.equals(uid, "473400804")){
//            PopNotification.show(R.drawable.email, "cwuom", "听说有人正在解析我?").autoDismiss(4000);
            PopNotification.build()
                    .setStyle(new MIUIStyle())
                    .setTitle("cwuom")
                    .setIconResId(R.drawable.email)
                    .setMessage("听说有人正在解析我?")
                    .autoDismiss(5000)
                    .show();

        }


        mBtnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iCookie = share.getBoolean("iCookie",false);
                if (!iCookie){
                    BottomDialog.show("强制性登录功能", "当前属性下的所有功能都需要登录后使用！\n当前处于无COOKIE状态（用户未登录），点击登录自动获取。")
                            .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                @Override
                                public boolean onClick(BottomDialog baseDialog, View v) {
                                    PopTip.show("请放心，软件无服务器(穷)，不会自动上传您的任何使用数据"); // 确实没有服务器，穷
                                    return false;
                                }
                            })
                            .setOkButton("登录", new OnDialogButtonClickListener<BottomDialog>() {
                                @Override
                                public boolean onClick(BottomDialog baseDialog, View v) {
                                    Intent intent = new Intent(UIDActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return false;
                                }
                            });

                }else{
                    BottomMenu.show(new String[]{"点赞所有视频", "投币视频", "收藏所有视频", "终止所有任务"})
                            .setMessage("自动化")
                            .setOnIconChangeCallBack(new OnIconChangeCallBack(true) {
                                @Override
                                public int getIcon(BaseDialog dialog, int index, String menuText) {
                                    switch (menuText) {
                                        case "点赞所有视频":
                                            return R.drawable.ic_obtain_like;
                                        case "投币视频":
                                            return R.drawable.ic_bangumi_coin_light;
                                        case "收藏所有视频":
                                            return R.drawable.ic_emoji_star;
                                        case "终止所有任务":
                                            return R.drawable.exit;
                                    }
                                    return 0;
                                }
                            })
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    if (index == 0 && !iLike){ // 点赞
                                        BottomMenu.show(new String[]{"取消点赞", "点赞"})
                                                .setMessage("选择操作")
                                                .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                    @Override
                                                    public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                        if (index == 1 && !iLike){
                                                            iLike = true;
                                                            iLShow = true;
                                                            iPopTipE = false;
                                                            PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    for (int x = 0; x < videoList.size(); x++){
                                                                        if (!iLike){
                                                                            PopTip.show("操作终止", "确定");
                                                                            handler.sendEmptyMessage(8);
                                                                            break;
                                                                        }
                                                                        try {
                                                                            Random random = new Random();
                                                                            int number = random.nextInt(700) + 100;
                                                                            Log.e("ms", String.valueOf(number));
                                                                            Thread.sleep(number);
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        String aid = String.valueOf(videoList.get(x).aid);

                                                                        final OkHttpClient client = new OkHttpClient();
                                                                        FormBody body = new FormBody.Builder()
                                                                                .add("aid", aid)
                                                                                .add("like", "1")
                                                                                .add("csrf", csrf)
                                                                                .build();

                                                                        final Request request = new Request.Builder()
                                                                                .url("https://api.bilibili.com/x/web-interface/archive/like")
                                                                                .header("Cookie", cookie)
                                                                                .post(body)
                                                                                .build();

                                                                        Response response = null;
                                                                        try {
                                                                            response = client.newCall(request).execute();
                                                                            if (response.isSuccessful()) {
                                                                                String res = response.body().string();
                                                                                Log.i("res","打印POST响应的数据：" + res);
                                                                                if (res.contains("已赞过")){
                                                                                    Log.e("已赞过", "已赞过");
                                                                                    handler.sendEmptyMessage(10);
                                                                                }
                                                                                handler.sendEmptyMessage(7);
                                                                                pLike++;
                                                                            } else {
                                                                                handler.sendEmptyMessage(9);
                                                                                throw new IOException("Unexpected code " + response);
                                                                            }
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }


                                                                    handler.sendEmptyMessage(8);

                                                                }
                                                            }).start();
                                                        }
                                                        if (index == 0 && !iLike){
                                                            iLike = true;
                                                            iLShow = true;
                                                            iPopTipE = false;
                                                            PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    for (int x = 0; x < videoList.size(); x++){
                                                                        if (!iLike){
                                                                            PopTip.show("操作终止", "确定");
                                                                            handler.sendEmptyMessage(8);
                                                                            break;
                                                                        }
                                                                        try {
                                                                            Random random = new Random();
                                                                            int number = random.nextInt(700) + 100;
                                                                            Thread.sleep(number);
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        String aid = String.valueOf(videoList.get(x).aid);

                                                                        final OkHttpClient client = new OkHttpClient();
                                                                        FormBody body = new FormBody.Builder()
                                                                                .add("aid", aid)
                                                                                .add("like", "2")
                                                                                .add("csrf", csrf)
                                                                                .build();

                                                                        final Request request = new Request.Builder()
                                                                                .url("https://api.bilibili.com/x/web-interface/archive/like")
                                                                                .header("Cookie", cookie)
                                                                                .post(body)
                                                                                .build();

                                                                        Response response = null;
                                                                        try {
                                                                            response = client.newCall(request).execute();
                                                                            if (response.isSuccessful()) {
                                                                                String res = response.body().string();
                                                                                Log.i("res","打印POST响应的数据：" + res);
                                                                                if (res.contains("取消赞失败")){
                                                                                    Log.e("未赞过", "未赞过");
                                                                                    handler.sendEmptyMessage(10);
                                                                                }
                                                                                handler.sendEmptyMessage(13);
                                                                                pLike++;
                                                                            } else {
                                                                                handler.sendEmptyMessage(9);
                                                                                throw new IOException("Unexpected code " + response);
                                                                            }
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }


                                                                    handler.sendEmptyMessage(8);

                                                                }
                                                            }).start();
                                                        }
                                                        if (index == 2 && !iLike){
                                                            iLike = false;
                                                        }
                                                        return false;
                                                    }
                                                });
                                    }
                                    if (index == 1 && !iLike){ // 投币
                                        String urlPath;
                                        URL url;
                                        URLConnection conn;
                                        BufferedReader br;
                                        StringBuilder sb;
                                        String line;

                                        urlPath = "https://account.bilibili.com/site/getCoin";
                                        try {
                                            url = new URL(urlPath);
                                            conn = url.openConnection();
                                            conn.setRequestProperty("Cookie", cookie);
                                            conn.setDoInput(true);
                                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                            sb = new StringBuilder();
                                            line = null;
                                            while ((line = br.readLine()) != null) {
                                                sb.append(line);
                                            }

                                            JSONObject data = (JSONObject) new JSONObject(String.valueOf(sb)).get("data");
                                            coins = (int) data.get("money");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        new InputDialog("你想投几个视频", "该操作无法撤销，请思考后再输入\n若想全部投出，请输入“all”", "确定", "取消", "1")
                                                .setCancelable(false)
                                                .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                                                    @Override
                                                    public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                                        Log.e("输入的内容：", inputStr);
                                                        try {
                                                            video_num_coin = Integer.parseInt(inputStr);
                                                            BottomMenu.show(new String[]{"1个硬币", "2个硬币"})
                                                                    .setMessage("每个视频的投币数量")
                                                                    .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                                        @Override
                                                                        public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                                            int num = -1;
                                                                            boolean i = true;
                                                                            if (index == 1){ // 不同的硬币数量计算
                                                                                num = coins / 2;
                                                                                if (video_num_coin > videoList.size()){
                                                                                    i = false;
                                                                                }
                                                                            }else{
                                                                                num = coins;
                                                                                if (video_num_coin > videoList.size()){
                                                                                    i = false;
                                                                                }
                                                                            }
                                                                            Log.e("num = ", String.valueOf(num));
                                                                            Log.e("size = ", String.valueOf(videoList.size()));
                                                                            if (!i){
                                                                                MessageDialog.show("有钱花不完", "你不能投完这些视频", "确定");
                                                                            }else{
                                                                                BottomDialog.show("最后的警告", "硬币余额还剩" + coins + "个\n预计可以投" + num + "个视频\n确定执行吗？")
                                                                                        .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                            @Override
                                                                                            public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                //...
                                                                                                return false;
                                                                                            }
                                                                                        })
                                                                                        .setOkButton("确定", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                            @Override
                                                                                            public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                if (index == 0){ // 一个硬币
                                                                                                    if (video_num_coin <= coins){
                                                                                                        iLike = true;
                                                                                                        iLShow = true;
                                                                                                        iPopTipE = false;
                                                                                                        PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");
                                                                                                        new Thread(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                for (int x = 0; x < video_num_coin; x++){
                                                                                                                    if (!iLike){
                                                                                                                        PopTip.show("操作终止", "确定");
                                                                                                                        handler.sendEmptyMessage(8);
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    try {
                                                                                                                        Random random = new Random();
                                                                                                                        int number = random.nextInt(700) + 100;
                                                                                                                        Log.e("ms", String.valueOf(number));
                                                                                                                        Thread.sleep(number);
                                                                                                                    } catch (InterruptedException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                    String aid = String.valueOf(videoList.get(x).aid);

                                                                                                                    final OkHttpClient client = new OkHttpClient();
                                                                                                                    FormBody body = new FormBody.Builder()
                                                                                                                            .add("csrf", csrf)
                                                                                                                            .add("aid", aid)
                                                                                                                            .add("multiply", "1")
                                                                                                                            .build();

                                                                                                                    final Request request = new Request.Builder()
                                                                                                                            .url("https://api.bilibili.com/x/web-interface/coin/add")
                                                                                                                            .header("Cookie", cookie)
                                                                                                                            .post(body)
                                                                                                                            .build();

                                                                                                                    Response response = null;
                                                                                                                    try {
                                                                                                                        response = client.newCall(request).execute();
                                                                                                                        if (response.isSuccessful()) {
                                                                                                                            String res = response.body().string();
                                                                                                                            Log.i("res","打印POST响应的数据：" + res);
                                                                                                                            if (res.contains("超过投币上限啦")){
                                                                                                                                Log.e("超过投币上限啦", "超过投币上限啦");
                                                                                                                                handler.sendEmptyMessage(10);
                                                                                                                            }
                                                                                                                            handler.sendEmptyMessage(14);
                                                                                                                            pLike++;
                                                                                                                        } else {
                                                                                                                            handler.sendEmptyMessage(9);
                                                                                                                            throw new IOException("Unexpected code " + response);
                                                                                                                        }
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }

                                                                                                                }


                                                                                                                handler.sendEmptyMessage(8);

                                                                                                            }
                                                                                                        }).start();
                                                                                                    }else{
                                                                                                        BottomDialog.show("你太穷了", "年轻人不该超前消费，你的硬币余额不够给" + video_num_coin + "个视频投币。\n所需硬币数" + video_num_coin + "个\n现有硬币数" + coins + "个")
                                                                                                                .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                                                    @Override
                                                                                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                                        //...
                                                                                                                        return false;
                                                                                                                    }
                                                                                                                })
                                                                                                                .setOkButton("向开发者借硬币", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                                                    @Override
                                                                                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                                        FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_nibeipianle) {
                                                                                                                            @SuppressLint("SetJavaScriptEnabled")
                                                                                                                            @Override
                                                                                                                            public void onBind(FullScreenDialog dialog, View v) {
                                                                                                                                WebView webView = v.findViewById(R.id.wv_pian);
                                                                                                                                webView.getSettings().setJavaScriptEnabled(true);
                                                                                                                                webView.setWebViewClient(new WebViewClient());
                                                                                                                                webView.loadUrl("https://www.bilibili.com/video/BV1GJ411x7h7");
                                                                                                                            }
                                                                                                                        });
                                                                                                                        return false;
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                }
                                                                                                if (index == 1){ // 两个硬币
                                                                                                    if (video_num_coin * 2 <= coins){
                                                                                                        iLike = true;
                                                                                                        iLShow = true;
                                                                                                        iPopTipE = false;
                                                                                                        PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");
                                                                                                        new Thread(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                for (int x = 0; x < video_num_coin; x++){
                                                                                                                    if (!iLike){
                                                                                                                        PopTip.show("操作终止", "确定");
                                                                                                                        handler.sendEmptyMessage(8);
                                                                                                                        break;
                                                                                                                    }
                                                                                                                    try {
                                                                                                                        Random random = new Random();
                                                                                                                        int number = random.nextInt(700) + 100;
                                                                                                                        Log.e("ms", String.valueOf(number));
                                                                                                                        Thread.sleep(number);
                                                                                                                    } catch (InterruptedException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                    String aid = String.valueOf(videoList.get(x).aid);

                                                                                                                    final OkHttpClient client = new OkHttpClient();
                                                                                                                    FormBody body = new FormBody.Builder()
                                                                                                                            .add("csrf", csrf)
                                                                                                                            .add("aid", aid)
                                                                                                                            .add("multiply", "2")
                                                                                                                            .build();

                                                                                                                    final Request request = new Request.Builder()
                                                                                                                            .url("https://api.bilibili.com/x/web-interface/coin/add")
                                                                                                                            .header("Cookie", cookie)
                                                                                                                            .post(body)
                                                                                                                            .build();

                                                                                                                    Response response = null;
                                                                                                                    try {
                                                                                                                        response = client.newCall(request).execute();
                                                                                                                        if (response.isSuccessful()) {
                                                                                                                            String res = response.body().string();
                                                                                                                            Log.i("res","打印POST响应的数据：" + res);
                                                                                                                            if (res.contains("超过投币上限啦")){
                                                                                                                                Log.e("超过投币上限啦", "超过投币上限啦");
                                                                                                                                handler.sendEmptyMessage(10);
                                                                                                                            }
                                                                                                                            handler.sendEmptyMessage(14);
                                                                                                                            pLike++;
                                                                                                                        } else {
                                                                                                                            handler.sendEmptyMessage(9);
                                                                                                                            throw new IOException("Unexpected code " + response);
                                                                                                                        }
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }

                                                                                                                }


                                                                                                                handler.sendEmptyMessage(8);

                                                                                                            }
                                                                                                        }).start();
                                                                                                    }else{
                                                                                                        BottomDialog.show("你太穷了", "年轻人不该超前消费，你的硬币余额不够给" + video_num_coin + "个视频投币。\n所需硬币数" + video_num_coin * 2 + "个\n现有硬币数" + coins + "个")
                                                                                                                .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                                                    @Override
                                                                                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                                        //...
                                                                                                                        return false;
                                                                                                                    }
                                                                                                                })
                                                                                                                .setOkButton("向开发者借硬币", new OnDialogButtonClickListener<BottomDialog>() {
                                                                                                                    @Override
                                                                                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                                                        FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_nibeipianle) {
                                                                                                                            @SuppressLint("SetJavaScriptEnabled")
                                                                                                                            @Override
                                                                                                                            public void onBind(FullScreenDialog dialog, View v) {
                                                                                                                                WebView webView = v.findViewById(R.id.wv_pian);
                                                                                                                                webView.getSettings().setJavaScriptEnabled(true);
                                                                                                                                webView.setWebViewClient(new WebViewClient());
                                                                                                                                webView.loadUrl("https://www.bilibili.com/video/BV1GJ411x7h7");
                                                                                                                            }
                                                                                                                        });
                                                                                                                        return false;
                                                                                                                    }
                                                                                                                });
                                                                                                    }
                                                                                                }
                                                                                                return false;
                                                                                            }
                                                                                        });
                                                                            }
                                                                            return false;
                                                                        }
                                                                    });
                                                        } catch (Exception e) {
                                                            Log.e("内容", inputStr.trim());
                                                            if (Objects.equals(inputStr.trim(), "all") || Objects.equals(inputStr.trim(), "All") || Objects.equals(inputStr.trim(), "ALL")){
                                                                int num = coins;
                                                                BottomDialog.show("你疯了吗？", "该操作一旦执行无法撤销\n你的硬币还剩余额" + coins + "个\n预计还能投" + num + "个视频")
                                                                        .setCancelButton("悄悄跑路", new OnDialogButtonClickListener<BottomDialog>() {
                                                                            @Override
                                                                            public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                return false;
                                                                            }
                                                                        })
                                                                        .setOkButton("我是认真的！", new OnDialogButtonClickListener<BottomDialog>() {
                                                                            @Override
                                                                            public boolean onClick(BottomDialog baseDialog, View v) {
                                                                                iLike = true;
                                                                                iLShow = true;
                                                                                iPopTipE = false;
                                                                                PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!\n该区块没有经过排雷，如有BUG请反馈");
                                                                                new Thread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        for (int x = 0; x < num; x++){
                                                                                            if (!iLike){
                                                                                                PopTip.show("操作终止", "确定");
                                                                                                handler.sendEmptyMessage(8);
                                                                                                break;
                                                                                            }
                                                                                            try {
                                                                                                Random random = new Random();
                                                                                                int number = random.nextInt(700) + 100;
                                                                                                Log.e("ms", String.valueOf(number));
                                                                                                Thread.sleep(number);
                                                                                            } catch (InterruptedException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            String aid = null;
                                                                                            try {
                                                                                                aid = String.valueOf(videoList.get(x).aid);
                                                                                            } catch (Exception exception) {
                                                                                                handler.sendEmptyMessage(21);
                                                                                                Log.e("error", String.valueOf(exception));
                                                                                                break;
                                                                                            }
                                                                                            final OkHttpClient client = new OkHttpClient();
                                                                                            FormBody body = new FormBody.Builder()
                                                                                                    .add("csrf", csrf)
                                                                                                    .add("aid", aid)
                                                                                                    .add("multiply", "1")
                                                                                                    .build();

                                                                                            final Request request = new Request.Builder()
                                                                                                    .url("https://api.bilibili.com/x/web-interface/coin/add")
                                                                                                    .header("Cookie", cookie)
                                                                                                    .post(body)
                                                                                                    .build();

                                                                                            Response response = null;
                                                                                            try {
                                                                                                response = client.newCall(request).execute();
                                                                                                if (response.isSuccessful()) {
                                                                                                    String res = response.body().string();
                                                                                                    Log.i("res","打印POST响应的数据：" + res);
                                                                                                    if (res.contains("超过投币上限啦")){
                                                                                                        Log.e("超过投币上限啦", "超过投币上限啦");
                                                                                                        handler.sendEmptyMessage(10);
                                                                                                    }
                                                                                                    handler.sendEmptyMessage(17);
                                                                                                    pLike++;
                                                                                                } else {
                                                                                                    handler.sendEmptyMessage(9);
                                                                                                    throw new IOException("Unexpected code " + response);
                                                                                                }
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            }

                                                                                        }


                                                                                        handler.sendEmptyMessage(8);

                                                                                    }
                                                                                }).start();
                                                                                return false;
                                                                            }
                                                                        });
                                                            }else{
                                                                BottomDialog.show("这是什么？", "我还看不懂这些(º﹃º)\n如果想让我干活，必须给我一个合适的指令哦");
                                                            }
                                                            e.printStackTrace();
                                                        }
                                                        return false;
                                                    }
                                                })
                                                .show();
                                    }
                                    if (index == 2 && !iLike){ // 收藏
                                        // https://api.bilibili.com/x/v3/fav/resource/deal
                                        BottomMenu.show(new String[]{"取消收藏", "收藏"})
                                                .setMessage("选项")
                                                .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                    @Override
                                                    public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                        if (index == 1){
                                                            iLike = true;
                                                            iLShow = true;
                                                            iPopTipE = false;
                                                            PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");

                                                            String[] fList = new String[favoriteList.size()];
                                                            for (int x = 0; x < fList.length; x++){
                                                                fList[x] = favoriteList.get(x).title;
                                                            }

                                                            BottomMenu.show(fList)
                                                                    .setMessage("选择收藏夹")
                                                                    .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                                        @Override
                                                                        public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                                            int mid = favoriteList.get(index).id;
                                                                            Log.e("title", favoriteList.get(index).title);

                                                                            new Thread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    for (int x = 0; x < videoList.size(); x++){
                                                                                        if (!iLike){
                                                                                            PopTip.show("操作终止", "确定");
                                                                                            handler.sendEmptyMessage(8);
                                                                                            break;
                                                                                        }
                                                                                        try {
                                                                                            Random random = new Random();
                                                                                            int number = random.nextInt(700) + 100;
                                                                                            Log.e("ms", String.valueOf(number));
                                                                                            Thread.sleep(number);
                                                                                        } catch (InterruptedException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                        String aid = String.valueOf(videoList.get(x).aid);

                                                                                        final OkHttpClient client = new OkHttpClient();
                                                                                        FormBody body = new FormBody.Builder()
                                                                                                .add("csrf", csrf)
                                                                                                .add("rid", aid)
                                                                                                .add("type", "2")
                                                                                                .add("add_media_ids", String.valueOf(mid))
                                                                                                .build();

                                                                                        final Request request = new Request.Builder()
                                                                                                .url("https://api.bilibili.com/x/v3/fav/resource/deal")
                                                                                                .header("Cookie", cookie)
                                                                                                .post(body)
                                                                                                .build();

                                                                                        Response response = null;
                                                                                        try {
                                                                                            response = client.newCall(request).execute();
                                                                                            if (response.isSuccessful()) {
                                                                                                String res = response.body().string();
                                                                                                Log.i("res","打印POST响应的数据：" + res);
                                                                                                handler.sendEmptyMessage(18);
                                                                                                pLike++;
                                                                                            } else {
                                                                                                handler.sendEmptyMessage(9);
                                                                                                throw new IOException("Unexpected code " + response);
                                                                                            }
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }

                                                                                    }


                                                                                    handler.sendEmptyMessage(8);

                                                                                }
                                                                            }).start();
                                                                            return false;
                                                                        }
                                                                    });
                                                        }else{
                                                            iLike = true;
                                                            iLShow = true;
                                                            iPopTipE = false;
                                                            PopTip.show("为了防止IP被限制，当前操作延迟为100 ~ 800毫秒!");

                                                            String[] fList = new String[favoriteList.size()];
                                                            for (int x = 0; x < fList.length; x++){
                                                                fList[x] = favoriteList.get(x).title;
                                                            }

                                                            BottomMenu.show(fList)
                                                                    .setMessage("选择收藏夹")
                                                                    .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                                                        @Override
                                                                        public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                                                            int mid = favoriteList.get(index).id;
                                                                            Log.e("title", favoriteList.get(index).title);

                                                                            new Thread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    for (int x = 0; x < videoList.size(); x++){
                                                                                        if (!iLike){
                                                                                            PopTip.show("操作终止", "确定");
                                                                                            handler.sendEmptyMessage(8);
                                                                                            break;
                                                                                        }
                                                                                        try {
                                                                                            Random random = new Random();
                                                                                            int number = random.nextInt(700) + 100;
                                                                                            Log.e("ms", String.valueOf(number));
                                                                                            Thread.sleep(number);
                                                                                        } catch (InterruptedException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                        String aid = String.valueOf(videoList.get(x).aid);

                                                                                        final OkHttpClient client = new OkHttpClient();
                                                                                        FormBody body = new FormBody.Builder()
                                                                                                .add("csrf", csrf)
                                                                                                .add("rid", aid)
                                                                                                .add("type", "2")
                                                                                                .add("del_media_ids", String.valueOf(mid))
                                                                                                .build();

                                                                                        final Request request = new Request.Builder()
                                                                                                .url("https://api.bilibili.com/x/v3/fav/resource/deal")
                                                                                                .header("Cookie", cookie)
                                                                                                .post(body)
                                                                                                .build();

                                                                                        Response response = null;
                                                                                        try {
                                                                                            response = client.newCall(request).execute();
                                                                                            if (response.isSuccessful()) {
                                                                                                String res = response.body().string();
                                                                                                Log.i("res","打印POST响应的数据：" + res);
                                                                                                handler.sendEmptyMessage(19);
                                                                                                pLike++;
                                                                                            } else {
                                                                                                handler.sendEmptyMessage(9);
                                                                                                throw new IOException("Unexpected code " + response);
                                                                                            }
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }

                                                                                    }


                                                                                    handler.sendEmptyMessage(8);

                                                                                }
                                                                            }).start();
                                                                            return false;
                                                                        }
                                                                    });
                                                        }
                                                        return false;
                                                    }
                                                });
                                    }

                                    if (index == 3){
                                        iLike = false;
                                    }
                                    return false;
                                }
                            });
                }
            }
        });

        mBtnPiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Thread> threads = new ArrayList<Thread>();
                iShow = false;

                if (!iLike){
                    BottomMenu.show(new String[]{"轻量版（没有风险，精度低）", "暴力爬虫（指定页数，精度高）", "放弃"})
                            .setMessage("选择计算方式")
                            .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                                @Override
                                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                    if (index == 0){
                                        try {
                                            sNbp = Integer.parseInt(sLikes);
                                            sView = Integer.parseInt(sViewArchive);
                                            handler.sendEmptyMessage(5);
                                        } catch (NumberFormatException e) {
                                            BottomDialog.show("哎呀，访问被拒绝了", "当前处于无COOKIE状态（用户未登录），点击登录自动获取。")
                                                    .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                                        @Override
                                                        public boolean onClick(BottomDialog baseDialog, View v) {
                                                            PopTip.show("请放心，软件无服务器(穷)，不会自动上传您的任何使用数据"); // 确实没有服务器，穷
                                                            return false;
                                                        }
                                                    })
                                                    .setOkButton("登录", new OnDialogButtonClickListener<BottomDialog>() {
                                                        @Override
                                                        public boolean onClick(BottomDialog baseDialog, View v) {
                                                            Intent intent = new Intent(UIDActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            return false;
                                                        }
                                                    });
                                            e.printStackTrace();
                                        }

                                    }
                                    if (index == 1){
                                        BottomDialog.show("警告！", "暴力频繁计算会被哔哩哔哩风控（操作被拦截）\n请不要频繁使用此功能！\n这可能会让你短时间内无法使用次软件。\n目前选定第" + sPage + "页")
                                                .setCancelButton("我不玩了", new OnDialogButtonClickListener<BottomDialog>() {
                                                    @Override
                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                        //...
                                                        return false;
                                                    }
                                                })
                                                .setOkButton("计算", new OnDialogButtonClickListener<BottomDialog>() {
                                                    @Override
                                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                                        for (int x = 0; x < videoList.size(); x++){
                                                            if (videoList.get(x).p == sPage){
                                                                int finalX = x;
                                                                Thread thread=new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        String view = null;
                                                                        String danmaku = null;
                                                                        String reply = null;
                                                                        String favorite = null;
                                                                        String coin = null;
                                                                        String share_ = null;
                                                                        String like = null;
                                                                        String cid = null;
                                                                        JSONObject checkJSON;
                                                                        try {
                                                                            // 获取昵称
                                                                            String urlPath = "https://api.bilibili.com/x/web-interface/view?bvid=" +  videoList.get(finalX).bvid;
                                                                            URL url = new URL(urlPath);
                                                                            URLConnection conn = url.openConnection();
                                                                            conn.setRequestProperty("Cookie", cookie);
                                                                            conn.setDoInput(true);
                                                                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                                                            StringBuilder sb = new StringBuilder();
                                                                            String line = null;
                                                                            while ((line = br.readLine()) != null) {
                                                                                sb.append(line);
                                                                            }
                                                                            String html = String.valueOf(sb);
//                                    String html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/web-interface/view?bvid=" +  videoList.get(finalX).bvid);
                                                                            Log.i("jsonData",html);
                                                                            JSONObject result = new JSONObject(html);
                                                                            result = (JSONObject) result.get("data");
                                                                            cid = result.get("cid").toString();
                                                                            String html2 = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/web-interface/archive/stat?bvid=" + videoList.get(finalX).bvid);
                                                                            Log.i("jsonData",html2);
                                                                            JSONObject result2 = new JSONObject(html2);
                                                                            result2 = (JSONObject) result2.get("data");
                                                                            checkJSON = result2;

                                                                            {
                                                                                try {
                                                                                    view = result2.get("view").toString();
                                                                                    danmaku = result2.get("danmaku").toString();
                                                                                    reply = result2.get("reply").toString();
                                                                                    favorite = result2.get("favorite").toString();
                                                                                    coin = result2.get("coin").toString();
                                                                                    share_ = result2.get("share").toString();
                                                                                    like = result2.get("like").toString();
                                                                                    reply = result2.get("reply").toString();
                                                                                    Log.e("11", cid);
                                                                                    sNbp = sNbp + Integer.parseInt(danmaku) + Integer.parseInt(reply) + Integer.parseInt(favorite) + Integer.parseInt(coin) + Integer.parseInt(share_) + Integer.parseInt(like);
                                                                                    sView = sView + Integer.parseInt(view);
                                                                                    Log.e("sView", String.valueOf(sView));
                                                                                    Log.e("sNbp", String.valueOf(sNbp));
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }

                                                                        } catch (Exception e) {
                                                                            if (!iShow){ // 有请求被拒绝
                                                                                PopTip.build()
                                                                                        .setStyle(KongzueStyle.style())
                                                                                        .setMessage("数据请求过大，部分(基本)请求被拒绝，数据精度下降")
                                                                                        .setButton("确定")
                                                                                        .show();
                                                                                iShow = true;
                                                                            }
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                });
                                                                threads.add(thread);
                                                                thread.start();
                                                            }

                                                        }

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                for (int z = 0; z < threads.size(); z++){
                                                                    try {
                                                                        threads.get(z).join();
                                                                    } catch (InterruptedException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                handler.sendEmptyMessage(5);
                                                            }

                                                        }).start();

                                                        return false;
                                                    }
                                                });

                                    }
                                    return false;
                                }
                            });
                }else {
                    PopTip.build()
                            .setStyle(KongzueStyle.style())
                            .setMessage("当前正在执行自动化任务！")
                            .setButton("终止任务", new OnDialogButtonClickListener<PopTip>() {
                                @Override
                                public boolean onClick(PopTip baseDialog, View v) {
                                    iLike = false;
                                    return false;
                                }
                            })
                            .show();
                }


                // https://api.bilibili.com/x/web-interface/archive/stat?bvid=BV1pb411e7BW


            }
        });

        stepperTouch.addStepCallback(new OnStepCallback() {
            @Override
            public void onStep(int value, boolean positive) {
                PopTip.build()
                        .setStyle(KongzueStyle.style())
                        .setMessage("指定视频=>第" + value + "页")
                        .setButton("确定")
                        .show();
                sPage = value;


            }

        });


        // 展示大图
        mIvBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(UIDActivity.this)
                        .asImageViewer(mIvBg, imgUrl, new SmartGlideImageLoader())
                        .show();
            }
        });

        // 展示数据
        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(UIDActivity.this)
                        .asBottomList("个人数据", new String[]{"性别: "+ sex, "UID长度: " + UIDl, "生日: " + birthday, "大会员到期: " + due_date, "房间号: " + liveRoomID},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {

                                    }
                                })
                        .show();
            }
        });

        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean iCookie = share.getBoolean("iCookie",false);
                Log.e("2333", String.valueOf(iCookie));
                // https://api.bilibili.com/x/space/upstat?mid=473400804
                if (!iCookie){
                    BottomDialog.show("哎呀，访问被拒绝了", "当前处于无COOKIE状态（用户未登录），点击登录自动获取。")
                            .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                @Override
                                public boolean onClick(BottomDialog baseDialog, View v) {
                                    PopTip.show("请放心，软件无服务器(穷)，不会自动上传您的任何使用数据"); // 确实没有服务器，穷
                                    return false;
                                }
                            })
                            .setOkButton("登录", new OnDialogButtonClickListener<BottomDialog>() {
                                @Override
                                public boolean onClick(BottomDialog baseDialog, View v) {
                                    Intent intent = new Intent(UIDActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return false;
                                }
                            });

                }else{
                    new XPopup.Builder(UIDActivity.this)
                            .asBottomList("数据总计", new String[]{"总赞数: "+ sLikes, "视频总浏览: " + sViewArchive, "文章总浏览: " + sViewArticle},
                                    new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String text) {

                                        }
                                    })
                            .show();
                }
            }
        });

        mBtnAllView.setOnClickListener(new View.OnClickListener() { // 获取指定页数视频
            @Override
            public void onClick(View v) {
                l = new String[30];
                l2 = new Videos[30];

                int y = 0;
                for (int x = 0; x < videoList.size(); x++){
                    if (videoList.get(x).p == sPage){
                        Log.e("sss", videoList.get(x).title + "=>[" +videoList.get(x).length + "]");
                        l[y] = videoList.get(x).title + "=>[" +videoList.get(x).length + "]";
                        l2[y] = new Videos(videoList.get(x).title, videoList.get(x).length, videoList.get(x).aid, videoList.get(x).bvid, videoList.get(x).pic, videoList.get(x).p);
                        y++;
                    }
                }

                BottomMenu.show(l)
                        .setMessage("第" + sPage + "页的视频")
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                int ix = ((30 * sPage) - (30 - index)) - 30*(sPage - 1);
                                if (sPage == 1){
                                    Log.e("title", l2[index].title);
                                    Intent intent = new Intent(UIDActivity.this, AVActivity.class);
                                    intent.putExtra("link","https://www.bilibili.com/video/" + l2[index].bvid);
                                    startActivity(intent);
                                }else{
                                    try {
                                        Log.e("title", l2[ix].title);
                                        Intent intent = new Intent(UIDActivity.this, AVActivity.class);
                                        intent.putExtra("link","https://www.bilibili.com/video/" + l2[ix].bvid);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                    }
                                }
                                return false;
                            }
                        });
            }
        });


        // 顶部名字点击事件
        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show(new String[]{"复制头像链接", "复制主页链接", "复制昵称"})
                        .setMessage("其他选项")
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                            @Override
                            public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                                if (index == 0){ // 复制头像链接
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData mClipData = ClipData.newPlainText("Label", imgUrl);
                                    cm.setPrimaryClip(mClipData);
                                    PopTip.show(R.mipmap.ok, "链接复制成功", "确定");
                                }
                                if (index == 1){ // 复制主页链接
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData mClipData = ClipData.newPlainText("Label", "https://space.bilibili.com/" + uid);
                                    cm.setPrimaryClip(mClipData);
                                    PopTip.show(R.mipmap.ok, "链接复制成功", "确定");
                                }
                                if (index == 2){ // 复制昵称
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData mClipData = ClipData.newPlainText("Label", name);
                                    cm.setPrimaryClip(mClipData);
                                    PopTip.show(R.mipmap.ok, "昵称复制成功", "确定");
                                }
                                return false;
                            }
                        });
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handler.sendEmptyMessage(2);

                    try {
                        String html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/space/acc/info?mid="+uid);
                        Log.i("jsonData",html);
                        JSONObject result = new JSONObject(html);
                        result = (JSONObject) result.get("data");
                        name = result.get("name").toString();
                        imgUrl = result.get("face").toString();
                        sex = result.get("sex").toString();
                        UIDl = String.valueOf(uid.length());
                        birthday = result.get("birthday").toString();
                        JSONObject vip = (JSONObject) result.get("vip");
                        due_date = vip.get("due_date").toString();

                        try {
                            JSONObject live_room = (JSONObject) result.get("live_room");
                            liveRoomID = live_room.get("roomid").toString();
                        } catch (Exception e) {
                            liveRoomID = "ta貌似木有这项数据"; // 没有直播过
                            e.printStackTrace();
                        }
                        due_date = timeToFormat(Long.parseLong(due_date));

                        Log.e("UIDl", UIDl);

                        String cookie = share.getString("cookie","");

                        if (Objects.equals(cookie, "")){
                            edt.putBoolean("iCookie", false);
                        }else {
                            String urlPath = "https://api.bilibili.com/x/space/upstat?mid=" + uid;
                            URL url = new URL(urlPath);
                            URLConnection conn = url.openConnection();
                            conn.setRequestProperty("Cookie", cookie);
                            conn.setDoInput(true);
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }



                            // https://api.bilibili.com/x/v3/fav/folder/created/list-all?type=2&up_mid=473400804
                            Log.e("html", String.valueOf(sb));

                            if (sb.toString().contains("\"data\":{}")){
                                handler.sendEmptyMessage(3);
                                edt.putBoolean("iCookie", false);
                            }

                            Log.e("cookie", cookie);
                            String[] cookieList = cookie.split(";");
                            for (int x = 0; x < cookieList.length; x++){
                                if (cookieList[x].contains("bili_jct=")){
                                    csrf = cookieList[x].replaceAll("bili_jct=", "").trim();
                                }
                                if (cookieList[x].contains("DedeUserID=")){
                                    imUID = cookieList[x].replaceAll("DedeUserID=", "").trim();
                                }
                            }
//                            Log.e("cookie", cookieList); 一定要小心，下标竟然不同账号不一样
//                            csrf = cookieList[10].replaceAll("bili_jct=", "").trim();
//                            imUID = cookieList[11].replaceAll("DedeUserID=", "").trim();
                            Log.e("bili_jct", csrf);
                            Log.e("imUID", imUID);
                            // {"code":0,"message":"0","ttl":1,"data":{"archive":{"view":45027262},"article":{"view":0},"likes":4873659}}
                            JSONObject data = new JSONObject(String.valueOf(sb));
                            data = (JSONObject) data.get("data");
                            sLikes = data.get("likes").toString();
                            JSONObject archive = new JSONObject(((JSONObject) data.get("archive")).toString());
                            JSONObject article = new JSONObject(((JSONObject) data.get("article")).toString());

                            sViewArchive = archive.get("view").toString();
                            sViewArticle = article.get("view").toString();

                            urlPath = "https://account.bilibili.com/site/getCoin";
                            url = new URL(urlPath);
                            conn = url.openConnection();
                            conn.setRequestProperty("Cookie", cookie);
                            conn.setDoInput(true);
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            sb = new StringBuilder();
                            line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                            data = (JSONObject) new JSONObject(String.valueOf(sb)).get("data");
                            Log.e("data", String.valueOf(data));
                            try {
                                coins = data.getInt("money");
                            } catch (Exception e) {
                                Log.e("eee", String.valueOf(e));
                                coins = 0;
                            }
                            Log.e("coins", String.valueOf(coins));


//                            html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/space/upstat?mid=" + uid);
                            urlPath = "https://api.bilibili.com/x/v3/fav/folder/created/list-all?type=2&up_mid=" + imUID;
                            url = new URL(urlPath);
                            conn = url.openConnection();
                            conn.setRequestProperty("Cookie", cookie);
                            conn.setDoInput(true);
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            sb = new StringBuilder();
                            line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            Log.e("sb", String.valueOf(sb));
                            JSONObject data_ = new JSONObject(String.valueOf(sb));
                            data_ = (JSONObject) data_.get("data");
                            JSONArray jsonArray = (JSONArray) data_.get("list");
                            for (int x = 0; x < jsonArray.length(); x++){
                                String title = (String) ((JSONObject) jsonArray.get(x)).get("title");
                                int id = (int) ((JSONObject) jsonArray.get(x)).get("id");
                                int fid = (int) ((JSONObject) jsonArray.get(x)).get("fid");
                                int mid = (int) ((JSONObject) jsonArray.get(x)).get("mid");
                                int media_count = (int) ((JSONObject) jsonArray.get(x)).get("media_count");
                                favoriteList.add(new Favorite(title, id, fid, mid, media_count));
                            }
                        }

                        handler.sendEmptyMessage(0);
                        Log.e("name", name);
                        Log.e("imgUrl", imgUrl);


                        handler.sendEmptyMessage(4); // 获取视频列表

//                        iLoading = false;
                    } catch (Exception e) {
                        finish();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    BottomDialog.show("你的连接出了点问题", "无法获取目标UID信息。但我知道，这一定是你的问题")
                                            .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                                                @Override
                                                public boolean onClick(BottomDialog baseDialog, View v) {
                                                    return false;
                                                }
                                            });
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        }).start();
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        if (!iLoading){
                            handler.sendEmptyMessage(1);
                            break;
                        }
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    // 视频列表
    static class Videos{ // 结构体
        String title;
        String length;
        int aid;
        String bvid;
        String pic;
        int p;


        public Videos(String title, String length, int aid, String bvid, String pic, int p) {
            this.title = title;
            this.length = length;
            this.aid = aid;
            this.bvid = bvid;
            this.pic = pic;
            this.p = p;
        }
    }

    // 收藏夹
    static class Favorite{ // 结构体
        String title;
        int id;
        int fid;
        int mid;
        int media_count;


        public Favorite(String title, int id, int fid, int mid, int media_count) {
            this.title = title;
            this.id = id;
            this.fid = fid;
            this.mid = mid;
            this.media_count = media_count;
        }
    }

    public static String timeToFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return sdf.format(time);
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        //弱引用持有HandlerActivity , GC 回收时会被回收掉
        private WeakReference<UIDActivity> weakReference;

        public MyHandler(UIDActivity activity) {
            this.weakReference = new WeakReference(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) { // 子线程不能更新UI
            UIDActivity activity = weakReference.get();
            super.handleMessage(msg);

            if (msg.what == 0){
                mTvName.setText(name);

                Glide.with(UIDActivity.this)
                        .load(imgUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))//圆角半径
                        .into(mIvBg);
            }

            if (msg.what == 1){
                xpLoading.smartDismiss();
            }

            if (msg.what == 2){
                xpLoading = new XPopup.Builder(UIDActivity.this)
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asLoading("获取信息中...")
                        .show();
            }


            if (msg.what == 3){
                BottomDialog.show("失效的COOKIE", "COOKIE目前失效（无法获取），请重新登录！")
                        .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                PopTip.show("请放心，软件无服务器(穷)，不会自动上传您的任何使用数据"); // 确实没有服务器，穷
                                return false;
                            }
                        })
                        .setOkButton("登录", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                Intent intent = new Intent(UIDActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        });
            }

            if (msg.what == 4){ // 获取视频列表
                //  https://api.bilibili.com/x/space/arc/search?mid=473400804&pn=1&ps=25&index=1&jsonp=jsonp
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String html_ = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/space/arc/search?mid="+uid+"&pn=" + "1");
                            JSONObject rp = new JSONObject(html_);
                            rp = (JSONObject) rp.get("data");
                            rp = (JSONObject) rp.get("page");
                            int x = (int) rp.get("count");

                            vc = x;
                            if (x > 800){
                                iParse = 0;
                                handler.sendEmptyMessage(11);
                            }else{
                                iParse = 1;
                            }

                            while (true){
                                if (iParse == 1){
                                    break;
                                }
                                if (iParse == 2){
                                    break;
                                }
                            }

                            if (iParse != 2){
                                iLoading = true;
                                handler.sendEmptyMessage(15);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            while (true){
                                                if (!iLoading){
                                                    handler.sendEmptyMessage(16);
                                                    break;
                                                }
                                                Thread.sleep(10);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }).start();
//                                double xb = x / 30.0;
                                if (x % 30 != 0){
                                    x = (x / 30) + 1;
                                }
//                                Log.e("xb", String.valueOf(xb));
//                                x = getSiShe(xb);
                                stepperTouch.setMaxValue(x);
                                Log.e("page = ", String.valueOf(x)); // 四舍五入后的结果

                                int y = 0;
                                for (int p = 1; p <= x; p++){
                                    String html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/space/arc/search?mid="+uid+"&pn=" + p);
                                    Log.e("jsonData",html);
                                    JSONObject result = new JSONObject(html);
                                    result = (JSONObject) result.get("data");
                                    result = (JSONObject) result.get("list");
//                                result = (JSONObject) result.get("vlist");
                                    JSONArray video = (JSONArray) result.get("vlist");
                                    for (int i = 0; i < video.length(); i++){
                                        Log.e("res", String.valueOf(video.get(i)));
                                        String title = (String) ((JSONObject) video.get(i)).get("title");
                                        String length = (String) ((JSONObject) video.get(i)).get("length");
                                        int aid = (int) ((JSONObject) video.get(i)).get("aid");
                                        String bvid = (String) ((JSONObject) video.get(i)).get("bvid");
                                        String pic = (String) ((JSONObject) video.get(i)).get("pic");
//                                    Log.e("title=>", title);
                                        videoList.add(new Videos(title, length, aid, bvid, pic, p));
                                        y++;
                                    }

                                }

                                Log.e("y = ", String.valueOf(y));
                                if (y == 0){
                                    handler.sendEmptyMessage(20);
                                }
                                iLoading = false;
                            }
                        } catch (Exception e) {
                            handler.sendEmptyMessage(12);
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
            if (msg.what == 5){
                float baipiao = (float) (sNbp / (float) (sView) * 100.0);

                Log.e("sView", String.valueOf(sView));
                Log.e("sNbp", String.valueOf(sNbp));
                Log.e("baipiao", String.valueOf(baipiao));

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (float x = 0; x < 100.0 - baipiao; x+=0.1){
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Fx = x;
                            handler.sendEmptyMessage(6);
                        }
                        mProgessPiao.setMaxCount(100);
                    }

                });
                thread.start();
            }
            if (msg.what == 6){
                mProgessPiao.setCurrentCount(Fx);
                mTvProgressBar.setText("白嫖率" + Fx +"%");
            }
            if (msg.what == 7){
                mProgessPiao.setMaxCount(videoList.size() - 1);
                mProgessPiao.setCurrentCount(pLike);
                mTvProgressBar.setText("点赞了" + pLike + "个视频");
            }

            if (msg.what == 13){
                mProgessPiao.setMaxCount(videoList.size() - 1);
                mProgessPiao.setCurrentCount(videoList.size() - pLike);
                mTvProgressBar.setText("取消点赞" + pLike + "个视频");
            }

            if (msg.what == 8){
                pLike = 0;
                mProgessPiao.setCurrentCount(0);
                mProgessPiao.setMaxCount(100);
                mTvProgressBar.setText("");
                if (iPopTipE){
                    popTipE.dismiss();
                }

                PopTip.build()
                        .setStyle(KongzueStyle.style())
                        .setMessage("自动化任务结束，点击确定关闭")
                        .setButton("确定")
                        .show()
                        .noAutoDismiss();
                iLike = false;
                iLShow = false;
            }

            if (msg.what == 9){
                PopTip.build()
                        .setStyle(KongzueStyle.style())
                        .setMessage("操作被拦截，自动终止")
                        .setButton("确定")
                        .show();
                iLike = false;
                pLike = 0;
            }

            if (msg.what == 10){
                if (iLShow){
                    popTipE = PopTip.build()
                            .setStyle(KongzueStyle.style())
                            .setMessage("重复任务，是否终止？")
                            .setButton("终止", new OnDialogButtonClickListener<PopTip>() {
                                @Override
                                public boolean onClick(PopTip baseDialog, View v) {
                                    iLike = false;
                                    return false;
                                }
                            })
                            .show()
                            .noAutoDismiss();
                    iLShow = false;
                    pLike = 0;
                    iPopTipE = true;
                }
            }

            if (msg.what == 11){
                iLoading = false;
                BottomDialog.show("警告！", "当前解析量超过800\n("+vc+"个视频)\n大量请求可能导致IP短暂封禁，是否继续解析？")
                        .setCancelButton("取消", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                iParse = 2;
                                finish();
                                //...
                                return false;
                            }
                        })
                        .setOkButton("继续", new OnDialogButtonClickListener<BottomDialog>() {
                            @Override
                            public boolean onClick(BottomDialog baseDialog, View v) {
                                iParse = 1;
                                return false;
                            }
                        });
            }

            if (msg.what == 12){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        PopTip.build()
                                .setStyle(KongzueStyle.style())
                                .setMessage("您的IP被短暂封禁！ 无法执行此操作")
                                .setButton("关闭")
                                .show();
                    }
                }).start();
                finish();

            }

            if (msg.what == 14){
                mProgessPiao.setMaxCount(video_num_coin);
                mProgessPiao.setCurrentCount(pLike);
                mTvProgressBar.setText("投币了" + pLike + "个视频");
            }
            if (msg.what == 17){
                mProgessPiao.setMaxCount(coins);
                mProgessPiao.setCurrentCount(pLike);
                mTvProgressBar.setText("投币了" + pLike + "个视频");
            }

            if (msg.what == 15){
                xpLoading2 = new XPopup.Builder(UIDActivity.this)
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asLoading("获取视频中...")
                        .show();
            }
            if (msg.what == 16){
                xpLoading2.smartDismiss();
            }
            if (msg.what == 21){
                PopTip.build()
                        .setStyle(KongzueStyle.style())
                        .setMessage("你的硬币数量高于视频数量！")
                        .setButton("确定")
                        .show()
                        .noAutoDismiss();
                iLike = false;
                pLike = 0;
            }

            if (msg.what == 18){
                mProgessPiao.setMaxCount(videoList.size() - 1);
                mProgessPiao.setCurrentCount(pLike);
                mTvProgressBar.setText("收藏了" + pLike + "个视频");
            }
            if (msg.what == 19){
                mProgessPiao.setMaxCount(videoList.size() - 1);
                mProgessPiao.setCurrentCount(videoList.size() - pLike);
                mTvProgressBar.setText("取消收藏了" + pLike + "个视频");
            }
            if (msg.what == 20){
                mBtnAllView.setEnabled(false);
                mBtnAllView.getBackground().setAlpha(100);
                stepperTouch.setMinValue(0);
                stepperTouch.setMinValue(0);
                stepperTouch.setCount(0);
                mBtnAuto.setEnabled(false);
                mBtnAuto.getBackground().setAlpha(100);
                mBtnPiao.setEnabled(false);
                mBtnPiao.getBackground().setAlpha(100);
                MessageDialog.show("TA好像在睡觉", "没有检测到该用户的任何视频。", "确定");
            }


        }

    }

    public static int getSiShe(double n1){
        //拿出n1的小数部分第一个数，double类型的数转换为int，要使用到强制类型转换
        int ge = (int)(n1*10)%10;
        //对n2进行判断，大于等于5，n1++
        if(ge >= 5){
            //继续用到强转
            return (int)n1+1;
        }else{
            return (int)n1;
        }
    }
}