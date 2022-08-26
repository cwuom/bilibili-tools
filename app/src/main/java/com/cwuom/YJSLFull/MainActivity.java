package com.cwuom.YJSLFull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvale.switcher.SwitcherC;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.android.material.textfield.TextInputLayout;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.KongzueStyle;
import com.lky.toucheffectsmodule.TouchEffectsManager;
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;
import com.lky.toucheffectsmodule.types.TouchEffectsViewType;
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
* 屎山保佑我
 * 这里是这个软件的核心哦
* */

public class MainActivity extends AppCompatActivity {

    static final String db_name = "testDatabase";
    static final String tb_name = "tempTable";
    SQLiteDatabase db;

    private TextView mTvTime;
    private String time_now;
    private EditText mEtUID;
    private EditText mEtAV;
    private ImageView mIvBg;
    private Bitmap blurBitmap;
    private RealtimeBlurView mBlurBg;
    private RealtimeBlurView mBlurBg2;
    private TextInputLayout mTextField;
    private TextInputLayout mTextField2;
//    private RadioGroup mRgUIDorAV;
//    private RadioButton mRbUID, mRbAV;
    private Button mBtnCheck;
    private int iUIDorAV = 0;
    private static String AVLink;
    private boolean iTvTime = false;
    private StickySwitch stickySwitch;

    private SwitcherC switcher;
    private static final int REQUEST_CODE = 1024;

    private ArrayList<Integer> images = new ArrayList<Integer>();
    private int imageNum = 0;
    private String backUID;
    private String backAV;



    static {
        TouchEffectsManager.build(TouchEffectsWholeType.SCALE)//设置全局使用哪种效果
                .addViewType(TouchEffectsViewType.Button)//添加哪些View支持这个效果
                .addViewType(TouchEffectsViewType.TextView)
                .setListWholeType(TouchEffectsWholeType.RIPPLE);//为父控件为列表的情况下，设置特定效果
    }

    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;

