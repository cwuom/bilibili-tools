package com.cwuom.YJSLFull;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 *
 * 自定义 进度条
 * Created by wenjing.tang on 2017/8/7.
 */

public class CustomizedProgressBar extends View {

    private float maxCount = 100; //进度条最大值
    private float currentCount; //进度条当前值
    // private Paint  mPaint ;
    private int mWidth,mHeight;
    private Context mContext;

    public CustomizedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomizedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomizedProgressBar(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int round = mHeight/2; //半径

        mPaint.setColor(getResources().getColor(R.color.white_alpha)); //设置边框背景颜色
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint);//绘制 最外面的大 圆角矩形，背景为白色

        float section = currentCount/maxCount; //进度条的比例
        RectF rectProgressBg = new RectF(0, 0, mWidth*section, mHeight);

        Log.e("CustomizedProgressBar", currentCount+"");
        Log.e("CustomizedProgressBar", section+"");

        //Paint设置setColor(白色无透明)和setShader，只让setShader生效；不然前面setColor设置了透明度，透明度会生效，和setShader效果叠加
        mPaint.setColor(getResources().getColor(R.color.white));
        mPaint.setShader(getLinearGradient());
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint); //最左边的圆角矩形

        if (maxCount != currentCount){ //如果不是100%，绘制第三段矩形
            RectF rectProgressBg2 = new RectF(mWidth*section-round, 0, mWidth*section, mHeight);
            mPaint.setShader(getLinearGradient());
            canvas.drawRect(rectProgressBg2, mPaint);
        }
    }

    private LinearGradient linearGradient;
    private LinearGradient getLinearGradient(){
        if(linearGradient==null){
            linearGradient = new LinearGradient(0, 0, getWidth(), mHeight, new int[]{mContext.getResources().getColor(R.color.progress_color_1),
                    mContext.getResources().getColor(R.color.progress_color_2)}, null, Shader.TileMode.CLAMP); //根据R文件中的id获取到color
        }
        return linearGradient;
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 设置最大的进度值
     * @param maxCount 最大的进度值
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     * @param currentCount 当前进度值
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(18);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }
}