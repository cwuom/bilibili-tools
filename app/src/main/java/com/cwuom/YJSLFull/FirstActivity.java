package com.cwuom.YJSLFull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.ncorti.slidetoact.SlideToActView;

import java.util.Objects;

import kotlin.Unit;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

/*
* 这是一位Android新手写的屎山代码
* 在此保佑代码不出BUG
* UI改版2.0(2022.7.8)
* */

public class FirstActivity extends AppCompatActivity {
    private TextView mTvLink;
    private Button mBtnStart;
    private ThemedToggleButtonGroup themedButtonGroup;
    private String btnSelect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        mBtnStart = findViewById(R.id.btn_start);
        themedButtonGroup = findViewById(R.id.cards);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(200);
                        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                        SharedPreferences.Editor edt = share.edit();
                        String itf = share.getString("itf","");
                        if (Objects.equals(itf, "true")){
                            Intent intent = new Intent(FirstActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
        SharedPreferences.Editor edt = share.edit();

        edt.putString("itf", "null");
        edt.putString("uid", "null");

        // ====================================OnClickListene====================================

        // 展示联系方式
        mTvLink = findViewById(R.id.tv_cwuom);
        mTvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(FirstActivity.this)
                        .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                        .asAttachList(new String[]{"QQ: 2594748568", "WeChat: cwuomcwuom00"},
                                new int[]{R.mipmap.qq_circle_fill, R.mipmap.wechat_fill},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
//                                        Log.d("log", "click " + text);
                                    }
                                })
                        .show();
            }
        });

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSelect == null){
                    new XPopup.Builder(FirstActivity.this)
                            .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                            .asAttachList(new String[]{"请选择身份后继续！"},
                                    new int[]{R.mipmap.error},
                                    new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String text) {
//                                        Log.d("log", "click " + text);
                                        }
                                    })
                            .show();
                }else {
                    if (btnSelect.equals("不是UP的粉丝")){
                        // 跳转到正式会员注册页面
                        Intent intent = new Intent(FirstActivity.this, VIPActivity.class);
                        startActivity(intent);
                    }else{
//                        new XPopup.Builder(FirstActivity.this).asConfirm("是真的吗？", "如果这是真的，那么感谢你对我长久的支持！\n如果你已经是我的粉丝了，可以点击确定按钮跳过注册( ゜- ゜)つロ",
//                                        new OnConfirmListener() {
//                                            @Override
//                                            public void onConfirm() {
//                                                SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
//                                                SharedPreferences.Editor edt = share.edit();
//                                                edt.putString("itf", "true");
//                                                edt.putString("uid", "跳过");
//                                                edt.commit();
//
//                                                finish();
//                                            }
//                                        })
//                                .show();

                        BottomDialog.show("是真的吗？", "如果这是真的，那么感谢你对我长久的支持！\n如果你已经是我的粉丝了，可以点击“我是”并跳过注册( ゜- ゜)つロ")
                                .setCancelButton("我只是看看", new OnDialogButtonClickListener<BottomDialog>() {
                                    @Override
                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                        return false;
                                    }
                                })
                                .setOkButton("我是", new OnDialogButtonClickListener<BottomDialog>() {
                                    @Override
                                    public boolean onClick(BottomDialog baseDialog, View v) {
                                        SharedPreferences share = getSharedPreferences("temp",MODE_PRIVATE);
                                        SharedPreferences.Editor edt = share.edit();
                                        edt.putString("itf", "true");
                                        edt.putString("uid", "跳过");
                                        edt.commit();

                                        finish();
                                        return false;
                                    }
                                });
                    }
                }
            }
        });


        themedButtonGroup.setOnSelectListener((ThemedButton btn) -> {
            btnSelect = btn.getText();
            Log.e("btn", btnSelect);
            return kotlin.Unit.INSTANCE;
        });

        // ====================================OnClickListene====================================

    }

}