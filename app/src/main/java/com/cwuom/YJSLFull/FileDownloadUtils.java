package com.cwuom.YJSLFull;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by YuShuangPing on 2018/11/12.
 */
public class FileDownloadUtils {
    public static FileDownloadUtils instance = null;

    public FileDownloadUtils() {
    }

    public static FileDownloadUtils getInstance() {
        if (null == instance) {
            instance = new FileDownloadUtils();
        }
        return instance;
    }

    /**
     * 单任务下载
     *
     * @param downLoadUri  文件下载网络地址
     * @param destinationUri 下载文件的存储绝对路径
     */
    public void startDownLoadFileSingle(String downLoadUri, String destinationUri,FileDownLoaderCallBack callBack) {
        FileDownloader.getImpl().create(downLoadUri).setPath(destinationUri).setListener(fileDownloadListener(callBack)).start();
    }

    // 下载方法
    private FileDownloadListener fileDownloadListener(final FileDownLoaderCallBack callBack) {
        return new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //等待，已经进入下载队列
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //下载进度回调
                if (callBack != null){
                    callBack.downLoadProgress(task,soFarBytes,totalBytes);
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                //完成整个下载过程
                if (callBack != null){
                    callBack.downLoadCompleted(task);
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //暂停下载
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                //下载出现错误
                if (callBack != null){
                    callBack.downLoadError(task,e);
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
            }
        };
    }

    public interface FileDownLoaderCallBack {
        //文件是否下载完成
        void downLoadCompleted(BaseDownloadTask task);

        //文件是否下载失败
        void downLoadError(BaseDownloadTask task, Throwable e);

        //文件下载进度
        void downLoadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes);
    }



}
