package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * 这就是那个该死的UP写的只有关注才能用的activity
 * 源码都在这里，你可以删去
 * */


public class VIPActivity extends AppCompatActivity {
    private VideoView loginVv;
    private EditText mEtUID;
    private Button mBtnReg;
    private boolean iReg = false;
    private Thread thread_FUID;
    private String name;
    private TextView mTvQ;

//    public static VIPActivity vipActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipactivity);

        // 播放视频
        loginVv = findViewById(R.id.videoview);
        initVideView();

        mEtUID = findViewById(R.id.et_uid);
        mBtnReg = findViewById(R.id.btn_reg);
//        mEtUID.setText("1158287770");
        mTvQ = findViewById(R.id.tv_q);


        mTvQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(VIPActivity.this).asConfirm("可能是因为这个？", "因为B站API的限制\n只能获取前5页的粉丝列表\n所以如果你是老粉，点击确定返回并选择另一个身份",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                })
                        .show();
            }
        });


        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iReg){
                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 获取昵称
                                String html = HtmlService.getHtml("https://api.bilibili.com/x/space/acc/info?mid="+mEtUID.getText());
                                Log.i("jsonData",html);
                                JSONObject result = new JSONObject(html);
                                result = (JSONObject) result.get("data");
                                name = result.get("name").toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                    thread.start();

                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (name != null){
                        new XPopup.Builder(VIPActivity.this)
                                .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                                .asAttachList(new String[]{"你是" + name + "吗？", "是我", "我需要修改我的UID"},
                                        new int[]{0,R.drawable.ok2, R.mipmap.error},
                                        new OnSelectListener() {
                                            @Override
                                            public void onSelect(int position, String text) {
                                                Log.d("log", "click " + text);
                                                if (Objects.equals(text, "是我")){
                                                    new XPopup.Builder(VIPActivity.this)
                                                            .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                                                            .asAttachList(new String[]{"请关注我，点击下一步跳转到主页", "关注成功后自动完成注册！"},
                                                                    new int[]{},
                                                                    new OnSelectListener() {
                                                                        @Override
                                                                        public void onSelect(int position, String text) {

                                                                        }
                                                                    })
                                                            .show();
                                                    mBtnReg.setText("下一步");
                                                    iReg = true;
                                                }

                                            }
                                        })
                                .show();
                    }else{
                        new XPopup.Builder(VIPActivity.this)
                                .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                                .asAttachList(new String[]{"本软件暂时不支持无名氏注册"},
                                        new int[]{R.mipmap.error},
                                        new OnSelectListener() {
                                            @Override
                                            public void onSelect(int position, String text) {
                                                Log.d("log", "click " + text);
                                                if (Objects.equals(text, "是我")){
                                                    new XPopup.Builder(VIPActivity.this)
                                                            .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                                                            .asAttachList(new String[]{"请关注我，点击下一步进行(完成)验证", "若无法完成验证，请重新关注"},
                                                                    new int[]{},
                                                                    new OnSelectListener() {
                                                                        @Override
                                                                        public void onSelect(int position, String text) {
//                                        Log.d("log", "click " + text);
                                                                        }
                                                                    })
                                                            .show();
                                                    mBtnReg.setText("下一步");
                                                    iReg = true;
                                                }

                                            }
                                        })
                                .show();
                    }
                }else{
                    Uri uri = Uri.parse("https://space.bilibili.com/473400804?spm_id_from=333.1007.0.0");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);


                    BasePopupView xpLoading = new XPopup.Builder(VIPActivity.this)
                            .dismissOnTouchOutside(false)
                            .asLoading("验证中")
                            .show();


                    thread_FUID = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                if (xpLoading.isDismiss()){ // 若弹窗终止，则线程终止
                                    break;
                                }
                                try {
                                    Thread.sleep(500);
                                    try {
                                        String html = HtmlService.getHtml("https://api.bilibili.com/x/relation/followers?vmid=473400804");
                                        Log.i("jsonData",html);
                                        if(html.contains("\"mid\":"+mEtUID.getText() + ","))
                                        {
                                            if (Looper.myLooper() == null)
                                            {
                                                Looper.prepare();
                                            }

                                            SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                                            SharedPreferences.Editor edt = share.edit();
                                            edt.putString("itf", "true");
                                            edt.putString("uid", mEtUID.getText().toString());
                                            edt.commit();


                                            xpLoading.smartDismiss(); // 关闭窗口
                                            Thread.sleep(1000);
                                            finish();
                                            Looper.loop();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

//                            tv_Json.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    tv_Json.setText(html);
//                                }
//                            });
                        }

                    });
                    thread_FUID.start();

                }
            }
        });
    }




    private void initVideView(){
        //播放路径
        loginVv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vip_bg));
        //播放
        loginVv.start();

        loginVv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) { // 静音播放
                mp.setVolume(0f, 0f);
            }
        });
        //循环播放
        loginVv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0f, 0f);
                loginVv.start();
            }
        });
    }
    @Override
    protected void onRestart() {
        //返回重新加载
        initVideView();
        super.onRestart();
    }

    public static class HtmlService {

        public static String getHtml(String path) throws Exception {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取html数据
            byte[] data = readInputStream(inStream);//得到html的二进制数据
            String html = new String(data, "UTF-8");
            return html;
        }
        public static byte[] readInputStream(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=inStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            return outStream.toByteArray();
        }
    }
}


