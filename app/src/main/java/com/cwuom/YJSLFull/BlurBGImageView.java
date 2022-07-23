package com.cwuom.YJSLFull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class BlurBGImageView extends ImageView {
    Bitmap overlay;
    int scaleFactor = 2;
    int radius = 8;
    public BlurBGImageView(Context context) {
        super(context);
    }

    public BlurBGImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlurBGImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }


    public void refreshBG(View bgView){
        bgView.setDrawingCacheEnabled(true);
        bgView.buildDrawingCache();
        Bitmap bitmap1 = null;
        try {
            bitmap1 = getBitmap(bgView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap1 != null){
            blur(bitmap1,this,radius);//模糊处理
            bitmap1.recycle();
        }
        bgView.setDrawingCacheEnabled(false);//清除缓存
    }

    private void blur(Bitmap bkg, ImageView view, float radius) {
        if (overlay != null){
            overlay.recycle();
        }
        overlay = Bitmap.createScaledBitmap(bkg, bkg.getWidth() / scaleFactor, bkg.getHeight() / scaleFactor, false);
        overlay = blur(getContext(),overlay, radius);//高斯模糊
        view.setImageBitmap(overlay);
    }

    private Bitmap blur(Context context, Bitmap image, float radius) {
        RenderScript rs = RenderScript.create(context);
        Bitmap outputBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Allocation in = Allocation.createFromBitmap(rs, image);
        Allocation out = Allocation.createFromBitmap(rs, outputBitmap);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        intrinsicBlur.setRadius(radius);
        intrinsicBlur.setInput(in);
        intrinsicBlur.forEach(out);

        out.copyTo(outputBitmap);
        image.recycle();
        rs.destroy();
        return outputBitmap;
    }
    private Bitmap getBitmap(View view){
        //获取屏幕整张图片
        Bitmap bitmap = view.getDrawingCache();

        if (bitmap != null) {

            //需要截取的长和宽
            int outWidth = this.getWidth();
            int outHeight = this.getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];


            this.getLocationOnScreen(viewLocationArray);

            int[] listLocationArray = new int[2];
            view.getLocationOnScreen(listLocationArray);

            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0] - listLocationArray[0], viewLocationArray[1]  - listLocationArray[1], outWidth, outHeight);

        }

        return bitmap;
    }
}