    private Handler handler = new MyHandler(this);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TouchEffectsFactory.initTouchEffects(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DialogX.init(this);

        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
        SharedPreferences.Editor edt = share.edit();



        // 添加图片
        images.add(R.drawable.main_bg);
        images.add(R.drawable.main_bg_city);
        images.add(R.drawable.main_bg_flowers);
        images.add(R.drawable.main_bg_rain);
        images.add(R.drawable.main_bg_rain3);
        images.add(R.drawable.main_bg_sea);
        images.add(R.drawable.main_bg_sea2);
        images.add(R.drawable.main_bg_snow);




        mBlurBg = findViewById(R.id.blur_bg);
        mBlurBg2 = findViewById(R.id.blur_bg2);

        mBlurBg2.setBlurRadius(0);
        mBlurBg2.setOverlayColor(0);
        mBlurBg.setBlurRadius(0);
        mBlurBg.setOverlayColor(0);
        stickySwitch = findViewById(R.id.sticky_switch);

        mTvTime = findViewById(R.id.tv_time);
        mEtUID = findViewById(R.id.et_uid);
        mEtAV = findViewById(R.id.et_av);
        mTextField2 = findViewById(R.id.textField2);
        mIvBg = findViewById(R.id.iv_bg);
        mTextField = findViewById(R.id.textField);
//        mRgUIDorAV = findViewById(R.id.rg_uidOrAV);
//        mRbUID = findViewById(R.id.rb_uid);
//        mRbAV = findViewById(R.id.rb_av);
        mBtnCheck = findViewById(R.id.btn_check);

        switcher = findViewById(R.id.switcher);




        int inum = share.getInt("inum",0);
        Log.e("inum", String.valueOf(inum));

        Log.e("num", String.valueOf(inum));
        mIvBg.setImageResource(images.get(inum));

        switcher.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                if (imageNum < images.size() - 1){
                    imageNum++;

                }else{
                    imageNum = 0;
                }

                edt.putInt("inum", imageNum);
                edt.commit();

                int inum = share.getInt("inum",0);
                Log.e("num", String.valueOf(inum));
                mIvBg.setImageResource(images.get(inum));
                mIvBg.setImageResource(images.get(inum));
                return null;
            }
        });



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        requestPermission();


        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iTvTime){
                    iTvTime = true;
                    new Thread(new Runnable() {
                        int x = 0;
                        @Override
                        public void run() {
                            while (true){
                                x++;
                                handler.sendEmptyMessage(3);
                                if (x == 10000){
                                    iTvTime = false;
                                    break;
                                }
                            }
                        }

                    }).start();
                }

                MainActivity.this.getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        //把获取到的内容打印出来
                        String paste = paste();
                        Log.i("剪贴板", paste);
                        // UID:220227641

                        if (paste.contains("UID:")){
                            backUID = mEtUID.getText().toString();
                            if (isInteger(paste.replaceAll("UID:", ""))){
                                if (!backUID.equals(paste.replaceAll("UID:", ""))){
                                    Log.e("back", backUID);
                                    Log.e("UID", paste.replaceAll("UID:", ""));
                                    mEtUID.setText(paste.replaceAll("UID:", ""));
                                    PopTip.build()
                                            .setStyle(KongzueStyle.style())
                                            .setMessage("已经为你自动替换UID")
                                            .setButton("撤销", new OnDialogButtonClickListener<PopTip>() {
                                                @Override
                                                public boolean onClick(PopTip baseDialog, View v) {
                                                    mEtUID.setText(backUID);
                                                    PopTip.show("撤销成功", "确定").showLong().setButton(new OnDialogButtonClickListener<PopTip>() {
                                                        @Override
                                                        public boolean onClick(PopTip popTip, View v) {
                                                            return false;
                                                        }
                                                    });
                                                    return false;
                                                }
                                            })
                                            .show();
                                }else{
                                    PopTip.build()
                                            .setStyle(KongzueStyle.style())
                                            .setMessage("已经替换成功了呢，你想清空UID吗")
                                            .setButton("清空", new OnDialogButtonClickListener<PopTip>() {
                                                @Override
                                                public boolean onClick(PopTip baseDialog, View v) {
                                                    mEtUID.setText("");
                                                    PopTip.show("清空成功", "确定").showLong().setButton(new OnDialogButtonClickListener<PopTip>() {
                                                        @Override
                                                        public boolean onClick(PopTip popTip, View v) {
                                                            //点击“撤回”按钮回调
                                                            return false;
                                                        }
                                                    });
                                                    return false;
                                                }
                                            })
                                            .show();
                                }
                            }

                        }

                        try {
                            String i = getLink(paste).get(0);
                            backAV = mEtAV.getText().toString();
                            if (!backAV.equals(paste)){
                                mEtAV.setText(paste);
                                PopTip.build()
                                        .setStyle(KongzueStyle.style())
                                        .setMessage("已经为你自动替换视频链接")
                                        .setButton("撤销", new OnDialogButtonClickListener<PopTip>() {
                                            @Override
                                            public boolean onClick(PopTip baseDialog, View v) {
                                                mEtAV.setText(backAV);
                                                PopTip.show("撤销成功", "确定").showLong().setButton(new OnDialogButtonClickListener<PopTip>() {
                                                    @Override
                                                    public boolean onClick(PopTip popTip, View v) {
                                                        return false;
                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .show();
                            }else{
                                PopTip.build()
                                        .setStyle(KongzueStyle.style())
                                        .setMessage("你已经替换过链接了，需要清空吗")
                                        .setButton("清空", new OnDialogButtonClickListener<PopTip>() {
                                            @Override
                                            public boolean onClick(PopTip baseDialog, View v) {
                                                mEtAV.setText("");
                                                PopTip.show("清空成功", "确定").showLong().setButton(new OnDialogButtonClickListener<PopTip>() {
                                                    @Override
                                                    public boolean onClick(PopTip popTip, View v) {
                                                        return false;
                                                    }
                                                });
                                                return false;
                                            }
                                        })
                                        .show();
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }

                    }
                });

            }
        });




        // 创建temp存放uid等信息
        String itf = share.getString("itf","");
        String uid = share.getString("uid","");
        String show = share.getString("show","");
        if (!Objects.equals(itf, "true")){
            // 初次使用跳转
            Intent intent = new Intent(MainActivity.this, FirstActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        edt.commit(); // 提交

        if (!Objects.equals(show, "false") && !Objects.equals(uid, "跳过") && !Objects.equals(uid, "")){
            Log.e("uid", uid);
            new Thread(new Runnable() { // 从B站粉丝列表来判断是否取关（由于B站API限制，不能保证不误判）
                @Override
                public void run() {
                    try {
                        String html = VIPActivity.HtmlService.getHtml("https://api.bilibili.com/x/relation/followers?vmid=473400804");
                        Log.e("jsonData",html);
                        if(!html.contains("\"mid\":"+ uid + ","))
                        {
                            mBlurBg2.setBlurRadius(300);
                            handler.sendEmptyMessage(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }



        new Thread(new Runnable() { // 设置时间
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(300);
                        LocalTime time = LocalTime.now(); // get the current time
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        time_now = time.format(formatter);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();



        mEtUID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){ // 一些基础的动画
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int x = 0; x < 50; x++){
                                    Thread.sleep(10);
                                    mBlurBg.setBlurRadius(x);
                                    mTextField.getBackground().setAlpha(100-x);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();
                    // 这代码太蠢了，固定数值无法适配这么多机型
//                    mTextField.animate().translationY(300).setDuration(1000).start();
//                    mTextField2.animate().translationY(-550).setDuration(1000).start();
                    mTextField.animate().translationY(mTextField2.getY() - mTextField.getY()).setDuration(1000).start();
                    mTextField2.animate().translationY(mTextField.getY() - mTextField2.getY()).setDuration(1000).start();

                    // FUCK！！！ 这个方法有BUG
//                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 300);
//                    translateAnimation.setDuration(1000);
//                    translateAnimation.setFillAfter(true); // 设置保持动画最后的状态
//                    mTextField.startAnimation(translateAnimation);
//
//
//
//                    TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 0, -400);
//                    translateAnimation2.setDuration(1000);
//                    translateAnimation2.setFillAfter(true); // 设置保持动画最后的状态
//                    mTextField2.startAnimation(translateAnimation2);
                }else{
//                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 300, 0);
//                    translateAnimation.setDuration(1000);
//                    translateAnimation.setFillAfter(true); // 设置保持动画最后的状态
//                    mTextField.startAnimation(translateAnimation);
//
//                    TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, -400, 0);
//                    translateAnimation2.setDuration(1000);
//                    translateAnimation2.setFillAfter(true); // 设置保持动画最后的状态
//                    mTextField2.startAnimation(translateAnimation2);

                    mTextField.animate().translationY(0).setDuration(1000).start();
                    mTextField2.animate().translationY(0).setDuration(1000).start();
                    mTextField.getBackground().setAlpha(255);
                    mBlurBg.setBlurRadius(0);
                }

            }
        });

        mEtAV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int x = 0; x < 50; x++){
                                    Thread.sleep(10);
                                    mBlurBg.setBlurRadius(x);
                                    mTextField2.getBackground().setAlpha(100-x);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();
                }else{
                    mBlurBg.setBlurRadius(0);
                    mTextField2.getBackground().setAlpha(255);
                }

            }
        });

//        mRgUIDorAV.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == mRbUID.getId()){ // UID
//                    iUIDorAV = 0;
//                }else{ // AV
//                    iUIDorAV = 1;
//                }
//            }
//        });

        stickySwitch.setAnimationDuration(300);

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
//                Log.d("111", "Now Selected : " + direction.name() + ", Current Text : " + text);
                if (text.equals("视频")){
                    iUIDorAV = 1;
                }
                if (text.equals("UID")){
                    iUIDorAV = 0;
                }
            }
        });


        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iUIDorAV == 1){
                    try {
                        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                        String iv = share.getString("iv","");
                        if (Objects.equals(iv, "true")){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(10);
                                }
                            }).start();
                            String i = getLink(mEtAV.getText().toString()).get(0);
                            Intent intent = new Intent(MainActivity.this, AVActivity.class);
                            intent.putExtra("link",mEtAV.getText().toString());
                            startActivity(intent);
                        }else{
                            BottomDialog.show("视频Beta", "欢迎使用视频解析，目前尚未完善(咕咕)。\n你可以通过此功能查看视频的各项详细数据\n如果有BUG请反馈给我!（￣▽￣）")
                                    .setCancelButton("我知道了", new OnDialogButtonClickListener<BottomDialog>() {
                                        @Override
                                        public boolean onClick(BottomDialog baseDialog, View v) {
                                            try {
                                                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                                                SharedPreferences.Editor edt = share.edit();
                                                edt.putString("iv", "true");
                                                edt.commit();

                                                String i = getLink(mEtAV.getText().toString()).get(0);
                                                Intent intent = new Intent(MainActivity.this, AVActivity.class);
                                                intent.putExtra("link",mEtAV.getText().toString());
                                                startActivity(intent);

                                                // 透明度BUG太不优雅了
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Thread.sleep(1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        handler.sendEmptyMessage(10);
                                                    }
                                                }).start();

                                            } catch (Exception e) {
                                                MessageDialog.show("你是故意找茬是吧？", "请输入链接后再继续！", "关闭");
                                                e.printStackTrace();
                                                e.printStackTrace();
                                            }

                                            return false;
                                        }
                                    });
                        }
                    } catch (Exception e) {
//                        new XPopup.Builder(MainActivity.this).asConfirm("你是故意找茬是吧？", "请输入链接后再继续！",
//                                        new OnConfirmListener() {
//                                            @Override
//                                            public void onConfirm() {
//
//                                            }
//                                        })
//                                .show();


                        MessageDialog.show("你是故意找茬是吧？", "请输入链接后再继续！", "关闭");
                        e.printStackTrace();
                    }
                }else{
                    // 透明度BUG太不优雅了
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(10);
                        }
                    }).start();

                    if (mEtUID.getText().toString().equals("")){
                        MessageDialog.show("我饿了", "请喂我UID我才能干活", "关闭");
                    }else{
                        Intent intent = new Intent(MainActivity.this, UIDActivity.class);
                        intent.putExtra("UID",mEtUID.getText().toString());
                        startActivity(intent);
                    }
                }
            }
        });

        String ifd = share.getString("ifd","");

        if (!Objects.equals(ifd, "true")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(11);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }



    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        //弱引用持有HandlerActivity , GC 回收时会被回收掉
        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            this.weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) { // 子线程不能更新UI
            MainActivity activity = weakReference.get();
            super.handleMessage(msg);
            if (msg.what == 0) {
//                new XPopup.Builder(MainActivity.this).asInputConfirm("你好像取关了我~", "取关是不能被容忍的！如果这是一个误判，请输入”我没有取关“。\n如果你足够诚实，本弹窗不会再复现",
//                                new OnInputConfirmListener() {
//                                    @Override
//                                    public void onConfirm(String text) {
//                                        if (Objects.equals(text, "我没有取关")){
//                                            SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
//                                            SharedPreferences.Editor edt = share.edit();
//                                            edt.putString("show", "false");
//                                            edt.commit();
//                                        }else{
//                                            finish();
//                                        }
//                                    }
//                                })
//                        .show();


                FullScreenDialog noTest = FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.out) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {
                        SlideToActView sta = v.findViewById(R.id.example);

                        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                            @Override
                            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                                SharedPreferences.Editor edt = share.edit();
                                edt.putString("show", "false");
                                edt.commit();

                                mBlurBg2.setBlurRadius(0);

                                PopTip.show("本弹窗将再不复现，有问题请私信", "确定").noAutoDismiss();
                                dialog.dismiss();
                            }
                        });
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            if (noTest.isCancelable()){
                                mBlurBg2.setBlurRadius(0);
                                break;
                            }
                        }
                    }

                }).start();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(5000);
