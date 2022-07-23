package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.mmin18.widget.RealtimeBlurView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.SmartGlideImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * AV（视频）解析
 * 又是一座屎山 注释可怜 将就看吧
 * 希望不会出什么奇怪的BUG..*/

public class AVActivity extends AppCompatActivity {
    private String link;
    private String longLink;
    private ImageView mIvAv;
    private String BV;
    private boolean iGet = false;
    private BasePopupView xpLoading;
    private Handler handler = new AVActivity.MyHandler(this);
    private TextView mTvAV;
    private String pic;
    private ImageView mIvBg;
    private RealtimeBlurView mBlurBg;
    private Button mBtnCheck;
    private JSONObject checkJSON;
    private String view = null;
    private String danmaku = null;
    private String reply = null;
    private String favorite = null;
    private String coin = null;
    private String share = null;
    private String like = null;
    private CustomizedProgressBar mProgessPiao;
    private TextView mTvProgressBar;
    private TextView mTvPiao;
    private float Fx = 0.0F;
    private Button mBtnDownload;
    private String cid = null;
    private String avURL;
    private int p;
    private boolean iDownload = false;
    private int iTvd = 0;
    private Button mBtnDanmu;
    private int x = 0;
    private boolean iThread = false;
    private String danmu;
    private String title;
    private String aid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_av);

        mTvAV = findViewById(R.id.tv_av);
        mIvBg = findViewById(R.id.iv_bg);
        mBlurBg = findViewById(R.id.blur_bg);
        mBtnCheck = findViewById(R.id.btn_check);
        mProgessPiao = findViewById(R.id.progress);
        mTvProgressBar = findViewById(R.id.tv_data_integrity);
        mTvPiao = findViewById(R.id.tv_piao);
        mBtnDownload = findViewById(R.id.download_av);
        mBtnDanmu = findViewById(R.id.danmu_check);


        Intent i = getIntent();
        link = i.getStringExtra("link");
        link = getLink(link).get(0);
        Log.e("link", link);
        // https://www.bilibili.com/video/BV1u34y1j7nn

        mIvAv = findViewById(R.id.iv_av);



        mTvAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(AVActivity.this)
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{"点击下方复制", longLink, BV, title, "AV" + aid},
                                new int[]{R.drawable.copy},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        if (Objects.equals(text, longLink)){
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData mClipData = ClipData.newPlainText("Label", longLink);
                                            cm.setPrimaryClip(mClipData);
                                            PopTip.show(R.mipmap.ok, "链接复制成功", "确定"); // <- 这个吐司好看

//                                            下面的代码弹窗太丑被我无情抛弃
//                                            Toast toast=new Toast(getApplicationContext());
//
//                                            LayoutInflater inflater = LayoutInflater.from(AVActivity.this);
//                                            View view = inflater.inflate(R.layout.toast_ok,null);
//                                            toast.setView(view);
//                                            toast.setDuration(Toast.LENGTH_SHORT);
//                                            toast.setGravity(Gravity.CENTER,0,0);
//                                            toast.show();
                                        }
                                        if (Objects.equals(text, BV)){
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData mClipData = ClipData.newPlainText("Label2", BV);
                                            cm.setPrimaryClip(mClipData);

                                            PopTip.show(R.mipmap.ok, "BV号复制成功", "确定");
//                                            Toast toast=new Toast(getApplicationContext());
//
//                                            LayoutInflater inflater = LayoutInflater.from(AVActivity.this);
//                                            View view = inflater.inflate(R.layout.toast_ok,null);
//                                            toast.setView(view);
//                                            toast.setDuration(Toast.LENGTH_SHORT);
//                                            toast.setGravity(Gravity.CENTER,0,0);
//                                            toast.show();
                                        }
                                        if (Objects.equals(text, title)){
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData mClipData = ClipData.newPlainText("Label2", title);
                                            cm.setPrimaryClip(mClipData);

                                            PopTip.show(R.mipmap.ok, "标题复制成功", "确定");
                                        }
                                        if (Objects.equals(text, "AV" + aid)){
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData mClipData = ClipData.newPlainText("Label2", "AV" + aid);
                                            cm.setPrimaryClip(mClipData);

                                            PopTip.show(R.mipmap.ok, "AV号复制成功", "确定");
                                        }
                                    }
                                })
                        .show();
            }
        });


        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(AVActivity.this)
                        .asBottomList("视频数据", new String[]{"播放量: " + view, "弹幕: " + danmaku, "回复: " + reply, "收藏: " + favorite, "硬币: " + coin, "分享: " + share, "点赞：" + like},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        
                                    }
                                })
                        .show();
            }
        });


        new Thread(new Runnable() { // 判断是否获取完全
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
                while (true){
                    try {
                        Thread.sleep(10);
                        if (iGet){
                            handler.sendEmptyMessage(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

        // https://api.bilibili.com/x/web-interface/archive/stat?bvid=BV1xt4y1s7Mc

        // https://api.bilibili.com/x/web-interface/view?bvid=BV1xt4y1s7Mc
        redirectUrl(new AVActivity.OnSuccessListener() {
            @Override
            public void doLogic(String s) {
                //获取到重定向的url可以操作啦
                Log.i("TAG", "doLogic: "+s);
                longLink = s;
                BV = getBV(s).get(0);
                Log.e("BV", BV);
                mTvAV.setText(BV);
                iGet = true;
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 获取昵称
                            String html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/web-interface/view?bvid=" + BV);
                            Log.i("jsonData",html);
                            JSONObject result = new JSONObject(html);
                            result = (JSONObject) result.get("data");
                            title = (String) result.get("title");
                            aid = result.getString("aid");
                            cid = result.get("cid").toString();
                            String html2 = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/web-interface/archive/stat?bvid=" + BV);
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
                                    share = result2.get("share").toString();
                                    like = result2.get("like").toString();
                                    reply = result2.get("reply").toString();
                                    Log.e("11", cid);
                                    handler.sendEmptyMessage(3);
                                } catch (JSONException e) {
                                    e.printStackTrace();
}
                            }

                            Log.e("data", checkJSON.toString());
                            pic = result.get("pic").toString();
                            handler.sendEmptyMessage(2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                thread.start();

            }
        });

        mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences share_ = getSharedPreferences("temp",MODE_PRIVATE);
                SharedPreferences.Editor edt = share_.edit();
//                String cookie = share_.getString("cookie","");
                String cookie = share_.getString("cookie","");
                new XPopup.Builder(AVActivity.this)
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{"确定要下载" + BV + "吗", "确定", "取消"},
                                new int[]{},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        if (Objects.equals(text, "确定")){
                                            BottomDialog.show("清晰度？", "注：1080P需要登录")
                                                    .setCancelButton("720P", new OnDialogButtonClickListener<BottomDialog>() {
                                                        @Override
                                                        public boolean onClick(BottomDialog baseDialog, View v) {
                                                            OkHttpClient client = new OkHttpClient();
                                                            Request request1 = new Request.Builder()
                                                                    .url("https://api.bilibili.com/x/player/playurl?cid="+cid+"&bvid="+BV)
                                                                    .addHeader("referer",longLink)
                                                                    .build();

                                                            client.newCall(request1).enqueue(new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {

                                                                }
                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    if(response.code() >= 200 && response.code() < 300) {
                                                                        String result = response.body().string();
                                                                        Log.e("r", result);
                                                                        JSONObject result2 = null;
                                                                        try {
                                                                            result2 = new JSONObject(result);
                                                                            result2 = (JSONObject) result2.get("data");
                                                                            Log.e("r", String.valueOf(result2));
                                                                            JSONArray result3 = (JSONArray) result2.get("durl");
                                                                            String result4 = result3.getJSONObject(0).getString("url");
                                                                            Log.e("url", String.valueOf(result4));
                                                                            avURL = result4;
                                                                            handler.sendEmptyMessage(5);
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            return false;
                                                        }
                                                    })
                                                    .setOkButton("1080P",new OnDialogButtonClickListener<BottomDialog>() {
                                                        @Override
                                                        public boolean onClick(BottomDialog baseDialog, View v) {
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            boolean iCookie = share_.getBoolean("iCookie",false);
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
                                                                                                Intent intent = new Intent(AVActivity.this, LoginActivity.class);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                                return false;
                                                                                            }
                                                                                        });
                                                                            }else {
                                                                                if (Objects.equals(cookie, "")){
                                                                                    edt.putBoolean("iCookie", false);
                                                                                }else {
                                                                                    String urlPath = "https://api.bilibili.com/x/space/upstat?mid=" + "30502823";
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


                                                                                    Log.e("html", String.valueOf(sb));

                                                                                    if (sb.toString().contains("\"data\":{}")){
                                                                                        handler.sendEmptyMessage(11);
                                                                                        edt.putBoolean("iCookie", false);
                                                                                    }else{
                                                                                        OkHttpClient client = new OkHttpClient();
                                                                                        Request request1 = new Request.Builder()
                                                                                                .url("https://api.bilibili.com/x/player/playurl?cid="+cid+"&bvid="+BV)
                                                                                                .header("Cookie", cookie)
                                                                                                .addHeader("referer",longLink)
                                                                                                .build();

                                                                                        client.newCall(request1).enqueue(new Callback() {
                                                                                            @Override
                                                                                            public void onFailure(Call call, IOException e) {

                                                                                            }
                                                                                            @Override
                                                                                            public void onResponse(Call call, Response response) throws IOException {
                                                                                                if(response.code() >= 200 && response.code() < 300) {
                                                                                                    String result = response.body().string();
                                                                                                    Log.e("r", result);
                                                                                                    JSONObject result2 = null;
                                                                                                    try {
                                                                                                        result2 = new JSONObject(result);
                                                                                                        result2 = (JSONObject) result2.get("data");
                                                                                                        Log.e("r", String.valueOf(result2));
                                                                                                        JSONArray result3 = (JSONArray) result2.get("durl");
                                                                                                        String result4 = result3.getJSONObject(0).getString("url");
                                                                                                        Log.e("url", String.valueOf(result4));
                                                                                                        avURL = result4;
                                                                                                        handler.sendEmptyMessage(5);
                                                                                                    } catch (JSONException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                }).start();
                                                            return false;
                                                        }
                                                    });
                                        }
                                    }
                                })
                        .show();
            }
        });

        mIvAv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(AVActivity.this)
                        .asImageViewer(mIvAv, pic.trim(), new SmartGlideImageLoader())
                        .show();
            }
        });

        mBtnDanmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
//                SharedPreferences.Editor edt = share.edit();
//                String cookie = share.getString("cookie","");
//                Log.e("cookie" ,cookie);

                final String[] danmuList = {""};
                final boolean[] i = {true};
                iThread = false;
                handler.sendEmptyMessage(9);


                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            org.jsoup.nodes.Document document = Jsoup.connect("http://comment.bilibili.com/"+cid+".xml").get();
                            Elements d = document.getElementsByTag("d");
                            Elements time = document.getElementsByTag("p");
                            x = 0;
                            for(org.jsoup.nodes.Element element: d){
                                if (wordCount(element.text().replaceAll("\\\\","").replaceAll("'","").replaceAll("\"","")) > 3){
                                    if (i[0]){
                                        danmuList[0] = danmuList[0] + "'" + element.text().replaceAll("\\\\","").replaceAll("'","").replaceAll("\"","") + "'";
                                        i[0] = false;
                                    }else{
                                        danmuList[0] = danmuList[0] + ",'" + element.text().replaceAll("\\\\","").replaceAll("'","").replaceAll("\"","") + "'";
                                    }
//                                Log.e("danmu",danmuList[0]);
                                    x++;
                                }

                                danmu = danmuList[0];

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                thread.start();

                Thread thread2=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            thread.join();
                            xpLoading.smartDismiss();
                            handler.sendEmptyMessage(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
                thread2.start();

            }
        });



    }


    private interface OnSuccessListener{
        void doLogic(String s);
    }

    @SuppressLint("StaticFieldLeak")
    private void redirectUrl(final OnSuccessListener onSuccessListener) {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL(link).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setInstanceFollowRedirects(false);
                conn.setConnectTimeout(5000);
                String url = conn.getHeaderField("Location");
                conn.disconnect();
                return url;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    onSuccessListener.doLogic(s);
                } catch (Exception e) {
//                    new XPopup.Builder(AVActivity.this).asConfirm("致命错误", "无法获取视频详情！\n日志:\n" + e,
//                                    new OnConfirmListener() {
//                                        @Override
//                                        public void onConfirm() {
//                                            finish();
//                                        }
//                                    })
//                            .show();

                    finish();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                BottomDialog.show("你的连接出了点问题", "无法获取目标视频信息。但我知道，这一定是你的问题")
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
                    handler.sendEmptyMessage(0);
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public static int wordCount(String string) {
        if (string == null) {
            return 0;
        }
        String englishString = string.replaceAll("[\u4e00-\u9fa5]", "");
        String[] englishWords = englishString.split("[\\p{P}\\p{S}\\p{Z}\\s]+");
        int chineseWordCount = string.length() - englishString.length();
        int otherWordCount = englishWords.length;
        if (englishWords.length > 0 && englishWords[0].length() < 1) {
            otherWordCount--;
        }
        if (englishWords.length > 1 && englishWords[englishWords.length - 1].length() < 1) {
            otherWordCount--;
        }
        return chineseWordCount + otherWordCount;
    }


    public List<String> getLink(String s) {
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("https{0,1}://[^\\x{4e00}-\\x{9fa5}\\n\\r\\s]{3,}");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }

    public List<String> getBV(String s) {
        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("(BV.*?).{10}");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        //弱引用持有HandlerActivity , GC 回收时会被回收掉
        private WeakReference<AVActivity> weakReference;

        public MyHandler(AVActivity activity) {
            this.weakReference = new WeakReference(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) { // 子线程不能更新UI
            AVActivity activity = weakReference.get();
            super.handleMessage(msg);
            if (msg.what == 0){
                xpLoading.smartDismiss();
            }
            if (msg.what == 1){
                xpLoading = new XPopup.Builder(AVActivity.this)
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asLoading("获取信息中...")
                        .show();
            }


            if (msg.what == 2){
                GlideUrl glideUrl = new GlideUrl(pic.trim(), new LazyHeaders.Builder()
                        .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                        .build());
                Glide.with(AVActivity.this)
                        .load(glideUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))//圆角半径
                        .into(mIvAv);

                Glide.with(AVActivity.this)
                        .load(glideUrl)
                        .into(mIvBg);
                mBlurBg.setBlurRadius(500);
            }

            if (msg.what == 3){ // 加载进度
                if (!iDownload){
                    float baipiao = (float) (((Integer.parseInt(danmaku) + Integer.parseInt(like) + Integer.parseInt(coin) + Integer.parseInt(share) + Integer.parseInt(favorite)) / (float) (Integer.parseInt(view))) * 100.0);

                    Log.e("11", String.valueOf(Integer.parseInt(danmaku) + Integer.parseInt(like) + Integer.parseInt(coin) + Integer.parseInt(share) + Integer.parseInt(favorite)));

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
                                handler.sendEmptyMessage(4);
                            }
                            mProgessPiao.setMaxCount(100);
                        }

                    });
                    thread.start();

                    int piao = Integer.parseInt(view) - (Integer.parseInt(danmaku) + Integer.parseInt(like) + Integer.parseInt(coin) + Integer.parseInt(share) + Integer.parseInt(favorite));

                    mTvPiao.setText("白嫖人数: " + piao + " | 观看人数: " + view +"");
                }
            }

            if (msg.what == 4){
                if (!iDownload){
                    mProgessPiao.setCurrentCount(Fx);
                    mTvProgressBar.setText("白嫖率" + Fx +"%");
                }
            }

            if (msg.what == 5){
                if (!iDownload){
                    DownloadUtil.get().download(avURL, longLink, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"", BV + ".flv", new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            if (mProgessPiao != null) {
                                iDownload = false;
                                handler.sendEmptyMessage(8);
                                handler.sendEmptyMessage(3);
                            }

                        }

                        @Override
                        public void onDownloading(int progress) {
                            iDownload = true;
                            p = progress;
                            handler.sendEmptyMessage(6);
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            if (Looper.myLooper() == null)
                            {
                                Looper.prepare();
                            }
                            Toast.makeText(AVActivity.this, "下载异常" + e, Toast.LENGTH_LONG).show();
                            Log.e("error", e.toString());
                            Looper.loop();;
                        }
                    });
                }

            }

            if (msg.what == 6){
                mProgessPiao.setCurrentCount(p);
                mTvProgressBar.setText("下载中|" + p +"%");
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (iTvd <= 3){
                            iTvd++;
                            handler.sendEmptyMessage(7);
                        }else{
                            iTvd = 0;
                            handler.sendEmptyMessage(7);
                        }
                    }

                });
                thread.start();
            }

            if (msg.what == 7){
                if (iDownload){
                    if (iTvd == 0){
                        mTvPiao.setText("下载中 | " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"\\" + BV + ".flv");
                    }
                    if (iTvd == 1){
                        mTvPiao.setText("下载中. | " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"\\" + BV + ".flv");
                    }
                    if (iTvd == 2){
                        mTvPiao.setText("下载中.. | " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"\\" + BV + ".flv");
                    }
                    if (iTvd == 3){
                        mTvPiao.setText("下载中... | " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"\\" + BV + ".flv");
                    }
                }
            }

            if (msg.what == 8){
                new XPopup.Builder(AVActivity.this).hasBlurBg(true).asConfirm("下载完成", "文件储存在\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"\\" + BV + ".flv" + "\n点击确定复制路径",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData mClipData = ClipData.newPlainText("Label", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
                                        cm.setPrimaryClip(mClipData);
                                    }
                                })
                        .show();
            }

            if (msg.what == 9){
                xpLoading = new XPopup.Builder(AVActivity.this)
                        .hasBlurBg(true)
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asLoading("解析数据中")
                        .show();
            }

            if (msg.what == 10){
                Intent intent = new Intent(AVActivity.this, WordActivity.class);
                intent.putExtra("list", danmu);
                intent.putExtra("cid", cid);
                intent.putExtra("num", String.valueOf(x));
                startActivity(intent);
            }

            if (msg.what == 11){
                BottomDialog.show("失效的COOKIE", "COOKIE目前失效，无法下载目标清晰度，请重新登录！")
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
                                Intent intent = new Intent(AVActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        });
            }


        }



    }

    public static byte[] decompress(byte[] data) throws IOException {
        byte[] decompressData = null;
        Inflater decompressor = new Inflater(true);
        decompressor.reset();
        decompressor.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int i = decompressor.inflate(buf);
                outputStream.write(buf, 0, i);
            }
            decompressData = outputStream.toByteArray();
        } catch (Exception e) {
        } finally {
            outputStream.close();
        }
        decompressor.end();
        return decompressData;
    }


    public void get_danmu(String danmuku, String BV){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter writer = null;

        try {
            fileOutputStream = new FileOutputStream(BV+".txt",true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
            writer = new BufferedWriter(outputStreamWriter);

            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(danmuku)));
            Element message = doc.getDocumentElement();
            NodeList list = message.getChildNodes();
            if (list != null){
                for (int i = 0; i < list.getLength(); i++){
                    Node node = list.item(i);
                    if(node.getNodeName() == "d"){
                        //appear time by sec
                        String t_sec = node.getAttributes().getNamedItem("p").getNodeValue();
                        int time_sec = Integer.valueOf(t_sec.substring(0,t_sec.indexOf(".")));
                        int min = time_sec/60;
                        int sec = time_sec%60;
                        writer.write(min+":"+sec+" ");
                        writer.write(node.getFirstChild().getNodeValue());
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (outputStreamWriter != null){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 拦截物理返回按键
     * */
    @Override
    public void onBackPressed() {
        if (iDownload){
            new XPopup.Builder(AVActivity.this).asConfirm("操作被拦截", "下载线程占用中\n当下载任务完成后才能返回！\n请耐心等待",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {

                                }
                            })
                    .show();
        }else{
            super.onBackPressed();
        }
    }

}