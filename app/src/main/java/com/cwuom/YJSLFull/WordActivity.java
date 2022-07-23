package com.cwuom.YJSLFull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import is.arontibo.library.ElasticDownloadView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordActivity extends AppCompatActivity {

    private static WebView mWvWordcloud;
    private String list;
    private ProgressBar progressBar;
    private BasePopupView xpLoading;
    private ElasticDownloadView mElasticDownloadView;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        Intent i = getIntent();
        list = i.getStringExtra("list");
        String x = i.getStringExtra("num");
        String cid = i.getStringExtra("cid");
        Log.e("cid", cid);

        // 不耗时，做一些简单初始化准备工作，不会启动下载进程
        FileDownloader.init(this);

        xpLoading = new XPopup.Builder(WordActivity.this)
                .dismissOnTouchOutside(false)
                .asLoading("读取中|数量" + x + "条")
                .show();

        mElasticDownloadView = findViewById(R.id.elastic_download_view);

        mWvWordcloud = findViewById(R.id.wv_wordcloud);

        mWvWordcloud.getSettings().setJavaScriptEnabled(true);
        mWvWordcloud.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvWordcloud.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvWordcloud.addJavascriptInterface(new AndroidAndJSInterface(), "Android");
        mWvWordcloud.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWvWordcloud.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 加载assets目录下面的html
        mWvWordcloud.loadUrl("file:///android_asset/index.html");
        mWvWordcloud.setWebViewClient(new MyWebViewClient());
        mWvWordcloud.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });



        mElasticDownloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mElasticDownloadView.startIntro();
//                DownloadUtil_danmu.get().download("http://comment.bilibili.com/"+cid+".xml", Environment.getExternalStorageDirectory().getAbsolutePath(), cid + ".xml", new DownloadUtil_danmu.OnDownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(File file) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mElasticDownloadView.success();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDownloading(int progress) {
//                        Log.e("进度", String.valueOf(progress));
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("进度", String.valueOf(progress));
////                                mElasticDownloadView.setProgress(progress);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDownloadFailed(Exception e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mElasticDownloadView.fail();
//                            }
//                        });
//                    }
//                });

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        org.jsoup.nodes.Document document = null;
                        try {
                            document = Jsoup.connect("http://comment.bilibili.com/"+cid+".xml").get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Elements d = document.getElementsByTag("d");
                        Elements time = document.getElementsByTag("p");
                        String text = "";
                        for(org.jsoup.nodes.Element element: d){
                            text = text + element.text() + "\n";
                        }

                        Random random = new Random();
                        int num = random.nextInt(20220709);

                        writeTxtToFile(text, Environment.getExternalStorageDirectory().getAbsolutePath() + "/弹幕文件/", num + "__" + cid + ".txt");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mElasticDownloadView.success();
                                new XPopup.Builder(WordActivity.this).asConfirm("下载完成", "弹幕已经下载完成\n保存在" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/弹幕文件/" + num + "__" + cid + ".txt" + "中\n点击确定复制路径",
                                                new OnConfirmListener() {
                                                    @Override
                                                    public void onConfirm() {
                                                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                        ClipData mClipData = ClipData.newPlainText("Label", Environment.getExternalStorageDirectory().getAbsolutePath() + "/弹幕文件/");
                                                        cm.setPrimaryClip(mClipData);
                                                    }
                                                })
                                        .show();
                            }
                        });
                    }

                });
                thread.start();



            }
        });



    }
    class AndroidAndJSInterface {
        @JavascriptInterface
        public void show() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WordActivity.this, "成功", Toast.LENGTH_LONG).show();
                    try {
                        xpLoading.smartDismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }



    public class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            webview.loadUrl(url);
//            Log.d("url", "LOAD URL:" + url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            mWvWordcloud.loadUrl("javascript:androidCallJs(["+list+"])");
            Log.e("2333", "["+list+"]");
            super.onPageFinished(view, url);
        }

    }

    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

        //生成文件

    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //生成文件夹

    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}