//                            finish();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }

            }
            if (msg.what == 1){
                mTvTime.setText(time_now);
            }

            if (msg.what == 2){
                mIvBg.setImageBitmap(blurBitmap);
            }

            if (msg.what == 3){
                mTvTime.setText("(=・ω・=)");
            }

            if (msg.what == 10){ // 不要问为什么是10
                mTextField2.getBackground().setAlpha(255);
                mTextField.getBackground().setAlpha(255);

                mBlurBg.setBlurRadius(0);
            }

            if (msg.what == 11){
                FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.layout_first) {
                    @Override
                    public void onBind(FullScreenDialog dialog, View v) {
                        SlideToActView sta = v.findViewById(R.id.example);
                        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                            @Override
                            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                                SharedPreferences.Editor edt = share.edit();
                                edt.putString("ifd", "true");
                                edt.commit();
                                MessageDialog.show("小彩蛋", "复制完成视频链接或UID时，点击上方的时间，可以快速粘贴！\nps: 长按时间又惊喜哦(可能是个空壳)", "确定");
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
//                writeFile();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
//            writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                writeFile();
            } else {
//                Toast.makeText(MainActivity.this, "下载选项需要储存权限\n您拒绝了该权限，下载选项将收到限制!", Toast.LENGTH_LONG).show();
                BottomDialog.show("访问被拒绝", "您拒绝了该权限，下载功能将收到限制。\n若要恢复，请在设置中启用！");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
//                writeFile();
            } else {
//                Toast.makeText(MainActivity.this, "下载选项需要储存权限\n您拒绝了该权限，下载选项将收到限制!", Toast.LENGTH_LONG).show();
                BottomDialog.show("访问被拒绝", "您拒绝了该权限，下载功能将收到限制。\n若要恢复，请在设置中启用！");
            }
        }
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

        /**
         * 获取剪切板内容
         * @return
         */
        public String paste(){
            ClipboardManager manager = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager != null) {
                if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                    CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                    String addedTextString = String.valueOf(addedText);
                    if (!TextUtils.isEmpty(addedTextString)) {
                        return addedTextString;
                    }
                }
            }
            return "";
        }

        /**
         * 清空剪切板
         */
        public void clear(){
            ClipboardManager manager = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager != null) {
                try {
                    manager.setPrimaryClip(manager.getPrimaryClip());
                    manager.setPrimaryClip(ClipData.newPlainText("",""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}





